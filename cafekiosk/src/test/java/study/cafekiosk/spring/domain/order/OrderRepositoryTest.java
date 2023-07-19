package study.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import study.cafekiosk.spring.domain.product.Product;
import study.cafekiosk.spring.domain.product.ProductRepository;
import study.cafekiosk.spring.domain.product.ProductType;
import study.cafekiosk.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static study.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    ProductRepository productRepository;

    @DisplayName("특정 시작일시, 다음날 시작일시, 주문 상태를 인자로 받아 주문 목록을 반환한다.")
    @Test
    void findOrdersByOrderDate() throws Exception {
        //given
        Product product1 = createProduct(ProductType.HANDMADE, "001", 4000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 4000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 4000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        LocalDateTime orderDate = LocalDateTime.of(2023, 6, 30, 18, 0, 0);

        Order order1 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 29, 23, 59, 59));
        Order order2 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 30, 0, 0, 0));
        Order order3 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 30, 23, 59, 59));
        Order order4 = createPayCompletedOrder(products, LocalDateTime.of(2023, 7, 1, 0, 0, 0));
        Order order5 = Order.create(List.of(product1), orderDate);

        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5));

        //when
        List<Order> result = orderRepository.findOrdersByOrderDate(
                orderDate.toLocalDate().atStartOfDay(),
                orderDate.toLocalDate().plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED);



        //then
        assertThat(result).hasSize(2)
                .extracting("orderStatus", "totalPrice", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        tuple(OrderStatus.PAYMENT_COMPLETED, 12000, LocalDateTime.of(2023, 6, 30, 0, 0, 0)),
                        tuple(OrderStatus.PAYMENT_COMPLETED, 12000, LocalDateTime.of(2023, 6, 30, 23, 59, 59))
                );

    }

    private Order createPayCompletedOrder(List<Product> products, LocalDateTime orderDate) {
        return Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .products(products)
                .registeredDateTime(orderDate)
                .build();
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴이름")
                .build();
    }

}