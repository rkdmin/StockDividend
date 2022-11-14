package com.zerobase.StockDividend.controller;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.model.CacheKey;
import com.zerobase.StockDividend.service.CompanyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/company")
@RequiredArgsConstructor
@RestController
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManager;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        var result = this.companyService.autocomplete(keyword);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('READ')")
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<Company> companyList = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyList);
    }

    @PreAuthorize("hasRole('WRITE')")
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        CompanyDto companyDto = this.companyService.save(ticker);
        // 회사를 저장할때마다 트라이에 회사명 저장
        this.companyService.addAutoCompleteKeyword(companyDto.getName());
        return ResponseEntity.ok(companyDto);
    }

    @PreAuthorize("hasRole('WRITE')")
    @DeleteMapping("/{ticker}")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker){
        String companyName = companyService.deleteCompany(ticker);
        clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName){
        redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}
