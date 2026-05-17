package ru.yandex.practicum.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.store.controller.StoreClient;
import ru.yandex.practicum.store.dto.ProductDto;
import ru.yandex.practicum.store.enums.ProductCategory;
import ru.yandex.practicum.store.enums.QuantityState;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements StoreClient {

    private final ProductService productService;

    @Override
    public Page<ProductDto> getProductsByProductCategory(@RequestParam(name = "category") ProductCategory category,
                                                         Pageable pageable) throws FeignException {
        return productService.getProductsByProductCategory(category, pageable);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) throws FeignException {
        return productService.addProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) throws FeignException {
        return productService.updateProduct(productDto);
    }

    @Override
    public Boolean removeProductFromStore(UUID productId) throws FeignException {
        return productService.removeProductFromStore(productId);
    }

    @Override
    public Boolean setQuantityState(UUID productId, QuantityState quantityState) throws FeignException {
        return productService.setQuantityState(productId, quantityState);
    }

    @Override
    public ProductDto getProduct(UUID productId) throws FeignException {
        return productService.getProduct(productId);
    }
}
