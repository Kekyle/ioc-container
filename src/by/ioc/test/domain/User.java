package by.ioc.test.domain;

import by.ioc.annotation.Autowired;
import by.ioc.annotation.Component;
import by.ioc.annotation.Qualifier;
import by.ioc.annotation.Value;

@Component
public class User {

    @Autowired
    @Qualifier(value = "dog2")
    private Animals dog;

    @Autowired
    private Cat cat;

    @Value(name = "Test User")
    private String name;

    public User(@Qualifier(value = "dog1") Animals dog2, Cat cat, String name) {
        this.dog = dog2;
        this.cat = cat;
        this.name = name;
    }

    public Animals getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "dog=" + dog +
                ", cat=" + cat +
                ", name='" + name + '\'' +
                '}';
    }
}
