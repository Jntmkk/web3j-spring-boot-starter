package xyz.utools.web3j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.utools.web3j.common.DefaultGasProvider;
import xyz.utools.web3j.inter.CandidateMethodComposite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {Web3jAutoConfiguration.class, Web3jContractAutoConfiguration.class})
//@ActiveProfiles("test")
class Web3jSpringBootStarterApplicationTests extends AbstractSpringBootTest {

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("web3j.rpc-url", () -> "http://127.0.0.1:7545");
        registry.add("web3j.key", () -> "f58321420b5322e266a7364f9169faefff2f497265d90b1d5145071f301111ce");
        registry.add("web3j.default-gas-provider", () -> "xyz.utools.web3j.common.DefaultGasProvider");
//        registry.add("DemoA", () -> "new");
//        registry.add("DemoB", () -> "new");
    }

    @Test
    void testWeb3j() {
        Web3j web3j = getSignalBean(Web3j.class);
        assertThat(web3j).isNotNull();

        Credentials credentials = getSignalBean(Credentials.class);
        assertThat(credentials).isNotNull();

        ContractGasProvider gasProvider = getSignalBean(ContractGasProvider.class);
        assertThat(gasProvider instanceof DefaultGasProvider).isTrue();
    }

    @Test
    void contextLoads() {

    }

    @Test
    void testWeb3jContractConfigurerRegistry() {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(CandidateMethodComposite.class);
        assertThat(beanNamesForType.length).isEqualTo(1);
        Object bean = applicationContext.getBean(beanNamesForType[0]);
        assertThat(bean).isNotNull();

    }

    @Test
    void testContractProvider() {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Contract.class);
        assertThat(beanNamesForType.length).isNotZero();
        if (beanNamesForType.length > 1) {
            Set<String> set = new HashSet<>(Arrays.asList(beanNamesForType));
            assertThat(set.size()).isNotEqualTo(1);
        }
        for (String s : beanNamesForType) {
            Object bean = applicationContext.getBean(s);
            assertThat(bean).isNotNull();
        }
    }

}
