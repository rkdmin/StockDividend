package com.zerobase.StockDividend.controller;

import com.zerobase.StockDividend.entity.Member;
import com.zerobase.StockDividend.model.Auth;
import com.zerobase.StockDividend.security.TokenProvider;
import com.zerobase.StockDividend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public String signup(@RequestBody Auth.SignUp request, BindingResult bindingResult){
        memberService.register(request);

        return "회원가입이 완료되었습니다.";
    }

    @PostMapping("/signin")
    public String signin(@RequestBody Auth.SignIn request, BindingResult bindingResult){

        Member member = memberService.login(request);

        String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
        log.info("user login -> " + request.getUsername());
        return token;
    }
}
