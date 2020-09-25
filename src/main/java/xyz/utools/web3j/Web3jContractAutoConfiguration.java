package xyz.utools.web3j;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.utools.web3j.common.DefaultDependencyHolder;
import xyz.utools.web3j.common.DependencyHolder;
import xyz.utools.web3j.inter.*;
import xyz.utools.web3j.registerar.ContractsRegistrar;
import xyz.utools.web3j.util.ClassUtils;

import java.util.*;

@Configuration
@ConditionalOnBean(value = {Web3j.class, Credentials.class, ContractGasProvider.class})
@AutoConfigureAfter(Web3jAutoConfiguration.class)
@Import({ContractsRegistrar.class})
public class Web3jContractAutoConfiguration {
    @Bean
    public CandidateMethodComposite candidateMethodComposite() {
        return new CandidateMethodComposite();
    }

    @Bean
    public CandidateMethodPostProcessor defaultCandidateMethodPostProcessor() {
        return new DefaultCandidateMethodPostProcessor();
    }


    public static class DefaultCandidateMethodPostProcessor implements CandidateMethodPostProcessor {
        @Override
        public void filterCandidateMethod(List<DependencyHolder> holders) {

        }

        @Override
        public DependencyHolder findCandidateMethod(List<DependencyHolder> holders, Class<? extends Contract> cls) {
            DependencyHolder target = null;
//            List<DependencyHolder> collect = holders.stream()
//                    .filter(holder -> ClassUtils.isValidated(holder))
//                    .collect(Collectors.toList());
            holders.sort(Comparator.comparingInt(dep -> (int) dep.getDependency().values().stream().filter(dependency -> dependency.getObj() != null).count()));
            Collections.reverse(holders);
            target = holders.get(0);

            if (!ClassUtils.isValidated(target))
                throw new RuntimeException(String.format("can not find %s method for class %s,the dependency whose name and class are %s and %s ,respectively is not satisfied ", target.getMethod().getName(), cls.getName(), ClassUtils.getUnsatisfiedDependency(target).getKey(), ClassUtils.getUnsatisfiedDependency(target).getCls().getName()));
            return target;
        }

        @Override
        public void resolveCandidateMethodParams(DependencyHolder holder, BeanFactory beanFactory, Class<? extends Contract> cls) {
            Iterator<Map.Entry<String, DefaultDependencyHolder.Dependency>> iterator = holder.getDependency().entrySet().iterator();
            Set<String> set = new HashSet<>();
            if (beanFactory instanceof ApplicationContext)
                set.addAll(Arrays.asList(((ApplicationContext) beanFactory).getBeanNamesForType(Contract.class)));
            while (iterator.hasNext()) {
                Map.Entry<String, DefaultDependencyHolder.Dependency> next = iterator.next();
                if (ClassUtils.isPrimaryParam(next.getValue().getCls()) && beanFactory.containsBean(StringUtils.uncapitalize(next.getValue().getCls().getSimpleName()))) {
                    next.getValue().setObj(beanFactory.getBean(next.getValue().getCls()));
                }
                if (next.getValue().getCls().equals(String.class) && set.contains(StringUtils.capitalize(next.getKey()))) {
                    next.getValue().setObj(beanFactory.getBean(StringUtils.capitalize(next.getKey()), Contract.class).getContractAddress());
                }
            }
        }

        @Override
        public void resolveDependencyMapping(DependencyHolder holder) {

        }
    }


}
