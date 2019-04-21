package com.itberries.technopark.itberries.websocket.games.services.strategies.models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChainAnswerList {
    List<String> data = new ArrayList<>();

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ChainAnswerList() {
    }

    public static void main(String[] args) {
        ChainAnswerList chainAnswerList = new ChainAnswerList();
        List<String> data = new ArrayList<>();
        data.add("http");
        data.add("://");
        data.add("circ");
        data.add(".org");
        data.add("/");
        data.add("slon");
        data.add(".txt");
        chainAnswerList.setData(data);

        Gson gson = new Gson();
        System.out.println(gson.toJson(chainAnswerList));
    }
}
