package com.irvza.jwt.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.irvza.jwt.Modelos.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
}
