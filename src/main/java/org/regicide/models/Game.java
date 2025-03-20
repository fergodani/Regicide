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
   AttackResult result = new AttackResult();

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
            return false;

         }
         return true;
      } else {
         if (cards.size() == 2 && isAce) {
            return true;
         }
         return false;
      }
   }

   public AttackResult attack(List<Card> cardsPlayed) {
      if (!checkCards(cardsPlayed)) {
         return new AttackResult(0);
      }
      int totalDamage = cardsPlayed.stream()
              .mapToInt(Card::getValue)
              .sum();
      result = new AttackResult();
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
               result.setDefend(totalDamage);
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
      result.setDamage(totalDamage);
      currentFace.damage(totalDamage);
      for (Card card : cardsPlayed) {
         hand.remove(card);
      }
      discard.addAll(cardsPlayed);

      return result;
   }

   public boolean defend(List<Card> cardsPlayed) {

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

   private void draw(int quantity) {
      int count = 0;
      List<Card> cardsDrawn = new ArrayList<>();
      for (int i = 0; i < quantity; i++) {
         if (hand.size() == 8) {
            result.setCardsDrawn(cardsDrawn);
            return;
         }
         count++;
         Card card = this.tavern.pop();
         cardsDrawn.add(card);
         this.hand.add(card);
      }
      result.setCardsDrawn(cardsDrawn);
   }

   private void heal(int quantity) {
      Collections.shuffle(this.discard);
      int count = 0;
      for (int i = 0; i < quantity; i++) {
         if (this.discard.isEmpty()) {
            result.setHeal(count);
            return;
         }
         count++;
         this.tavern.insertElementAt(this.discard.pop(), this.tavern.size() - 1);
      }
      result.setHeal(count);
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

}
