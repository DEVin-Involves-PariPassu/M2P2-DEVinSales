package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.CidadeDTO;
import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.*;
import br.com.senai.p2m02.devinsales.service.exception.EntityIsReferencedException;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadeEntityService {
    @Autowired
    private CidadeEntityRepository cidadeRepository;

    @Autowired
    private EstadoEntityRepository estadoRepository;

    @Autowired
    private EnderecoEntityRepository enderecoRepository;

    @Transactional
    public List<CidadeEntity> listar(String nome, Long idEstado) {
        EstadoEntity estadoEntity = estadoRepository.findById(idEstado).orElseThrow(() ->
                new EntityNotFoundException("Estado não encontrado!"));
        return cidadeRepository.findAll(Specification.where(
                SpecificationsCidadeEntity.nome(nome).and(SpecificationsCidadeEntity.idEstado(idEstado))));

    }

    @Transactional
    public CidadeEntity listarPorId(Long idCidade, Long idEstado) {

        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(() ->
                new EntityNotFoundException("Cidade não encontrada!"));

        EstadoEntity estadoEntity = estadoRepository.findById(idEstado).orElseThrow(() ->
                new EntityNotFoundException("Estado não encontrado!"));

        if (!idEstado.equals(cidadeEntity.getEstado().getId())){
            throw new IllegalArgumentException("O ID do estado não coincide com o ID especificado!");
        }

        return cidadeEntity;

    }

    @Transactional
    public Long salvar(CidadeDTO cidadeDTO, Long idEstado){
        EstadoEntity estado = estadoRepository.findById(idEstado).orElseThrow(
                () -> new EntityNotFoundException("Não existe estado com o id " + idEstado)
        );
        CidadeEntity cidade = validateAndConvertDto(cidadeDTO);
        cidade.setEstado(estado);
        cidade = cidadeRepository.save(cidade);
        return cidade.getId();
    }

    @Transactional
    public void deletar(Long idCidade, Long idEstado) {
        estadoRepository.findById(idEstado).orElseThrow(
                () -> new EntityNotFoundException("Não existe estado com o id " + idEstado)
        );

        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(
                () -> new EntityNotFoundException("Não existe cidade com o id " + idCidade)
        );

        if (!idEstado.equals(cidadeEntity.getEstado().getId())) {
            throw new IllegalArgumentException("O id do Estado passado não é o id do Estado dessa cidade");
        }
        List<EnderecoEntity> enderecos = enderecoRepository.findAll(Specification.where(SpecificationsEnderecoEntity.idCidade(idCidade)));
        if (!enderecos.isEmpty()) {
            throw new EntityIsReferencedException("A cidade com id fornecido é referenciado por algum(ns) endereço(s)");
        }
        cidadeRepository.delete(cidadeEntity);
    }

    private CidadeEntity validateAndConvertDto(CidadeDTO cidadeDTO){
        existsNome(cidadeDTO);
        isUniqueNome(cidadeDTO);
        CidadeEntity cidade = new CidadeEntity();
        cidade.setNome(cidadeDTO.getNome());
        return cidade;
    }

    private void isUniqueNome(CidadeDTO cidadeDTO) {
        Optional<CidadeEntity> optionalCidade = cidadeRepository.findFirstByNome(cidadeDTO.getNome());
        if(optionalCidade.isPresent()){
            throw new EntityExistsException("Já existe um estado com o nome " + cidadeDTO.getNome());
        }
    }

    private void existsNome(CidadeDTO cidadeDTO) {
        if(cidadeDTO.getNome() == null){
            throw new RequiredFieldMissingException("O campo nome é obrigatório.");
        }
    }
}
