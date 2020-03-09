package com.kbj.shop.domain;

import com.kbj.shop.domain.item.Item;
import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OrderItemDto {

    private Long id;
    private Item item;
    private Order order;
    private int orderPrice;
    private int count;

}
