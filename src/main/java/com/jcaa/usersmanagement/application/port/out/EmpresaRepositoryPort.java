package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepositoryPort {

    //guardar empresa
    EmpresaModel saveEmpresa(EmpresaModel empresa);

    //encontrar por id
    Optional<EmpresaModel> findEmpresaById(EmpresaId idEmpresa);

    //encontrar por nombre
    Optional<EmpresaModel> findEmpresaByName(EmpresaName nameEmpresa);

    //mostrar todas las id
    List<EmpresaModel> findAllEmpresas();

    //actualizar facturación anual
    EmpresaModel updateEmpresaAnnualBilling(EmpresaModel empresa);

    //cambiar sede
    EmpresaModel changeEmpresaSede(EmpresaModel empresa);

    //cambiar sector
    EmpresaModel chngeEmpresaSector(EmpresaModel empresa);

    //eliminar empresa
    void deleteEmpresa(EmpresaId idEmpresa);
}