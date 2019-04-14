package com.itberries.technopark.itberries.websocket.games;

import com.itberries.technopark.itberries.websocket.events.Turn;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

public interface IGameResponseService {
    /**
     * Формируется список с шагами пользователя,  которые он успел сделать
     * до того, как соединение порвалось
     * @param turns список шагов
     * @return список пар в виде 1 байт - 8 бит
     */
    List<ImmutablePair<String, String>> getMathResponse(List<Turn> turns);
}
