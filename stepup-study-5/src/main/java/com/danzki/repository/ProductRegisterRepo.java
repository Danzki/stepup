package com.danzki.repository;

import com.danzki.model.ProductRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegisterRepo extends JpaRepository<ProductRegister, Long> {
   List<ProductRegister> findByProductId(long var1);
}
