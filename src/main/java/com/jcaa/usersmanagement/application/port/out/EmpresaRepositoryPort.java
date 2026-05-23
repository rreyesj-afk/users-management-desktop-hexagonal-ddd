package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import java.util.List;
import java.util.Optional;


public interface EmpresaRepositoryPort {

    //guardar empresa
    EmpresaModel save(EmpresaModel empresa);
    //encontrar por id
    Optional<EmpresaModel> findById(EmpresaId empresaId);
    //mostrar todas las id
    List<EmpresaModel> findAll();
    //id existente
    boolean existsById(EmpresaId empresaId);
    //eliminar empresa
    void delete(EmpresaId empresaId);
}