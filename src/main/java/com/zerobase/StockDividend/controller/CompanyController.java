package com.zerobase.StockDividend.controller;

import com.zerobase.StockDividend.dto.CompanyDto;
import com.zerobase.StockDividend.entity.Company;
import com.zerobase.StockDividend.service.CompanyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword){
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<Company> companyList = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companyList);
    }

    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request){
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        CompanyDto companyDto = this.companyService.save(ticker);

        return ResponseEntity.ok(companyDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
