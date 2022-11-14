package com.zerobase.StockDividend.service;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.dto.DividendDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.entity.Dividend;
import com.zerobase.StockDividend.exception.impl.NoCompanyException;
import com.zerobase.StockDividend.model.CacheKey;
import com.zerobase.StockDividend.model.ScrapedResult;
import com.zerobase.StockDividend.repository.CompanyRepository;
import com.zerobase.StockDividend.repository.DividendRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName){
        // 1. 회사명을 기준으로 회사 정보를 조회
        Company company = companyRepository.findByName(companyName).orElseThrow(
            () -> new NoCompanyException()
        );
        // 2. 조회된 회사 id로 배당금 정보 조회
        List<Dividend> dividendList = dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<DividendDto> dividendDtoList = new ArrayList();
        for(Dividend dividend: dividendList){
            dividendDtoList.add(DividendDto.builder()
                    .date(dividend.getDate())
                    .dividend((dividend.getDividend()))
                    .build());
        }

        return new ScrapedResult(CompanyDto.builder()
            .ticker(company.getTicker())
            .name(company.getName())
            .build(),
            dividendDtoList
        );
    }

}
