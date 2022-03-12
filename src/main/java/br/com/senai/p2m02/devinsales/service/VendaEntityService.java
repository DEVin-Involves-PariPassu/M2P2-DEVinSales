package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.VendaEntity;
import br.com.senai.p2m02.devinsales.repository.SpecificationsVendaEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.repository.VendaEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class VendaEntityService {

    @Autowired
    private VendaEntityRepository vendaEntityRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Transactional
    public List<VendaEntity> listarVendas(Long idVendedor){
        userEntityRepository.findById(idVendedor).orElseThrow(()->new EntityNotFoundException("Não existe vendedor com ID " +idVendedor));
        return vendaEntityRepository.findAll(Specification.where(
                SpecificationsVendaEntity.idVendedor(idVendedor)
        ));
    }

    @Transactional
    public VendaEntity listarPorId(Long id) {
        return vendaEntityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Não existe venda " + id)
        );
    }

    @Transactional
    public boolean hasUserId(Long id_user){
        Optional<UserEntity> user = userEntityRepository.findById(id_user);
        if (user.isPresent()){
            return true;
        }
        return false;
    }

    @Transactional
    public UserEntity getUser(Long id_user){
        Optional<UserEntity> user = userEntityRepository.findById(id_user);
        UserEntity userEntity = user.get();
        return userEntity;
    }

    @Transactional
    public Long salvar(Long idUser, VendaEntity vendaEntity){
        VendaEntity venda = validarVenda(idUser, vendaEntity);
        vendaEntityRepository.save(venda);
        return venda.getId();
    }

    private VendaEntity validarVenda(Long idUser, VendaEntity vendaEntity){
        existsDate(vendaEntity);
        UserEntity comprador = changeCompradorId(idUser);
        validateUsers(idUser, vendaEntity);
        VendaEntity venda = new VendaEntity();
        venda.setDataVenda(vendaEntity.getDataVenda());
        venda.setVendedor(vendaEntity.getVendedor());
        venda.setComprador(comprador);
        return venda;
    }

    private VendaEntity existsDate(VendaEntity vendaEntity){
        if(vendaEntity.getDataVenda() == null){
            LocalDate agora = LocalDate.now();
            vendaEntity.setDataVenda(agora);
        }
        VendaEntity venda = new VendaEntity();
        venda.setDataVenda(vendaEntity.getDataVenda());
        return venda;
    }

    private ResponseEntity<Void> validateUsers(Long idUser, VendaEntity vendaEntity){
        if(!this.hasUserId(idUser)){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }else if(vendaEntity.getVendedor() != null){
            if(!this.hasUserId(vendaEntity.getVendedor().getId())){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }

        return null;
    }

    private UserEntity changeCompradorId(Long idUser){
        UserEntity comprador = this.getUser(idUser);
        return comprador;
    }


}
