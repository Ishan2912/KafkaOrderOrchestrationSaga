package com.united.productservice.controller;

import com.united.core.records.dto.Product;
import com.united.core.records.dto.ProductCreationRequest;
import com.united.core.records.dto.ProductCreationResponse;
import com.united.productservice.dao.jpa.entity.ProductEntity;
import com.united.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreationResponse save(@RequestBody @Valid ProductCreationRequest request) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(request, productEntity);
        Product result = productService.save(productEntity);

        return new ProductCreationResponse(result.id(),
                result.name(), result.price(), result.quantity());
    }
}
