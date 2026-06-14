package com.autobots.automanager_usuarios.servicos.endereco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.modelo.dto.EnderecoDto;
import com.autobots.automanager_usuarios.repositorios.EnderecoRepositorio;
import com.autobots.automanager_usuarios.servicos.VerificadorNulo;

@Service
public class EnderecoAtualizador {

    @Autowired
    private EnderecoRepositorio repositorio;

    private VerificadorNulo verificador = new VerificadorNulo();

    private Endereco converterEnderecoDTO(EnderecoDto enderecoDto) {
        Endereco endereco = new Endereco();
        endereco.setId(enderecoDto.getId());
        endereco.setEstado(enderecoDto.getEstado());
        endereco.setCidade(enderecoDto.getCidade());
        endereco.setBairro(enderecoDto.getBairro());
        endereco.setRua(enderecoDto.getRua());
        endereco.setNumero(enderecoDto.getNumero());
        endereco.setCodigoPostal(enderecoDto.getCodigoPostal());
        endereco.setInformacoesAdicionais(enderecoDto.getInformacoesAdicionais());
        return endereco;
    }

    public void atualizar(Endereco endereco, Endereco atualizacao) {
        if (endereco != null && atualizacao != null) {
            if (!verificador.verificar(atualizacao.getEstado())) {
                endereco.setEstado(atualizacao.getEstado());
            }
            if (!verificador.verificar(atualizacao.getCidade())) {
                endereco.setCidade(atualizacao.getCidade());
            }
            if (!verificador.verificar(atualizacao.getBairro())) {
                endereco.setBairro(atualizacao.getBairro());
            }
            if (!verificador.verificar(atualizacao.getRua())) {
                endereco.setRua(atualizacao.getRua());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                endereco.setNumero(atualizacao.getNumero());
            }
            if (!verificador.verificar(atualizacao.getCodigoPostal())) {
                endereco.setCodigoPostal(atualizacao.getCodigoPostal());
            }
            if (!verificador.verificar(atualizacao.getInformacoesAdicionais())) {
                endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
            }
        }
    }

    public Endereco atualizarEnderecoId(Long id, EnderecoDto atualizacaoDto) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("O endereço de ID " + id + " não foi encontrado no sistema.");
        }
        Endereco endereco = repositorio.findById(id).orElseThrow(() -> new RuntimeException("Endereco não encontrado"));
        if (atualizacaoDto != null) {
            if (!verificador.verificar(atualizacaoDto.getEstado())) {
                endereco.setEstado(atualizacaoDto.getEstado());
            }
            if (!verificador.verificar(atualizacaoDto.getCidade())) {
                endereco.setCidade(atualizacaoDto.getCidade());
            }
            if (!verificador.verificar(atualizacaoDto.getBairro())) {
                endereco.setBairro(atualizacaoDto.getBairro());
            }
            if (!verificador.verificar(atualizacaoDto.getRua())) {
                endereco.setRua(atualizacaoDto.getRua());
            }
            if (!verificador.verificar(atualizacaoDto.getNumero())) {
                endereco.setNumero(atualizacaoDto.getNumero());
            }
            if(!verificador.verificar(atualizacaoDto.getCodigoPostal())){
                endereco.setCodigoPostal(atualizacaoDto.getCodigoPostal());
            }
            if (!verificador.verificar(atualizacaoDto.getInformacoesAdicionais())) {
                endereco.setInformacoesAdicionais(atualizacaoDto.getInformacoesAdicionais());
            }
            return repositorio.save(endereco);
        }
        return endereco;
    }
}