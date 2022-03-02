package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEstadoEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoEntityService {
    @Autowired
    private EstadoEntityRepository repository;

    @Transactional
    public List<EstadoEntity> listar(String nome, UserEntity usuario){
        if(nome==null){
            return (List<EstadoEntity>) repository.findAll();
        }
        return repository.findAll(Specification.where(
                SpecificationsEstadoEntity.nome(nome)
        ));
    }

    @Transactional
    public EstadoEntity listarPorId(Long id, UserEntity usuario){
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe estado com o id " + id)
        );
    }
}
