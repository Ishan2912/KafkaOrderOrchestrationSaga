package com.united.productservice.service;


import com.united.core.records.dto.Product;
import com.united.productservice.dao.jpa.entity.ProductEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> findAll();

    Product reserve(Product desiredProduct, UUID orderId);

    void cancelReservation(Product productToCancel, UUID orderId);

    Product save(ProductEntity product);
}
