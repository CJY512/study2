package study.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.cafekiosk.spring.api.ApiResponse;
import study.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import study.cafekiosk.spring.api.service.product.ProductService;
import study.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Validated @RequestBody ProductCreateRequest request) {
        return ApiResponse.ok(productService.createProduct(request.toServiceRequest()));
    }
    @GetMapping("/api/v1/products/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.ok(productService.getSellingProducts());
    }
}
