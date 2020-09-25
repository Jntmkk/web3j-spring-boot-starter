package xyz.utools.web3j.factory;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.utools.web3j.Web3jProperties;
import xyz.utools.web3j.common.DefaultDependencyHolder;
import xyz.utools.web3j.common.DependencyHolder;
import xyz.utools.web3j.inter.CandidateMethodComposite;
import xyz.utools.web3j.inter.CandidateMethodPostProcessor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public abstract class AbstractContractFactorySupport implements  ApplicationContextAware {
    ApplicationContext beanFactory;
    //    BeanFactory beanFactory;
    ClassLoader classLoader;

    Web3j web3j;
    Credentials credentials;
    ContractGasProvider gasProvider;

    Class<? extends Contract> cls;
    Web3jProperties properties;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Method> methods;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    Method candidateMethod;

    CandidateMethodComposite composite;

    public boolean isDeploy() {
        return getContractAddress().equals("new");
    }

    public String getContractAddress() {
        String s = properties.getContracts().get(cls.getSimpleName());
        return s.equals("new") ? "new" : "0x" + s;
    }

    public String getMethodName() {
        return isDeploy() ? "deploy" : "load";
    }

    private Method[] collectMethods(Class<?> cls) {
        return Stream.of(cls.getMethods()).filter(method -> method.getModifiers() == 9 && method.getName().equals(getMethodName())).collect(Collectors.toList()).toArray(new Method[]{});
    }

    private DependencyHolder findCandidateMethod(List<DependencyHolder> holders, Class<? extends
            Contract> cls, BeanFactory beanFactory) {
        DependencyHolder obj = null;
        for (CandidateMethodPostProcessor candidateMethodPostProcessor : composite.getAllCandidateMethodPostProcessor()) {
            obj = candidateMethodPostProcessor.findCandidateMethod(holders, cls);
            if (obj != null)
                return obj;
        }
        return null;
    }

    private void resolveDependencyMapping(List<DependencyHolder> holders) {
        for (DependencyHolder holder : holders) {
            for (CandidateMethodPostProcessor candidateMethodPostProcessor : composite.getAllCandidateMethodPostProcessor()) {
                candidateMethodPostProcessor.resolveDependencyMapping(holder);
            }
        }

    }

    private void resolveMethodParams(List<DependencyHolder> holders) {
        for (DependencyHolder holder : holders) {
            for (CandidateMethodPostProcessor candidateMethodPostProcessor : composite.getAllCandidateMethodPostProcessor()) {
                candidateMethodPostProcessor.resolveCandidateMethodParams(holder, beanFactory, cls);
            }
        }
    }

    private void resolveMethodParamsMapping(List<DependencyHolder> holders) {
    }

    private <T> T invokeMethod(DependencyHolder holder) throws Exception {
        return (T) ((RemoteCall) holder.getMethod().invoke(null, holder.getParams())).send();
    }

    private void filterCandidateMethod(List<DependencyHolder> holders) {
        for (CandidateMethodPostProcessor candidateMethodPostProcessor : composite.getAllCandidateMethodPostProcessor()) {
            candidateMethodPostProcessor.filterCandidateMethod(holders);
        }
    }

    private void validate(DependencyHolder holder) {
        List<Map.Entry<String, DefaultDependencyHolder.Dependency>> collect = holder.getDependency().entrySet().stream().filter(entry -> entry.getValue() == null).collect(Collectors.toList());
        Assert.isTrue(collect.size() == 0, String.format("can not resolve dependency which name and class are %s , %s", collect.get(0).getKey(), collect.get(0).getValue().getCls()));
    }

    public <T> T getContract() throws Exception {
        T t = null;
        /**
         * 更改处理逻辑
         */
//        DependencyHolder dependencyHolder = findCandidateMethod(collectMethods(cls), cls, beanFactory);
//        resolveDependencyMapping(dependencyHolder);
//        resolveMethodParamsMapping(dependencyHolder);
//        resolveMethodParams(dependencyHolder);
//        t = invokeMethod(dependencyHolder);
//        Assert.notNull(t, "");
        /**
         *  处理逻辑
         *      1.过滤方法，credential or transactionManager
         *      2.依赖属性映射，可修改依赖属性的class信息，例如合约的地址为{@link org.web3j.abi.datatypes.Address Address} ，但
         *    编写合约写的是 string ，生成的Java类也是string。
         *      3.注入依赖属性的实体
         *      4.获取对象
         */
        Method[] methods = collectMethods(cls);
        List<DependencyHolder> holders = new ArrayList<>(Arrays.asList(DefaultDependencyHolder.build(methods)));
        filterCandidateMethod(holders);
        resolveMethodParamsMapping(holders);
        resolveMethodParams(holders);
        DependencyHolder candidateMethod = findCandidateMethod(holders, cls, beanFactory);

        t = invokeMethod(candidateMethod);
        return t;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext;
    }
}
