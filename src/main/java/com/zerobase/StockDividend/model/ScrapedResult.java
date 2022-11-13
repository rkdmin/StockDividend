package com.zerobase.StockDividend.model;

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
    private Company company;
    private List<Dividend> dividendList;

    public ScrapedResult(){
        this.dividendList = new ArrayList<>();
    }
}
