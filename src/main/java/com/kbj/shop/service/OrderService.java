package com.kbj.shop.service;

import com.kbj.shop.domain.Delivery;
import com.kbj.shop.domain.Member;
import com.kbj.shop.domain.Order;
import com.kbj.shop.domain.OrderItem;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.repository.ItemRepository;
import com.kbj.shop.repository.MemberRepository;
import com.kbj.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findById(memberId);
        Item item = itemRepository.findById(itemId);

        //배송정보 조회
        Delivery delivery = Delivery.builder()
                .address(member.getAddress())
                .build();

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findById(orderId);
        // 주문 취소
        order.cancel();
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
