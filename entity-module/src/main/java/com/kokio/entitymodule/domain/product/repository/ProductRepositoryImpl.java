package com.kokio.entitymodule.domain.product.repository;

import com.kokio.entitymodule.domain.product.entity.Product;
import com.kokio.entitymodule.domain.product.entity.QProduct;
import com.kokio.entitymodule.domain.user.model.UserDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<Product> searchByName(UserDto seller, String productName, Pageable pageable) {

    String search = '%' + productName + '%';

    QProduct qProduct = QProduct.product;

    JPAQuery<Product> query = jpaQueryFactory.selectFrom(qProduct)
        .where(qProduct.name.like(search), qProduct.sellerId.eq(seller.getId()));

    if (pageable.getSort().isSorted()) {
      pageable.getSort().forEach(order -> {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
            order.isAscending() ? Order.ASC : Order.DESC,
            Expressions.stringPath(order.getProperty()));
        query.orderBy(orderSpecifier);
      });
    }
    query.offset(pageable.getOffset());
    query.limit(pageable.getPageSize());
    QueryResults<Product> productQueryResults = query.fetchResults();

    return new PageImpl<>(productQueryResults.getResults(), pageable,
        productQueryResults.getTotal());
  }

  @Override
  public Page<Product> customerSearchByName(String productName, Pageable pageable) {

    String search = '%' + productName + '%';

    QProduct qProduct = QProduct.product;

    JPAQuery<Product> query = jpaQueryFactory.selectFrom(qProduct)
        .where(qProduct.name.like(search));
    if (pageable.getSort().isSorted()) {
      pageable.getSort().forEach(order -> {
        OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
            order.isAscending() ? Order.ASC : Order.DESC,
            Expressions.stringPath(order.getProperty()));
        query.orderBy(orderSpecifier);
      });
    }
    query.offset(pageable.getOffset());
    query.limit(pageable.getPageSize());
    QueryResults<Product> productQueryResults = query.fetchResults();
    return new PageImpl<>(productQueryResults.getResults(), pageable,
        productQueryResults.getTotal());
  }
}
