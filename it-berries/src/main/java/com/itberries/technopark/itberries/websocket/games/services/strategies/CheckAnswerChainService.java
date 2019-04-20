package com.itberries.technopark.itberries.websocket.games.services.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itberries.technopark.itberries.websocket.games.dao.IAnswerOnChainDAO;
import com.itberries.technopark.itberries.websocket.games.services.ICheckAnswerService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.ChainAnswerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CheckAnswerChainService implements ICheckAnswerService {

    private IAnswerOnChainDAO iAnswerOnChainDAO;
    private final ObjectMapper objectMapper;
    private Gson gson = new Gson();

    @Autowired
    public CheckAnswerChainService(IAnswerOnChainDAO iAnswerOnChainDAO, ObjectMapper objectMapper) {
        this.iAnswerOnChainDAO = iAnswerOnChainDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean checkAnswerByGameId(Long gameId, String answer) throws IOException {
        String answerByGameId = iAnswerOnChainDAO.findAnswerByGameId(gameId);
        //верный ответ из БД
        ChainAnswerList correctChainAnswerList = gson.fromJson(answerByGameId, ChainAnswerList.class);
        //ответ пользователя
        List<String> userData = gson.fromJson(answer, new TypeToken<List<String>>(){}.getType());
        List<String> correctData = correctChainAnswerList.getData();
        for(int i = 0; i< userData.size(); i++) {
            if(!userData.get(i).equals(correctData.get(i))){
                return false;
            }
        }
        return true;
    }
}
