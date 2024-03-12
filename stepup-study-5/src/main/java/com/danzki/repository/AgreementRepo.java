package com.danzki.repository;

import com.danzki.model.Agreement;
import com.danzki.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgreementRepo extends JpaRepository<Agreement, Long> {
   Optional<Agreement> findById(Long var1);

   Optional<Agreement> findByProductAndNumber(Product var1, String var2);
}
