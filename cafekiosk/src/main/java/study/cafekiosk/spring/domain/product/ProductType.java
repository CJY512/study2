package study.cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리");

    private final String text;
    private static final List<ProductType> stockType = List.of(BOTTLE, BAKERY);

    public static boolean containsStockType(ProductType type) {
        return stockType.contains(type);
    }
}
