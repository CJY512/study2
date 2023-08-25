package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 포인트컷과 어드바이스 분리
 */
@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* hello.aop.order..*(..))")
    private void orderAll(){} //메서드와 파라미터를 합쳐 포인트컷 시그니쳐라고 한다.
    //public 으로 열어두면 다른 Aspect 에서 재사용할 수 있다.
    //반환값은 void

    @Around("orderAll()") //포인트컷 시그니쳐를 설정
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

}
