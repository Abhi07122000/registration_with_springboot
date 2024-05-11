package com.example.CashrichLogin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CashrichLogin.domain.User;


public interface UserRepository extends JpaRepository<User , Long>{

	Optional<User> findByUsername(String username);

}
