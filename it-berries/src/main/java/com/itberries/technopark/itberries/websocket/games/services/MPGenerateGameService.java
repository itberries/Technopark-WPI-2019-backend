package com.itberries.technopark.itberries.websocket.games.services;

import com.itberries.technopark.itberries.websocket.games.IMPGenerateGameService;
import com.itberries.technopark.itberries.websocket.games.dao.IInteracriveGameDao;
import com.itberries.technopark.itberries.websocket.games.models.MPGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MPGenerateGameService implements IMPGenerateGameService {

    private IInteracriveGameDao iInteracriveGameDao;

    @Autowired
    public MPGenerateGameService(IInteracriveGameDao iInteracriveGameDao) {
        this.iInteracriveGameDao = iInteracriveGameDao;
    }

    @Override
    public List<MPGame> getMultiPlayerGamesData() {
        List<MPGame> tasks = iInteracriveGameDao.getTasks();
        return tasks;
    }
}
