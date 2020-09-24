package xyz.utools.web3j;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = Web3jProperties.PREFIX)
public class Web3jProperties {
    public static final String PREFIX = "web3j";
    String contractPackage;
    String rpcUrl;
    Map<String, String> contracts;
    String key;
    String defaultGasProvider;
}
