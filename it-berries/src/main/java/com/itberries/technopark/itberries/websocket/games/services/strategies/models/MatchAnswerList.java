package com.itberries.technopark.itberries.websocket.games.services.strategies.models;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchAnswerList {
    private List<Map<String, String>> data;

    public MatchAnswerList(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public MatchAnswerList() {
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MatchAnswerList{" +
                "data=" + data +
                '}';
    }

    public static void main(String[] args) {
        Gson gson = new Gson();

        List<Map<String, String>> data = new ArrayList<>();
        data.add(ImmutableMap.of("1 байт", "8 бит"));
        data.add(ImmutableMap.of("1 Мбайт", "1024 байта"));
        data.add(ImmutableMap.of("1 Гбайт", "1024 Мбайта"));

        MatchAnswerList matchAnswerList = new MatchAnswerList(data);

        String s = gson.toJson(matchAnswerList);

        gson.fromJson(s, MatchAnswerList.class);
        System.out.println(s);
    }
}
