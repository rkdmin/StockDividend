package com.zerobase.StockDividend.service;

import com.zerobase.StockDividend.entity.Member;
import com.zerobase.StockDividend.model.Auth;
import com.zerobase.StockDividend.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    public Member register(Auth.SignUp request){
        Optional<Member> optionalMember = memberRepository.findByUsername(request.getUsername());
        if(optionalMember.isPresent()){
            throw new RuntimeException("존재하는 아이디 입니다.");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Member member = memberRepository.save(Member.toEntity(request));

        return member;
    }

    public Member login(Auth.SignIn member){
        Optional<Member> optionalMember = memberRepository.findByUsername(member.getUsername());
        if(!optionalMember.isPresent()){
            throw new RuntimeException("회원 정보가 일치하지 않습니다.");
        }

        if(!passwordEncoder.matches(member.getPassword(), optionalMember.get().getPassword())){
            throw new RuntimeException("회원 정보가 일치하지 않습니다.");
        }

        return optionalMember.get();
    }

}
