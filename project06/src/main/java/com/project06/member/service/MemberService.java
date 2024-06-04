package com.project06.member.service;

import com.project06.member.domain.Member;
import com.project06.member.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService{

    @Autowired
    private MemberRepository MemberRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member addUser(Member user) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(user.getUserpw());
        user.setUserpw(encodedPassword);
        
        // 인코딩된 비밀번호를 사용하여 사용자 저장
        return MemberRepository.save(user);
    }

    public Optional<Member> findUserById(String userid) {
        return MemberRepository.findById(userid);
    }

    public boolean updateUser(Member user) {
        // Assuming 'user' contains the 'userid' to identify the record to be updated
        if (!MemberRepository.existsById(user.getUserid())) {
            return false; // Return false if user does not exist
        }
        MemberRepository.save(user); // Save method updates the user if it exists
        return true;
    }

    public boolean deleteUser(String userId) {
        if (!MemberRepository.existsById(userId)) {
            return false; // Return false if user does not exist
        }
        MemberRepository.deleteById(userId);
        return true;
    }

    public boolean validateUserLogin(String userid, String userpw) {
        Optional<Member> user = MemberRepository.findByUserid(userid);
        if (user.isPresent() && passwordEncoder.matches(userpw, user.get().getUserpw())) {
            return true;
        }
        return false;
    }
}
