package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final String adminCnpj;
    private final String adminSenha;
    private final String adminUsuarioProvisorio;
    private final String adminSenhaProvisoria;

    public LoginService(@Value("${app.admin.cnpj}") String adminCnpj,
                        @Value("${app.admin.senha}") String adminSenha,
                        @Value("${app.admin.usuario-provisorio}") String adminUsuarioProvisorio,
                        @Value("${app.admin.senha-provisoria}") String adminSenhaProvisoria) {
        this.adminCnpj = adminCnpj;
        this.adminSenha = adminSenha;
        this.adminUsuarioProvisorio = adminUsuarioProvisorio;
        this.adminSenhaProvisoria = adminSenhaProvisoria;
    }

    public boolean autenticarAdmin(String cnpj, String senha) {
        return (adminCnpj.equals(cnpj) && adminSenha.equals(senha))
                || (adminUsuarioProvisorio.equalsIgnoreCase(cnpj) && adminSenhaProvisoria.equals(senha));
    }

    public Empresa autenticarEmpresa(String cnpj, String senha) throws DadosEmpresaException {
        return BancoEmMemoria.autenticar(cnpj, senha);
    }
}
