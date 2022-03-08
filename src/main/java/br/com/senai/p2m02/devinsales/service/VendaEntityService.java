package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class VendaEntityService {

    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    @Transactional
    public VendaEntity listarPorId(Long id) {
        return vendaEntityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe venda " + id)
        );
    }
}
