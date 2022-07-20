package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)   // Member 엔티티와 1:1 매핑
    @JoinColumn(name = "member_id")     // 매핑할 FK 이름 지정.
    private Member member;

    /**
     * 장바구니 생성 메서드
     * @param member 회원
     * @return 장바구니
     */
    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
