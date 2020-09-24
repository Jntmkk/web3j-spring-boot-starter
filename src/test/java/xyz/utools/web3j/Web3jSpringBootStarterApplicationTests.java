package xyz.utools.web3j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.tx.Contract;
import xyz.utools.web3j.inter.CandidateMethodComposite;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {Web3jAutoConfiguration.class, Web3jContractAutoConfiguration.class})
@ActiveProfiles("test")
class Web3jSpringBootStarterApplicationTests extends AbstractSpringBootTest {

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
