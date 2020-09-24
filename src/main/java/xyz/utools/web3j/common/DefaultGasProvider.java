package xyz.utools.web3j.common;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class DefaultGasProvider implements ContractGasProvider {
    @Override
    public BigInteger getGasPrice(String s) {
        return getGasPrice();
    }

    @Override
    public BigInteger getGasPrice() {
        return BigInteger.valueOf(20000000000L);
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return getGasLimit();
    }

    @Override
    public BigInteger getGasLimit() {
        return BigInteger.valueOf(5000000L);
    }
}
