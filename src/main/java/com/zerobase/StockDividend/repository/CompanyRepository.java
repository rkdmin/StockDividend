package com.zerobase.StockDividend.repository;

import com.zerobase.StockDividend.entity.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByTicker(String ticker);
    Optional<Company> findByName(String companyName);
    Optional<Company> findByTicker(String ticker);
}
