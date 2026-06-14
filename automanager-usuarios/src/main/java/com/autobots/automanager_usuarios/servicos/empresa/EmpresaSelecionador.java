package com.autobots.automanager_usuarios.servicos.empresa;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Empresa;
import com.autobots.automanager_usuarios.modelo.Usuario;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaResponseDto;
import com.autobots.automanager_usuarios.modelo.dto.UsuarioResponseDto;
import com.autobots.automanager_usuarios.repositorios.EmpresaRepositorio;

@Service
public class EmpresaSelecionador {

    @Autowired
    private EmpresaRepositorio repositorio;

    public EmpresaResponseDto converterParaResponse(Empresa empresa) {
        EmpresaResponseDto empresaDto = new EmpresaResponseDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setRazaoSocial(empresa.getRazaoSocial());
        empresaDto.setNomeFantasia(empresa.getNomeFantasia());

        if (empresa.getData_cadastro() != null) {
            SimpleDateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
            empresaDto.setData_cadastro(dataFormatada.format(empresa.getData_cadastro()));
        }

        empresaDto.setTelefones(empresa.getTelefones());
        empresaDto.setEndereco(empresa.getEndereco());
        empresaDto.setUsuarios(converterUsuarios(empresa.getUsuarios()));

        return empresaDto;
    }

    public List<EmpresaResponseDto> converterListaParaResponse(List<Empresa> empresas) {
        return empresas.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public Empresa obtenerEmpresa(Long id) {
        return repositorio.findById(id).orElseThrow(()
                -> new RuntimeException("Empresa não encontrada"));
    }

    public List<Empresa> obterEmpresas() {
        return repositorio.findAll();
    }

    private Set<UsuarioResponseDto> converterUsuarios(Set<Usuario> usuarios) {
        return usuarios.stream().map(usuario -> {
            UsuarioResponseDto dto = new UsuarioResponseDto();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setPerfis(usuario.getPerfis());
            return dto;
        }).collect(Collectors.toSet());
    }
}