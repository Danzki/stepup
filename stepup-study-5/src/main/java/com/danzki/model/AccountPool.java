package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
   name = "account_pool"
)
@Getter
@Setter
public class AccountPool {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "account_pool_seq"
   )
   @SequenceGenerator(
      name = "account_pool_seq",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "id",
      nullable = false
   )
   private long id;
   @Column(
      name = "branch_code",
      nullable = false
   )
   private String branchCode;
   @Column(
      name = "currency_code",
      nullable = false
   )
   private String currencyCode;
   @Column(
      name = "mdm_code",
      nullable = false
   )
   private String mdmCode;
   @Column(
      name = "priority_code",
      nullable = false
   )
   private String priorityCode;
   @Column(
      name = "registry_type_code",
      nullable = false
   )
   private String registryTypeCode;
}
