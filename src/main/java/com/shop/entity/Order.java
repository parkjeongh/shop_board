package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 주문상품을 주문객체에 더하는 메서드
     * 1. 주문상품을 order 객체의 orderItems 에 추가한다.
     * 2. orderItem 객체에 order 객체를 세팅한다.
     * @param orderItem 주문상품
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 객체(주문서)를 생성하는 메서드
     * 1. 상품을 주문한 회원정보를 세팅한다.
     * 2. 주문 객체에 주문상품 객체를 추가한다.
     * 3. 주문상태를 ORDER 상태로 세팅한다.
     * 4. 주문시간을 현재시간으로 세팅한다.
     * @param member 회원정보
     * @param orderItemList 주문한 아이템목록
     * @return
     */
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문목록의 총주문금액을 구하는 메서드
     * @return 총주문금액
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    /**
     * 주문상태를 취소상태로 바꿔주는 메서드
     * cancel() 메서드로 주문상품 수량을 되돌려준다.
     */
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}
