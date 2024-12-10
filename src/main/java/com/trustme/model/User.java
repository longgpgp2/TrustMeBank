package com.trustme.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    Long id;
    String username;
    String password;
    @ManyToOne
    Role role;
    Date createdAt;
    Date updatedAt;
    private User(String username, String password, Role role, Date createdAt, Date updatedAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt != null ? createdAt : new Date();
        this.updatedAt = updatedAt;
    }

    public static class UserBuilder {
        private String username;
        private String password;
        private Role role;
        private Date createdAt;
        private Date updatedAt;

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setRole(Role role) {
            this.role = role;
            return this;
        }


        public UserBuilder setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public User build() {
            return new User(username, password, role, null, updatedAt);
        }
    }
}
