package study.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.cafekiosk.spring.api.service.product.response.ProductResponse;
import study.cafekiosk.spring.domain.product.Product;
import study.cafekiosk.spring.domain.product.ProductRepository;
import study.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        return productRepository.findBySellingStatusIn(ProductSellingStatus.forDisplay())
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

    }
}
