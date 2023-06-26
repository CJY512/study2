package study.cafekiosk.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime OrderDateTime;
    private final List<Beverage> beverages;

}
