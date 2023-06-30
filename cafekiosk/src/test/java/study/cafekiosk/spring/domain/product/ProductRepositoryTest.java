package study.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static study.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
@DisplayName("ProductRepository 인터페이스")
class ProductRepositoryTest {
    
    @Autowired
    ProductRepository productRepository;

    @Nested
    @DisplayName("findByProductSellingStatusIn 메소드는")
    class Describe_findByProductSellingStatusIn {

        @Nested
        @DisplayName("만약 판매중, 판매보류, 판매중지 상품이 주어진다면")
        class Context_with_all_selling_status {

            @BeforeEach
            void save() {
                Product product1 = Product.builder()
                        .sellingStatus(SELLING)
                        .productNumber("001")
                        .type(HANDMADE)
                        .name("아메리카노")
                        .price(4000)
                        .build();
                Product product2 = Product.builder()
                        .sellingStatus(HOLD)
                        .productNumber("002")
                        .type(HANDMADE)
                        .name("카페라떼")
                        .price(4500)
                        .build();
                Product product3 = Product.builder()
                        .sellingStatus(STOP_SELLING)
                        .productNumber("003")
                        .type(HANDMADE)
                        .name("팥빙수")
                        .price(7000)
                        .build();
                productRepository.saveAll(List.of(product1, product2, product3));

            }

            @DisplayName("설정한 판매 상태의 상품을 리턴한다")
            @Test
            void it_returns_products_we_defined() throws Exception {
                List<Product> byProductSellingStatusIn = productRepository.findBySellingStatusIn(List.of(SELLING, HOLD));

                assertThat(byProductSellingStatusIn).hasSize(2)
                        .extracting("productNumber", "name", "sellingStatus")
                        .containsExactlyInAnyOrder(
                                tuple("001", "아메리카노", SELLING),
                                tuple("002", "카페라떼", HOLD)
                        );
            }
        }
    }

    @Nested
    @DisplayName("findByProductSellingStatusIn 메소드는")
    class Describe_findAllByProductNumberIn {

        @Nested
        @DisplayName("만약 상품 번호를 가진 상품이 일반적으로 주어진다면")
        class Context_with_all_selling_status {

            @BeforeEach
            void save() {
                Product product1 = Product.builder()
                        .sellingStatus(SELLING)
                        .productNumber("001")
                        .type(HANDMADE)
                        .name("아메리카노")
                        .price(4000)
                        .build();
                Product product2 = Product.builder()
                        .sellingStatus(HOLD)
                        .productNumber("002")
                        .type(HANDMADE)
                        .name("카페라떼")
                        .price(4500)
                        .build();
                Product product3 = Product.builder()
                        .sellingStatus(STOP_SELLING)
                        .productNumber("003")
                        .type(HANDMADE)
                        .name("팥빙수")
                        .price(7000)
                        .build();
                productRepository.saveAll(List.of(product1, product2, product3));

            }

            @DisplayName("설정한 상품 번호의 상품들을 리턴한다")
            @Test
            void it_returns_products_we_defined() throws Exception {
                List<Product> byProductSellingStatusIn = productRepository.findAllByProductNumberIn(List.of("001", "002"));

                assertThat(byProductSellingStatusIn).hasSize(2)
                        .extracting("productNumber", "name", "sellingStatus")
                        .containsExactlyInAnyOrder(
                                tuple("001", "아메리카노", SELLING),
                                tuple("002", "카페라떼", HOLD)
                        );
            }
        }
    }
    @Nested
    @DisplayName("findLatestProductNumber 메소드는")
    class Describe_findLatestProductNumber {

        @Nested
        @DisplayName("만약 여러 상품이 주어진다면")
        class Context_with_various_products {

            @BeforeEach
            void save() {
                Product product1 = Product.builder()
                        .sellingStatus(SELLING)
                        .productNumber("001")
                        .type(HANDMADE)
                        .name("아메리카노")
                        .price(4000)
                        .build();
                Product product2 = Product.builder()
                        .sellingStatus(HOLD)
                        .productNumber("002")
                        .type(HANDMADE)
                        .name("카페라떼")
                        .price(4500)
                        .build();
                Product product3 = Product.builder()
                        .sellingStatus(STOP_SELLING)
                        .productNumber("003")
                        .type(HANDMADE)
                        .name("팥빙수")
                        .price(7000)
                        .build();
                productRepository.saveAll(List.of(product1, product2, product3));

            }

            @DisplayName("가장 최근에 등록한 상품 번호를 리턴한다")
            @Test
            void it_returns_the_latest_product_number() throws Exception {
                String latestProductNumber = productRepository.findLatestProductNumber();

                assertThat(latestProductNumber).isEqualTo("003");
            }
        }
        @Nested
        @DisplayName("만약 등록된 상품이 없다면")
        class Context_with_empty_repository {


            @DisplayName("null을 리턴한다")
            @Test
            void it_returns_null() throws Exception {
                String latestProductNumber = productRepository.findLatestProductNumber();

                assertThat(latestProductNumber).isNull();
            }
        }
    }

}