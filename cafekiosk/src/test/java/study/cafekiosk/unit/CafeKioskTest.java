package study.cafekiosk.unit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.cafekiosk.unit.beverage.Americano;
import study.cafekiosk.unit.beverage.Latte;
import study.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;


class CafeKioskTest {

    @Test
    void add() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        //when
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //then
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(latte);

    }

    @Test
    void addMultiBeverage() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //when
        cafeKiosk.add(americano, 2);

        //then
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);

    }
    @Test
    void addZeroBeverage() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //when

        //then
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문할 수 있습니다.");

    }

    @Test
    void remove() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        cafeKiosk.remove(americano);
        cafeKiosk.remove(latte);

        //then
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    @Test
    void clear() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        cafeKiosk.clear();

        //then
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    @Test
    @DisplayName("전체 가격")
    void getTotalPrice() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        int totalPrice = cafeKiosk.calculateTotalPrice();
        int result = americano.getPrice() + latte.getPrice();

        //then
        assertThat(totalPrice).isEqualTo(result);

    }

    @Test
    void createOrderOnTime() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 06, 26, 10, 0));

        //then
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(order.getBeverages()).hasSize(2);

    }

    @Test
    @DisplayName("운영 시간 외에 주문")
    void createOrderNotOnTime() throws Exception {
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when

        //then
        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 06, 26, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");

    }

}