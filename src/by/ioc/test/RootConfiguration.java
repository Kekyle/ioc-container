package by.ioc.test;

import by.ioc.annotation.Bean;
import by.ioc.annotation.Configuration;
import by.ioc.annotation.Value;
import by.ioc.test.domain.Animals;
import by.ioc.test.domain.Cat;
import by.ioc.test.domain.Dog;
import by.ioc.test.domain.User;

@Configuration
@ComponentScan(basePackage = "by.ioc.")
public class RootConfiguration {

//    @Bean
//    public User user(Dog dog, Cat cat, @Value(name = "Test User") String name){
//        return new User(dog, cat, name);
//    }

    @Bean
    public Cat cat(){
        return new Cat("Test Cat");
    }

    @Bean
    public Animals dog1(){
        Dog dog = new Dog();
        dog.setName("Dog1");
        return dog;
    }

    @Bean
    public Animals dog2(){
        Dog dog = new Dog();
        dog.setName("Dog2");
        return dog;
    }
}
