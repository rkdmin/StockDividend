package com.zerobase.StockDividend.scraper;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.model.ScrapedResult;

public interface Scraper {
    CompanyDto scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(CompanyDto companyDto);
}
