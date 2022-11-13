package com.zerobase.StockDividend.scraper;

import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
