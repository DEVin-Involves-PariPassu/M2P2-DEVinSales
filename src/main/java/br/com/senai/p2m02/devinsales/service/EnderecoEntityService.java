package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.CidadeEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.EstadoEntity;
import br.com.senai.p2m02.devinsales.repository.CidadeEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.EstadoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.SpecificationsEnderecoEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


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
        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(() ->
                new EntityNotFoundException("Cidade não encontrada!"));

        estadoRepository.findById(idEstado).orElseThrow(() ->
                    new EntityNotFoundException("Estado não encontrado!"));

         if (!idEstado.equals(cidadeEntity.getEstado().getId())){
            throw new IllegalArgumentException("O ID do estado não coincide com o ID especificado!");
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

    @Transactional
    public EnderecoEntity getById(Long idCidade, Long idEstado, Long id) {
        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(() ->
                new EntityNotFoundException("Cidade não encontrada!"));

        EstadoEntity estadoEntity = estadoRepository.findById(idEstado).orElseThrow(() ->
                    new EntityNotFoundException("Estado não encontrado!"));

        EnderecoEntity enderecoEntity = enderecoRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Endereço não encontrado!"));

        if (!idEstado.equals(cidadeEntity.getEstado().getId())){
            throw new IllegalArgumentException("O ID do estado não coincide com o ID especificado!");
        }

        if(!idCidade.equals(enderecoEntity.getCidade().getId())){
            throw new IllegalArgumentException("O ID da cidade não coincide com o ID especificado!");
        }

        return enderecoEntity;
    }


    @Transactional
    public void deletar(Long idEstado, Long idCidade, Long idEndereco){

        EstadoEntity estadoEntity = estadoRepository.findById(idEstado).orElseThrow(() -> new EntityNotFoundException("Não existe Estado com o id: " + idEstado));
        CidadeEntity cidadeEntity = cidadeRepository.findById(idCidade).orElseThrow(() -> new EntityNotFoundException("Não existe Cidade com o id: " + idCidade));
        EnderecoEntity enderecoEntity = enderecoRepository.findById(idEndereco).orElseThrow(() -> new EntityNotFoundException("Não existe Endereço com o id: " + idEndereco));
        if(!idEstado.equals(cidadeEntity.getEstado().getId())){
            throw new IllegalArgumentException("O ID do estado não coincide com o ID especificado!");
        }
        if(!idCidade.equals(enderecoEntity.getCidade().getId())){
            throw new IllegalArgumentException("O ID da cidade não coincide com o ID especificado!");
        }

        enderecoRepository.delete(enderecoEntity);
    }
}


