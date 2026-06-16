package com.ShopSphere.e_commerce.Service.impl;

import com.ShopSphere.e_commerce.Entity.Cart;
import com.ShopSphere.e_commerce.Entity.CartItem;
import com.ShopSphere.e_commerce.Entity.Product;
import com.ShopSphere.e_commerce.Entity.User;
import com.ShopSphere.e_commerce.Exception.CartItemNotFoundException;
import com.ShopSphere.e_commerce.Exception.CartNotFoundException;
import com.ShopSphere.e_commerce.Exception.ProductNotFoundException;
import com.ShopSphere.e_commerce.Exception.UserNotFoundException;
import com.ShopSphere.e_commerce.Repository.CartItemRepository;
import com.ShopSphere.e_commerce.Repository.CartRepository;
import com.ShopSphere.e_commerce.Repository.ProductRepository;
import com.ShopSphere.e_commerce.Repository.UserRepository;
import com.ShopSphere.e_commerce.Service.CartService;
import com.ShopSphere.e_commerce.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public AddToCartResponseDto addToCart(AddToCartRequestDto dto){

        if(dto.getQuantity() <= 0){
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        // step 1 : get logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email =  authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User With email " + email + " Not Found"));

        // step 2 : find product By Product Id
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product with id " + dto.getProductId() + " not found"));

        // step 3 : Find cart by userId If cart doesn't exist -> create new cart
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(()-> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // step 4 : check cartItem exists for cart + product
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        CartItem cartItem;
        if(existingCartItem.isPresent()){
            // if exist increase quantity
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());

        }else{
            // else create CartItem
            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(dto.getQuantity());

        }
        cartItemRepository.save(cartItem);

        return new AddToCartResponseDto(
                product.getName() + "Product added successfully",
                product.getId(),
                product.getName(),
                cartItem.getQuantity()
        );
    }

    @Override
    public CartResponseDto getCart(){
        // step 1 : get logged in user .
        //          Because client only fetch the cart not send cart_id so, we already have user based on user we return cart items
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User With email " + email + " Not Found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart with id " + user.getId() + " not Found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        List<CartItemResponseDto> itemDtos = cartItems.stream()
                .map(cartItem -> {
                    Double subTotal = cartItem.getProduct().getPrice() * cartItem.getQuantity();

                    return new CartItemResponseDto(
                            cartItem.getProduct().getId(),
                            cartItem.getProduct().getName(),
                            cartItem.getProduct().getPrice(),
                            cartItem.getQuantity(),
                            subTotal
                    );

                }).toList();

        Integer totalItems = cartItems.stream().mapToInt(CartItem::getQuantity).sum();

        Double totalPrice = itemDtos.stream().mapToDouble(CartItemResponseDto::getSubtotal).sum();

        return new CartResponseDto(
                cart.getId(),
                user.getId(),
                totalItems,
                totalPrice,
                itemDtos
        );

    }

    @Override
    public RemoveFromCartResponseDto removeFromCart(Long productId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User With email " + email + " Not Found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user id " + user.getId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new CartItemNotFoundException( "Product with id " + productId + " is not present in cart"));

        cartItemRepository.delete(cartItem);

         return new RemoveFromCartResponseDto(
                "Product removed from cart successfully",
                product.getId(),
                product.getName()
        );
    }

    @Override
    public UpdateCartItemResponseDto updateCartItem(Long productId, UpdateCartItemRequestDto dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User With email " + email + " Not Found"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user id " + user.getId()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new CartItemNotFoundException("Product with id " + productId + " is not present in cart"));

        cartItem.setQuantity(dto.getQuantity());
        cartItemRepository.save(cartItem);

        return new UpdateCartItemResponseDto(
                "Quantity updated successfully",
                product.getId(),
                product.getName(),
                cartItem.getQuantity()
        );

    }

}
