package com.itberries.technopark.itberries.websocket.games;

import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.websocket.events.JoinGame;
import com.itberries.technopark.itberries.websocket.events.Message;
import com.itberries.technopark.itberries.websocket.events.Turn;
import com.itberries.technopark.itberries.websocket.events.TurnMatch;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface IGamePlayService {

    /**
     * Открыть сокет для
     * общения с игроком
     */
    void joinGame(JoinGame joinGameMessage, WebSocketSession webSocketSession, User user) throws IOException;

    /**
     * Проверка на доступность текущего шага
     * игры
     * @param stepId уникальный ключ шага {@link com.itberries.technopark.itberries.models.Step}
     * @return
     */

    boolean isStepAllowed(Long stepId, Long userId);
    /**
     * Выполнить игровой ход
     * @return
     */
    String getTurnData();

    /**
     * Выполнены ли все задачи?
     * @return
     */
    boolean isCompletedGame(Long userId);

    /**
     * Очистка всех контейнеров
     * после завершения игры
     * или в случае разрыва соединения
     */
    void clearStateAfterCompletedGame(User user) throws IOException;

    /**
     * Обработчик payload в message для
     * любого шага игры
     * @param turn шаг
     * @param webSocketSession веб сокет сессия
     * @param user пользователь
     */
    void handleGameTurn(Turn turn, WebSocketSession webSocketSession, User user) throws IOException;

    /**
     * Отправка сообщения по сессии пользователю
     * @param userId уникальный идентификатор пользователя
     * @param message сообщение
     * @throws IOException
     */
    void sendMessageToUser(Long userId, Message message) throws IOException;


    /**
     * Проверка на то, что сессия пользователя жива
     * @param userId уникальный идентификатор пользователя
     * @return true/false (подключен  или не подключен)
     */
    boolean isConnected(Long userId);
}
