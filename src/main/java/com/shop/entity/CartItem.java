package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Cart 엔티티와 1:1 매핑
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)  // Item 엔티티와 다:1 매핑
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    /**
     * 장바구니에 담기는 상품엔티티를 생성하는 메서드
     * @param cart 장바구니
     * @param item 상품
     * @param count 수량
     * @return 장바구니상품
     */
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    /**
     * 장바구니에 담긴 상품 수량을 증가시키는 메서드
     * @param count 수량
     */
    public void addCount(int count){
        this.count += count;
    }

    /**
     * 장바구니에 담긴 상품 수량을 변경하는 메서드
     * @param count 수량
     */
    public void updateCount(int count){
        this.count = count;
    }

}
