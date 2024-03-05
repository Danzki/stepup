package com.danzki.model;

public enum CoefficientAction {
   PLUS {
      public String getAction() {
         return "+";
      }
   },
   MINUS {
      public String getAction() {
         return "-";
      }
   };

   public String getAction() {
      return null;
   }

   public static CoefficientAction getCoefficientAction(String sign) {
      if (sign.equals(PLUS.getAction())) {
         return PLUS;
      } else {
         return sign.equals(MINUS.getAction()) ? MINUS : null;
      }
   }

   // $FF: synthetic method
   private static CoefficientAction[] $values() {
      return new CoefficientAction[]{PLUS, MINUS};
   }
}
