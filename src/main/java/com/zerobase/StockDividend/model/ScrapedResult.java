package com.zerobase.StockDividend.model;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.dto.DividendDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.entity.Dividend;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ScrapedResult {
    private CompanyDto companyDto;
    private List<DividendDto> dividendDtoList;

    public ScrapedResult(){
        this.dividendDtoList = new ArrayList<>();
    }
}
