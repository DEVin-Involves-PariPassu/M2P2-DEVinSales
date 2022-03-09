package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.*;
import br.com.senai.p2m02.devinsales.service.exception.EntityIsReferencedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeEntityService {
    @Autowired
    private CidadeEntityRepository cidadeRepository;

    @Autowired
    private EstadoEntityRepository estadoRepository;

    @Autowired
    private EnderecoEntityRepository enderecoRepository;

    @Transactional
    public List<CidadeEntity> listar(String nome, Long idEstado){
        return cidadeRepository.findAll(Specification.where(
                SpecificationsCidadeEntity.nome(nome).and(SpecificationsCidadeEntity.idEstado(idEstado))));
    }
    @Transactional
    public CidadeEntity listarPorId(Long id, UserEntity usuario){
        return cidadeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe cidade com o id " + id)
        );
    }

    @Transactional
    public void deletar(Long idCidade, Long idEstado){
        estadoRepository.findById(idEstado).orElseThrow(
                () -> new EntityNotFoundException("Não existe estado com o id " + idEstado)
        );

        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(
                () -> new EntityNotFoundException("Não existe cidade com o id " + idCidade)
        );

        if (!idEstado.equals(cidadeEntity.getEstado().getId())){
            throw new IllegalArgumentException("O id do Estado passado não é o id do Estado dessa cidade");
        }
        List<EnderecoEntity> enderecos = enderecoRepository.findAll(Specification.where(SpecificationsEnderecoEntity.idCidade(idCidade)));
        if (!enderecos.isEmpty()){
            throw new EntityIsReferencedException("A cidade com id fornecido é referenciado por algum(ns) endereço(s)");
        }
        cidadeRepository.delete(cidadeEntity);
    }
}
