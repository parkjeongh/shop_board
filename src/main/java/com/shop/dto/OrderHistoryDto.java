package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 정보를 담는 클래스
 */
@Getter
@Setter
public class OrderHistoryDto {

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문 상태

    /**
     * OrderHistoryDto 생성자
     * 주문 객체를 전달 받아서 주문번호, 주문날짜, 주문상태를 세팅한다.
     * @param order 주문 객체
     */
    public OrderHistoryDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    /**
     * orderItemDto 객체를 주문상품리스트에 추가하는 메서드
     * @param orderItemDto
     */
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
