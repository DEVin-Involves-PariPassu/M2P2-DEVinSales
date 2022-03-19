package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.DeliveryRepository;
import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DeliveryEntityService {

    @Autowired
    private EnderecoEntityRepository enderecoEntityRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    private Long validateEndereco(DeliveryEntity delivery) {
        EnderecoEntity endereco = delivery.getEndereco();
        if (endereco != null) {
            return delivery.getEndereco().getId();
        }
        throw new IllegalArgumentException("Endereço não fornecido.");
    }

    public Long postEntrega(DeliveryEntity delivery, Long idVenda) {

        VendaEntity venda = vendaEntityRepository.findById(idVenda).orElseThrow(() ->
                new EntityNotFoundException("Venda não encontrada: " + idVenda));

        Long idEndereco = validateEndereco(delivery);

        enderecoEntityRepository.findById(idEndereco).orElseThrow(() ->
                new EntityNotFoundException("Endereço não encontrado: " + idEndereco));

        if (delivery.getPrevisaoEntrega() == null) {
            delivery.setPrevisaoEntrega(LocalDate.now().plusDays(7));
        }

        if (delivery.getPrevisaoEntrega().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data não pode ser inferior a data atual.");
        }

        delivery.setVenda(venda);
        delivery = deliveryRepository.save(delivery);
        return delivery.getId();
    }
}
