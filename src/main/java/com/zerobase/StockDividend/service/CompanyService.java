package com.zerobase.StockDividend.service;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.entity.Dividend;
import com.zerobase.StockDividend.exception.impl.NoCompanyException;
import com.zerobase.StockDividend.model.ScrapedResult;
import com.zerobase.StockDividend.repository.CompanyRepository;
import com.zerobase.StockDividend.repository.DividendRepository;
import com.zerobase.StockDividend.scraper.Scraper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final Trie trie;// config에서 Bean으로 생성되어 공용으로 사용가능
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    public CompanyDto save(String ticker){
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if(exists){
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private CompanyDto storeCompanyAndDividend(String ticker){
        // ticker를 기준으로 회사를 스크래핑
        CompanyDto companyDto = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(companyDto)){
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(companyDto);

        // 스크래핑 결과
        Company companyEntity = this.companyRepository.save(new Company(companyDto));
        List<Dividend> dividendEntityList = scrapedResult.getDividendDtoList().stream()
            .map(e -> new Dividend(companyEntity.getId(), e))
            .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendEntityList);

        return companyDto;
    }

    public Page<Company> getAllCompany(Pageable pageable){
        return this.companyRepository.findAll(pageable);
    }

    public void addAutoCompleteKeyword(String keyword){
        this.trie.put(keyword, null);// 자동완성기능만 구현할것이기에 value는 null넣음
    }

    public List<String> autocomplete(String keyword){
        return (List<String>) this.trie.prefixMap(keyword).keySet()
            .stream()
            .limit(10)// 10개로 제한
            .collect(Collectors.toList());
    }

    public void deleteAutocompleteKeyword(String keyword){
        this.trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {
        var company = companyRepository.findByTicker(ticker).orElseThrow(
            () -> new NoCompanyException()
        );

        dividendRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);
        deleteAutocompleteKeyword(company.getName());
        return company.getName();
    }
}
