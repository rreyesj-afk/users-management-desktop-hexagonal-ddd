package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import java.util.List;

public interface GetAllEmpresasUseCase {
    List<EmpresaModel> execute();
}