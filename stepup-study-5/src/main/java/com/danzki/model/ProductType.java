package com.danzki.model;

public enum ProductType {
   NSO {
      public String getName() {
         return "НСО";
      }
   },
   SMO {
      public String getName() {
         return "СМО";
      }
   },
   EZHO {
      public String getName() {
         return "ЕЖО";
      }
   },
   DBDS {
      public String getName() {
         return "ДБДС";
      }
   },
   DOGOVOR {
      public String getName() {
         return "договор";
      }
   };

   public String getName() {
      return null;
   }

   public static ProductType getProductType(String name) {
      if (name.equals(NSO.getName())) {
         return NSO;
      } else if (name.equals(SMO.getName())) {
         return SMO;
      } else if (name.equals(EZHO.getName())) {
         return EZHO;
      } else if (name.equals(DBDS.getName())) {
         return DBDS;
      } else {
         return name.equals(DOGOVOR.getName()) ? DOGOVOR : null;
      }
   }

   // $FF: synthetic method
   private static ProductType[] $values() {
      return new ProductType[]{NSO, SMO, EZHO, DBDS, DOGOVOR};
   }
}
