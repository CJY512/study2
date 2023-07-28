package study.cafekiosk.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.cafekiosk.spring.api.ApiResponse;
import study.cafekiosk.spring.api.controller.order.request.OrderRequest;
import study.cafekiosk.spring.api.service.order.OrderService;
import study.cafekiosk.spring.api.service.order.response.OrderResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/api/v1/orders/new")
    public ApiResponse<OrderResponse> createOrder(@Validated @RequestBody OrderRequest orderRequest) {
        LocalDateTime registeredDateTime = LocalDateTime.now();
        return ApiResponse.ok(orderService.createOrder(orderRequest.toServiceRequest(), registeredDateTime));
    }
}
