package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;

import java.util.List;
import java.util.Optional;


public interface EmpresaRepositoryPort {

    //guardar empresa
    EmpresaModel save(EmpresaModel empresa);

    //encontrar por id
    Optional<EmpresaModel> findById(EmpresaId idEmpresa);

    //encontrar por nombre
    Optional<UserModel> findByName(EmpresaName nameEmpresa);

    //mostrar todas las id
    List<EmpresaModel> findAll();

    //id existente
    boolean existsById(EmpresaId idEmpresa);

    //eliminar empresa
    void delete(EmpresaId idEmpresa);
}