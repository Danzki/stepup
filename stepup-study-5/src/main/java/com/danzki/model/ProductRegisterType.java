package com.danzki.model;

import jakarta.persistence.*;

@Entity
@Table(
   name = "tpp_ref_product_register_type"
)
public class ProductRegisterType {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "product_register_type"
   )
   @SequenceGenerator(
      name = "product_register_type",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "internal_id",
      nullable = false
   )
   private long id;
   @Column(
      name = "value",
      nullable = false
   )
   private String value;
   @Column(
      name = "register_type_name",
      nullable = false,
      unique = true
   )
   private String registerTypeName;
   @ManyToOne(
      cascade = {CascadeType.DETACH}
   )
   @JoinColumn(
      name = "product_class_code",
      referencedColumnName = "glb_code"
   )
   private ProductClass productClass;
   @OneToOne(
      cascade = {CascadeType.DETACH}
   )
   @JoinColumn(
      name = "account_type",
      referencedColumnName = "value"
   )
   private AccountType accountType;

   public long getId() {
      return this.id;
   }

   public String getValue() {
      return this.value;
   }

   public String getRegisterTypeName() {
      return this.registerTypeName;
   }

   public ProductClass getProductClass() {
      return this.productClass;
   }

   public AccountType getAccountType() {
      return this.accountType;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setRegisterTypeName(String registerTypeName) {
      this.registerTypeName = registerTypeName;
   }

   public void setProductClass(ProductClass productClass) {
      this.productClass = productClass;
   }

   public void setAccountType(AccountType accountType) {
      this.accountType = accountType;
   }
}
