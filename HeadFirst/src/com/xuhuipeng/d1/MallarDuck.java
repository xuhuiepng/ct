package com.xuhuipeng.d1;

public class MallarDuck extends Duck {

    @Override
    public void display() {
        System.out.println("I'm a real Mallard duck.");
    }

    public MallarDuck (){
        quackBehavior = new Quack();
        flyBehavior = new FlyWithWings();
    }
}
