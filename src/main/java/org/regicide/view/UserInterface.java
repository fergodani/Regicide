package org.regicide.view;

import org.regicide.models.Card;

import java.util.List;

public interface UserInterface {

   public void updateInfo();
   public void showMessage(String message);
   public void showCardsDrawn(List<Card> cartas);
   public List<Card> playCards();
}
