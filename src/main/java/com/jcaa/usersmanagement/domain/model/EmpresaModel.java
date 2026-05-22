package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import lombok.Value;

@Value
public class EmpresaModel {

    EmpresaId                   idEmpresa;
    EmpresaName                 nameEmpresa;
    EmpresaIncorporationDate    incorporationDate;
    EmpresaAnnualBilling        annualBilling;
    EmpresaSede                 sede;
    EmpresaSector               sector;

    public static EmpresaModel create(
            final EmpresaId                 idEmpresa,
            final EmpresaName               nameEmpresa,
            final EmpresaIncorporationDate  incorporationDate,
            final EmpresaAnnualBilling      annualBilling,
            final EmpresaSede               sede,
            final EmpresaSector sector) {

        return new EmpresaModel(
                idEmpresa,
                nameEmpresa,
                incorporationDate,
                annualBilling,
                sede,
                sector);
    }

    public EmpresaModel updateAnnualBilling(final EmpresaAnnualBilling newBilling) {

        return new EmpresaModel(
                idEmpresa,
                nameEmpresa,
                incorporationDate,
                newBilling,
                sede,
                sector);
    }

    public EmpresaModel changeSede(final EmpresaSede newSede) {

        return new EmpresaModel(
                idEmpresa,
                nameEmpresa,
                incorporationDate,
                annualBilling,
                newSede,
                sector);
    }

    public EmpresaModel changeSector(final EmpresaSector newSector) {

        return new EmpresaModel(
                idEmpresa,
                nameEmpresa,
                incorporationDate,
                annualBilling,
                sede,
                newSector);
    }
}