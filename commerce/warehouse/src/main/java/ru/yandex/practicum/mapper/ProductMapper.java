package ru.yandex.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequest;

@Component
public class ProductMapper {
    public WarehouseProduct toWarehouseProduct(NewProductInWarehouseRequest request) {
        if (request == null) {
            return null;
        }

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setProductId(request.getProductId());
        warehouseProduct.setFragile(request.getFragile());
        warehouseProduct.setWeight(request.getWeight());
        if (request.getDimension() != null) {
            warehouseProduct.setWidth(request.getDimension().getWidth());
            warehouseProduct.setHeight(request.getDimension().getHeight());
            warehouseProduct.setDepth(request.getDimension().getDepth());
        }
        warehouseProduct.setQuantity(0L);
        return warehouseProduct;
    }
}
