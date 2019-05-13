package com.itberries.technopark.itberries.websocket.games.models;

import com.google.gson.Gson;
import com.itberries.technopark.itberries.websocket.events.Turn;
import com.itberries.technopark.itberries.websocket.events.TurnChain;
import com.itberries.technopark.itberries.websocket.events.TurnMatch;
import com.itberries.technopark.itberries.websocket.events.TurnQuestion;
import com.itberries.technopark.itberries.websocket.games.impl.MultiUserGameServiceImpl;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerChainService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerMatchService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.CheckAnswerQuestionService;
import com.itberries.technopark.itberries.websocket.games.services.strategies.models.MatchAnswerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class MPGamePlayer implements Cloneable {
    private Long id;
    private LocalDateTime dateTimeStart;
    private List<MPGame> tasks;
    private List<MPGame> resolvedTasks;
    private int currentPosition;
    private static final Logger LOGGER = LoggerFactory.getLogger(MPGamePlayer.class);
    private boolean isWinner;

    public MPGamePlayer(Long id, Integer currentPosition, LocalDateTime dateTimeStart, List<MPGame> tasks)  {
        this.id = id;
        this.dateTimeStart = dateTimeStart;
        this.currentPosition = currentPosition;
        this.tasks = new ArrayList<>();
        for(int i = 0; i<tasks.size(); i++){
            try {
                this.tasks.add(tasks.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        this.resolvedTasks = new ArrayList<>();
        for(int i = 0; i<tasks.size(); i++){
            try {
                this.resolvedTasks.add(tasks.get(i).clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public List<MPGame> getTasks() {
        return tasks;
    }

    public void setTasks(List<MPGame> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public String getCurrentGameType() {
        return tasks.get(currentPosition).getType();
    }

    public String getCurrentGameAnswer() {
        return tasks.get(currentPosition).getAnswer();
    }


    /**
     * В случае верного ответа игрока сдвигаем на
     * одну позицию
     */
    private void movePosition() {
        //сдвигать нужо только в случаеб если игрок ответил на все вопросы предыдущей иры
        MPGame mpGame = tasks.get(currentPosition);
        if (mpGame.getResolved() == Boolean.TRUE) {
            currentPosition += 1;
        }
    }


    public boolean markRightAnswer(Turn turn, boolean result) {
        boolean resolved = Boolean.FALSE;
        Gson gson = new Gson();
        String type = tasks.get(currentPosition).getType();
        switch (type) {
            case "match":
                TurnMatch turnMatch = (TurnMatch) turn;
                final String task = resolvedTasks.get(currentPosition).getTask();
                //все пары
                MatchAnswerList matchAnswerList = gson.fromJson(task, MatchAnswerList.class);
                //ответ пользователя
                Map<String, String> answer = turnMatch.getPayload().getData();
                if (result == Boolean.TRUE) {//если ответ был дан верный, удаляем эту пару
                    List<Map<String, String>> data1 = matchAnswerList.getData();
                    removeData(data1, answer);

                    matchAnswerList.setData(data1);
                    String changedTask = gson.toJson(matchAnswerList);

                    LOGGER.info(String.format("Осталось пар: %s, %s",
                            matchAnswerList.getData().size(),
                            matchAnswerList.getData()));

                    resolvedTasks.get(currentPosition).setTask(changedTask);
                    if (matchAnswerList.getData().isEmpty()) {
                        if (!Boolean.TRUE.equals(tasks.get(currentPosition).getResolved())) {
                            tasks.get(currentPosition).setResolved(Boolean.TRUE);
                            movePosition();
                        }
                        resolved = Boolean.TRUE;

                    }
                }
                break;
            case "chain":
                if (result == Boolean.TRUE) {
                    if (!Boolean.TRUE.equals(tasks.get(currentPosition).getResolved())) {
                        tasks.get(currentPosition).setResolved(Boolean.TRUE);
                        movePosition();
                    }
                    resolved = Boolean.TRUE;
                }
                break;

            case "question":
                if (result == Boolean.TRUE) {

                    if (!Boolean.TRUE.equals(tasks.get(currentPosition).getResolved())) {
                        tasks.get(currentPosition).setResolved(Boolean.TRUE);
                        movePosition();
                    }
                    resolved = Boolean.TRUE;
                }
                break;
            default:
                LOGGER.error("---------------ERROR! WRONG GAME TYPE!------------");

        }
        return resolved;
    }

    private void removeData(List<Map<String, String>> data1, Map<String, String> answer) {
        Optional<Map.Entry<String, String>> first = answer.entrySet().stream().findFirst();
        if (first.isPresent()) {
            Map.Entry<String, String> stringStringEntry = first.get();
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            int idx = -1;
            for (int i = 0; i < data1.size(); i++) {
                Map<String, String> stringStringMap = data1.get(i);
                if (stringStringMap.containsKey(key) && stringStringMap.get(key).equals(value)) {
                    idx = i;
                    break;
                } else if (stringStringMap.containsKey(value) && stringStringMap.get(value).equals(key)) {
                    idx = i;
                    break;
                }
            }

            if (idx != -1) {
                data1.remove(idx);
            }
        }
    }

    /**
     * Пропуск задачи
     */
    public void skipTask() {
        currentPosition += 1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean getCurrentTaskStatus() {
        return tasks.get(currentPosition).getResolved();
    }

    @Override
    public String toString() {
        return "MPGamePlayer{" +
                "id=" + id +
                ", dateTimeStart=" + dateTimeStart +
                ", tasks=" + tasks +
                ", resolvedTasks=" + resolvedTasks +
                ", currentPosition=" + currentPosition +
                ", isWinner=" + isWinner +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
