package com.moyoprototype.user.repository;

import com.moyoprototype.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByAppId(String appId);

    Optional<User> findByName(String Name);
}
