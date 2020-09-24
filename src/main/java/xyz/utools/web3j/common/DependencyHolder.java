package xyz.utools.web3j.common;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface DependencyHolder {
    Method getMethod();

    void updateDependency(String key, Class<?> cls);

    void removeDependency(Class<?> cls);

    void removeDependency(String key);

    void addDependency(String key, Class<?> cls);

    void resolveDependency(String key, Object obj);

    Map<String, DefaultDependencyHolder.Dependency> getDependency();

    Object[] getParams();
}