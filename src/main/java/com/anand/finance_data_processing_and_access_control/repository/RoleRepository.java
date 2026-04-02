package com.anand.finance_data_processing_and_access_control.repository;

import com.anand.finance_data_processing_and_access_control.model.entity.Role;
import com.anand.finance_data_processing_and_access_control.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}