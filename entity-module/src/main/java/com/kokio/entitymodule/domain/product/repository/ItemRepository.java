package com.kokio.entitymodule.domain.product.repository;

import com.kokio.entitymodule.domain.product.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
