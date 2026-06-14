package com.autobots.automanager_usuarios.servicos.empresa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Empresa;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaRequestDto;
import com.autobots.automanager_usuarios.repositorios.EmpresaRepositorio;

@Service
public class EmpresaCadastradora {

    @Autowired
    private EmpresaRepositorio repositorio;

    public Empresa cadastrar(EmpresaRequestDto empresaDto) {
        Empresa empresa = new Empresa();
        dados(empresa, empresaDto);
        return repositorio.save(empresa);
    }

    public List<Empresa> cadastrarVarios(List<EmpresaRequestDto> empresasDtos) {
        List<Empresa> empresas = new ArrayList<>();
        for (EmpresaRequestDto empresaDto : empresasDtos) {
            Empresa empresa = new Empresa();
            dados(empresa, empresaDto);
            empresas.add(empresa);
        }
        return repositorio.saveAll(empresas);
    }

    private void dados(Empresa empresa, EmpresaRequestDto empresaDto) {
        empresa.setRazaoSocial(empresaDto.getRazaoSocial());
        empresa.setNomeFantasia(empresaDto.getNomeFantasia());
        empresa.setTelefones(empresaDto.getTelefones());
        empresa.setEndereco(empresaDto.getEndereco());
        if (empresa.getData_cadastro() == null) {
            empresa.setData_cadastro(new Date());
        }
    }
}