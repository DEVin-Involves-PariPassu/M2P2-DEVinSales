package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.ItemVendaEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.ItemVendaEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (!itemVendaEntity.getIdVenda().equals(vendaEntity.getId())){
            throw new IllegalArgumentException
                    ("O item com o id " + idItem + " não é da venda com id " +idVenda);
        }
        if (quantity <= 0){
            throw new IllegalArgumentException
                    ("A quantidade do item com id " + idItem + " não é maior que zero ");
        }
        itemVendaEntity.setQuantidade(quantity);
        itemVendaEntityRepository.save(itemVendaEntity);
    }
}
