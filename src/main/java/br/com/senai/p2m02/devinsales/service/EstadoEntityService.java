package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.EstadoDTO;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.model.SiglaEstado;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEstadoEntity;
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
public class EstadoEntityService {
    @Autowired
    private EstadoEntityRepository repository;

    @Transactional
    public List<EstadoEntity> listar(String nome){
//        if(nome==null){
//            return (List<EstadoEntity>) repository.findAll();
//        }
        return repository.findAll(Specification.where(
                SpecificationsEstadoEntity.nome(nome)
        ));
    }

    @Transactional
    public EstadoEntity listarPorId(Long id){
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe estado com o id " + id)
        );
    }

    public Long salvar(EstadoDTO estadoDTO){
        EstadoEntity estado = validateAndConvertDto(estadoDTO);
        estado = repository.save(estado);
        return estado.getId();
    }

    private EstadoEntity validateAndConvertDto(EstadoDTO estadoDTO){
        existsNome(estadoDTO);
        existsSigla(estadoDTO);
        isUniqueNome(estadoDTO);
        isUniqueSigla(estadoDTO);
        EstadoEntity estado = new EstadoEntity();
        estado.setNome(estadoDTO.getNome());
        estado.setSigla(SiglaEstado.valueOf(estadoDTO.getSigla()));
        return estado;
    }

    private void existsNome(EstadoDTO estadoDTO){
        if(estadoDTO.getNome() == null){
            throw new RequiredFieldMissingException("O campo nome é obrigatório.");
        }
    }

    private void existsSigla(EstadoDTO estadoDTO){
        if(estadoDTO.getSigla() == null){
            throw new RequiredFieldMissingException("O campo sigla é obrigatório.");
        }
    }

    private void isUniqueSigla(EstadoDTO estadoDTO) {
        Optional<EstadoEntity> optionalEstado = repository.findFirstBySigla(SiglaEstado.valueOf(estadoDTO.getSigla()));
        if(optionalEstado.isPresent()){
            throw new EntityExistsException("Já existe um estado com a sigla " + estadoDTO.getSigla());
        }
    }

    private void isUniqueNome(EstadoDTO estadoDTO) {
        Optional<EstadoEntity> optionalEstado = repository.findFirstByNome(estadoDTO.getNome());
        if(optionalEstado.isPresent()){
            throw new EntityExistsException("Já existe um estado com o nome " + estadoDTO.getNome());
        }
    }
}
