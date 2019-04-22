package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.itberries.technopark.itberries.models.Reward;

public class GameCompleted extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public GameCompleted(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "GameCompleted{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload {

        private final Integer score;

        private final Reward reward;

        @JsonCreator
        public Payload(@JsonProperty("score") Integer score,
                       @JsonProperty("reward") Reward reward) {
            this.score = score;
            this.reward = reward;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "score=" + score +
                    ", reward=" + reward +
                    '}';
        }

        public Reward getReward() {
            return reward;
        }

        public Integer getResult() {
            return score;
        }
    }

    public static void main(String[] args) {
        Reward reward = new Reward(3L, "Вы молодец",
                "https://it-berries.ru/rewards/reward1.png");
        GameCompleted gameCompleted = new GameCompleted(new GameCompleted.Payload(400, reward));
        Gson gson = new Gson();
        String s = gson.toJson(gameCompleted);
        System.out.println(s);
    }
}
