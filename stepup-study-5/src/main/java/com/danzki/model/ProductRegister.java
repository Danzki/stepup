package com.danzki.model;

import jakarta.persistence.*;

@Entity
@Table(
   name = "tpp_product_register"
)
public class ProductRegister {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "product_register_seq"
   )
   @SequenceGenerator(
      name = "product_register_seq",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "id",
      nullable = false
   )
   private long id;
   @OneToOne(
      cascade = {CascadeType.DETACH}
   )
   @JoinColumn(
      name = "type",
      referencedColumnName = "register_type_name"
   )
   private ProductRegisterType productRegisterType;
   @ManyToOne(
      cascade = {CascadeType.ALL}
   )
   @JoinColumn(
      name = "product_id"
   )
   private Product product;
   @ManyToOne(
      cascade = {CascadeType.ALL}
   )
   @JoinColumn(
      name = "account_id"
   )
   private Account account;
   @Column(
      name = "currency_code",
      nullable = false
   )
   private String currencyCode;
   @Enumerated(EnumType.STRING)
   @Column(
      name = "state"
   )
   private State state;
   @Column(
      name = "account_number"
   )
   private String accountNumber;

   public long getId() {
      return this.id;
   }

   public ProductRegisterType getProductRegisterType() {
      return this.productRegisterType;
   }

   public Product getProduct() {
      return this.product;
   }

   public Account getAccount() {
      return this.account;
   }

   public String getCurrencyCode() {
      return this.currencyCode;
   }

   public State getState() {
      return this.state;
   }

   public String getAccountNumber() {
      return this.accountNumber;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setProductRegisterType(ProductRegisterType productRegisterType) {
      this.productRegisterType = productRegisterType;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public void setCurrencyCode(String currencyCode) {
      this.currencyCode = currencyCode;
   }

   public void setState(State state) {
      this.state = state;
   }

   public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
   }
}
