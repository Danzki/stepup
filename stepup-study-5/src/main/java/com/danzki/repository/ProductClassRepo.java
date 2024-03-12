package com.danzki.repository;

import com.danzki.model.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductClassRepo extends JpaRepository<ProductClass, Long> {
   Optional<ProductClass> findByGlobalCode(String var1);
}
