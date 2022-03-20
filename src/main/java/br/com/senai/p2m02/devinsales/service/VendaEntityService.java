package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.ItemVendaDTO;
import br.com.senai.p2m02.devinsales.dto.VendaDTO;
import br.com.senai.p2m02.devinsales.model.*;
import br.com.senai.p2m02.devinsales.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class VendaEntityService {

    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private ItemVendaEntityService itemVendaEntityService;

    @Transactional
    public List<VendaEntity> listarVendas(Long idVendedor) {
        userEntityRepository.findById(idVendedor).orElseThrow(() -> new EntityNotFoundException("Não existe vendedor com ID " + idVendedor));
        return vendaEntityRepository.findAll(Specification.where(
                SpecificationsVendaEntity.idVendedor(idVendedor)
        ));
    }

    @Transactional
    public VendaDTO listarPorId(Long id) {
        VendaEntity venda = getVenda(id);
        VendaDTO vendaDTO = converterVendaDTO(venda);
        return vendaDTO;

    }

    private VendaDTO converterVendaDTO(VendaEntity vendaEntity) {
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(vendaEntity.getId());
        vendaDTO.setNomeComprador(vendaEntity.getComprador().getNome());
        if (vendaEntity.getVendedor() != null) {
            vendaDTO.setNomeVendedor(vendaEntity.getVendedor().getNome());
        } else {
            vendaDTO.setNomeVendedor(null);
        }
        vendaDTO.setDataVenda(vendaEntity.getDataVenda());
        List<ItemVendaDTO> lista = itemVendaEntityService.listarItens(vendaEntity);
        vendaDTO.setTotalVenda(getTotalItens(lista));
        vendaDTO.setListaItens(lista);

        return vendaDTO;
    }

    public VendaEntity getVenda(Long idVenda) {
        Optional<VendaEntity> user = Optional.ofNullable(vendaEntityRepository.findById(idVenda).orElseThrow(
                () -> new EntityNotFoundException("Não existe venda " + idVenda)));

        VendaEntity vendaEntity = user.get();
        return vendaEntity;
    }

    private BigDecimal getTotalItens(List<ItemVendaDTO> listItemDTO) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVendaDTO item : listItemDTO
        ) {
            BigDecimal totalItens = item.getTotalItensVenda();
            total = totalItens.add(total);
        }
        return total;
    }

    @Transactional
    public List<VendaEntity> listarComprador(Long idComprador) {
        userEntityRepository.findById(idComprador).orElseThrow(() -> new EntityNotFoundException("Não existe comprador com id" + idComprador));
        return vendaEntityRepository.findAll(Specification.where(
                SpecificationsVendaEntity.idComprador(idComprador)
        ));
    }

    @Transactional
    public UserEntity getUser(Long idUser) {
        UserEntity user = userEntityRepository.findById(idUser).orElseThrow(
                () -> new EntityNotFoundException("Não existe usuario com id " + idUser)
        );
        return user;
    }


    @Transactional
    public Long salvarBuy(Long idUser, VendaEntity vendaEntity) {
        VendaEntity venda = validarVendaBuy(idUser, vendaEntity);
        vendaEntityRepository.save(venda);
        return venda.getId();
    }

    private VendaEntity validarVendaBuy(Long idUser, VendaEntity vendaEntity) {
        existsDate(vendaEntity);
        UserEntity comprador = changeCompradorId(idUser);
        validateUsers(idUser, vendaEntity);

        VendaEntity venda = new VendaEntity();
        venda.setDataVenda(vendaEntity.getDataVenda());
        venda.setVendedor(vendaEntity.getVendedor());
        venda.setComprador(comprador);
        return venda;
    }

    private VendaEntity existsDate(VendaEntity vendaEntity) {
        if (vendaEntity.getDataVenda() == null) {
            LocalDateTime agora = LocalDateTime.now();
            vendaEntity.setDataVenda(agora);
        }
        VendaEntity venda = new VendaEntity();
        venda.setDataVenda(vendaEntity.getDataVenda());
        return venda;
    }

    private UserEntity changeCompradorId(Long idUser) {
        UserEntity comprador = this.getUser(idUser);
        return comprador;
    }

    private ResponseEntity<Void> validateUsers(Long idUser, VendaEntity vendaEntity) {
        if (this.getUser(idUser) == null) {
            System.out.println("ID Null");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (vendaEntity.getVendedor() != null) {
            if (this.getUser(vendaEntity.getVendedor().getId()) == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return null;
    }


    //****EDVAN***
    @Transactional
    public Long salvarSale(Long idUser, VendaEntity vendaEntity) {
        VendaEntity venda = validarVendaSale(idUser, vendaEntity);
        vendaEntityRepository.save(venda);
        return venda.getId();
    }

    private VendaEntity validarVendaSale(Long idUser, VendaEntity vendaEntity) {
        existsDate(vendaEntity);
        UserEntity vendedor = changeVendedorId(idUser);
        validateBuyer(idUser, vendaEntity);

        VendaEntity venda = new VendaEntity();
        venda.setDataVenda(vendaEntity.getDataVenda());
        venda.setComprador(vendaEntity.getComprador());
        venda.setVendedor(vendedor);
        return venda;
    }

    private UserEntity changeVendedorId(Long idUser) {
        UserEntity vendedor = this.getUser(idUser);
        return vendedor;
    }

    private ResponseEntity<Void> validateBuyer(Long idUser, VendaEntity vendaEntity) {
        if (this.getUser(idUser) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (this.getUser(vendaEntity.getComprador().getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return null;
    }
}
