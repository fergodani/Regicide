package org.regicide;

import org.regicide.controllers.GameController;
import org.regicide.models.Game;
import org.regicide.view.ConsoleView;

public class Main {

   public static void main(String[] args) {
      Game game = new Game();
      GameController controller = new GameController(new ConsoleView(game));
      controller.start();
   }
}
