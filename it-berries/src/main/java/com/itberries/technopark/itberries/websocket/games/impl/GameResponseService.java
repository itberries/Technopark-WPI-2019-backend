package com.itberries.technopark.itberries.websocket.games.impl;

import com.itberries.technopark.itberries.websocket.events.Turn;
import com.itberries.technopark.itberries.websocket.games.IGameResponseService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameResponseService implements IGameResponseService {
    @Override
    public List<ImmutablePair<String, String>> getMathResponse(List<Turn> turns) {
        List<ImmutablePair<String, String>> pairs = new ArrayList<>();
            for (Turn turn : turns) {
                Map<String, String> data = turn.getPayload().getData();
                String key = data.keySet().stream().findFirst().get();
                pairs.add(new ImmutablePair<>(key, data.get(key)));
            }
        return pairs;
    }
}
