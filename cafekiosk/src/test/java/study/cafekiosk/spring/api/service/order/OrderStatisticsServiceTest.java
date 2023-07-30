package study.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import study.cafekiosk.spring.client.mail.MailSendClient;
import study.cafekiosk.spring.domain.history.mail.MailSendHistory;
import study.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import study.cafekiosk.spring.domain.order.Order;
import study.cafekiosk.spring.domain.order.OrderRepository;
import study.cafekiosk.spring.domain.order.OrderStatus;
import study.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import study.cafekiosk.spring.domain.product.Product;
import study.cafekiosk.spring.domain.product.ProductRepository;
import study.cafekiosk.spring.domain.product.ProductType;
import study.cafekiosk.spring.support.IntegrationTestSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static study.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

@Transactional
class OrderStatisticsServiceTest extends IntegrationTestSupport {

    @Autowired
    OrderStatisticsService orderStatisticsService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    MailSendHistoryRepository mailSendHistoryRepository;



    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() throws Exception {
        //given
        Product product1 = createProduct(ProductType.HANDMADE, "001", 4000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 4000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 4000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        LocalDate orderDate = LocalDate.of(2023, 6, 30);

        Order order1 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 29, 23, 59, 59));
        Order order2 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 30, 0, 0, 0));
        Order order3 = createPayCompletedOrder(products, LocalDateTime.of(2023, 6, 30, 23, 59, 59));
        Order order4 = createPayCompletedOrder(products, LocalDateTime.of(2023, 7, 1, 0, 0, 0));
        Order order5 = Order.create(List.of(product1), orderDate.atStartOfDay());

        orderRepository.saveAll(List.of(order1, order2, order3, order4, order5));

        // stubbing
        when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        //when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(orderDate, "test@test.com");

        //then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains(
                        "총 매출 합계는 24000원입니다."
                );

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

    private Order createPayCompletedOrder(List<Product> products, LocalDateTime orderDate) {
        return Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .products(products)
                .registeredDateTime(orderDate)
                .build();
    }
}