package com.zerobase.StockDividend.scheduler;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.entity.Dividend;
import com.zerobase.StockDividend.model.CacheKey;
import com.zerobase.StockDividend.model.ScrapedResult;
import com.zerobase.StockDividend.repository.CompanyRepository;
import com.zerobase.StockDividend.repository.DividendRepository;
import com.zerobase.StockDividend.scraper.Scraper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    // 매시 정각
    //@CacheEvict(value = "finance", key = "MMM")// finance에 key로 데이터를 찾아 그거만 지움
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)// value캐시에 있는 모든 데이터를 다 지움
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("scraping scheduler is started");

        // 저장된 회사 목록을 조회
        List<Company> companyList = companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for(Company company: companyList){
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(CompanyDto.builder()
                .name(company.getName())
                .ticker(company.getTicker())
                .build());

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 것은 저장
            // this.dividendRepository.saveAll(); 유니크키때매 오류
            scrapedResult.getDividendDtoList().stream()
                // dto -> entity
                .map(e -> new Dividend(company.getId(), e))
                // 엘리먼트를 하나씩 디비든 레퍼지토리에 삽입
                .forEach(e -> {
                    boolean exist = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                    if(!exist){
                        dividendRepository.save(e);
                        log.info("insert new dividend -> " + e.toString());
                    }
                });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지!
            try {
                Thread.sleep(3000);// 3초
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }


    }

}
