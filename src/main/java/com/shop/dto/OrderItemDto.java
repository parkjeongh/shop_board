package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문이력 조회
 * 조회한 주문데이터를 화면에 보낼때 사용하는 클래스
 */
@Getter
@Setter
public class OrderItemDto {

    private String itemNm; //상품명

    private int count; //주문 수량

    private int orderPrice; //주문 금액

    private String imgUrl; //상품 이미지 경로

    /**
     * OrderItemDto 생성자
     * 주문상품 객체와 상품이미지경로를 전달 받아서 제품이름, 주문수량, 주문가격, 상품이미지를 세팅한다.
     * @param orderItem 주문상품정보
     * @param imgUrl 상품의 이미지경로
     */
    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
