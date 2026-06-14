package com.autobots.automanager_usuarios.servicos.endereco;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autobots.automanager_usuarios.modelo.Endereco;
import com.autobots.automanager_usuarios.modelo.dto.EnderecoDto;
import com.autobots.automanager_usuarios.repositorios.EnderecoRepositorio;

@Service
public class EnderecoCadastrador {

    @Autowired
    private EnderecoRepositorio repositorio;

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

    private List<Endereco> converterListaEnderecoDTO(List<EnderecoDto> enderecosDto) {
        List<Endereco> entidades = new ArrayList<>();
        for (EnderecoDto dto : enderecosDto) {
            entidades.add(converterEnderecoDTO(dto));
        }
        return entidades;
    }

    public Endereco cadastrar(EnderecoDto enderecoDto) {
        Endereco endereco = converterEnderecoDTO(enderecoDto);
        if (endereco.getId() != null && repositorio.existsById(endereco.getId())) {
            throw new RuntimeException("ID do endereço já existe na base de dados");
        }
        return repositorio.save(endereco);
    }

    public List<Endereco> cadastrarTodos(List<EnderecoDto> enderecosDtos) {
        List<Endereco> enderecos = converterListaEnderecoDTO(enderecosDtos);
        return repositorio.saveAll(enderecos);
    }
}
