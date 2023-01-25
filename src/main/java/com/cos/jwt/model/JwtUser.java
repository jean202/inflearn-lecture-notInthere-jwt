package com.cos.jwt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class JwtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO?
    private long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN등 콤마를 사용해 롤 들을 만들 것이다D

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>(); // null 값이 발생하지 않도록 처리
    }
}
