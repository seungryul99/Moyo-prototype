package com.moyoprototype.user.repository;

import com.moyoprototype.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByAppId(String appId);
}
