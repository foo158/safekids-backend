package com.safekids.backend.service;

import com.safekids.backend.domain.User;
import com.safekids.backend.dto.ChildRegisterRequestDto;
import com.safekids.backend.dto.LoginRequestDto;
import com.safekids.backend.dto.LoginResponseDto;
import com.safekids.backend.dto.UserSignupRequestDto;
import com.safekids.backend.repository.UserRepository;
import com.safekids.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long signup(UserSignupRequestDto requestDto) {
        // 중복 이메일 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 유저 생성 및 저장
        User user = requestDto.toEntity(encodedPassword);
        userRepository.save(user);

        return user.getId();
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.createToken(user);

        return new LoginResponseDto(token, user.getName(), user.getEmail(), user.getRole());
    }

    public LoginResponseDto getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new LoginResponseDto(user, jwtUtil.createToken(user));
    }
    @Transactional
    public void updateUserRole(String email, String newRole) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        try {
            User.Role role = User.Role.valueOf(newRole.toUpperCase()); // ADMIN, GUARDIAN, CHILD
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("올바르지 않은 역할(Role)입니다: " + newRole);
        }
    }

    @Transactional
    public Long registerChild(String parentEmail, ChildRegisterRequestDto requestDto) {
        // 보호자 조회
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("부모 사용자를 찾을 수 없습니다."));

        // 이메일 중복 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 자녀 User 생성
        User child = User.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .role(User.Role.CHILD)
                .parent(parent)  // 부모 설정
                .build();

        userRepository.save(child);
        return child.getId();
    }
    //자녀 목록 조회
    @Transactional(readOnly = true)
    public List<User> getChildren(String parentEmail) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("부모 사용자를 찾을 수 없습니다."));

        return parent.getChildren();
    }

    //자녀 삭제
    @Transactional
    public void deleteChild(String parentEmail, Long childId) {
        User parent = userRepository.findByEmail(parentEmail)
                .orElseThrow(() -> new IllegalArgumentException("부모 사용자를 찾을 수 없습니다."));

        User child = userRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("자녀를 찾을 수 없습니다."));

        // 부모와 일치하는 자녀인지 검증
        if (!child.getParent().equals(parent)) {
            throw new SecurityException("해당 자녀를 삭제할 권한이 없습니다.");
        }

        userRepository.delete(child);
    }


}
