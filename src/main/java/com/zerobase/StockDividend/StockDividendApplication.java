package com.zerobase.StockDividend;

import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.scraper.YahooFinanceScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockDividendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockDividendApplication.class, args);
    }
}
