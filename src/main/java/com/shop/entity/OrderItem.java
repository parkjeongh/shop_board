package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    /**
     *  주문상품과 주문수량으로 주문상품 객체를 만드는 메서드
     *  1. 주문상품과 주문수량을 세팅한다.
     *  2. 현재시간 기준으로 상품가격을 주문가격으로 세팅한다. (상품가격이 시간에 따라 달라질 수 있기 때문)
     *  3. 주문수량만큼 재고수량을 감소시킨다.
     * @param item 상품
     * @param count 수량
     * @return orderItem 주문상품
     */
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }

    /**
     * 주문가격과 주문수량을 곱하여 주문한 상품의총가격을 구하는 메서드
     * @return 주문상품총가격
     */
    public int getTotalPrice() {
        return orderPrice*count;
    }

    /**
     * 주문을 취소하는 메서드
     * 주문 취소시 상품재고를 다시 더해준다.
     */
    public void cancel() {
        this.getItem().addStock(count);
    }
}
