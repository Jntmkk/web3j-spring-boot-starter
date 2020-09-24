package xyz.utools.web3j.util;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.Assert;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.utools.web3j.common.DefaultDependencyHolder;
import xyz.utools.web3j.common.DependencyHolder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ClassUtils {
    public static boolean isWeb3jContract(Class<?> cls) {
        return Contract.class.isAssignableFrom(cls);
    }

    public static Properties getValueFromConfiguration(Environment environment, String prefix) {
        Properties properties = new Properties();
        AbstractEnvironment abstractEnvironment = (AbstractEnvironment) environment;
        MutablePropertySources propertySources = abstractEnvironment.getPropertySources();
        StreamSupport.stream(propertySources.spliterator(), false).filter(it -> it instanceof OriginTrackedMapPropertySource)//
                .map(it -> ((OriginTrackedMapPropertySource) it).getPropertyNames())
                .flatMap(Arrays::stream)
                .forEach(propertyName -> {
                    if (propertyName.startsWith(prefix))
                        properties.put(propertyName.substring(propertyName.lastIndexOf(".") + 1), environment.getProperty(propertyName));
                });
        return properties;
    }

    public static Parameter[] getParameter(Method methods) {
        return Stream.of(methods.getParameters()).filter(parameter -> !isPrimaryParam(parameter.getType())).toArray(Parameter[]::new);
    }

    public static boolean isPrimaryParam(Class<?> cls) {
        return cls.equals(Web3j.class) || cls.equals(Credentials.class) || cls.equals(TransactionManager.class) || cls.equals(ContractGasProvider.class);
    }

    public static boolean isValidated(DependencyHolder holder) {
        for (DefaultDependencyHolder.Dependency value : holder.getDependency().values()) {
            if (value.getObj() == null)
                return false;
        }
        return true;
    }

    public static int validNum(DependencyHolder holder) {
        return (int) holder.getDependency().values().stream().filter(dependency -> dependency.getObj() != null).count();
    }

    public static DefaultDependencyHolder.Dependency getUnsatisfiedDependency(DependencyHolder holder) {
        for (DefaultDependencyHolder.Dependency value : holder.getDependency().values()) {
            if (value.getObj() == null)
                return value;
        }
        Assert.notNull(null,"there is no unsatisfied field ");
        return null;
    }
}
