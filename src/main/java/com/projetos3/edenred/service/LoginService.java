package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public boolean autenticarAdmin(String cnpj, String senha) {
        return BancoEmMemoria.autenticarAdmin(cnpj, senha);
    }

    public Empresa autenticarEmpresa(String cnpj, String senha) throws DadosEmpresaException {
        return BancoEmMemoria.autenticar(cnpj, senha);
    }
}
