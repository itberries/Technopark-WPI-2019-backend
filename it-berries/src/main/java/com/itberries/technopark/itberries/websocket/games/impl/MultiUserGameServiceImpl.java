package com.itberries.technopark.itberries.websocket.games.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itberries.technopark.itberries.dao.IUserDAO;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.services.IRewardService;
import com.itberries.technopark.itberries.websocket.events.*;
import com.itberries.technopark.itberries.websocket.games.IMPGenerateGameService;
import com.itberries.technopark.itberries.websocket.games.IMultiUserGameService;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnChainDAO;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnMatchDAO;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnQuestionDAO;
import com.itberries.technopark.itberries.websocket.games.models.MPGame;
import com.itberries.technopark.itberries.websocket.games.models.MPGamePlayer;
import com.itberries.technopark.itberries.websocket.games.models.MPGameSession;
import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerChainService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerMatchService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class MultiUserGameServiceImpl implements IMultiUserGameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiUserGameServiceImpl.class);

    private final Queue<Long> waiters = new ConcurrentLinkedDeque<>();
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper;
    //private final CopyOnWriteArrayList<MPGameSession> games = new CopyOnWriteArrayList<>();
    private final Map<Long, MPGameSession> gameMap = new ConcurrentHashMap<>();
    private ICheckAnswerService iCheckAnswerService;
    private IUserDAO iUserDAO;
    private IRewardService iRewardService;
    private Gson gson = new Gson();
    private IMPGenerateGameService impGenerateGameService;

    private IAnswerOnMatchDAO iAnswerOnMatchDAO;
    private IAnswerOnChainDAO iAnswerOnChainDAO;
    private IAnswerOnQuestionDAO iAnswerOnQuestionDAO;

    @Autowired
    public MultiUserGameServiceImpl(ObjectMapper objectMapper,
                                    ICheckAnswerService iCheckAnswerService,
                                    IUserDAO iUserDAO,
                                    IRewardService iRewardService,
                                    IMPGenerateGameService impGenerateGameService, IAnswerOnMatchDAO iAnswerOnMatchDAO, IAnswerOnChainDAO iAnswerOnChainDAO, IAnswerOnQuestionDAO iAnswerOnQuestionDAO) {
        this.objectMapper = objectMapper;
        this.iCheckAnswerService = iCheckAnswerService;
        this.iUserDAO = iUserDAO;
        this.iRewardService = iRewardService;
        this.impGenerateGameService = impGenerateGameService;
        this.iAnswerOnMatchDAO = iAnswerOnMatchDAO;
        this.iAnswerOnChainDAO = iAnswerOnChainDAO;
        this.iAnswerOnQuestionDAO = iAnswerOnQuestionDAO;
    }

    @Override
    public void joinGame(JoinGame joinGameMessage, WebSocketSession webSocketSession, User user) throws IOException {
        addWaiter(user.getId(), webSocketSession);
    }

    @Override
    public void handleGameTurn(Turn turn, WebSocketSession webSocketSession, User user) throws IOException {
        LOGGER.info(String.format("handleGameTurn [%s] from user [%s]", turn, user.getId()));


        MPGameSession mpGameSession = gameMap.get(user.getId());
        MPGamePlayer player;
        int num = 0;
        if (Boolean.TRUE.equals(mpGameSession.getPlayer1().getId().equals(user.getId()))) {
            player = mpGameSession.getPlayer1();
            num = 1;
        } else {
            player = mpGameSession.getPlayer2();
            num = 2;
        }

        boolean result = Boolean.FALSE;
        //проверка на то, что игра не завершена
        if (mpGameSession.getPlayer2().isWinner()) {
            sendMessageToUser(player.getId(), new DeliveryStatus(new DeliveryStatus.Payload("MP_GAME_IS_OVER")));

        } else if (mpGameSession.getPlayer1().isWinner()) {
            sendMessageToUser(player.getId(), new DeliveryStatus(new DeliveryStatus.Payload("MP_GAME_IS_OVER")));

        } else {
            TurnResultMP turnResult;
            Boolean resolved = Boolean.FALSE;

            LocalDateTime answerTime = LocalDateTime.now();
            if (Duration.between(answerTime, player.getDateTimeStart()).toMinutes() >= 1) {
                LOGGER.info(String.format("timeout for user [%s]", user.getId()));
                turnResult = new TurnResultMP(new TurnResultMP.Payload("false", "false"));//две попытки нужно дать
            } else {
                result = checkAnswer(player.getCurrentGameType(), turn, player.getCurrentGameAnswer());
                if (result) {
                    //увеличиваем количество верных ответов на 1 (сдвигаем позицию игрока)
                    resolved = player.markRightAnswer(turn, result);
                    turnResult = new TurnResultMP(new TurnResultMP.Payload("true", String.valueOf(resolved)));
                    //Сохраняем шаг пользователя на случай разрыва сессии
                    LOGGER.info(String.format("handleGameTurn MP: turn correct, turn: %s\n", turn));
                } else {
                    resolved = player.markRightAnswer(turn, result);
                    turnResult = new TurnResultMP(new TurnResultMP.Payload("false", String.valueOf(resolved)));
                    LOGGER.info(String.format("handleGameTurn MP: turn incorrect, turn: %s\n", turn));
                }


                sendMessageToUser(user.getId(), turnResult);
                LOGGER.info(String.format("user id=%d, user turn was = %s\n", user.getId(), turn));
                LOGGER.info(String.format("send message to user id=%d, user turnResult  = %s\n", user.getId(), turnResult));
                LOGGER.info(String.format("Status of current task completed %s", resolved));

                //обновляем таймер в случае, если задача была завершена
                if (Boolean.TRUE.equals(resolved)) {
                    LOGGER.info(String.format("restart timer for user id = %s\n", user.getId()));
                    player.setDateTimeStart(LocalDateTime.now());
                    //sendMessageToUser(user.getId(), turnResult);
                    //sendMessageToUser(user.getId(), new DeliveryStatus(new DeliveryStatus.Payload("MINI_GAME_COMPLETED")));
                    if (num == 1) {
                        sendMessageToUser(mpGameSession.getPlayer2().getId(),
                                new DeliveryStepStatus(new DeliveryStepStatus.Payload("OPPONENT_HAS_STEPPED", String.valueOf(result))));
                    } else {
                        sendMessageToUser(mpGameSession.getPlayer1().getId(),
                                new DeliveryStepStatus(new DeliveryStepStatus.Payload("OPPONENT_HAS_STEPPED", String.valueOf(result))));
                    }
                }


            }

            checkGameEnd(player);
        }
    }


    private void checkGameEnd(MPGamePlayer player) throws IOException {
        if (isCompletedGame(player)) {
            final MPGameSession mpGameSession = gameMap.get(player.getId());
            int amountAnswersUser1 = mpGameSession.getPlayer1().getAmountRightAnswers();
            int amountAnswersUser2 = mpGameSession.getPlayer2().getAmountRightAnswers();


            int currentPlayerNumber = 0;
            if (Boolean.TRUE.equals(mpGameSession.getPlayer1().getId().equals(player.getId()))) {
                currentPlayerNumber = 1;
            } else {
                currentPlayerNumber = 2;
            }

            int winnerNum;
            if (amountAnswersUser1 == amountAnswersUser2) {
                winnerNum = 0;//ничья, но выигрывает  num (так как раньше ответил)
            } else if (amountAnswersUser1 > amountAnswersUser2) {
                winnerNum = 1;
            } else {
                winnerNum = 2;
            }

            int score = 100;  //todo: придумать получаемое количество очков пооригинальнее


            if (currentPlayerNumber == 1 && currentPlayerNumber == winnerNum) {

                iUserDAO.updateScore(score, mpGameSession.getPlayer1().getId());
                //Проверить,не заслужил ли пользователь новую ачивку?
                Reward reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer1().getId());


                sendMessageToUser(mpGameSession.getPlayer1().getId(), new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "")));
                LOGGER.info(String.format("Send message to user %s, message=%s",
                        mpGameSession.getPlayer1().getId(),
                        new GameCompleted.Payload(score, reward)));


                LOGGER.info(String.format("PLAYER with ID=%s WIN", player.getId()));
                mpGameSession.getPlayer1().setWinner(Boolean.TRUE);
                mpGameSession.getPlayer2().setWinner(Boolean.FALSE);

                sendMessageToUser(mpGameSession.getPlayer2().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(0, null, "lose", "")));
            } else if (currentPlayerNumber == 2 && currentPlayerNumber == winnerNum) {

                iUserDAO.updateScore(score, mpGameSession.getPlayer2().getId());
                //Проверить,не заслужил ли пользователь новую ачивку?
                Reward reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer2().getId());


                sendMessageToUser(mpGameSession.getPlayer2().getId(), new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "")));
                LOGGER.info(String.format("Send message to user %s, message=%s",
                        mpGameSession.getPlayer2().getId(),
                        new GameCompleted.Payload(score, reward)));


                mpGameSession.getPlayer2().setWinner(Boolean.TRUE);
                mpGameSession.getPlayer1().setWinner(Boolean.FALSE);
                sendMessageToUser(mpGameSession.getPlayer1().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(0, null, "lose", "")));
            } else {
                //увеличиваем очки обоим пользователям
                iUserDAO.updateScore(score, mpGameSession.getPlayer1().getId());
                iUserDAO.updateScore(score, mpGameSession.getPlayer2().getId());


                Reward reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer1().getId());
                sendMessageToUser(mpGameSession.getPlayer1().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(score / 2, reward, "draw", "")));
                LOGGER.info(String.format("Send message to user %s, message=%s",
                        mpGameSession.getPlayer1().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(score / 2, reward, "draw", ""))));

                reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer2().getId());
                sendMessageToUser(mpGameSession.getPlayer2().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(score / 2, reward, "draw", "")));
                LOGGER.info(String.format("Send message to user %s, message=%s",
                        mpGameSession.getPlayer2().getId(),
                        new GameCompletedMP(new GameCompletedMP.Payload(score / 2, reward, "draw", ""))));


                mpGameSession.getPlayer2().setWinner(Boolean.TRUE);
                mpGameSession.getPlayer1().setWinner(Boolean.TRUE);

            }

            LOGGER.info(String.format("updated score for user id = %s", player.getId()));
            //проставляем флаг, что игра завершена - нужно для восстановления сессии
            //sessions.get(player.getId()).setGameCompleted(Boolean.TRUE);
        }
    }

    private boolean isCompletedGame(MPGamePlayer player) {
        final int TOTAL_NUMBERS_MP_GAMES = 3;
        return Objects.equals(TOTAL_NUMBERS_MP_GAMES, player.getCurrentPosition());
    }

    @Override
    public void startTimer(DeliveryStatus deliveryStatus, WebSocketSession webSocketSession, User user) {
        final String READY_TO_START_MP_GAME = "READY_TO_START_MP_GAME";
        if (READY_TO_START_MP_GAME.equals(deliveryStatus.getPayload().getResult())) {
            MPGameSession mpGameSession = gameMap.get(user.getId());
            LocalDateTime now = LocalDateTime.now();
            mpGameSession.getPlayer1().setDateTimeStart(now);
            mpGameSession.getPlayer2().setDateTimeStart(now);
        }
    }

    @Override
    public void clearStateAfterCompletedGame(User user) throws IOException {
        int score = 100;
        LOGGER.info(String.format("Clear state after close connection for user id =%s", user.getId()));
        waiters.remove(user.getId());

        Optional<Map.Entry<Long, MPGameSession>> first = gameMap.entrySet().stream()
                .filter(s -> s.getValue().getPlayer1().getId().equals(user.getId())
                        || s.getValue().getPlayer2().getId().equals(user.getId()))
                .findFirst();

        if (first.isPresent()) {
            MPGameSession mpGameSession = first.get().getValue();
            MPGamePlayer player1 = mpGameSession.getPlayer1();
            MPGamePlayer player2 = mpGameSession.getPlayer2();
            //final DeliveryStatus deliveryStatus = new DeliveryStatus(new DeliveryStatus.Payload("OPPONENT_HAS_LEFT"));

            if (!(player1.isWinner() || player2.isWinner())) {
                if (player1.getId().equals(user.getId())) {
                    //  sendMessageToUser(player2.getId(), deliveryStatus);
                    iUserDAO.updateScore(score, player2.getId());
                    Reward reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer2().getId());
                    sendMessageToUser(mpGameSession.getPlayer2().getId(),
                            new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "opponnet_has_left")));
                    LOGGER.info(String.format("Send message to user %s, message=%s",
                            mpGameSession.getPlayer2().getId(),
                            new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "opponnet_has_left"))));


                    mpGameSession.getPlayer2().setWinner(Boolean.TRUE);
                    mpGameSession.getPlayer1().setWinner(Boolean.FALSE);
                } else {
                    iUserDAO.updateScore(score, player1.getId());
                    Reward reward = iRewardService.updateRewardsByUser(mpGameSession.getPlayer1().getId());
                    sendMessageToUser(mpGameSession.getPlayer1().getId(),
                            new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "opponnet_has_left")));
                    LOGGER.info(String.format("Send message to user %s, message=%s",
                            mpGameSession.getPlayer1().getId(),
                            new GameCompletedMP(new GameCompletedMP.Payload(score, reward, "win", "opponnet_has_left"))));
                    mpGameSession.getPlayer1().setWinner(Boolean.TRUE);
                    mpGameSession.getPlayer2().setWinner(Boolean.FALSE);
                }
            }


            sessions.remove(player1.getId());
            sessions.remove(player2.getId());
            gameMap.remove(player2.getId());
            gameMap.remove(player1.getId());
            LOGGER.info(String.format("remove player1 = %s and player2 = %s from sessions", player1.getId(), player2.getId()));
        }
    }


    //todo: повторяет синг плеер - стоит исправить
    private boolean checkAnswer(String type, Turn turn, String correctAnswer) throws IOException {
        LOGGER.info(String.format("checkAnswer type= %s, turn = %s, correctAnswer=%s", type, turn, correctAnswer));
        boolean result = false;
        switch (type) {
            case "match":
                TurnMatch turnMatch = (TurnMatch) turn;
                iCheckAnswerService = new CheckAnswerMatchService(iAnswerOnMatchDAO, objectMapper);
                result = iCheckAnswerService
                        .checkAnswerByGameId(correctAnswer, gson.toJson(turnMatch.getPayload().getData()));
                break;
            case "chain":
                iCheckAnswerService = new CheckAnswerChainService(iAnswerOnChainDAO, objectMapper);
                TurnChain turnChain = (TurnChain) turn;
                result = iCheckAnswerService
                        .checkAnswerByGameId(correctAnswer, gson.toJson(turnChain.getPayload().getData()));
                break;

            case "question":
                TurnQuestion turnQuestion = (TurnQuestion) turn;
                iCheckAnswerService = new CheckAnswerQuestionService();
                result = iCheckAnswerService.checkAnswerByGameId(correctAnswer, turnQuestion.getPayload().getData());
                break;
            default:
                LOGGER.error("---------------ERROR! WRONG GAME TYPE!------------");
                break;
        }
        return result;
    }


    private void addWaiter(Long userId, WebSocketSession webSocketSession) throws IOException {
        if (!waiters.contains(userId)) {
            waiters.add(userId);
            sessions.put(userId, webSocketSession);
            LOGGER.info(String.format("user with id=%s was added to queue", userId));
        } else {
            LOGGER.info(String.format("user with id=%s has already exist in queue", userId));
        }
        LOGGER.info(String.format("WAITERS LIST: %s", waiters.toString()));
        if (waiters.size() > 1) {
            //Достаем двух игроков
            Long user1 = waiters.poll();
            Long user2 = waiters.poll();
            LOGGER.info(String.format("Two players started the game id1=%s, id2=%s", user1, user2));
            initGame(user1, user2);
        } else {
            DeliveryStatus deliveryStatus = new DeliveryStatus(new DeliveryStatus.Payload("WAIT"));
            sendMessageToUser(userId, deliveryStatus);
            LOGGER.info(String.format("Send message to user id=%s, message =%s", userId, deliveryStatus));
        }
    }

    /**
     * Инициализировать игру, отправить на фронт 9 заданий
     *
     * @param user1
     * @param user2
     * @throws IOException
     */
    private void initGame(Long user1, Long user2) throws IOException {
        LOGGER.info(String.format("init game for user1=%s, user2=%s", user1, user2));
        final Integer START_GAME_POSITION = 0;
        final List<MPGame> multiPlayerGamesData = impGenerateGameService.getMultiPlayerGamesData();
        LocalDateTime dateTimeStart = LocalDateTime.now();
        MPGameSession mpGameSession = new MPGameSession(new MPGamePlayer(user1, START_GAME_POSITION, dateTimeStart, multiPlayerGamesData),
                new MPGamePlayer(user2, START_GAME_POSITION, dateTimeStart, multiPlayerGamesData), impGenerateGameService.getMultiPlayerGamesData());

        gameMap.put(user1, mpGameSession);
        gameMap.put(user2, mpGameSession);
        //games.add(mpGameSession);

        //Формирование приветсвенных сообщений для игроков
        //Содержат в себе id оппонента и первое задание
        MPStartGameMessage messageToUser1 = new MPStartGameMessage(new MPStartGameMessage.Payload(user2, multiPlayerGamesData));
        MPStartGameMessage messageToUser2 = new MPStartGameMessage(new MPStartGameMessage.Payload(user1, multiPlayerGamesData));
        LOGGER.info(
                String.format("create init messages for user1=%s, user2=%s, messag1=%s, message2=%s",
                        user1, user2, messageToUser1, messageToUser2));
        sendMessageToUser(user1, messageToUser1);
        sendMessageToUser(user2, messageToUser2);
    }

    /**
     * Отправка сообщения по сокету конкретному пользователю
     *
     * @param userId  уникальный ключ пользователя
     * @param message сообщение для пользователя
     * @throws IOException
     */
    private void sendMessageToUser(Long userId, Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("No game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("Session is closed or not exsists ");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (Exception ex) {
            throw new IOException("Unable to send message", ex);
        }
    }

    private class GameDispatcher implements Runnable {

        @Override
        public void run() {

            synchronized (gameMap) {
                for (Map.Entry<Long, MPGameSession> game : gameMap.entrySet()) {
                    checkTimeout(game.getValue().getPlayer1());
                    checkTimeout(game.getValue().getPlayer2());
                }
            }
        }


        void checkTimeout(MPGamePlayer player) {
            final long MAXIMUM_TIME_FOR_TASK = 1;
            if (Duration.between(player.getDateTimeStart(), LocalDateTime.now())
                    .toMinutes() >= MAXIMUM_TIME_FOR_TASK) {
                TurnResultMP turnResult = new TurnResultMP(new TurnResultMP.Payload("TIMEOUT", "false"));
                try {
                    sendMessageToUser(player.getId(), turnResult);
                    player.skipTask(); //todo: проверить сдвинится ли
                    player.setDateTimeStart(LocalDateTime.now());
                    checkGameEnd(player);
                } catch (IOException e) {
                    LOGGER.info("ERROR while TIMEOUT for player1", e.getCause());
                }
            }
        }
    }
}
