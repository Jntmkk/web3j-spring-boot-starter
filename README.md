# web3j-spring-boot-starter
通过 `Spring` 的依赖注入应用程序集成 `web3j` 到你的 `Spring Boot` 应用程序中。[官方项目](https://github.com/web3j/web3j-spring-boot-starter.git)只提供了`Web3j`自动注入功能，使用合约时不太方便，随有此项目。

## 快速开始

示例应用[在此](https://)

为了使用本工具，请包含以下依赖：

**暂时未发布到中央仓库**
Maven:
```xml
<dependency>
    <groupId>xyz.utools.web3j</groupId>
    <artifactId>web3j-spring-boot-starter</artifactId>
    <version>lasted</version>
</dependency>
```

为了配置连接、合约等信息，请合适配置以下字段：

```yml
web3j:
  # 合约所在的包路径，将自动扫描所有继承自 org.web3j.tx.contract 的类，暂时不提供注解的方式。
  contract-package: xyz.utools.web3j.contracts
  # 服务器地址示例，国内用户建议使用本地测试
  rpc-url: http://127.0.0.1:7545
  # 部署合约的默认账户秘钥，暂不提供其它获取身份凭证的方式
  key: f58321420b5322e266a7364f9169faefff2f497265d90b1d5145071f301111ce
  # 合约信息，名称请严格对应类名，值可为合约地址或“new”,为new是将部署合约。合约地址请去掉 0x
  contracts:
    # 示例，将从此地址加载合约
    UserContract: b07812e8d6ab69584c5361bc09b4e2edc187c029
    # 重新部署合约
    TaskContract: new
    # 其它合约
```



使用方式：

```java
@Service
public interface DemoService{
  @autowired
  UserContract userContact;
}
```



## 注意事项

由于合约的依赖无法自动推导，所以本工具只能解决固定的依赖，包括`Web3j`、`Credentials`、`ContractGasProvider`。若某些合约有其他依赖属性，则需要自己实现本工具提供的接口进行修改。

例如：某合约`B`依赖于合约`A`的地址，其`Solidity`代码如下:

```
pragma solidity ^0.4.4;

contract Register {
	Address managerAddr;
	constructor() public {
    managerAddr = msg.sender;
  }
}

contract UserSummary {
	address regAddr;
  Register reg;
	constructor(string regAddr) public {
    regAddr = msg.sender;
    reg = Register(regAddr);
  }
}

```

加载合约时，由于无法找到`string regAddr`依赖将会报错。解决方法是：实现`CandidateMethodPostProcessor`接口。示例如下：

```java
@Configuration
public class Demo{
  @Bean
  CandidateMethodPostProcessor postProcessor(){
    new AddressPostProcessor();
  }
  public static class AddressPostProcessor implements CandidateMethodPostProcessor {

        @Override
        public void resolveCandidateMethodParams(DependencyHolder holder, BeanFactory beanFactory, Class<? extends Contract> cls) {
            Iterator<Map.Entry<String, DefaultDependencyHolder.Dependency>> iterator = holder.getDependency().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, DefaultDependencyHolder.Dependency> next = iterator.next();
                if (next.getKey().equals("regAddr")) {
                    UserContract userContract = beanFactory.getBean("dependContractName", UserContract.class);
                    next.getValue().setObj(userContract.getContractAddress());
                }
            }
        }
    }
}
```

同时本工具解决了合约间的依赖关系，但合约地址字段命名有要求。如上文中的`string regAddr`改为`Address userContract`就能自动处理。


