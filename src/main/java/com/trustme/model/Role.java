package com.trustme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class Role {
    @Id
    Long id;
    String[] roles;

    public Role(Long id, String[] roles) {
        this.id = id;
        this.roles = roles;
    }
}
