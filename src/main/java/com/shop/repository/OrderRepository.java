package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 주문 이력을 조회사는 쿼리 메서드
     * (가독성을 위한 마지막칸 띄어쓰기 주의)
     * 현재 로그인한 사용자의 주문데이터를 페이징 조건에 맞춰서 조회한다.
     * @param email
     * @param pageable
     * @return
     */
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    /**
     * 혅재 로그인한 회원의 주문개수를 조회한다.
     * @param email
     * @return
     */
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);

}
