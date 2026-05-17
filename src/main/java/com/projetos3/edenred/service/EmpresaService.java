package com.projetos3.edenred.service;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.DadosEmpresaException;
import com.projetos3.edenred.model.Empresa;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    public List<Empresa> listarEmpresas() {
        return BancoEmMemoria.listarEmpresas();
    }

    public long contarEmpresas() {
        return listarEmpresas().size();
    }

    public long contarColaboradores() {
        return listarEmpresas().stream()
                .mapToLong(Empresa::getColaboradores)
                .sum();
    }

    public void cadastrarEmpresa(String cnpj,
                                  String nome,
                                  String senha,
                                  int colaboradores,
                                  int numeroBeneficios,
                                  boolean multibeneficio,
                                  double vidaUtilCartaoAnos,
                                  double taxaTurnover,
                                  double taxaReemissao,
                                  int transacoesMensais,
                                  double porcentagemDigitalAtual) throws DadosEmpresaException {

        Empresa empresa = new Empresa(
                cnpj, nome, senha, colaboradores,
                numeroBeneficios, multibeneficio,
                vidaUtilCartaoAnos, taxaTurnover,
                taxaReemissao, transacoesMensais,
                porcentagemDigitalAtual
        );

        BancoEmMemoria.cadastrarEmpresa(empresa);
    }

    public void removerEmpresa(String cnpj) {
        BancoEmMemoria.removerEmpresa(cnpj);
    }
}
