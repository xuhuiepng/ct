package com.xuhuipeng.d1;

public class Quack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("quack!!呱呱叫");
    }
}
