package com.kokio.entitymodule.domain.product.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long sellerId;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private String name;
  private Integer price;
  private Integer count;

  public void updateProduct(Product product) {
    this.product = product;
    this.product.getItems().add(this);
  }


}
