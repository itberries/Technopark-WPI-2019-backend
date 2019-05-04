package com.itberries.technopark.itberries.websocket.games.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itberries.technopark.itberries.dao.IUserDAO;
import com.itberries.technopark.itberries.dao.IUserStateDAO;
import com.itberries.technopark.itberries.models.Reward;
import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.UserState;
import com.itberries.technopark.itberries.services.IMiniGamesService;
import com.itberries.technopark.itberries.services.IRewardService;
import com.itberries.technopark.itberries.services.IUserService;
import com.itberries.technopark.itberries.websocket.events.*;
import com.itberries.technopark.itberries.websocket.games.IGamePlayService;
import com.itberries.technopark.itberries.websocket.games.IGameResponseService;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnChainDAO;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnMatchDAO;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnQuestionDAO;
import com.itberries.technopark.itberries.websocket.games.models.GamePlayerStatus;
import com.itberries.technopark.itberries.websocket.games.models.GameSession;
import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerChainService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerMatchService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerQuestionService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.ChainAnswerList;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.MatchAnswerList;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class GamePlayServiceImpl implements IGamePlayService {

    private static final Logger logger = LoggerFactory.getLogger(GamePlayServiceImpl.class);

    private final Map<Long, GameSession> sessions = new ConcurrentHashMap<>();
    private final Map<Long, GamePlayerStatus> statusGames = new ConcurrentHashMap<>();

    private IUserService userService;
    private IMiniGamesService iMiniGamesService;
    private ICheckAnswerService iCheckAnswerService;
    private IAnswerOnMatchDAO iAnswerOnMatchDAO;
    private IAnswerOnQuestionDAO iAnswerOnQuestionDAO;
    private IAnswerOnChainDAO iAnswerOnChainDAO;
    private ObjectMapper objectMapper;
    private IUserDAO iUserDAO;
    private IGameResponseService iGameResponseService;
    private IRewardService iRewardService;
    private IUserStateDAO iUserStateDAO;
    private IUserService iUserService;

    Gson gson = new Gson();
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    @Autowired
    public GamePlayServiceImpl(IUserService userService,
                               IMiniGamesService iMiniGamesService,
                               IAnswerOnMatchDAO iAnswerOnMatchDAO,
                               IAnswerOnQuestionDAO iAnswerOnQuestionDAO, IAnswerOnChainDAO iAnswerOnChainDAO, ObjectMapper objectMapper, IUserDAO iUserDAO, IGameResponseService iGameResponseService, IRewardService iRewardService, IUserStateDAO iUserStateDAO, IUserService iUserService) {
        this.userService = userService;
        this.iMiniGamesService = iMiniGamesService;
        this.iAnswerOnMatchDAO = iAnswerOnMatchDAO;
        this.iAnswerOnQuestionDAO = iAnswerOnQuestionDAO;
        this.iAnswerOnChainDAO = iAnswerOnChainDAO;
        this.objectMapper = objectMapper;
        this.iUserDAO = iUserDAO;
        this.iGameResponseService = iGameResponseService;
        this.iRewardService = iRewardService;
        this.iUserStateDAO = iUserStateDAO;
        this.iUserService = iUserService;
        this.service.scheduleAtFixedRate(new GameDispatcher(), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void joinGame(JoinGame joinGameMessage, WebSocketSession webSocketSession, User user) throws IOException {


        //проверка на возможность осуществить шаг
        isStepAllowed(joinGameMessage.getStepId(), user.getId());
        //получаем ID игры и ее тип
        Long gameId = iMiniGamesService.getGamesIdByStepId(joinGameMessage.getStepId()).get(0);
        String gameType = iMiniGamesService.getGameTypeByGameId(gameId);
        //в зависимости от типа игры нужно использовать различную логику для проверки правильного ответа
        //то есть нужно идти в разные таблицы
        switch (gameType) {
            case "match":
                iCheckAnswerService = new CheckAnswerMatchService(iAnswerOnMatchDAO, objectMapper);
                break;
            case "chain":
                iCheckAnswerService = new CheckAnswerChainService(iAnswerOnChainDAO, objectMapper);
                break;
            case "question":
                iCheckAnswerService = new CheckAnswerQuestionService();
                break;
            default:
                throw new IOException("Unable to find answer for the task");
        }

        final long MINIMUM_TIME_WITHOUT_CONNECTION = 100;
        //если игровая сессия уже есть в коллекции и crash time было менее 5 минут назад
        if (sessions.containsKey(user.getId())
                && sessions.get(user.getId()).getLocalDateTime() != null
                && Duration.between(sessions.get(user.getId()).getLocalDateTime(), LocalDateTime.now()).toMinutes() < MINIMUM_TIME_WITHOUT_CONNECTION
                && statusGames.containsKey(user.getId())
                && statusGames.get(user.getId()).getStepId().equals(joinGameMessage.getStepId())) {

            sessions.get(user.getId()).setLocalDateTime(null);//обнуляем краш тайм
            sessions.get(user.getId()).setWebSocketSession(webSocketSession);//устанавливаем новую сессию


            List<ImmutablePair<String, String>> mathResponse = iGameResponseService
                    .getMathResponse(sessions.get(user.getId()).getTurns());
            sendMessageToUser(user.getId(), new RecoveryGame(new RecoveryGame.Payload("RECONNECT_OK", mathResponse)));
            logger.info(String.format("joinGame: reconnect join, joinGameMessage: %s\n", joinGameMessage));
        } else {
            hardDestructConnection(user.getId());
            sessions.put(user.getId(), new GameSession(webSocketSession));
            GamePlayerStatus gamePlayerStatus = initStatusGame(joinGameMessage.getGameType(), joinGameMessage.getStepId(), gameId);
            statusGames.put(user.getId(), gamePlayerStatus);
            sendMessageToUser(user.getId(), new DeliveryStatus(new DeliveryStatus.Payload("NEW_OK")));
            logger.info(String.format("joinGame: correct join, joinGameMessage: %s\n", joinGameMessage));
        }
    }

    private GamePlayerStatus initStatusGame(String gameType, Long stepId, Long gameId) throws IOException {

        String correctAnswer;
        //Получаем все пары корректных ответов
        MatchAnswerList correctMathPairs;
        ChainAnswerList chainAnswerList;
        int totalAnswers;
        switch (gameType) {
            case "match":
                correctAnswer = iAnswerOnMatchDAO.findAnswerByGameId(gameId);
                correctMathPairs = gson.fromJson(correctAnswer, MatchAnswerList.class);
                totalAnswers = correctMathPairs.getData().size();
                break;
            case "chain":
                correctAnswer = iAnswerOnChainDAO.findAnswerByGameId(gameId);
                chainAnswerList = gson.fromJson(correctAnswer, ChainAnswerList.class);
                totalAnswers = 1;
                break;
            case "question":
                correctAnswer = iAnswerOnQuestionDAO.findAnswerByGameId(gameId);
                totalAnswers = 1;
                break;
            default:
                throw new IOException(String.format("Unable to find answer for the task type = {}, step id = {}, game id ={}",
                        gameType, stepId, gameId));
        }

        int correctAnswers = 0;

        String task = "условие задачи";//todo: возможно, понадобится   в будущем

        return new GamePlayerStatus(gameType,
                correctAnswers,
                totalAnswers,
                task,
                stepId,
                gameId,
                correctAnswer);
    }


    @Override
    public boolean isStepAllowed(Long stepId,
                                 Long userId) throws RuntimeException { //todo: внести правильную логику проверки доступности интерактива для прохождения
//        UserState currentUserState = userService.getCurrentUserState(userId);
//        if (!currentUserState.getStepId().equals(stepId)) {
//            throw new RuntimeException("Данный шаг не может быть достигнут");
//        }
        return true;
    }

    @Override
    public String getTurnData() {
        return null;
    }

    @Override
    public boolean isCompletedGame(Long userId) {
        int correctAnswers = statusGames.get(userId).getCorrectAnswers();
        int totalQuestions = statusGames.get(userId).getTotalQuestions();
        return Objects.equals(correctAnswers, totalQuestions);
    }

    @Override
    public void clearStateAfterCompletedGame(User user) throws IOException {
        if (sessions.containsKey(user.getId())) {
            if (sessions.get(user.getId()).isGameCompleted()) {
                hardDestructConnection(user.getId());
            } else {
                softDestructConnection(user);
            }
        }
    }


    private void softDestructConnection(User user) throws IOException {
        sessions.get(user.getId()).setLocalDateTime(LocalDateTime.now());
        sessions.get(user.getId()).getWebSocketSession().close();
    }

    private void hardDestructConnection(Long userId) throws IOException {
        if (sessions.containsKey(userId)) {
            if (sessions.get(userId).getWebSocketSession().isOpen()) {
                sessions.get(userId).getWebSocketSession().close();
            }
            sessions.remove(userId);
        }
        statusGames.remove(userId);
    }

    @Override
    public void handleGameTurn(Turn turn, WebSocketSession webSocketSession, User user) throws IOException {
        isStepAllowed(statusGames.get(user.getId()).getStepId(), user.getId());
        String type = statusGames.get(user.getId()).getType();//достаем тип игры
        boolean result = false;
        String correctAnswer = statusGames.get(user.getId()).getCorrectAnswer();
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
                    System.out.println("---------------ERROR! WRONG GAME TYPE!------------");
        }
        TurnResult turnResult;
        if (result) {
            turnResult = new TurnResult(new TurnResult.Payload("true"));
            //увеличиваем количество верных ответов на 1
            statusGames.get(user.getId()).setCorrectAnswers(statusGames.get(user.getId()).getCorrectAnswers() + 1);
            //Сохраняем шаг пользователя на случай разрыва сессии
            if (sessions.get(user.getId()).getTurns().size() > 0) {
                sessions.get(user.getId()).getTurns().add(turn);
            } else {
                List<Turn> list = new ArrayList<>();
                list.add(turn);
                sessions.get(user.getId()).setTurns(list);
            }
            logger.info(String.format("handleGameTurn: turn correct, turn: %s\n", turn));
        } else {
            turnResult = new TurnResult(new TurnResult.Payload("false"));
            logger.info(String.format("handleGameTurn: turn incorrect, turn: %s\n", turn));
        }

        sendMessageToUser(user.getId(), turnResult);
        //Проверка на окончание игры
        if (isCompletedGame(user.getId())) {
            int score = 500;  //todo: придумать получаемое количество очков пооригинальнее

            Long stepId = statusGames.get(user.getId()).getStepId();
            UserState stepsState = iUserStateDAO.getCurrentStateByStepId(stepId);
            stepsState.setUserId(user.getId());

            if (iUserService.isStateCurrent(stepsState) == 1) {
                iUserDAO.updateScore(score, user.getId());
                //Проверить,не заслужил ли пользователь новую ачивку?
                Reward reward = iRewardService.updateRewardsByUser(user.getId());
                sendMessageToUser(user.getId(), new GameCompleted(new GameCompleted.Payload(score, reward)));
                logger.info("handleGameTurn: updated score\n");
            }

            //проставляем флаг, что игра завершена
            sessions.get(user.getId()).setGameCompleted(Boolean.TRUE);
        }
    }

    @Override
    public void sendMessageToUser(Long userId, Message message) throws IOException {
        if (isConnected(userId)) {
            try {
                logger.info(String.format("Sending message = %s\n", objectMapper.writeValueAsString(message)));
                sessions.get(userId).getWebSocketSession().sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (Exception ex) {
                logger.info(String.format("sendMessageToUser: unable to send message, message: %s\n", message));
                throw new IOException("Unable to send message", ex);
            }
        } else {
            logger.info(String.format("sendMessageToUser: unable to send message - user not connected, message: %s\n", message));
            throw new IOException("Unable to send message");
        }
    }

    @Override
    public boolean isConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).getWebSocketSession().isOpen();
    }

    private class GameDispatcher implements Runnable {

        @Override
        public void run() {
            final long MAXIMUM_TIME_WITHOUT_CONNECTION = 100;
            synchronized (sessions) {
                for (Map.Entry<Long, GameSession> game : sessions.entrySet()) {
                    if (game.getValue().getLocalDateTime() != null
                            && Duration.between(game.getValue().getLocalDateTime(), LocalDateTime.now()).toMinutes() > MAXIMUM_TIME_WITHOUT_CONNECTION) {
                        try {
                            hardDestructConnection(game.getKey());
                        } catch (IOException e) {
                            logger.warn("Error while deleting the game status info");
                        }
                    }
                }
            }
        }
    }
}
