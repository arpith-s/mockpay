package com.arpith.mockpay.identityservice.model;

import com.arpith.mockpay.identityservice.constant.Constant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecuredUser implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String authorities;

    @Column(nullable = false)
    private Boolean isAccountNonExpired;

    @Column(nullable = false)
    private Boolean isAccountNonLocked;

    @Column(nullable = false)
    private Boolean isCredentialsNonExpired;

    @Column(nullable = false)
    private Boolean isEnabled;

    @CreationTimestamp
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;

    @OneToOne(mappedBy = "securedUser")
    private AppUser appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.authorities == null || this.authorities.isBlank()) return Collections.emptyList();
        return Stream.of(this.authorities.split(Constant.DELIMITER_COMMA))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public String getAuthoritiesAsString() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}

