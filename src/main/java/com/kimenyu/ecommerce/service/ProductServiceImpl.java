package com.kimenyu.ecommerce.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimenyu.ecommerce.dto.ProductDto;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private ProductDto convertToDTO(Product product) {
        ProductDto productDTO = new ProductDto();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    private Product convertToEntity(ProductDto productDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        return product;
    }

    private void updateProductFromDTO(Product product, ProductDto productDTO) {
        BeanUtils.copyProperties(productDTO, product);
    }
    
    @Override
    public void createProduct(ProductDto productDTO) {
        Product product = convertToEntity(productDTO);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductByName(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return convertToDTO(product);
    }

    @Override
    public void updateProduct(Long productId, ProductDto productDTO) {
        Product product = getProductById(productId);
        updateProductFromDTO(product, productDTO);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }


    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        return productOptional.orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}