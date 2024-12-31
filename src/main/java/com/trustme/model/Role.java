package com.trustme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="roles")
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
