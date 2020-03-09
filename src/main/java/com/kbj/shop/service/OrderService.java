package com.kbj.shop.service;

import com.kbj.shop.domain.*;
import com.kbj.shop.domain.item.Book;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.repository.ItemRepository;
import com.kbj.shop.repository.MemberRepository;
import com.kbj.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        //회원 조회
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            throw new NoResultException("No member data by memberId=" + memberId);
        }

        //상품 조회
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NoResultException("No item data by itemId=" + itemId);
        }

        Member member = optionalMember.get();
        Item item = optionalItem.get();

        // 배송을 위한 주소 생성
        Address address = member.getAddress();

        // 상품 생성
        List<OrderItemDto> orderItems = new ArrayList<>();
        OrderItemDto orderItemDto = OrderItemDto.builder()
                .item(item)
                .count(count)
                .orderPrice(item.getPrice())
                .build();
        orderItems.add(orderItemDto);

        //주문 생성
        Order order = Order.createEntity(member, address, orderItems);

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
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
