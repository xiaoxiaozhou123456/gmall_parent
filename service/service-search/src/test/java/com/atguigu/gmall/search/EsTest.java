package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: cxz
 * @create； 2022-09-03 22:50
 **/
@SpringBootTest
public class EsTest {
    @Autowired
    UserRepository repository;

    @Test
    public void test(){
        Person person = new Person();
        person.setAddress("渭南龙背");
        person.setAge(13L);
        person.setId(2L);
        person.setName("婷婷");
        Person person2 = new Person();
        person2.setAddress("渭南新市");
        person2.setAge(33L);
        person2.setId(3L);
        person2.setName("程豪");
        Person person3 = new Person();
        person3.setAddress("西安高新");
        person3.setAge(28L);
        person3.setId(4L);
        person3.setName("程创");
        Person person4 = new Person();
        person4.setAddress("渭南四马路");
        person4.setAge(11L);
        person4.setId(5L);
        person4.setName("左铿");

        repository.save(person);
        repository.save(person2);
        repository.save(person3);
        repository.save(person4);
    }
    @Test
    public void testes(){
        List<Person> 渭南 = repository.findAllByAddressLikeAndAgeGreaterThanEqual("渭南", 14L);
//        for (Person person : 渭南) {
//                System.out.println(person);
//        }

        List<Person> allByAgeBetween = repository.findAllByAgeBetween(14L, 29L);
//        for (Person person : allByAgeBetween) {
//            System.out.println("person = " + person);
//        }

        List<Person> allByIdLessThan = repository.findAllByIdLessThan(3L);
        for (Person person : allByIdLessThan) {
            System.out.println("person = " + person);
        }
    }
}
