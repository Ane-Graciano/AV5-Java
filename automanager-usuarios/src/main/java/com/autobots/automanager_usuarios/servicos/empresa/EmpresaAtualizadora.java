package com.autobots.automanager_usuarios.servicos.empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager_usuarios.modelo.Empresa;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.modelo.dto.EmpresaRequestDto;
import com.autobots.automanager_usuarios.repositorios.EmpresaRepositorio;
import com.autobots.automanager_usuarios.servicos.VerificadorNulo; 

@Service
public class EmpresaAtualizadora {

    @Autowired
    private EmpresaRepositorio repositorio;

    private VerificadorNulo verificador = new VerificadorNulo();

    public Empresa atualizar(Long id, EmpresaRequestDto empresaDto) {
        Empresa empresa = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada para o ID: " + id));

        if (!verificador.verificar(empresaDto.getRazaoSocial())) {
            empresa.setRazaoSocial(empresaDto.getRazaoSocial());
        }
        if (!verificador.verificar(empresaDto.getNomeFantasia())) {
            empresa.setNomeFantasia(empresaDto.getNomeFantasia());
        }
        if (empresaDto.getEndereco() != null) {
            if (empresa.getEndereco() != null) {
                atualizarEndereco(empresa.getEndereco(), empresaDto.getEndereco());
            } else {
                empresa.setEndereco(empresaDto.getEndereco());
            }
        }
        if (empresaDto.getTelefones() != null && !empresaDto.getTelefones().isEmpty()) {
            empresa.getTelefones().clear();
            empresa.getTelefones().addAll(empresaDto.getTelefones());
        }

        return repositorio.save(empresa);
    }

    private void atualizarEndereco(Endereco atual, Endereco novo) {
        if (!verificador.verificar(novo.getRua())) {
            atual.setRua(novo.getRua());
        }
        if (!verificador.verificar(novo.getBairro())) {
            atual.setBairro(novo.getBairro());
        }
        if (!verificador.verificar(novo.getCidade())) {
            atual.setCidade(novo.getCidade());
        }
        if (!verificador.verificar(novo.getEstado())) {
            atual.setEstado(novo.getEstado());
        }
        if (!verificador.verificar(novo.getCodigoPostal())) {
            atual.setCodigoPostal(novo.getCodigoPostal());
        }
        if (!verificador.verificar(novo.getNumero())) {
            atual.setNumero(novo.getNumero());
        }
        if (!verificador.verificar(novo.getInformacoesAdicionais())) {
            atual.setInformacoesAdicionais(novo.getInformacoesAdicionais());
        }
    }
}
