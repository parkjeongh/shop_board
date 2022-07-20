package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 현재 로그인한 회원의 Cart 엔티티를 찾기 위한 쿼리 메서드
     * @param memberId 회원아이디
     * @return
     */
    Cart findByMemberId(Long memberId);
}
