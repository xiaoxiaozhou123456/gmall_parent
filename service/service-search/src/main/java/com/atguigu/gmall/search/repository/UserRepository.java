package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<Person,Long> {

    List<Person> findAllByAddressLikeAndAgeGreaterThanEqual(String address, Long age);
    List<Person> findAllByAgeBetween(Long age, Long age2);

    List<Person> findAllByIdLessThan(Long id);
}
