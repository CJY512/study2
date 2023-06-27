package study.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    select *
    from Product
    where productSellingStatus in (' ', ' ', ... )
     */
    List<Product> findBySellingStatusIn(List<ProductSellingStatus> productSellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);
}
