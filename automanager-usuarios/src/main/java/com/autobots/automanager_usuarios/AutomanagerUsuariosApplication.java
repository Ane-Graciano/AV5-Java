package com.autobots.automanager_usuarios;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager_usuarios.enumeracoes.PerfilUsuario;
import com.autobots.automanager_usuarios.modelo.CredencialUsuarioSenha;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.repositorios.UsuarioRepositorio;


@SpringBootApplication
public class AutomanagerUsuariosApplication implements CommandLineRunner{

	@Autowired
    private UsuarioRepositorio repositorio;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerUsuariosApplication.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        if (repositorio.count() == 0) {
            BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
            
            Usuario usuario = new Usuario();
            usuario.setNome("administrador");
			usuario.setIdEmpresa(1L);
            usuario.getPerfis().add(PerfilUsuario.ROLE_ADMIN);
            
            CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
            credencial.setNomeUsuario("admin");
            
            String senha = "123456";
            credencial.setSenha(codificador.encode(senha));
            credencial.setCriacao(new Date());
            credencial.setInativo(false);
            
            usuario.getCredenciais().add(credencial);
            
            repositorio.save(usuario);
            System.out.println("\n======> USUÁRIO ADMINISTRADOR INICIAL CRIADO: admin / 123456 <======\n");
        }
	}
}
