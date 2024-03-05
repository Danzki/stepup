package com.danzki.model;

import jakarta.persistence.*;

@Entity
@Table(
   name = "account"
)
public class Account {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "account_seq"
   )
   @SequenceGenerator(
      name = "account_seq",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "id",
      nullable = false
   )
   private long id;
   @Column(
      name = "account_num",
      nullable = false
   )
   private String accountNum;
   @ManyToOne(
      cascade = {CascadeType.ALL}
   )
   @JoinColumn(
      name = "account_pool_id"
   )
   private AccountPool accountPool;

   public long getId() {
      return this.id;
   }

   public String getAccountNum() {
      return this.accountNum;
   }

   public AccountPool getAccountPool() {
      return this.accountPool;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setAccountNum(String accountNum) {
      this.accountNum = accountNum;
   }

   public void setAccountPool(AccountPool accountPool) {
      this.accountPool = accountPool;
   }
}
