package xyz.utools.web3j.inter;

import org.springframework.beans.factory.BeanFactory;
import org.web3j.tx.Contract;
import xyz.utools.web3j.common.DependencyHolder;

import java.lang.reflect.Method;
import java.util.List;

public interface CandidateMethodPostProcessor {
    default void filterCandidateMethod(List<DependencyHolder> holders) {
    }

    default DependencyHolder findCandidateMethod(List<DependencyHolder> holders, Class<? extends Contract> cls) {
        return null;
    }


    default void resolveCandidateMethodParams(DependencyHolder holder, BeanFactory beanFactory, Class<? extends Contract> cls) {
    }


    default void resolveDependencyMapping(DependencyHolder holder) {
    }
}
