package org.regicide.models;

import java.util.ArrayList;
import java.util.List;

public class AttackResult {

    private List<Card> cardsDrawn = new ArrayList<>();
    private int damage;
    private int heal;
    private int defend;

    public AttackResult() {}

    public AttackResult(int damage) {
        this.damage = damage;
    }

    public AttackResult(List<Card> cardsDrawn, int damage, int heal, int defend) {
        this.cardsDrawn = cardsDrawn;
        this.heal = heal;
        this.damage = damage;
        this.defend = defend;
    }

    public void clear() {
        this.cardsDrawn = new ArrayList<>();
        this.damage = 0;
        this.heal = 0;
        this.defend = 0;
    }

    public List<Card> getCardsDrawn() {
        return cardsDrawn;
    }

    public void setCardsDrawn(List<Card> cardsDrawn) {
        this.cardsDrawn = cardsDrawn;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHeal() {
        return heal;
    }

    public void setHeal(int heal) {
        this.heal = heal;
    }

    public int getDefend() {
        return defend;
    }

    public void setDefend(int defend) {
        this.defend = defend;
    }
}
