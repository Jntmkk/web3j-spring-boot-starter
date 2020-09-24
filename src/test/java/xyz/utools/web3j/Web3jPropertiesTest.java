package xyz.utools.web3j;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@Data
class Web3jPropertiesTest {
    @Test
    public void testList() {
        String[] arr = {"a", "b", "c"};
        List<String> list = new ArrayList<>(Arrays.asList(arr));
        list.add("D");
        log.info(list.toString());
    }
}