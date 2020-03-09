package com.kbj.shop.domain;

import com.kbj.shop.domain.item.Item;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량


    //==생성 매서드==//
    public static OrderItem createEntity(Order order, Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.order = order;
        orderItem.orderPrice = item.getPrice();
        orderItem.count = count;

        //재고 감소
        item.removeStock(count);
        return orderItem;
    }


    //==비즈니스 로직==//

    /**
     * 재고 수량 원복
     */
    public void cancel() {
        getItem().addStock(count);
    }


    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     * @return totalPrice
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
