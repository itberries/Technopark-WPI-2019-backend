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
import com.itberries.technopark.itberries.websocket.games.models.MPGame;
import com.itberries.technopark.itberries.websocket.games.models.MPGamePlayer;
import com.itberries.technopark.itberries.websocket.games.models.MPGameSession;
import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;
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
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MultiUserGameServiceImpl implements IMultiUserGameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiUserGameServiceImpl.class);

    private final Queue<Long> waiters = new ConcurrentLinkedDeque<>();
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper;
    private final CopyOnWriteArrayList<MPGameSession> games = new CopyOnWriteArrayList<>();
    private final Map<Long, MPGameSession> gameMap = new ConcurrentHashMap<>();
    private ICheckAnswerService iCheckAnswerService;
    private IUserDAO iUserDAO;
    private IRewardService iRewardService;
    private Gson gson = new Gson();
    private IMPGenerateGameService impGenerateGameService;

    @Autowired
    public MultiUserGameServiceImpl(ObjectMapper objectMapper,
                                    ICheckAnswerService iCheckAnswerService,
                                    IUserDAO iUserDAO,
                                    IRewardService iRewardService,
                                    IMPGenerateGameService impGenerateGameService) {
        this.objectMapper = objectMapper;
        this.iCheckAnswerService = iCheckAnswerService;
        this.iUserDAO = iUserDAO;
        this.iRewardService = iRewardService;
        this.impGenerateGameService = impGenerateGameService;
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
        if (Boolean.TRUE.equals(mpGameSession.getPlayer1().getId().equals(user.getId()))) {
            player = mpGameSession.getPlayer1();
        } else {
            player = mpGameSession.getPlayer2();
        }

        TurnResult turnResult;

        LocalDateTime answerTime = LocalDateTime.now();
        if (Duration.between(answerTime, player.getDateTimeStart()).toMinutes() >= 1) {
            LOGGER.info(String.format("timeout for user [%s]", user.getId()));
            turnResult = new TurnResult(new TurnResult.Payload("false"));
        } else {
            boolean result = checkAnswer(player.getCurrentGameType(), turn, player.getCurrentGameAnswer());
            if (result) {
                turnResult = new TurnResult(new TurnResult.Payload("true"));
                //увеличиваем количество верных ответов на 1 (сдвигаем позицию игрока)
                player.movePosition();//todo: изменится ли мне исходный игрок?

                //Сохраняем шаг пользователя на случай разрыва сессии

                LOGGER.info(String.format("handleGameTurn MP: turn correct, turn: %s\n", turn));
            } else {
                turnResult = new TurnResult(new TurnResult.Payload("false"));
                LOGGER.info(String.format("handleGameTurn MP: turn incorrect, turn: %s\n", turn));
            }
        }
        //обновляем таймер
        LOGGER.info(String.format("restart timer for user id = %s\n", user.getId()));
        player.setDateTimeStart(LocalDateTime.now());

        sendMessageToUser(user.getId(), turnResult);
        LOGGER.info(String.format("send message to user id=%d, turn = %s\n", user.getId(), turn));
        checkGameEnd(player);
    }


    private void checkGameEnd(MPGamePlayer player) throws IOException {
        if (isCompletedGame(player)) {
            int score = 100;  //todo: придумать получаемое количество очков пооригинальнее
            iUserDAO.updateScore(score, player.getId());
            //Проверить,не заслужил ли пользователь новую ачивку?
            Reward reward = iRewardService.updateRewardsByUser(player.getId());
            sendMessageToUser(player.getId(), new GameCompleted(new GameCompleted.Payload(score, reward)));
            LOGGER.info(String.format("updated score for user id = \n", player.getId()));
            //проставляем флаг, что игра завершена - нужно для восстановления сессии
            //sessions.get(player.getId()).setGameCompleted(Boolean.TRUE);
        }
    }

    public boolean isCompletedGame(MPGamePlayer player) {
        final int TOTAL_NUMBERS_MP_GAMES = 9;
        return Objects.equals(TOTAL_NUMBERS_MP_GAMES - 1, player.getCurrentPosition());
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
        LOGGER.info(String.format("Clear state after close connection for user id =%s", user.getId()));
        waiters.remove(user.getId());
        gameMap.remove(user.getId());
        Optional<MPGameSession> first = games.stream()
                .filter(s -> s.getPlayer1().getId().equals(user.getId()) || s.getPlayer2().getId().equals(user.getId()))
                .findFirst();

        if (first.isPresent()) {
            MPGameSession mpGameSession = first.get();
            MPGamePlayer player1 = mpGameSession.getPlayer1();
            MPGamePlayer player2 = mpGameSession.getPlayer2();
            final DeliveryStatus deliveryStatus = new DeliveryStatus(new DeliveryStatus.Payload("OPPONENT_HAS_LEFT"));
            if (player1.getId().equals(user.getId())) {
                sendMessageToUser(player2.getId(), deliveryStatus);
                LOGGER.info(String.format("notify user2 = %s for %s = \n", player2.getId(), deliveryStatus));
            } else {
                sendMessageToUser(player1.getId(), new DeliveryStatus(new DeliveryStatus.Payload("OPPONENT_HAS_LEFT")));
                LOGGER.info(String.format("notify user1 = %s for %s = \n", player1.getId(), deliveryStatus));
            }
            sessions.remove(player1.getId());
            sessions.remove(player2.getId());
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
                result = iCheckAnswerService
                        .checkAnswerByGameId(correctAnswer, gson.toJson(turnMatch.getPayload().getData()));
                break;
            case "chain":
                TurnChain turnChain = (TurnChain) turn;
                result = iCheckAnswerService
                        .checkAnswerByGameId(correctAnswer, gson.toJson(turnChain.getPayload().getData()));
                break;

            case "question":
                TurnQuestion turnQuestion = (TurnQuestion) turn;
                result = iCheckAnswerService.checkAnswerByGameId(correctAnswer, turnQuestion.getPayload().getData());
                break;
            default:
                LOGGER.error("---------------ERROR! WRONG GAME TYPE!------------");
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
        MPGameSession mpGameSession = new MPGameSession(new MPGamePlayer(user1, START_GAME_POSITION),
                new MPGamePlayer(user2, START_GAME_POSITION), impGenerateGameService.getMultiPlayerGamesData());

        gameMap.put(user1, mpGameSession);
        gameMap.put(user2, mpGameSession);
        games.add(mpGameSession);

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
            LOGGER.info(String.format("send message to user %s, message = %s", userId, objectMapper.writeValueAsString(message)));
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (Exception ex) {
            throw new IOException("Unable to send message", ex);
        }
        LOGGER.info("Send message to user id=%s, message=%s", userId, message);
    }
}
