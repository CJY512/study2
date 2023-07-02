package study.cafekiosk.spring.api.service.order.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderServiceRequest {

    private List<String> productNumbers;

    @Builder
    private OrderServiceRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
