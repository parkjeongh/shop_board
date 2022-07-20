package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 장바구니 페이지에서 주문할 상품데이터를 전달하는 클래스
 */
@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;

    private List<CartOrderDto> cartOrderDtoList;

}