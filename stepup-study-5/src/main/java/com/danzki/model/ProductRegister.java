package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
   name = "tpp_product_register"
)
@Getter
@Setter
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

}
