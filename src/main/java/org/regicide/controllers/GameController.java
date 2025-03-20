package org.regicide.controllers;

import org.regicide.models.Card;
import org.regicide.models.Game;
import org.regicide.view.UserInterface;

import java.util.List;

public class GameController {

   private Game game;
   private UserInterface ui;

   public GameController(UserInterface userInterface) {
      this.game = new Game();
      this.ui = userInterface;
   }

   public void start() {
      while (!game.isFinished()) {

         List<Card> cardsPlayed;
         int damageDone = 0;
         do {
            ui.updateInfo();
            cardsPlayed = ui.playCards();
            damageDone = game.attack(cardsPlayed); // TODO: no le baja la vida al enemigo
            if (damageDone == 0)
               ui.showMessage("Por favor, haz una jugada válida");
         } while (damageDone == 0);
         ui.showMessage("Has hecho " + damageDone + " puntos de daño");
         cardsPlayed.clear();
         if (game.checkEnemy()) {
            ui.showMessage("Enemigo derrotado");
            if (!game.checkFinish()) {
               game.nextEnemy();
            }
         } else {
            boolean defense = false;
            do {
               ui.updateInfo();
               cardsPlayed = ui.playCards();
               defense = game.defend(cardsPlayed);
               if (!defense)
                  ui.showMessage("No te has defendido con suficientes puntos");
            } while (!game.defend(cardsPlayed)); // TODO: Mostrar que ahora va la defensa
         }

      }
      ui.showMessage("Has ganado");
   }
}
