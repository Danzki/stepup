package com.danzki.repository;

import com.danzki.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
   Optional<Product> findById(long var1);

   Optional<Product> findByNumber(String var1);

   void delete(Product var1);
}
