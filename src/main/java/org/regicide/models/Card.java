package org.regicide.models;

public class Card {

   private Suit suit;

   private int value;

   public Card(Suit suit, int value) {
      this.suit = suit;
      this.value = value;
   }

   public Suit getSuit() {
      return suit;
   }

   public void setSuit(Suit suit) {
      this.suit = suit;
   }

   public int getValue() {
      return value;
   }

   public void setValue(int value) {
      this.value = value;
   }

   @Override
   public String toString() {
      String valorStr;
      if (value == 1) {
         valorStr = "A";
      } else {
         valorStr = String.valueOf(value);
      }
      return valorStr + this.suit.getSymbol();
   }
}
