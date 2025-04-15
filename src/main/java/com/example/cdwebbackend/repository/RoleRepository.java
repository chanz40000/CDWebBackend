package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    public Optional<RoleEntity> findOneById(long id);
    @Query("SELECT r FROM RoleEntity r JOIN r.users u WHERE u.id = :userId")
    List<RoleEntity> findAllByUserId(@Param("userId") Long userId);

    public Optional<RoleEntity> findOneByName(String roleUser);
}
