package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    @Query("select o from OrderEntity o where o.user.id =:userId")
    Page<OrderEntity> findOrderByUserId(Long userId, Pageable pageable);

}
