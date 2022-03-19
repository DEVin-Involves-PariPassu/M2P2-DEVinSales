package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.DeliveryDTO;
import br.com.senai.p2m02.devinsales.model.DeliveryEntity;
import br.com.senai.p2m02.devinsales.model.EnderecoEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.DeliveryRepository;
import br.com.senai.p2m02.devinsales.repository.EnderecoEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DeliveryEntityService {

    @Autowired
    private DeliveryRepository repository;

    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    @Autowired
    private EnderecoEntityRepository enderecoEntityRepository;

    @Transactional
    public List<DeliveryDTO> listar(Long idEndereço, Long idVenda) {
        if (idEndereço == null && idVenda == null) {
            List<DeliveryEntity> list = (List<DeliveryEntity>) repository.findAll();
            List<DeliveryDTO> listDTO = convertDeliveryDTO(list);
            if (list.isEmpty()) {
                throw new NoSuchElementException("Não existem entregas");
            }
            return listDTO;
        }
        List<DeliveryDTO> listDTO = listarEntregas(idEndereço, idVenda);
        return listDTO;
    }

    private List<DeliveryDTO> convertDeliveryDTO(List<DeliveryEntity> list){
        List<DeliveryDTO> listDTO = new ArrayList<>();
        for (DeliveryEntity delivery : list
        ) {
            DeliveryDTO deliveryDTO = new DeliveryDTO();
            deliveryDTO.setId(delivery.getId());
            deliveryDTO.setEndereco(delivery.getEndereco());
            deliveryDTO.setVenda(delivery.getVenda().getId());
            deliveryDTO.setPrevisaoEntrega(delivery.getPrevisaoEntrega());
            listDTO.add(deliveryDTO);
        }
        return listDTO;
    }

    @Transactional
    private List<DeliveryDTO> listarEntregas(Long endereco, Long venda) {

        if (endereco == null && venda != null) {
            Optional<VendaEntity> listVenda = Optional.ofNullable(vendaEntityRepository.findById(venda).orElseThrow(
                    () -> new EntityNotFoundException("Venda não encontrada")
            ));
            VendaEntity vendaEntity = listVenda.get();
            List<DeliveryDTO> deliveryDTO = convertDeliveryDTO(repository.findDeliveryEntityByVenda(vendaEntity));
            return deliveryDTO;

        } else if (endereco != null && venda == null) {
            Optional<EnderecoEntity> listEndereco = Optional.ofNullable(enderecoEntityRepository.findById(endereco).orElseThrow(
                    () -> new EntityNotFoundException("Endereço não encontrado")));
            EnderecoEntity enderecoEntity = listEndereco.get();
            List<DeliveryDTO> deliveryDTO = convertDeliveryDTO(repository.findDeliveryEntityByEndereco(enderecoEntity));
            return deliveryDTO;

        }
        Optional<VendaEntity> listVenda = Optional.ofNullable(vendaEntityRepository.findById(venda).orElseThrow(
                () -> new EntityNotFoundException("Venda não encontrada")));
        VendaEntity vendaEntity = listVenda.get();

        Optional<EnderecoEntity> listEndereco = Optional.ofNullable(enderecoEntityRepository.findById(endereco).orElseThrow(
                () -> new EntityNotFoundException("Endereço não encontrado")));
        EnderecoEntity enderecoEntity = listEndereco.get();
        List<DeliveryDTO> deliveryDTO = convertDeliveryDTO(repository.findDeliveryEntityByEnderecoAndVenda(enderecoEntity, vendaEntity));

        return deliveryDTO;
    }

}

