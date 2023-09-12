package com.kokio.productapi.controller;


import com.kokio.commonmodule.utill.PageRequest;
import com.kokio.entitymodule.domain.product.model.ProductDto;
import com.kokio.productapi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  @GetMapping("/product")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> searchByName(@RequestParam String dir, @RequestParam String sort,
      @RequestParam int page, @RequestParam int size, @RequestParam String name, Pageable pageable

  ) {
    PageRequest pageRequest = new PageRequest();
    pageRequest.setPage(page);
    pageRequest.setSize(size);
    if (!ObjectUtils.isEmpty(dir) && !ObjectUtils.isEmpty(sort)) {
      if (dir.equals("desc")) {
        pageable = pageRequest.of(Direction.DESC, sort);
      } else {
        pageable = pageRequest.of(Direction.ASC, sort);
      }
    } else {
      pageable = pageRequest.of();
    }
    return ResponseEntity.ok(
        searchService.searchByName(name, pageable).map(ProductDto::toDtoWithOutItem));
  }

  @GetMapping("/product/detail")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<?> getDetail(@RequestParam Long productId) {

    return ResponseEntity.ok(ProductDto.toDto(searchService.getDetail(productId)));
  }


}
