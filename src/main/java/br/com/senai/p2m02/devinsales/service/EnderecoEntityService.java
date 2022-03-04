package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoEntityService {

    @Autowired
    private EnderecoEntityRepository repository;
}
