package xyz.utools.web3j.registerar;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.Assert;
import org.web3j.tx.Contract;
import xyz.utools.web3j.util.ClassUtils;

import java.io.IOException;
import java.util.List;

@Data
@Slf4j
public class ContractComponentProvider extends ClassPathScanningCandidateComponentProvider {
    List<String> configuredContracts;

    public ContractComponentProvider() {
        super(false);
        super.addIncludeFilter(new ClassTypeFilter(Contract.class));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        Assert.notNull(configuredContracts, "configured contracts list can not be null");
        String beanClassName = beanDefinition.getBeanClassName();
        if (!configuredContracts.contains(beanClassName.substring(beanClassName.lastIndexOf(".") + 1))) {
            log.warn(String.format("find contract %s but do not configure contract address!!!", beanDefinition.getBeanClassName()));
            return false;
        }
        Class<?> cls = null;
        try {
            cls = Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Assert.notNull(cls, String.format("class %s do not exist", beanDefinition.getBeanClassName()));
        return ClassUtils.isWeb3jContract(cls);
    }

    private static class ClassTypeFilter extends AssignableTypeFilter {
        /**
         * Create a new AssignableTypeFilter for the given type.
         *
         * @param targetType the type to match
         */
        public ClassTypeFilter(Class<?> targetType) {
            super(targetType);
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return super.match(metadataReader, metadataReaderFactory) && metadataReader.getClassMetadata().isConcrete();
        }
    }
}
