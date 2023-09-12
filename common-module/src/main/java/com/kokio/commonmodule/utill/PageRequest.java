package com.kokio.commonmodule.utill;


import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

@Getter
public class PageRequest {

  private int page = 1;
  private int size = 10;
  private Sort.Direction direction = Sort.Direction.ASC;

  public void setPage(int page) {
    this.page = page <= 0 ? 1 : page;
  }

  public void setSize(int size) {
    int DEFAULT_SIZE = 10;
    int MAX_SIZE = 500;
    this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
  }

  public void setDirection(Sort.Direction direction) {
    this.direction = direction;
  }

  public org.springframework.data.domain.PageRequest of() {
    return org.springframework.data.domain.PageRequest.of(page - 1, size);
  }

  public org.springframework.data.domain.PageRequest of(Sort.Direction dir, String col) {
    if (ObjectUtils.isEmpty(col)) {
      col = "name";
    }
    if (ObjectUtils.isEmpty(dir)) {
      dir = direction;
    }
    return org.springframework.data.domain.PageRequest.of(page - 1, size, dir, col);
  }
}
