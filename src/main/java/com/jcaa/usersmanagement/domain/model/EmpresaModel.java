package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.enums.PaisName;
import lombok.Value;
@Value

public class EmpresaModel {

    EmpresaId idEmpresa;
    EmpresaName empresaName;
    IncomporationDate fechaIncorporacion;
    AnnualBilling facturacionAnual;
    NumVendedores numVendedores;
    AreaName areaName;
    PaisName paisName;

    public static EmpresaModel create (
            final EmpresaId idEmpresa,
            final EmpresaName empresaName,
            final IncorporationDate fechaIncorporacion,
            final AnnualBilling facturacionAnual,
            final NumVendedores numVendedores,
            final AreaName areaName,
            final PaisName paisName) {
        return new EmpresaModel(idEmpresa, empresaName, fechaIncorporacion, facturacionAnual, numVendedores, areaName, paisName);
    }
}