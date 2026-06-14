package com.autobots.automanager_catalogo.servico.pecaMercadoria;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.autobots.automanager_catalogo.modelo.Mercadoria;
import com.autobots.automanager_catalogo.repositorio.MercadoriaRepositorio;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PecaMercadoriaCadastrador {

    @Autowired
    private MercadoriaRepositorio repositorio;

    @Autowired
    private RestTemplate restTemplate;

    private final String URL_USUARIOS = "http://localhost:8081/empresas/";

    public Mercadoria cadastrar(Mercadoria mercadoria) {
        if (mercadoria.getIdEmpresa() != null) {
            verificarSeEmpresaExiste(mercadoria.getIdEmpresa());
        }

        if (mercadoria.getCadastro() == null) {
            mercadoria.setCadastro(new java.util.Date());
        }
        
        if (mercadoria.getId() != null && repositorio.existsById(mercadoria.getId())) {
            throw new RuntimeException("ID da mercadoria já existe na base de dados");
        }
        return repositorio.save(mercadoria);
    }

    public List<Mercadoria> cadastrarVarios(List<Mercadoria> mercadorias) {
        for (Mercadoria mercadoria : mercadorias) {
            if (mercadoria.getIdEmpresa() != null) {
                verificarSeEmpresaExiste(mercadoria.getIdEmpresa());
            }

            if (mercadoria.getCadastro() == null) {
                mercadoria.setCadastro(new java.util.Date());
            }
        }
        return repositorio.saveAll(mercadorias);
    }

    private void verificarSeEmpresaExiste(Long idEmpresa) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpHeaders headers = new HttpHeaders();
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String tokenOriginal = request.getHeader("Authorization");
                
                if (tokenOriginal != null) {
                    headers.set("Authorization", tokenOriginal);
                }
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(URL_USUARIOS + idEmpresa, HttpMethod.GET, entity, Object.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Não foi possível cadastrar: A empresa com ID " + idEmpresa + " não existe no sistema de usuários.");
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("Erro de autorização: O token enviado não tem permissão para validar empresas.");
            }
            throw new RuntimeException("Erro ao comunicar com o serviço de usuários: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("O serviço de usuários está fora do ar ou indisponível.");
        }
    }
}