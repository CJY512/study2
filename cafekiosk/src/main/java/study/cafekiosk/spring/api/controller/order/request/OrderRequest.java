package study.cafekiosk.spring.api.controller.order.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.cafekiosk.spring.api.service.order.request.OrderServiceRequest;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequest {

    @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
    private List<String> productNumbers;

    @Builder
    private OrderRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderServiceRequest toServiceRequest() {
        return OrderServiceRequest.builder()
                .productNumbers(productNumbers)
                .build();
    }
}
