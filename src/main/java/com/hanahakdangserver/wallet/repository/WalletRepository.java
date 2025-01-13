package com.hanahakdangserver.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanahakdangserver.wallet.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
