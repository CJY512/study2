package study.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {
    
    @DisplayName("상품이 재고 관리 상품 타입이면 true를 리턴한다")
    @Test
    void containsStockType() throws Exception {
        //given
        ProductType productType = ProductType.BOTTLE;

        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        assertThat(result).isTrue();
        
    }

    @DisplayName("상품이 재고 관리 상품 타입이 아니라면 false를 리턴한다")
    @Test
    void containsStockType2() throws Exception {
        //given
        ProductType productType = ProductType.HANDMADE;

        //when
        boolean result = ProductType.containsStockType(productType);

        //then
        assertThat(result).isFalse();

    }

}