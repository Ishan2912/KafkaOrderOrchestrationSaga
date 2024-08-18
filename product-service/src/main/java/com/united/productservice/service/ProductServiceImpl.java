package com.united.productservice.service;

import com.united.core.exceptions.ProductInsufficientQuantityException;
import com.united.core.records.dto.Product;
import com.united.productservice.dao.jpa.entity.ProductEntity;
import com.united.productservice.dao.jpa.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product reserve(Product desiredProduct, UUID orderId) {
        ProductEntity productEntity = productRepository.findById(desiredProduct.id()).orElseThrow();
        if (desiredProduct.quantity() > productEntity.getQuantity()) {
            throw new ProductInsufficientQuantityException(productEntity.getId(), orderId);
        }

        productEntity.setQuantity(productEntity.getQuantity() - desiredProduct.quantity());
        productRepository.save(productEntity);

        Product reservedProduct = new Product(desiredProduct.quantity());
        BeanUtils.copyProperties(productEntity, reservedProduct);
        return reservedProduct;
    }

    @Override
    public void cancelReservation(Product productToCancel, UUID orderId) {
        ProductEntity productEntity = productRepository.findById(productToCancel.id()).orElseThrow();
        productEntity.setQuantity(productEntity.getQuantity() + productToCancel.quantity());
        productRepository.save(productEntity);
    }

    @Override
    public Product save(ProductEntity productEntity) {
        productRepository.save(productEntity);

        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getPrice(), productEntity.getQuantity());
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity()))
                .collect(Collectors.toList());
    }
}
