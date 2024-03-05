package com.danzki.model;

import jakarta.persistence.*;

@Entity
@Table(
   name = "account_pool"
)
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

   public long getId() {
      return this.id;
   }

   public String getBranchCode() {
      return this.branchCode;
   }

   public String getCurrencyCode() {
      return this.currencyCode;
   }

   public String getMdmCode() {
      return this.mdmCode;
   }

   public String getPriorityCode() {
      return this.priorityCode;
   }

   public String getRegistryTypeCode() {
      return this.registryTypeCode;
   }

   public void setId(long id) {
      this.id = id;
   }

   public void setBranchCode(String branchCode) {
      this.branchCode = branchCode;
   }

   public void setCurrencyCode(String currencyCode) {
      this.currencyCode = currencyCode;
   }

   public void setMdmCode(String mdmCode) {
      this.mdmCode = mdmCode;
   }

   public void setPriorityCode(String priorityCode) {
      this.priorityCode = priorityCode;
   }

   public void setRegistryTypeCode(String registryTypeCode) {
      this.registryTypeCode = registryTypeCode;
   }
}
