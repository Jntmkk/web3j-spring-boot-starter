package xyz.utools.web3j.common;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Data
public class DefaultDependencyHolder implements DependencyHolder {
    Map<String, Dependency> maps = new LinkedHashMap<>();
    Method method;

    @Data
    public static class Dependency {
        String key;
        Class<?> cls;
        Object obj;

        public Dependency(Class<?> cls) {
            this(cls, null);
        }

        public Dependency(Class<?> cls, Object obj) {
            this.cls = cls;
            this.obj = obj;
        }

        public Dependency(Class<?> cls, String key) {
            this.key = key;
            this.cls = cls;
        }
    }

    public DefaultDependencyHolder(Method method) {
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public void updateDependency(String key, Class<?> cls) {
        maps.put(key, new Dependency(cls));
    }

    @Override
    public void removeDependency(Class<?> cls) {

    }

    @Override
    public void removeDependency(String key) {

//        maps.remove(maps.get(key));
    }

    @Override
    public void addDependency(String key, Class<?> cls) {
        maps.put(key, new Dependency(cls, key));
    }

    @Override
    public void resolveDependency(String key, Object obj) {
        Dependency dependency = maps.get(key);
        maps.replace(key, new Dependency(dependency.cls, obj));
    }

    @Override
    public Map<String, Dependency> getDependency() {
        return maps;
    }

    @Override
    public Object[] getParams() {
        return maps.values().stream().map(value -> value.getObj()).toArray();
    }

    public static DependencyHolder build(Method method) {
        DefaultDependencyHolder holder = new DefaultDependencyHolder(method);
        Stream.of(method.getParameters()).forEach(parameter -> holder.addDependency(parameter.getName(), parameter.getType()));
        return holder;
    }

    public static DependencyHolder[] build(Method[] methods) {
        return Stream.of(methods).map(method1 -> DefaultDependencyHolder.build(method1)).toArray(DependencyHolder[]::new);
    }
}
