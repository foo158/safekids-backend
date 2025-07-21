package com.safekids.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 부모(보호자)와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private User parent;

    // 자녀 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> children = new ArrayList<>();

    // 편의 메서드
    public void setParent(User parent) {
        this.parent = parent;
    }

    public void addChild(User child) {
        children.add(child);
        child.setParent(this);
    }


    public enum Role {
        ADMIN, GUARDIAN, CHILD, ALLOWED_USER
    }



}
