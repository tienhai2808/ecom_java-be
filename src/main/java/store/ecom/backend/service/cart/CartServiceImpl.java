package store.ecom.backend.service.cart;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.CartDto;
import store.ecom.backend.dto.CartItemDto;
import store.ecom.backend.dto.CategoryDto;
import store.ecom.backend.dto.ProductDto;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.Cart;
import store.ecom.backend.model.Product;
import store.ecom.backend.repository.CartItemRepository;
import store.ecom.backend.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ModelMapper modelMapper;

  @Override
  public Cart getCart(Long id) {
    return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));
  }

  @Transactional
  @Override
  public void clearCart(Long id) {
    Cart cart = getCart(id);
    cartItemRepository.deleteAllByCartId(id);
    cart.getItems().clear();
    cartRepository.deleteById(id);
  }

  @Override
  public BigDecimal getTotalPrice(Long id) {
    Cart cart = getCart(id);
    return cart.getTotalAmount();
  }

  @Override
  public Long initializeNewCart() {
    Cart newCart = new Cart();
    newCart.setTotalAmount(BigDecimal.ZERO);
    return cartRepository.save(newCart).getId();
  }

  @Override
  public Cart getCartByUserId(Long userId) {
    return cartRepository.findByUserId(userId);
  }

  @Override
  public CartDto convertToDto(Cart cart) {
    CartDto cartDto = modelMapper.map(cart, CartDto.class);
    Set<CartItemDto> itemDtos = cart.getItems().stream().map(item -> {
      CartItemDto itemDto = modelMapper.map(item, CartItemDto.class);

      Product product = item.getProduct();
      if (product != null) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        CategoryDto categoryDto = modelMapper.map(product.getCategory(), CategoryDto.class);
        productDto.setCategory(categoryDto);
        itemDto.setProduct(productDto);
      }

      return itemDto;
    }).collect(Collectors.toSet());

    cartDto.setItems(itemDtos);
    return cartDto;
  }
}
