package com.danzki.model;

public enum RateType {
   DIFF {
      public String getRateTypeCode() {
         return "0";
      }
   },
   PROG {
      public String getRateTypeCode() {
         return "1";
      }
   };

   public String getRateTypeCode() {
      return null;
   }

   public static RateType getRateTypeCode(String sign) {
      if (sign.equals(PROG.getRateTypeCode())) {
         return PROG;
      } else {
         return sign.equals(DIFF.getRateTypeCode()) ? DIFF : null;
      }
   }

   // $FF: synthetic method
   private static RateType[] $values() {
      return new RateType[]{DIFF, PROG};
   }
}
