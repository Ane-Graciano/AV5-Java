package com.autobots.automanager_usuarios.modelo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Usuario extends RepresentationModel<Usuario> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String nomeSocial;

    @Column(nullable = false)
    private Long idEmpresa; 

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<PerfilUsuario> perfis = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Telefone> telefones = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Documento> documentos = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Email> emails = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Credencial> credenciais = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.name()))
                .toList();
    }


    @Override
    public String getPassword() {
        for (Object cred : this.credenciais) {
            if (cred != null) {
                try {
                    java.lang.reflect.Method m = cred.getClass().getMethod("getSenha");
                    return (String) m.invoke(cred);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    @Override
    public String getUsername() {
        for (Object cred : this.credenciais) {
            if (cred != null) {
                try {
                    java.lang.reflect.Method m = cred.getClass().getMethod("getNomeUsuario");
                    return (String) m.invoke(cred);
                } catch (Exception e) {
                    try {
                        java.lang.reflect.Method m = cred.getClass().getMethod("getLogin");
                        return (String) m.invoke(cred);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return null;
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