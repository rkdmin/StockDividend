package com.zerobase.StockDividend.repository;

import com.zerobase.StockDividend.entity.Dividend;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
    List<Dividend> findAllByCompanyId(Long companyId);
    // 복합 유니크키(인덱스를 걸었음) 설정 해줬으므로 매우 빠르게 검색 가능
    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);
}
