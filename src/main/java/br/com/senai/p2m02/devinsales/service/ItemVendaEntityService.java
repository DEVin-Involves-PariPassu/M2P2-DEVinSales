package br.com.senai.p2m02.devinsales.service;


import br.com.senai.p2m02.devinsales.dto.ItemVendaDTO;
import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemVendaEntityService {
    @Autowired
    private ItemVendaEntityRepository itemVendaEntityRepository;
    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    public void patchQuantity(Long idVenda, Long idItem, Integer quantity){

        ItemVendaEntity itemVendaEntity = itemVendaEntityRepository.findById(idItem).orElseThrow(
                () -> new EntityNotFoundException("Não existe item com id " + idItem)
        );

        VendaEntity vendaEntity = vendaEntityRepository.findById(idVenda).orElseThrow(
                () -> new EntityNotFoundException("Não existe venda com id " + idVenda)
        );

        if (!itemVendaEntity.getVenda().getId().equals(vendaEntity.getId())){
            throw new IllegalArgumentException
                    ("O item com o id " + idItem + " não é da venda com id " + idVenda);
        }
        if (quantity <= 0){
            throw new IllegalArgumentException
                    ("A quantidade do item com id " + idItem + " não é maior que zero ");
        }
        itemVendaEntity.setQuantidade(quantity);
        itemVendaEntityRepository.save(itemVendaEntity);
    }

    public ResponseEntity<Object> patchPrice(Long idVenda, Long idItem, BigDecimal price) {

        Optional<ItemVendaEntity> itemVendaToUpdateOpt = this.itemVendaEntityRepository.findById(idItem);
        Optional<VendaEntity> vendaOpt = this.vendaEntityRepository.findById(idVenda);

        if (vendaOpt.isEmpty() || itemVendaToUpdateOpt.isEmpty()) {
            return new ResponseEntity<>("Venda ou item não encontrado.", HttpStatus.NOT_FOUND);
        }

        ItemVendaEntity itemVendaToUpdate = itemVendaToUpdateOpt.get();
        VendaEntity venda = vendaOpt.get();
        if (!itemVendaToUpdate.getVenda().getId().equals(venda.getId())) {
            return new ResponseEntity<>("O item não pertence a venda informada.", HttpStatus.BAD_REQUEST);
        }

        if (price.intValue() <= 0) {
            return new ResponseEntity<>("Preço não pode ser menor ou igual a 0.", HttpStatus.BAD_REQUEST);
        }

        itemVendaToUpdate.setPrecoUnitario(price);
        itemVendaEntityRepository.save(itemVendaToUpdate);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    public List<ItemVendaDTO> listarItens (VendaEntity vendaEntity){
        List <ItemVendaEntity> listItens = itemVendaEntityRepository.findByVenda(vendaEntity);
        List<ItemVendaDTO> listItensDTO = convertoItemDTO(listItens);
        return listItensDTO;
    }

    private List<ItemVendaDTO> convertoItemDTO(List<ItemVendaEntity> itemVendaEntityList){
        List<ItemVendaDTO> listItensDTO = new ArrayList<>();
        for (ItemVendaEntity item : itemVendaEntityList
             ) {
            ItemVendaDTO itemDTO = new ItemVendaDTO();
            itemDTO.setId(item.getId());
            itemDTO.setNomeProduto(item.getProduto().getNome());
            itemDTO.setPrecoUnitario(item.getPrecoUnitario().intValue());
            itemDTO.setQuantidade(item.getQuantidade());
            Integer totalItensInt = itemDTO.getQuantidade() * itemDTO.getPrecoUnitario();
            BigDecimal totalItens = BigDecimal.valueOf(totalItensInt);
            itemDTO.setTotalItensVenda(totalItens);
            listItensDTO.add(itemDTO);
        }
        return listItensDTO;
    }

}
