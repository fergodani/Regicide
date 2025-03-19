package org.regicide.models;

public class Face extends Card{

   private int health;
   private int damage;

   public Face(Card card) {
      super(card.getSuit(), card.getValue());
      this.damage = card.getValue();
      switch (card.getValue()) {
         case 10: {
            this.health = 20;
            break;
         }
         case 15: {
            this.health = 30;
            break;
         }
         case 20: {
            this.health = 40;
            break;
         }
         default: {
            throw new RuntimeException("Se ha añadido una carte que no es una figura");
         }
      }
   }

   public void damage(int quantity) {
      this.health -= quantity;
   }

   public void defend(int quantity) {
      System.out.println("Te defiendes de " + quantity + " de daño");
      this.damage -= quantity;
      if (this.damage < 0) this.damage = 0;
   }

   public int getHealth() {
      return health;
   }

   public void setHealth(int health) {
      this.health = health;
   }

   public int getDamage() {
      return damage;
   }

   public void setDamage(int damage) {
      this.damage = damage;
   }

   @Override
   public String toString() {
      String valorStr;
      switch (getValue()) {
         case 10: {
            valorStr = "J";
            break;
         }
         case 15: {
            valorStr = "Q";
            break;
         }
         case 20: {
            valorStr = "K";
            break;
         }
         default: {
            valorStr = String.valueOf(getValue());
         }
      }
      return valorStr + this.getSuit().getSymbol();
   }
}
