package xyz.utools.web3j.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.11.
 */
@SuppressWarnings("rawtypes")
public class DemoB extends Contract {
    public static final String BINARY = "6080604052348015600f57600080fd5b506040516020806090833981016040525160008054600160a060020a0319908116331790915560018054600160a060020a0390931692909116919091179055603580605b6000396000f3006080604052600080fd00a165627a7a72305820f115718249bf900f31962c6fe757a2dd498c302ae547cf97ff977e0b1d2105c80029";

    @Deprecated
    protected DemoB(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DemoB(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DemoB(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DemoB(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Deprecated
    public static DemoB load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DemoB(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DemoB load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DemoB(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DemoB load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DemoB(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DemoB load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DemoB(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DemoB> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String demoA) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, demoA)));
        return deployRemoteCall(DemoB.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<DemoB> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String demoA) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, demoA)));
        return deployRemoteCall(DemoB.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DemoB> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String demoA) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, demoA)));
        return deployRemoteCall(DemoB.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<DemoB> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String demoA) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, demoA)));
        return deployRemoteCall(DemoB.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
