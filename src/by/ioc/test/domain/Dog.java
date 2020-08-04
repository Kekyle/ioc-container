package by.ioc.test.domain;

import by.ioc.annotation.Component;
import by.ioc.annotation.Value;

public class Dog implements Animals {

//    @Value(name = "Test Dog")
    private String name;

//    public Dog(String name){
//        this.name = name;
//    }


    public Dog() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
