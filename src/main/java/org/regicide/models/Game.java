package org.regicide.models;

import org.regicide.models.Card;
import org.regicide.models.Face;
import org.regicide.models.Suit;

import java.util.*;

public class Game {
   Scanner scanner = new Scanner(System.in);

   private Stack<Card> tavern;
   private List<Card> hand;
   private Stack<Card> discard;
   private Stack<Card> castle;
   private boolean isFinished = false;

   private Face currentFace;

   public Game() {
      this.tavern = new Stack<>();
      this.hand = new ArrayList<>();
      this.discard = new Stack<>();
      this.castle = new Stack<>();
      initCastle();
      initTavern();
      nextEnemy();
      draw(8);
   }

   /*
   public void start() {
      while (true) {
         showInfo();
         List<Card> cardsPlayed = playCards();
         attack(cardsPlayed);
         if (currentFace.getHealth() <= 0) {
            System.out.println("Enemigo derrotado");
            if (currentFace.getHealth() == 0) {
               tavern.push(currentFace);
            } else {
               discard.push(currentFace);
            }
            if (!castle.isEmpty()) {
               nextEnemy();
            } else {
               System.out.println("Felicidades, has ganado!!!");
               return;
            }
         } else {
            //defend();
         }

         System.out.println("\n*****************************\n\n");
      }
   }

    */

   public boolean checkEnemy() {
      if (getCurrentFace().getHealth() <= 0) {

         if (getCurrentFace().getHealth() == 0) {
            getTavern().push(currentFace);
         } else {
            discard.push(currentFace);
         }
         return true;
      }
      return false;
   }

   public boolean checkFinish() {
      if (castle.isEmpty()) {
         this.isFinished = true;
         return true;
      }
      return false;
   }

   private boolean checkCards(List<Card> cards) {
      if (cards.size() == 1) return true;
      boolean isAce = false;
      for (Card card : cards) {
         if (card.getValue() == 1) isAce = true;
      }
      int totalDamage = cards.stream()
              .mapToInt(Card::getValue)
              .sum();

      if (totalDamage <= 10) {
         // Comprobar que son iguales sus valores
         boolean areSameValue = true;
         int firstValue = cards.get(0).getValue();
         for (Card card : cards) {
            if (card.getValue() != firstValue) {
               areSameValue = false;
            }
         }
         if (!areSameValue) {
            if (isAce && cards.size() == 2) {
               return true;
            }
            System.out.println("No se pueden jugar esas cartas juntas");
            //showHand();
            return false;

         }
         return true;
      } else {
         if (cards.size() == 2 && isAce) {
            return true;
         }
         System.out.println("No se pueden jugar esas cartas juntas");
         //showHand();
         return false;
      }
   }

   public int attack(List<Card> cardsPlayed) {
      if (!checkCards(cardsPlayed)) {
         return 0;
      }
      int totalDamage = cardsPlayed.stream()
              .mapToInt(Card::getValue)
              .sum();
      List<Suit> suitsApplied = new ArrayList<>();
      for (Card card : cardsPlayed) {
         switch (card.getSuit()) {
            case CLUBS: {
               if (currentFace.getSuit() == Suit.CLUBS) break;
               if (suitsApplied.contains(Suit.CLUBS)) break;
               totalDamage *= 2;
               suitsApplied.add(Suit.CLUBS);
               break;
            }
            case SPADES: {
               if (currentFace.getSuit() == Suit.SPADES) break;
               if (suitsApplied.contains(Suit.SPADES)) break;
               currentFace.defend(totalDamage);
               suitsApplied.add(Suit.SPADES);
               break;
            }
            case HEARTHS: {
               if (currentFace.getSuit() == Suit.HEARTHS) break;
               if (suitsApplied.contains(Suit.HEARTHS)) break;
               this.heal(totalDamage);
               suitsApplied.add(Suit.HEARTHS);
               break;
            }
            case DIAMONDS: {
               if (currentFace.getSuit() == Suit.DIAMONDS) break;
               if (suitsApplied.contains(Suit.DIAMONDS)) break;
               this.draw(totalDamage);
               suitsApplied.add(Suit.DIAMONDS);
               break;
            }
         }
      }

      currentFace.damage(totalDamage);
      for (Card card : cardsPlayed) {
         hand.remove(card);
      }
      discard.addAll(cardsPlayed);

      return totalDamage;
   }

   public boolean defend(List<Card> cardsPlayed) {
      if (currentFace.getDamage() == 0) return true;

      int defendPoints = 0;
      for (Card card : cardsPlayed) {
         defendPoints += card.getValue();
      }
      if (defendPoints < currentFace.getDamage()) {
         return false;
      }
      for (Card card : cardsPlayed) {
         hand.remove(card);
      }
      discard.addAll(cardsPlayed);
      return true;

   }

   /*
   private void showInfo() {
      System.out.println("### Taverna: " + tavern.size() + " - Descarte: " + discard.size() + " - Castillo: " + castle.size() + " ###");
      System.out.println("\n### Enemigo ###");
      System.out.println(this.currentFace.toString());
      System.out.println("### Vida: " + currentFace.getHealth() + " - Ataque: " + currentFace.getDamage() + " ###");
      showHand();
   }



   private void showHand() {
      System.out.println("\n### Tu mano ###");
      for (int i = 0; i < this.hand.size(); i++) {
         System.out.print(i + ":" + this.hand.get(i) + " ");
      }
      System.out.println();
      System.out.print("Elige las cartas que quieras jugar (separados por espacios): ");
   }

    */

