package xyz.utools.web3j.annotation;

import java.lang.annotation.*;

/**
 * enable web3j contracts auto injection
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnableWeb3jContracts {
}
