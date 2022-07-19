package com.yaanlabs.healthpredictionapi.repository;

import com.yaanlabs.healthpredictionapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String emailAddress);

    List<User> findUserByName(String name);

    List<User> findUserByMobileNo(String mobileNo);

    List<User> findUserByNameAndMobileNo(String name, String mobileNo);

}
