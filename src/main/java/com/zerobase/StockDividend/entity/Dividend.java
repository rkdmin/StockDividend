package com.zerobase.StockDividend.entity;

import com.zerobase.StockDividend.dto.DividendDto;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"companyId", "date"}// 복합 기본키 설정
        )
    }
)
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public Dividend(Long companyId, DividendDto dividendDto){
        this.companyId = companyId;
        this.date = dividendDto.getDate();
        this.dividend = dividendDto.getDividend();
    }
}
