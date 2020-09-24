package xyz.utools.web3j.inter;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
public class CandidateMethodComposite implements InitializingBean, ApplicationContextAware {
    ApplicationContext applicationContext;
    List<CandidateMethodPostProcessor> postProcessors = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(CandidateMethodPostProcessor.class).entrySet().stream().forEach(entry -> postProcessors.add(entry.getValue()));
    }

    public CandidateMethodPostProcessor[] getAllCandidateMethodPostProcessor() {
        return postProcessors.toArray(new CandidateMethodPostProcessor[postProcessors.size()]);
    }

}
