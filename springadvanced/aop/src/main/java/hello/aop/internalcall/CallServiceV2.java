package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {
/*
    private ApplicationContext applicationContext;

    public CallServiceV2(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
*/
    private ObjectProvider<CallServiceV2> objectProvider;

    public CallServiceV2(ObjectProvider<CallServiceV2> objectProvider) {
        this.objectProvider = objectProvider;
    }

    public void external() {
        log.info("call external");
//        CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callServiceV2 = objectProvider.getObject();
        callServiceV2.internal(); // 내부 메서드 호출(this.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
