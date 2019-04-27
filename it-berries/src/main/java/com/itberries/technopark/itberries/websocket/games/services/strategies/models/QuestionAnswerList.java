package com.itberries.technopark.itberries.websocket.games.services.strategies.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerList {
    List<String> data;

    public QuestionAnswerList(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("7");
        list.add("6");
        list.add("5");
        list.add("4");
        QuestionAnswerList questionAnswerList = new QuestionAnswerList(list);
        Gson gson = new Gson();
        System.out.println(gson.toJson(questionAnswerList));
    }
}
