package com.danzki.model;

import jakarta.persistence.*;

@Entity
@Table(
   name = "tpp_ref_product_class"
)
public class ProductClass {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "product_class_seq"
   )
   @SequenceGenerator(
      name = "product_class_seq",
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
      name = "glb_code",
      nullable = false,
      unique = true
   )
   private String globalCode;
   @Column(
      name = "glb_name"
   )
   private String globalName;
   @Column(
      name = "product_row_code"
   )
   private String productRowCode;
   @Column(
      name = "product_row_name"
   )
   private String productRowName;
   @Column(
      name = "subclass_code"
   )
   private String subclassCode;
   @Column(
      name = "subclass_name"
   )
   private String subclassName;

   public long getId() {
      return this.id;
   }

   public String getValue() {
      return this.value;
   }

   public String getGlobalCode() {
      return this.globalCode;
   }

   public String getGlobalName() {
      return this.globalName;
   }

   public String getProductRowCode() {
      return this.productRowCode;
   }

   public String getProductRowName() {
      return this.productRowName;
   }

   public String getSubclassCode() {
      return this.subclassCode;
   }

   public String getSubclassName() {
      return this.subclassName;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setGlobalCode(String globalCode) {
      this.globalCode = globalCode;
   }

   public void setGlobalName(String globalName) {
      this.globalName = globalName;
   }

   public void setProductRowCode(String productRowCode) {
      this.productRowCode = productRowCode;
   }

   public void setProductRowName(String productRowName) {
      this.productRowName = productRowName;
   }

   public void setSubclassCode(String subclassCode) {
      this.subclassCode = subclassCode;
   }

   public void setSubclassName(String subclassName) {
      this.subclassName = subclassName;
   }
}
