package com.danzki.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(
   name = "tpp_product"
)
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

   public static Product of(long id) {
      Product prod = new Product();
      prod.setId(id);
      return prod;
   }

   public long getId() {
      return this.id;
   }

   public List<Agreement> getAgreements() {
      return this.agreements;
   }

   public String getProductCodeId() {
      return this.productCodeId;
   }

   public long getClientId() {
      return this.clientId;
   }

   public String getType() {
      return this.type;
   }

   public String getNumber() {
      return this.number;
   }

   public int getPriority() {
      return this.priority;
   }

   public Date getDateConclusion() {
      return this.dateConclusion;
   }

   public Date getStartDate() {
      return this.startDate;
   }

   public Date getEndDate() {
      return this.endDate;
   }

   public int getDays() {
      return this.days;
   }

   public float getPenaltyRate() {
      return this.penaltyRate;
   }

   public float getNso() {
      return this.nso;
   }

   public float getThresholdAmount() {
      return this.thresholdAmount;
   }

   public String getRequisiteType() {
      return this.requisiteType;
   }

   public String getInterestRateType() {
      return this.interestRateType;
   }

   public float getTaxRate() {
      return this.taxRate;
   }

   public String getReasonClose() {
      return this.reasonClose;
   }

   public State getState() {
      return this.state;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setAgreements(List<Agreement> agreements) {
      this.agreements = agreements;
   }

   public void setProductCodeId(String productCodeId) {
      this.productCodeId = productCodeId;
   }

   public void setClientId(long clientId) {
      this.clientId = clientId;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public void setDateConclusion(Date dateConclusion) {
      this.dateConclusion = dateConclusion;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public void setDays(int days) {
      this.days = days;
   }

   public void setPenaltyRate(float penaltyRate) {
      this.penaltyRate = penaltyRate;
   }

   public void setNso(float nso) {
      this.nso = nso;
   }

   public void setThresholdAmount(float thresholdAmount) {
      this.thresholdAmount = thresholdAmount;
   }

   public void setRequisiteType(String requisiteType) {
      this.requisiteType = requisiteType;
   }

   public void setInterestRateType(String interestRateType) {
      this.interestRateType = interestRateType;
   }

   public void setTaxRate(float taxRate) {
      this.taxRate = taxRate;
   }

   public void setReasonClose(String reasonClose) {
      this.reasonClose = reasonClose;
   }

   public void setState(State state) {
      this.state = state;
   }
}
