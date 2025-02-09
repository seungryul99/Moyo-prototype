package com.moyoprototype.user.domain;

import com.moyoprototype.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private String appId;

    @Builder
    public User(String name, String profileImgUrl, String appId){
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.appId = appId;
    }
}
