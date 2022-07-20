package com.shop.repository;

import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.dto.CartDetailDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 장바구니에 들어갈 상품을 저장하거나 조회하기 위한 인터페이스
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 상품이 장바구니에 들어있는지 조회한다.
     * @param cartId 장바구니번호
     * @param itemId 상품번호
     * @return 장바구니상품
     */
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    /**
     * CartDetailDto 리스트를 쿼리로 조회하는 JPQL
     * 성능최적화가 필요한 경우 DTO 생성자를 이용하여 반환 값으로 DTO 객체를 생성할 수 있다.
     * @param cartId 장바구니번호
     * @return 장바구니리스트
     */
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.frontImgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

}