package com.questcontest.service.interfaces;

import com.questcontest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameAndPass(String name, String pass);

    List<User> findByName(String name);

}
