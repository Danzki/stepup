package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
   name = "tpp_ref_product_class"
)
@Getter
@Setter
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

}
