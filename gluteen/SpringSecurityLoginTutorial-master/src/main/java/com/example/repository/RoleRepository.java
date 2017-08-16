package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRole(String role);

}
