package study.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import study.cafekiosk.spring.api.service.order.request.OrderServiceRequest;
import study.cafekiosk.spring.api.service.order.response.OrderResponse;
import study.cafekiosk.spring.domain.order.OrderRepository;
import study.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import study.cafekiosk.spring.domain.product.Product;
import study.cafekiosk.spring.domain.product.ProductRepository;
import study.cafekiosk.spring.domain.product.ProductType;
import study.cafekiosk.spring.domain.stock.Stock;
import study.cafekiosk.spring.domain.stock.StockRepository;
import study.cafekiosk.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static study.cafekiosk.spring.domain.product.ProductType.*;

//@Transactional
class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    StockRepository stockRepository;

    @AfterEach
    void tearDown() {
//        productRepository.deleteAll();

        /*orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
*/
        orderProductRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();

        stockRepository.deleteAllInBatch();
    }

    @DisplayName("상품번호 리스트를 받아 주문을 생성한다")
    @Test
    void createOrder() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 4000);
        Product product2 = createProduct(HANDMADE, "002", 4500);
        Product product3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderServiceRequest request = OrderServiceRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8500);
        assertThat(orderResponse.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );

    }

    @DisplayName("재고 관리 상품이 포함된 상품번호 리스트를 받아 주문을 생성한다")
    @Test
    void createOrderWithStock() throws Exception {
        //given
        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 4000);
        Product product2 = createProduct(BAKERY, "002", 4500);
        Product product3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderServiceRequest request = OrderServiceRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);
        List<Stock> stocks = stockRepository.findAllById(List.of(stock1.getId(), stock2.getId()));

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 19500);
        assertThat(orderResponse.getProducts())
                .hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 4500),
                        tuple("003", 7000)
                );

        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );

    }

    @DisplayName("재고 수량보다 많은 주문을 생성하면 예외가 발생한다")
    @Test
    void createOrderWithNoStock() throws Exception {
        //given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(BOTTLE, "001", 4000);
        Product product2 = createProduct(BAKERY, "002", 4500);
        Product product3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderServiceRequest request = OrderServiceRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        //when
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");


    }

    @DisplayName("중복 상품번호 리스트를 받아 주문을 생성한다")
    @Test
    void createOrderFromDuplicatedProductNumbers() throws Exception {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "001", 4000);
        Product product2 = createProduct(HANDMADE, "002", 4500);
        Product product3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderServiceRequest request = OrderServiceRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        //when
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8000);
        assertThat(orderResponse.getProducts())
                .hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
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
}