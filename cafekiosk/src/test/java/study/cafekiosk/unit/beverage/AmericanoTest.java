package study.cafekiosk.unit.beverage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {
    
    @Test
    public void AmericanoTest() throws Exception {
        //given
        //when
        Americano americano = new Americano();

        //then
        assertThat(americano.getName()).isEqualTo("아메리카노");
        assertThat(americano.getPrice()).isEqualTo(4000);
        
    }

}