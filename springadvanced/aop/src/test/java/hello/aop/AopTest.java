package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
//@Import(AspectV4Pointcuts.class)
@Import({AspectV5Order.LogAspect.class, AspectV5Order.TxAspect.class})
class AopTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @DisplayName("AOP 프록시 적용됐나?")
    @Test
    void aopInfo() throws Exception {
        log.info("isAopProxy(orderService)={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy(orderRepository)={}", AopUtils.isAopProxy(orderRepository));
    }

    @DisplayName("정상 로직")
    @Test
    void success() throws Exception {
        orderService.orderItem("itemA");
    }
    
    @DisplayName("예외")
    @Test
    void exception() throws Exception {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
