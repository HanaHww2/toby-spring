package me.study.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/contextTest.xml")
public class SpringContainerTest {
    @Autowired
    ApplicationContext context;
    static ApplicationContext contextObject = null;

    @DisplayName("스프링테스트프레임워크가 제공하는 컨텍스트가 각 메소드에게 공유되는지1")
    @Test
    public void test1() {
        assertThat(contextObject).satisfiesAnyOf(
                contextObject -> assertThat(contextObject).isNull(),
                contextObject -> assertThat(contextObject).isEqualTo(this.context)
        );
        contextObject = this.context;
    }

    @DisplayName("스프링테스트프레임워크가 제공하는 컨텍스트가 각 메소드에게 공유되는지2")
    @Test
    public void test2() {
        assertThat(contextObject).satisfiesAnyOf(
                contextObject -> assertThat(contextObject).isNull(),
                contextObject -> assertThat(contextObject).isEqualTo(this.context)
        );
        contextObject = this.context;
    }

    @DisplayName("스프링테스트프레임워크가 제공하는 컨텍스트가 각 메소드에게 공유되는지3")
    @Test
    public void test3() {
        assertThat(contextObject).satisfiesAnyOf(
                contextObject -> assertThat(contextObject).isNull(),
                contextObject -> assertThat(contextObject).isEqualTo(this.context)
        );
        contextObject = this.context;
    }
}
