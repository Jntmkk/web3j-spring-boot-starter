package xyz.utools.web3j.registerar;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.web3j.tx.Contract;
import xyz.utools.web3j.factory.ContractFactoryBean;
import xyz.utools.web3j.util.ClassUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Data
public abstract class ContractsRegistrarSupport implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    Environment environment;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Assert.notNull(environment, "environment must not be null");

        Properties valueFromConfiguration = ClassUtils.getValueFromConfiguration(environment, "web3j.contracts-package");
        if (valueFromConfiguration == null) {
            log.warn("contracts package is not configured!");
            return;
        }
        List<String> arr = new LinkedList<>();
        ClassUtils.getValueFromConfiguration(environment, "web3j.contracts").forEach((k, v) -> arr.add((String) k));
        ContractComponentProvider componentProvider = new ContractComponentProvider();
        componentProvider.setConfiguredContracts(arr);
        Set<BeanDefinition> beanDefinitions = componentProvider.findCandidateComponents(ClassUtils.getValueFromConfiguration(environment, "web3j.contract-package").getProperty("contract-package"));
        for (BeanDefinition beanDefinition : beanDefinitions) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ContractFactoryBean.class);
            AbstractBeanDefinition factoryBeanDefinition = builder.getBeanDefinition();

            Class<? extends Contract> cls = (Class<? extends Contract>) Class.forName(beanDefinition.getBeanClassName());

            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, cls);
            factoryBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);

            String beanClassName = beanDefinition.getBeanClassName();

            registry.registerBeanDefinition(beanClassName.substring(beanClassName.lastIndexOf(".") + 1), factoryBeanDefinition);
        }
    }
}
