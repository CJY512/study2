package study.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import study.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import study.cafekiosk.spring.api.service.product.response.ProductResponse;
import study.cafekiosk.spring.domain.product.Product;
import study.cafekiosk.spring.domain.product.ProductRepository;
import study.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductNumberFactory productNumberFactory;

    //동시성 이슈
    //상품 등록을 동시에 하는 경우 ->  상품 번호가 겹칠 수 있다.
    //1. 상품 번호에 unique 제약 조건 -> 3번 정도 다시 시도할 수 있도록 만들기
    //2. 상품 번호를 UUID로 만들기
    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        return productRepository.findBySellingStatusIn(ProductSellingStatus.forDisplay())
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

    }
}
