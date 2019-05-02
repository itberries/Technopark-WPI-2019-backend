package com.itberries.technopark.itberries.websocket.games.dao;

import com.itberries.technopark.itberries.websocket.games.models.MPGame;

import java.util.List;

public interface IInteracriveGameDao {
    //Получаем по n-игр каждого типа ( по 3)
    List<MPGame> getTasks();
}
