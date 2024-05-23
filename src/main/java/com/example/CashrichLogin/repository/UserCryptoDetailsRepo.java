package com.example.CashrichLogin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CashrichLogin.domain.UserCryptoDetails;

@Repository
public interface UserCryptoDetailsRepo extends JpaRepository<UserCryptoDetails, Integer> {

}
