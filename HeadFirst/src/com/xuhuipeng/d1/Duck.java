package com.xuhuipeng.d1;

public abstract class Duck {

    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    /**
     * 显示
     */
   public abstract void display();

   public void performFly(){
       flyBehavior.fly();
   }

   public void performQuack(){
       quackBehavior.quack();
   }

   public void swim(){
       System.out.println("All ducks float");
   }

   // 动态设定行为
   public void setFlyBehavior (FlyBehavior fb ){
       flyBehavior = fb;
   }
   public void setQuackBehavior(QuackBehavior qb){
       quackBehavior = qb;
   }

}
