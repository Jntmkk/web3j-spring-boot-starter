package xyz.utools.web3j.factory;

import lombok.Data;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.util.Lazy;
import org.springframework.lang.Nullable;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.utools.web3j.Web3jProperties;
import xyz.utools.web3j.inter.CandidateMethodComposite;

@Data
@EnableConfigurationProperties(Web3jProperties.class)
public abstract class AbstractContractFactoryBeanSupport<T extends Contract> implements BeanFactoryAware, FactoryBean<T>, InitializingBean {
    @Autowired
    Web3jProperties properties;
    protected Class<T> cls;
    @Autowired
    protected Web3j web3j;
    protected @Nullable
    TransactionManager manager;
    @Autowired
    protected @Nullable
    Credentials credentials;
    @Autowired
    protected ContractGasProvider contractGasProvider;
    protected Lazy<T> target;
    protected BeanFactory beanFactory;
    //    public ClassLoader classLoader;
    protected AbstractContractFactorySupport factorySupport;

    public AbstractContractFactoryBeanSupport(Class<T> cls) {
        this.cls = cls;
    }
    @Autowired
    CandidateMethodComposite composite;

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public T getObject() throws Exception {
        return target.get();
    }

    @Override
    public Class<?> getObjectType() {
        return cls;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.factorySupport = createAbstractContractFactorySupport();
        this.factorySupport.setBeanFactory(beanFactory);
        this.factorySupport.setCls(cls);
        this.factorySupport.setWeb3j(web3j);
        this.factorySupport.setCredentials(credentials);
        this.factorySupport.setGasProvider(contractGasProvider);
        this.factorySupport.setProperties(properties);
        this.factorySupport.setComposite(composite);
        target = Lazy.of(() -> {
            try {
                return factorySupport.getContract();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    abstract AbstractContractFactorySupport createAbstractContractFactorySupport();
}
