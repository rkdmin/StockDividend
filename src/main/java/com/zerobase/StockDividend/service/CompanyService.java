package com.zerobase.StockDividend.service;

import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.entity.Dividend;
import com.zerobase.StockDividend.model.ScrapedResult;
import com.zerobase.StockDividend.repository.CompanyRepository;
import com.zerobase.StockDividend.repository.DividendRepository;
import com.zerobase.StockDividend.scraper.Scraper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    public Company save(String ticker){
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker){
        // ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)){
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        // 스크래핑 결과
        Company companyEntity = this.companyRepository.save(new Company(company));
        List<Dividend> dividendEntityList = scrapedResult.getDividendList().stream()
            .map(e -> new Dividend(companyEntity.getId(), e))
            .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendEntityList);

        return company;
    }
}
