package com.danzki.service;

import com.danzki.controller.ProductAgreementController;
import com.danzki.model.*;
import com.danzki.repository.AgreementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AgreementService {
   private final AgreementRepo agreementRepo;

   @Autowired
   public AgreementService(AgreementRepo agreementRepo) {
      this.agreementRepo = agreementRepo;
   }

   public Optional<Agreement> findByProductAndNumber(Product product, String number) {
      return this.agreementRepo.findByProductAndNumber(product, number);
   }

   @Transactional
   public Agreement saveFromRequest(Product product, ProductAgreementController.InstanceArrangement request) {
      Agreement agreement = new Agreement();
      agreement.setProduct(product);
      agreement.setGeneralAgreementId(request.generalAgreementId());
      agreement.setSupplementaryAgreementId(request.supplementaryAgreementId());
      agreement.setArrangementType(ProductType.getProductType(request.arrangementType()).getName());
      agreement.setShedulerJobId(request.shedulerJobId());
      agreement.setNumber(request.number());
      agreement.setOpeningDate(request.openingDate());
      agreement.setClosingDate(request.closingDate());
      agreement.setCancelDate(request.cancelDate());
      agreement.setValidityDuration(request.validityDuration());
      agreement.setCancellationReason(request.cancellationReason());
      agreement.setStatus(Status.getStatus(request.status()));
      agreement.setInterestCalculationDate(request.interestCalculationDate());
      agreement.setInterestRate(request.interestRate());
      agreement.setCoefficient(request.coefficient());
      agreement.setCoefficientAction(request.coefficientAction());
      agreement.setMinimumInterestRate(request.minimumInterestRate());
      agreement.setMinimumInterestRateCoefficient(request.minimumInterestRateCoefficient());
      agreement.setMinimumInterestRateCoefficientAction(request.minimumInterestRateCoefficientAction() != null ? CoefficientAction.getCoefficientAction(request.minimumInterestRateCoefficientAction()).getAction() : null);
      agreement.setMaximumInterestRate(request.maximumInterestRate());
      agreement.setMaximumInterestRateCoefficient(request.maximumInterestRateCoefficient());
      agreement.setMaximumInterestRateCoefficientAction(request.maximumInterestRateCoefficientAction() != null ? CoefficientAction.getCoefficientAction(request.maximumInterestRateCoefficientAction()).getAction() : null);
      this.agreementRepo.save(agreement);
      return agreement;
   }
}
