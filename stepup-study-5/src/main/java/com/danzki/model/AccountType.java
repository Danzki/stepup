package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
   name = "tpp_ref_account_type"
)
@Getter
@Setter
public class AccountType {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "account_type_seq"
   )
   @SequenceGenerator(
      name = "account_type_seq",
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
      nullable = false,
      unique = true
   )
   private String value;

}
