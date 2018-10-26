package com.xuhuipeng.d1;

/**
 * 火箭动力的飞行行为
 */
public class FlyRocketPowered implements FlyBehavior{

    @Override
    public void fly() {
        System.out.println("I'm flying with a rocket!");
    }
}
