package com.itberries.technopark.itberries.websocket.games;

import com.itberries.technopark.itberries.websocket.games.models.MPGame;

import java.util.List;

public interface IMPGenerateGameService {
    List<MPGame> getMultiPlayerGamesData();
}
