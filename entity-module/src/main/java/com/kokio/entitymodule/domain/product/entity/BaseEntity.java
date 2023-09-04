package com.kokio.entitymodule.domain.product.entity;


import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {

  @CreatedDate
  private LocalDateTime createdAt;

  @CreatedDate
  private LocalDateTime modifiedAt;


}
