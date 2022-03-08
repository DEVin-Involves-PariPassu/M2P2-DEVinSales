package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsCidadeEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeEntityService {
    @Autowired
    private CidadeEntityRepository repository;

    @Autowired
    private EstadoEntityRepository estadoRepository;

    @Transactional
    public List<CidadeEntity> listar(String nome, Long idEstado){
        return repository.findAll(Specification.where(
                SpecificationsCidadeEntity.nome(nome).and(SpecificationsCidadeEntity.idEstado(idEstado))));
    }
    @Transactional
    public CidadeEntity listarPorId(Long id, UserEntity usuario){
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe cidade com o id " + id)
        );
    }
}
