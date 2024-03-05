package com.danzki.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(
   name = "agreements"
)
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

   public long getId() {
      return this.id;
   }

   public Product getProduct() {
      return this.product;
   }

   public String getGeneralAgreementId() {
      return this.generalAgreementId;
   }

   public String getSupplementaryAgreementId() {
      return this.supplementaryAgreementId;
   }

   public String getArrangementType() {
      return this.arrangementType;
   }

   public long getShedulerJobId() {
      return this.shedulerJobId;
   }

   public String getNumber() {
      return this.number;
   }

   public Date getOpeningDate() {
      return this.openingDate;
   }

   public Date getClosingDate() {
      return this.closingDate;
   }

   public Date getCancelDate() {
      return this.cancelDate;
   }

   public int getValidityDuration() {
      return this.validityDuration;
   }

   public String getCancellationReason() {
      return this.cancellationReason;
   }

   public Status getStatus() {
      return this.status;
   }

   public Date getInterestCalculationDate() {
      return this.interestCalculationDate;
   }

   public float getInterestRate() {
      return this.interestRate;
   }

   public float getCoefficient() {
      return this.coefficient;
   }

   public String getCoefficientAction() {
      return this.coefficientAction;
   }

   public float getMinimumInterestRate() {
      return this.minimumInterestRate;
   }

   public String getMinimumInterestRateCoefficient() {
      return this.minimumInterestRateCoefficient;
   }

   public String getMinimumInterestRateCoefficientAction() {
      return this.minimumInterestRateCoefficientAction;
   }

   public float getMaximumInterestRate() {
      return this.maximumInterestRate;
   }

   public String getMaximumInterestRateCoefficient() {
      return this.maximumInterestRateCoefficient;
   }

   public String getMaximumInterestRateCoefficientAction() {
      return this.maximumInterestRateCoefficientAction;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public void setGeneralAgreementId(String generalAgreementId) {
      this.generalAgreementId = generalAgreementId;
   }

   public void setSupplementaryAgreementId(String supplementaryAgreementId) {
      this.supplementaryAgreementId = supplementaryAgreementId;
   }

   public void setArrangementType(String arrangementType) {
      this.arrangementType = arrangementType;
   }

   public void setShedulerJobId(long shedulerJobId) {
      this.shedulerJobId = shedulerJobId;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public void setOpeningDate(Date openingDate) {
      this.openingDate = openingDate;
   }

   public void setClosingDate(Date closingDate) {
      this.closingDate = closingDate;
   }

   public void setCancelDate(Date cancelDate) {
      this.cancelDate = cancelDate;
   }

   public void setValidityDuration(int validityDuration) {
      this.validityDuration = validityDuration;
   }

   public void setCancellationReason(String cancellationReason) {
      this.cancellationReason = cancellationReason;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public void setInterestCalculationDate(Date interestCalculationDate) {
      this.interestCalculationDate = interestCalculationDate;
   }

   public void setInterestRate(float interestRate) {
      this.interestRate = interestRate;
   }

   public void setCoefficient(float coefficient) {
      this.coefficient = coefficient;
   }

   public void setCoefficientAction(String coefficientAction) {
      this.coefficientAction = coefficientAction;
   }

   public void setMinimumInterestRate(float minimumInterestRate) {
      this.minimumInterestRate = minimumInterestRate;
   }

   public void setMinimumInterestRateCoefficient(String minimumInterestRateCoefficient) {
      this.minimumInterestRateCoefficient = minimumInterestRateCoefficient;
   }

   public void setMinimumInterestRateCoefficientAction(String minimumInterestRateCoefficientAction) {
      this.minimumInterestRateCoefficientAction = minimumInterestRateCoefficientAction;
   }

   public void setMaximumInterestRate(float maximumInterestRate) {
      this.maximumInterestRate = maximumInterestRate;
   }

   public void setMaximumInterestRateCoefficient(String maximumInterestRateCoefficient) {
      this.maximumInterestRateCoefficient = maximumInterestRateCoefficient;
   }

   public void setMaximumInterestRateCoefficientAction(String maximumInterestRateCoefficientAction) {
      this.maximumInterestRateCoefficientAction = maximumInterestRateCoefficientAction;
   }
}
