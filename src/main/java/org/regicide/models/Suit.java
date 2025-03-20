package org.regicide.models;

public enum Suit {
   HEARTHS("\u2665"), DIAMONDS("\u2660"), CLUBS("\u2666"), SPADES("\u2663");

   private final String symbol;

   Suit(String symbol) {
      this.symbol = symbol;
   }

   public String getSymbol() {
      return symbol;
   }
}
