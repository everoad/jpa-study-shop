package com.kbj.shop.domain;

import com.kbj.shop.domain.item.Item;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]


    // 양방향인 경우.
    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItemDto dto) {
        OrderItem entity = OrderItem.createEntity(this, dto.getItem(), dto.getCount());
        orderItems.add(entity);
    }

    public void setDelivery(Address address) {
        Delivery entity = Delivery.createEntity(this, address);
        this.delivery = entity;
    }


    //==생성 메서드==//
    public static Order createEntity(Member member, Address address, List<OrderItemDto> orderItems) {
        Order entity = new Order();

        //연관관계 메서드 실행
        entity.setMember(member);
        entity.setDelivery(address);
        orderItems.forEach(entity::addOrderItem);

        entity.status = OrderStatus.ORDER;
        entity.orderDate = LocalDateTime.now();
        return entity;
    }


    //==비즈니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.status = OrderStatus.CANCEL;
        orderItems.forEach(OrderItem::cancel);
    }


    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     * @return totalPrice
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
