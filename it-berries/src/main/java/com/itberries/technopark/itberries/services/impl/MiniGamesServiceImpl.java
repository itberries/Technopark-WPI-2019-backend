package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IMiniGamesDAO;
import com.itberries.technopark.itberries.models.CardInteractive;
import com.itberries.technopark.itberries.services.IMiniGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MiniGamesServiceImpl implements IMiniGamesService {

    private IMiniGamesDAO iMiniGamesDAO;

    @Autowired
    public MiniGamesServiceImpl(IMiniGamesDAO iMiniGamesDAO) {
        this.iMiniGamesDAO = iMiniGamesDAO;
    }

    @Override
    public List<Long> getGamesIdByStepId(Long stepId) {
        return iMiniGamesDAO.getGamesIdByStepId(stepId);
    }

    @Override
    public String getGameTypeByGameId(Long gameId) {
        return iMiniGamesDAO.getGameTypeByGameId(gameId);
    }

    @Override
    public List<CardInteractive> getCardsByStepId(Long stepId) {
        return iMiniGamesDAO.getCardsByStepId(stepId);
    }
}
