package xyz.utools.web3j.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.util.function.SupplierUtils;
import xyz.utools.web3j.AbstractSpringBootTest;
import xyz.utools.web3j.Web3jAutoConfiguration;

import java.util.Properties;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@Data
@SpringBootTest(classes = {Web3jAutoConfiguration.class})
@ActiveProfiles("test")
class ClassUtilsTest extends AbstractSpringBootTest {
    @Test
    void isWeb3jContract() {
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("web3j.contract.Device", () -> "ADB");
    }

    @Test
    void getValueFromConfiguration() {
        Properties web3j = ClassUtils.getValueFromConfiguration(environment, "web3j.contracts");
        assertThat(web3j.size()).isNotZero();
        Properties web3j2 = ClassUtils.getValueFromConfiguration(environment, "web3j.contract-package");
        assertThat(web3j2.size()).isNotZero();
    }
}