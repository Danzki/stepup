package com.danzki.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(
   name = "agreements"
)
@Getter
@Setter
public class Agreement {
   @Id
   @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "agreement_seq"
   )
   @SequenceGenerator(
      name = "agreement_seq",
      sequenceName = "seq_id",
      allocationSize = 1
   )
   @Column(
      name = "id",
      nullable = false
   )
   private long id;
   @ManyToOne(
      cascade = {CascadeType.ALL}
   )
   @JoinColumn(
      name = "product_id"
   )
   private Product product;
   @Column(
      name = "general_agreement_id"
   )
   private String generalAgreementId;
   @Column(
      name = "supplementary_agreement_id"
   )
   private String supplementaryAgreementId;
   @Column(
      name = "arrangement_type"
   )
   private String arrangementType;
   @Column(
      name = "sheduler_job_id"
   )
   private long shedulerJobId;
   @Column(
      name = "number"
   )
   private String number;
   @Column(
      name = "opening_date"
   )
   private Date openingDate;
   @Column(
      name = "closing_date"
   )
   private Date closingDate;
   @Column(
      name = "cancel_date"
   )
   private Date cancelDate;
   @Column(
      name = "validity_duration"
   )
   private int validityDuration;
   @Column(
      name = "cancellation_reason"
   )
   private String cancellationReason;
   @Enumerated(EnumType.STRING)
   @Column(
      name = "status"
   )
   private Status status;
   @Column(
      name = "interest_calculation_date"
   )
   private Date interestCalculationDate;
   @Column(
      name = "interest_rate"
   )
   private float interestRate;
   @Column(
      name = "coefficient"
   )
   private float coefficient;
   @Column(
      name = "coefficient_action"
   )
   private String coefficientAction;
   @Column(
      name = "minimum_interest_rate"
   )
   private float minimumInterestRate;
   @Column(
      name = "minimum_interest_rate_coefficient"
   )
   private String minimumInterestRateCoefficient;
   @Column(
      name = "minimum_interest_rate_coefficient_action"
   )
   private String minimumInterestRateCoefficientAction;
   @Column(
      name = "maximum_interest_rate"
   )
   private float maximumInterestRate;
   @Column(
      name = "maximum_interest_rate_coefficient"
   )
   private String maximumInterestRateCoefficient;
   @Column(
      name = "maximum_interest_rate_coefficient_action"
   )
   private String maximumInterestRateCoefficientAction;

}
