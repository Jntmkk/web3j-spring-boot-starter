package xyz.utools.web3j.factory;

public class ContractFactoryBean extends AbstractContractFactoryBeanSupport {
    public ContractFactoryBean(Class cls) {
        super(cls);
    }

    @Override
    AbstractContractFactorySupport createAbstractContractFactorySupport() {
        return new DefaultContractFactory();
    }
}
