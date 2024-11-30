package com.cnpm.service.interfaces;

import com.cnpm.entity.CartItem;
import org.springframework.stereotype.Service;

@Service
public interface ICartItemService {
    CartItem getCartItemById(Long id);
}
