package org.multibit.mbm.interfaces.rest.api.adapters.cart.usecases;

import org.multibit.mbm.domain.model.model.CartItem;
import org.multibit.mbm.interfaces.common.DomainAdapter;
import org.multibit.mbm.interfaces.rest.api.adapters.item.ItemAdapters;
import org.multibit.mbm.interfaces.rest.api.cart.CartItemDto;

/**
 * <p>Adapter to provide the following to {@link org.multibit.mbm.interfaces.rest.api.cart.CartDto}:</p>
 * <ul>
 * <li>Mapping between DTO and domain objects</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class CopyCartItem implements DomainAdapter<CartItemDto, CartItem> {

  @Override
  public CartItemDto on(CartItem cartItem) {

    CartItemDto dto = new CartItemDto();

    dto.setIndex(cartItem.getIndex());

    dto.setQuantity(cartItem.getQuantity());

    dto.setItem(ItemAdapters.copyItem(cartItem.getItem()));

    // Sub totals
    dto.setCartItemSubtotal(cartItem.getCartItemSubtotal().toString());
    dto.setPriceSubtotal(cartItem.getPriceSubtotal().toString());
    dto.setTaxSubtotal(cartItem.getTaxSubtotal().toString());

    return dto;
  }

}
