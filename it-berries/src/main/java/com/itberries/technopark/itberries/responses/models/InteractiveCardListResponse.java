package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.Card;
import com.itberries.technopark.itberries.models.CardInteractive;

import java.util.List;
import java.util.Objects;

public class InteractiveCardListResponse {
    /**
     * Тип интерактивной игры
     */
    private String type;

    private List<CardInteractive> interactiveCards;

    public InteractiveCardListResponse(String type, List<CardInteractive> interactiveCards) {
        this.type = type;
        this.interactiveCards = interactiveCards;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CardInteractive> getInteractiveCards() {
        return interactiveCards;
    }

    public void setInteractiveCards(List<CardInteractive> interactiveCards) {
        this.interactiveCards = interactiveCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InteractiveCardListResponse that = (InteractiveCardListResponse) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(interactiveCards, that.interactiveCards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, interactiveCards);
    }
}
