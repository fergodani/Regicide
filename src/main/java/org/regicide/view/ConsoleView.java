package org.regicide.view;

import org.regicide.models.Game;
import org.regicide.models.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements UserInterface {

   Scanner scanner = new Scanner(System.in);
   private Game game;

   public ConsoleView(Game game) {
      this.game = game;
   }
   @Override
   public void updateInfo() {
      System.out.println("### Taverna: " + game.getTavern().size() + " - Descarte: " + game.getDiscard().size() + " - Castillo: " + game.getCastle().size() + " ###");
      System.out.println("\n### Enemigo ###");
      System.out.println(game.getCurrentFace().toString());
      System.out.println("### Vida: " + game.getCurrentFace().getHealth() + " - Ataque: " + game.getCurrentFace().getDamage() + " ###");
      showHand();
   }

   private void showHand() {
      System.out.println("\n### Tu mano ###");
      for (int i = 0; i < game.getHand().size(); i++) {
         System.out.print(i + ":" + game.getHand().get(i) + " ");
      }
      System.out.println();
   }

   @Override
   public void showMessage(String message) {
      System.out.println(message);
   }

   @Override
   public void showCardsDrawn(List<Card> cards) {
      System.out.println("Has robado " + cards.size() + " carta(s):");
      for (Card card : cards) {
         System.out.println(card);
      }
   }

   @Override
   public List<Card> playCards() {
      System.out.print("Elige las cartas que quieras jugar (separados por espacios): ");

      String input = scanner.nextLine();

      // Procesar los índices introducidos
      String[] indicesStr = input.split(" ");
      List<Integer> indices = new ArrayList<>();
      boolean indicesValidos = true;

      // Verificar que todos los índices sean válidos
      for (String str : indicesStr) {
         try {
            int indice = Integer.parseInt(str);
            if (indice >= 0 && indice < game.getHand().size()) {
               indices.add(indice);
            } else {
               System.out.println("Índice " + indice + " fuera de rango.");
               indicesValidos = false;
               break;
            }
         } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Asegúrate de ingresar números.");
            indicesValidos = false;
            break;
         }
      }

      if (indicesValidos) {
         List<Card> cards = new ArrayList<>();
         for (int index : indices) {
            Card card = game.getHand().get(index);
            cards.add(card);
         }
         return cards;
      }
      return playCards();
   }
}
