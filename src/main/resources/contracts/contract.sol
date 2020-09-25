pragma solidity ^0.4.0;

contract DemoA{
    address admin;
    constructor() public{
        admin = msg.sender;
    }


}

contract DemoB{
    address admin;
    DemoA a;
    constructor(address demoA) public{
        admin = msg.sender;
        a = DemoA(demoA);
    }
}

