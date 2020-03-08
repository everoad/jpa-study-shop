package com.kbj.shop.service;

import com.kbj.shop.domain.Address;
import com.kbj.shop.domain.Member;
import com.kbj.shop.domain.Order;
import com.kbj.shop.domain.OrderStatus;
import com.kbj.shop.domain.item.Book;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.exception.NotEnoughStockException;
import com.kbj.shop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // Given
        Member member = createMember("회원1", new Address("서울", "성북", "123-123"));

        int itemPrice = 10000;
        int stockQuantity = 10;
        Book book = createBook("제목제목", "랄랄라", itemPrice, stockQuantity);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findById(orderId);

        assertThat(getOrder.getStatus()).as("상품 주문시 상태는 ORDER.").isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getOrderItems().size()).as("주문한 상품 죵류가 정확해야 한다.").isEqualTo(1);
        assertThat(getOrder.getTotalPrice()).as("주문 가격은 가격 * 수량이다.").isEqualTo(itemPrice * orderCount);
        assertThat(book.getStockQuantity()).as("주문 수량만큼 재고가 줄어야 한다.").isEqualTo(stockQuantity - orderCount);
    }



    @Test
    public void 주문취소() throws Exception {
        // Given
        Member member = createMember("회원1", new Address("서울", "성북", "123-123"));
        int itemPrice = 10000;
        int stockQuantity = 10;
        Book book = createBook("제목제목", "랄랄라", itemPrice, stockQuantity);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Order order = orderRepository.findById(orderId);
        assertThat(order.getStatus()).as("주문 취소시 상태는 CANCEL").isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).as("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.").isEqualTo(stockQuantity);
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // Given
        int itemPrice = 10000;
        int stockQuantity = 10;
        Member member = createMember("회원1", new Address("서울", "성북", "123-123"));
        Item item = createBook("제목제목", "랄랄라", itemPrice, stockQuantity);

        int orderCount = 11;

        // When
        Executable executable = () -> orderService.order(member.getId(), item.getId(), orderCount);

        // Then
        assertThrows(NotEnoughStockException.class, executable);
    }

    private Book createBook(String name, String author, int itemPrice, int stockQuantity) {
        Book book = Book.builder()
                .name(name)
                .author(author)
                .price(itemPrice)
                .stockQuantity(stockQuantity)
                .build();
        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = Member.builder()
                .name(name)
                .address(address)
                .build();
        em.persist(member);
        return member;
    }
}