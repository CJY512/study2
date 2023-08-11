package study.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.cafekiosk.spring.domain.product.ProductRepository;

/*
ProductService에서 다음 ProductNumber를 생성하는 과정을 따로 객체로 생성
 */
@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }
        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return String.format("%03d", nextProductNumberInt);
    }
}
