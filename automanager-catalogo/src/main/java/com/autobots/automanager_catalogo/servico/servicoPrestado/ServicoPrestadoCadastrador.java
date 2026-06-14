package com.autobots.automanager_catalogo.servico.servicoPrestado;

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

import com.autobots.automanager_catalogo.modelo.ServicoPrestado;
import com.autobots.automanager_catalogo.repositorio.ServicoPrestadoRepositorio;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ServicoPrestadoCadastrador {

    @Autowired
    private ServicoPrestadoRepositorio repositorio;

    @Autowired
    private RestTemplate restTemplate;

    private final String URL_USUARIOS = "http://localhost:8081/empresas/";

    public ServicoPrestado cadastrar(ServicoPrestado servicoPrestado) {
        if (servicoPrestado.getIdEmpresa() != null) {
            verificarSeEmpresaExiste(servicoPrestado.getIdEmpresa());
        }

        if (servicoPrestado.getId() != null && repositorio.existsById(servicoPrestado.getId())) {
            throw new RuntimeException("ID do evento já existe na base de dados");
        }
        return repositorio.save(servicoPrestado);
    }

    public List<ServicoPrestado> cadastrarVarios(List<ServicoPrestado> servicosPrestados) {
        for (ServicoPrestado servicoPrestado : servicosPrestados) {
            if (servicoPrestado.getIdEmpresa() != null) {
                verificarSeEmpresaExiste(servicoPrestado.getIdEmpresa());
            }
        }
        return repositorio.saveAll(servicosPrestados);
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