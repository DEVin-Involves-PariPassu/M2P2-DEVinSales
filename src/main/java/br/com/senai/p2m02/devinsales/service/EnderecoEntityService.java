package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoEntityService {

    @Autowired
    private EnderecoEntityRepository enderecoRepository;

    @Autowired
    private CidadeEntityRepository cidadeRepository;

    @Autowired
    private EstadoEntityRepository estadoRepository;

    @Transactional
    public List<EnderecoEntity> listar(Long idCidade, Long idEstado, String rua, Integer numero, String complemento) {
        if(idCidade != null){
            cidadeRepository.findById(idCidade).orElseThrow(() ->
                    new EntityNotFoundException("Cidade não encontrada!"));
        }
        if(idEstado != null){
            estadoRepository.findById(idEstado).orElseThrow(() ->
                    new EntityNotFoundException("Estado não encontrado!"));
        }
        return enderecoRepository.findAll(
                Specification.where(
                        SpecificationsEnderecoEntity.idCidade(idCidade).and(
                                SpecificationsEnderecoEntity.idEstado(idEstado).and(
                                        SpecificationsEnderecoEntity.rua(rua).and(
                                                SpecificationsEnderecoEntity.numero(numero).and(
                                                        SpecificationsEnderecoEntity.complemento(complemento)
                                                )
                                        )
                                )
                        )
                )
        );
    }


}


