package com.kbj.shop.domain;


import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY, COMP

    //==생성 메서드==//
    public static Delivery createEntity(Order order, Address address) {
        Delivery entity = new Delivery();
        entity.address = address;
        entity.order = order;
        entity.status = DeliveryStatus.READY;
        return entity;
    }

}
