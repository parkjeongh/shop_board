package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistoryDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 로직을 작성하는 클래스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    /**
     *  입력 받은 주문정보를 저장하는 메서드
     *  1. orderDto 에서 전달받은 상품아이디로 주문상품을 조회한다.
     *  2. email 로 주문자 회원정보를 조회한다.
     *  3. 조회했던 주문상품과 주문수량으로 주문상품 리스트를 생성한다.
     *  4. 회원정보와 주문상품 리스트로 주문 엔티티를 생성한다.
     *  5. 생성한 주문 엔티티를 orderRepository 에 저장한다.
     * @param orderDto 입력 받은 주문정보
     * @param email 주문고객 이메일
     * @return 주문번호
     */
    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 구매이력에 표시할 주문목록을 조회하는 메서드
     * 1. 회원이메일과 페이징조건으로 저장된 주문을 조회한다.
     * 2. 회원 이메일로 총주문수를 조회한다.
     * 3. 주문리스트를 순회하면서 구매이력페이지에 전달할 DTO 를 생성한다.
     * 4. 주문한 상품의 대표이미지를 조회한다.
     * 5. 페이지구현 객체를 생성하여 반환한다.
     * @param email
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistoryDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistoryDto orderHistDto = new OrderHistoryDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndFrontImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistoryDto>(orderHistDtos, pageable, totalCount);
    }

    /**
     * 현재 로그인한 회원과 주문정보를 작성한 회원이 일치하는지 검사하는 메서드
     * @param orderId
     * @param email
     * @return true or false
     */
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    /**
     * 주문을 취소하는 로직
     * @param orderId
     */
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    /**
     * 장바구니에서 주문할 상품데이터를 전달 받아 주문을 생성하는 로직
     * @param orderDtoList 주문리스트
     * @param email 회원이메일
     * @return 주문번호
     */
    public Long orders(List<OrderDto> orderDtoList, String email){

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList) {
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

}
