package xyz.utools.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

@Slf4j
@Configuration
@EnableConfigurationProperties(Web3jProperties.class)
public class Web3jAutoConfiguration {
    @Autowired
    Web3jProperties properties;

    /**
     * 由配置文件生成 {@link Web3j Web3j}，支持 HTTP、HTTPS、websocket
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = Web3jProperties.PREFIX, name = "rpc-url")
    public Web3j web3j() throws Exception {
        String rpcUrl = properties.getRpcUrl();
        Web3j web3j = null;
        if (rpcUrl.startsWith("http"))
            web3j = Web3j.build(new HttpService(rpcUrl));
        if (rpcUrl.startsWith("ws")) {
            WebSocketService webSocketService = new WebSocketService(rpcUrl, false);
            webSocketService.connect();
            web3j = Web3j.build(webSocketService);
        }
        if (log.isDebugEnabled())
            log.debug("build web3j");
        return web3j;
    }

    /**
     * 由配置文件生成默认凭证 {@link Credentials Credentials} ，加载合约时使用。
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = Web3jProperties.PREFIX, name = "key")
    public Credentials credentials() {
        if (!StringUtils.isEmpty(properties.getKey()))
            return Credentials.create(properties.getKey());
        return null;
    }

    /**
     * 有配置文件生成默认 {@link ContractGasProvider ContractGasProvider} ,配置文件未提供时，生成默认 {@link org.web3j.tx.gas.DefaultGasProvider DefaultGasProvider}
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = Web3jProperties.PREFIX, name = "default-gas-provider", matchIfMissing = true)
    public ContractGasProvider contractGasProvider() {
        String className = properties.getDefaultGasProvider();
        ContractGasProvider obj = null;
        if (StringUtils.hasText(className)) {
            try {
                obj = (ContractGasProvider) Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        obj = new xyz.utools.web3j.common.DefaultGasProvider();
        return obj;
    }
}
