package com.safekids.backend.service;

import com.safekids.backend.domain.User;
import com.safekids.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        // ✅ ROLE 접두어를 포함한 권한 부여 (Spring Security에서 필수)
        String roleName = "ROLE_" + user.getRole().name(); // 예: ROLE_GUARDIAN
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // ✅ Spring Security의 기본 User 객체에 사용자 정보와 권한 주입
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(authority)
        );
    }
}
