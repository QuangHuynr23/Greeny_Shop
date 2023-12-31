package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail (String email);
    List<UserEntity> findAll();
}
