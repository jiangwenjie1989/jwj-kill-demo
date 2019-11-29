package com.jwj.im.repository;



import com.jwj.domain.KillOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KillOrderRepository extends JpaRepository<KillOrder, Long> {
    Optional<KillOrder> findByUserId(Long userId);
}
