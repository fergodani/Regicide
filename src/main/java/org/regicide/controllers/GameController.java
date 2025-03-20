package org.regicide.controllers;

import org.regicide.models.AttackResult;
import org.regicide.models.Card;
import org.regicide.models.Game;
import org.regicide.view.UserInterface;

import java.util.List;

public class GameController {

   private Game game;
   private UserInterface ui;

   public GameController(UserInterface userInterface, Game game) {
      this.game = game;
      this.ui = userInterface;
   }

   public void start() {
      while (!game.isFinished()) {

         List<Card> cardsPlayed;
         AttackResult attackResult;
         do {
            ui.showMessage("\n*** ATAQUE ***\n");
            ui.updateInfo();
            cardsPlayed = ui.playCards();
            attackResult = game.attack(cardsPlayed);
            if (attackResult.getDamage() == 0) {
               ui.showMessage("Por favor, haz una jugada válida");
            } else {
               showAttackResult(attackResult);
            }
         } while (attackResult.getDamage() == 0);

         cardsPlayed.clear();
         if (game.checkEnemy()) {
            ui.showMessage("Enemigo derrotado");
            if (!game.checkFinish()) {
               game.nextEnemy();
            }
         } else {
            boolean defense = false;
            do {
               ui.showMessage("\n*** DEFENSA ***\n");
               ui.updateInfo();
               if (game.getCurrentFace().getDamage() == 0) {
                  defense = true;
                  continue;
               }
               cardsPlayed = ui.playCards();
               defense = game.defend(cardsPlayed);
               if (!defense)
                  ui.showMessage("No te has defendido con suficientes puntos");
            } while (!defense);
         }
         attackResult.clear();
      }
      ui.showMessage("Has ganado");
   }

   private void showAttackResult(AttackResult result) {
      if (result.getDamage() != 0) ui.showMessage("Has hecho " + result.getDamage() + " puntos de daño");
      if (result.getHeal() != 0) ui.showMessage("Te has curado " + result.getHeal() + " cartas");
      if (result.getDefend() != 0) ui.showMessage("Te defiendes de " + result.getDefend() + " puntos de daño");
      if (!result.getCardsDrawn().isEmpty()) {
         ui.showCardsDrawn(result.getCardsDrawn());
      }
   }
}
