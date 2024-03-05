package com.danzki.repository;

import com.danzki.model.ProductClass;
import com.danzki.model.ProductRegisterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegisterTypeRepo extends JpaRepository<ProductRegisterType, Long> {
   List<ProductRegisterType> findAllByValue(String var1);

   ProductRegisterType findByRegisterTypeName(String var1);

   List<ProductRegisterType> findAllByProductClass(ProductClass var1);
}
