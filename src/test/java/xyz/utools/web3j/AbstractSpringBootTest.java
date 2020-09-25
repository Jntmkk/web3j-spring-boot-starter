package xyz.utools.web3j;

import lombok.Data;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.stream.Stream;

@Data
public abstract class AbstractSpringBootTest extends AbstractJUnit4SpringContextTests implements EnvironmentAware {
    public Environment environment;

    public ConfigurableApplicationContext getConfigApplicationContext() {
        return (ConfigurableApplicationContext) applicationContext;
    }

    public Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public void addProperties(String prefix, String... args) {
        TestPropertyValues.of(Stream.of(args).map(arg -> prefix + "." + arg)).applyTo(getConfigApplicationContext());
    }

    public <T> T getSignalBean(Class<T> cls) {
        return (T) applicationContext.getBean(cls);
    }
}
