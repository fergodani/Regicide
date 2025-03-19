package org.regicide.models;

public enum Suit {
   HEARTHS("H"), DIAMONDS("D"), CLUBS("C"), SPADES("S");

   private final String symbol;

   Suit(String symbol) {
      this.symbol = symbol;
   }

   public String getSymbol() {
      return symbol;
   }
}
