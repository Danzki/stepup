package com.danzki.model;

public enum Status {
   OPEN {
      public String getName() {
         return "Открыт";
      }
   },
   CLOSE {
      public String getName() {
         return "Закрыт";
      }
   };

   public String getName() {
      return null;
   }

   public static Status getStatus(String status) {
      if (status.toLowerCase().equals(OPEN.getName().toLowerCase())) {
         return OPEN;
      } else {
         return status.equals(CLOSE.getName().toLowerCase()) ? CLOSE : null;
      }
   }

   // $FF: synthetic method
   private static Status[] $values() {
      return new Status[]{OPEN, CLOSE};
   }
}
