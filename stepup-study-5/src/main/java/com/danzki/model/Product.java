package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(
   name = "tpp_product"
)
@Getter
@Setter
public class Product {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "product_seq"
   )
   @SequenceGenerator(
      name = "product_seq",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "id",
      nullable = false
   )
   private long id;
   @OneToMany(
      cascade = {CascadeType.ALL}
   )
   @JoinColumn(
      name = "agreement_id"
   )
   private List<Agreement> agreements;
   @Column(
      name = "product_code_id"
   )
   private String productCodeId;
   @Column(
      name = "client_id"
   )
   private long clientId;
   @Column(
      name = "type"
   )
   private String type;
   @Column(
      name = "number",
      nullable = false,
      unique = true
   )
   private String number;
   @Column(
      name = "priority"
   )
   private int priority;
   @Column(
      name = "date_of_conclusion"
   )
   private Date dateConclusion;
   @Column(
      name = "start_date_time"
   )
   private Date startDate;
   @Column(
      name = "end_date_time"
   )
   private Date endDate;
   @Column(
      name = "days"
   )
   private int days;
   @Column(
      name = "penalty_rate"
   )
   private float penaltyRate;
   @Column(
      name = "nso"
   )
   private float nso;
   @Column(
      name = "threshold_amount"
   )
   private float thresholdAmount;
   @Column(
      name = "requisite_type"
   )
   private String requisiteType;
   @Column(
      name = "interest_rate_type"
   )
   private String interestRateType;
   @Column(
      name = "tax_rate"
   )
   private float taxRate;
   @Column(
      name = "reason_close"
   )
   private String reasonClose;
   @Enumerated(EnumType.STRING)
   @Column(
      name = "state"
   )
   private State state;

}