   private List<Card> playCards() {
      String input = scanner.nextLine();

      // Procesar los índices introducidos
      String[] indicesStr = input.split(" ");
      List<Integer> indices = new ArrayList<>();
      boolean indicesValidos = true;

      // Verificar que todos los índices sean válidos
      for (String str : indicesStr) {
         try {
            int indice = Integer.parseInt(str);
            if (indice >= 0 && indice < hand.size()) {
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
         boolean isAce = false;
         Collections.sort(indices, Comparator.reverseOrder());
         for (int index : indices) {
            Card card = hand.get(index);
            cards.add(card);
            if (card.getValue() == 1) isAce = true;
         }
         int totalDamage = cards.stream()
                 .mapToInt(Card::getValue)
                 .sum();
         if (cards.size() > 1) {
            if (totalDamage <= 10) {
               // Comprobar que son iguales sus valores
               boolean areSameValue = true;
               int firstValue = cards.get(0).getValue();
               for (Card card : cards) {
                  if (card.getValue() != firstValue) {
                     areSameValue = false;
                  }
               }
               if (!areSameValue) {
                  if (isAce && cards.size() == 2) {
                     return cards;
                  }
                  //System.out.println("No se pueden jugar esas cartas juntas");
                  //showHand();
                  return playCards();

               }
               return cards;
            } else {
               if (cards.size() == 2 && isAce) {
                  return cards;
               }
               //System.out.println("No se pueden jugar esas cartas juntas");
               //showHand();
               return playCards();
            }
         }

         return cards;
      } else {
         System.out.println("Introduce índices válidos");
         return playCards();
      }
   }
   /*
   private List<Card> playCards() {
      while (true) {
         System.out.print("Elige el índice de la carta que quieres jugar: ");
         int index;
         try {
            index = scanner.nextInt();
            if (index < 0 || index >= this.hand.size()) {
               System.out.println("Índice inválido, intenta de nuevo.");
               continue;
            }
         } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Introduce un número.");
            scanner.next(); // Limpiar el buffer del scanner
            continue;
         }
         scanner.nextLine();
         Card cardPlayed = this.hand.remove(index);
         System.out.println("Has jugado: " + cardPlayed + "\n");
         return cardPlayed;
      }
   }

    */

   private void initCastle() {
      Card jacks[] = {
              new Card(Suit.CLUBS, 10),
              new Card(Suit.HEARTHS, 10),
              new Card(Suit.DIAMONDS, 10),
              new Card(Suit.SPADES, 10),
      };
      Card queens[] = {
              new Card(Suit.CLUBS, 15),
              new Card(Suit.HEARTHS, 15),
              new Card(Suit.DIAMONDS, 15),
              new Card(Suit.SPADES, 15),
      };
      Card kings[] = {
              new Card(Suit.CLUBS, 20),
              new Card(Suit.HEARTHS, 20),
              new Card(Suit.DIAMONDS, 20),
              new Card(Suit.SPADES, 20),
      };
      List<Integer> numbers = new ArrayList<>(4);
      Random random = new Random();

      for (int i = 0; i < 4; i++) {
         int index = random.nextInt(4);
         while (numbers.contains(index)) {
            index = random.nextInt(4);
         }
         numbers.add(index);
         this.castle.push(kings[index]);
      }
      numbers.clear();
      for (int i = 0; i < 4; i++) {
         int index = random.nextInt(4);
         while (numbers.contains(index)) {
            index = random.nextInt(4);
         }
         numbers.add(index);
         this.castle.push(queens[index]);
      }
      numbers.clear();
      for (int i = 0; i < 4; i++) {
         int index = random.nextInt(4);
         while (numbers.contains(index)) {
            index = random.nextInt(4);
         }
         numbers.add(index);
         this.castle.push(jacks[index]);
      }
   }

   private void initTavern() {
      Suit[] suits = Suit.values();
      for (Suit suit : suits) {
         for (int i = 1; i <= 10; i++) { // As (1) hasta 10
            this.tavern.push(new Card(suit, i));
         }
      }
      Collections.shuffle(this.tavern);
   }

   private void draw(int quantity) {
      int count = 0;
      for (int i = 0; i < quantity; i++) {
         if (hand.size() == 8) {
            System.out.println("Has robado " + count + " cartas");
            return;
         }
         count++;
         this.hand.add(this.tavern.pop());
      }
      System.out.println("Has robado " + count + " cartas");
   }

   private void heal(int quantity) {
      Collections.shuffle(this.discard);
      int count = 0;
      for (int i = 0; i < quantity; i++) {
         if (this.discard.isEmpty()) {
            System.out.println("Te has curado " + count + " cartas");
            return;
         }
         count++;
         this.tavern.insertElementAt(this.discard.pop(), this.tavern.size() - 1);
      }
      System.out.println("Te has curado " + count + " cartas");
   }

   public void nextEnemy() {
      this.currentFace = new Face(this.castle.pop());
   }

   public Stack<Card> getTavern() {
      return tavern;
   }

   public List<Card> getHand() {
      return hand;
   }

   public Stack<Card> getDiscard() {
      return discard;
   }

   public Stack<Card> getCastle() {
      return castle;
   }

   public Face getCurrentFace() {
      return currentFace;
   }

   public boolean isFinished() {
      return isFinished;
   }

   public void setFinished(boolean finished) {
      isFinished = finished;
   }
}
