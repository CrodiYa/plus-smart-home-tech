package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.cart.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.model.Cart;
import ru.yandex.practicum.repository.CartRepository;
import ru.yandex.practicum.warehouse.controller.WarehouseClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        Cart cart = getOrCreateCart(username);
        return cartMapper.toShoppingCartDto(cart);
    }

    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Integer> products) {
        Cart cart = getOrCreateCart(username);

        Map<UUID, Integer> current = cart.getProducts();
        Map<UUID, Integer> checkMap = new HashMap<>(current);

        products.forEach((id, i) -> checkMap.merge(id, i, Integer::sum));
        warehouseClient.checkQuantity(new ShoppingCartDto(cart.getShoppingCartId(), checkMap));

        products.forEach((id, i) -> current.merge(id, i, Integer::sum));
        cartRepository.save(cart);

        return cartMapper.toShoppingCartDto(cart);
    }

    @Override
    public void deactivateCart(String username) {
        Cart shoppingCart = getOrCreateCart(username);
        shoppingCart.setActive(false);
        cartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto removeProductsFromCart(String username, List<UUID> products) {
        Cart shoppingCart = getOrCreateCart(username);
        products.forEach(p -> shoppingCart.getProducts().remove(p));
        cartRepository.save(shoppingCart);
        return cartMapper.toShoppingCartDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        Cart shoppingCart = getOrCreateCart(username);
        UUID productId = request.getProductId();

        Integer newQuantity = request.getNewQuantity();

        if (!shoppingCart.getProducts().containsKey(productId)) {
            throw new NoProductsInShoppingCartException(productId);
        }
        warehouseClient.checkQuantity(new ShoppingCartDto(shoppingCart.getShoppingCartId(),
                Map.of(productId, newQuantity)));

        shoppingCart.getProducts().put(productId, newQuantity);
        cartRepository.save(shoppingCart);
        return cartMapper.toShoppingCartDto(shoppingCart);
    }

    private Cart getOrCreateCart(String username) {
        Cart shoppingCart = cartRepository.findByUsernameAndActive(username, true)
                .orElseGet(() -> createAndSaveCart(username));

        if (shoppingCart.getProducts() == null) {
            shoppingCart.setProducts(new HashMap<>());
            cartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }

    private Cart createAndSaveCart(String username) {
        Cart cart = Cart.builder()
                .username(username)
                .active(true)
                .products(new HashMap<>())
                .build();

        return cartRepository.save(cart);
    }
}
