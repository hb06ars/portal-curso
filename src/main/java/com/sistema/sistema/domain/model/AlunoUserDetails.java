package com.sistema.sistema.domain.model;

import com.sistema.sistema.domain.entity.AlunoEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AlunoUserDetails implements UserDetails {

    private final AlunoEntity aluno;

    public AlunoUserDetails(AlunoEntity aluno) {
        this.aluno = aluno;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_".concat(aluno.getRole())));
    }

    @Override
    public String getPassword() {
        return aluno.getPassword();
    }

    @Override
    public String getUsername() {
        return aluno.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
