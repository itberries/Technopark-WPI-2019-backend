package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.itberries.technopark.itberries.websocket.games.models.MPGame;

import java.util.List;

/**
 * В случае старта игры отправляем пользователю
 * id его оппонента и условие  9 заданий
 */
public class MPStartGameMessage extends Message {

    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "MPStartGameMessage{" +
                "payload=" + payload +
                '}';
    }

    @JsonCreator
    public MPStartGameMessage(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        private final Long id;
        private List<MPGame> tasks;

        @JsonCreator
        public Payload(@JsonProperty("id") Long id,
                       @JsonProperty("tasks") List<MPGame> tasks) {
            this.id = id;
            this.tasks = tasks;
        }

        public Long getId() {
            return id;
        }

        public List<MPGame> getTasks() {
            return tasks;
        }

        public void setTasks(List<MPGame> tasks) {
            this.tasks = tasks;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "id=" + id +
                    ", tasks=" + tasks +
                    '}';
        }
    }
}
