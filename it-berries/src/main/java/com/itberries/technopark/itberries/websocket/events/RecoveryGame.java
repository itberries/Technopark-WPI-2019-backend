package com.itberries.technopark.itberries.websocket.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;


/**
 * Сообщение отправляемое на фронт в случае восстановления
 * сессии в течение 100 минтут
 */
public class RecoveryGame extends Message {
    /**
     * Значащая информация для конкретного
     * сообщения
     */
    private final Payload payload;

    @JsonCreator
    public RecoveryGame(@JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }


    public Payload getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "DeliveryStatus{" +
                "payload=" + payload +
                '}';
    }

    public static class Payload {


        private final String result;
        private final List<ImmutablePair<String, String>> turns;
        @JsonCreator
        public Payload(@JsonProperty("result") String result, @JsonProperty("turns") List<ImmutablePair<String, String>> turns) {
            this.result = result;
            this.turns = turns;
        }

        @Override
        public String toString() {
            return "Payload{" +
                    "result='" + result + '\'' +
                    ", turns=" + turns +
                    '}';
        }

        public String getResult() {
            return result;
        }

        public List<ImmutablePair<String, String>> getTurns() {
            return turns;
        }
    }
}
