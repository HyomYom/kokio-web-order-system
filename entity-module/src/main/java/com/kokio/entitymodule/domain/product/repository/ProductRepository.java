package com.kokio.entitymodule.domain.product.repository;

import com.kokio.entitymodule.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Override
  @EntityGraph(attributePaths = {"items"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Product> findById(Long aLong);

  @EntityGraph(attributePaths = {"items"}, type = EntityGraph.EntityGraphType.LOAD)
  Optional<Product> findByIdAndSellerId(Long id, Long sellerId);

  boolean existsBySellerIdAndName(Long sellerId, String name);
}
