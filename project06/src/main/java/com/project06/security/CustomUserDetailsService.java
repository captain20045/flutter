 package com.project06.security;

import com.project06.member.domain.Member;
import com.project06.member.repository.MemberRepository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserid(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        return User.builder()
                .username(member.getUserid())
                .password(member.getUserpw())
                // 여기에는 권한(authorities) 설정을 추가할 수 있습니다.
                .authorities(new ArrayList<>())
                .build();
    }
}