package com.example.CashrichLogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CashrichLogin.domain.UserCryptoDetails;

public interface UserCryptoDetailsRepo extends JpaRepository<UserCryptoDetails, Integer> {

}
