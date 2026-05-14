package com.projetos3.edenred.dados;

import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.DadosEmpresaException;
import java.util.ArrayList;

public class BancoEmMemoria {

    // Lista estática que simula o banco de dados
    private static ArrayList<Empresa> empresas = new ArrayList<>();

    static {
        // Empresa 1: Multinacional com baixo índice digital
        empresas.add(new Empresa(
            "12.345.678/0001-99", "TechCorp Soluções", "senha123", 
            5000, 3, true, 
            5.0, 0.15, 0.10, 8, 20.0
        ));

        // Empresa 2: Empresa de médio porte com bom índice digital
        empresas.add(new Empresa(
            "98.765.432/0001-00", "Logística Avançada", "senha456", 
            850, 2, false, 
            4.0, 0.10, 0.05, 5, 65.0
        ));

        // Empresa 3: Pequena empresa quase 100% digital
        empresas.add(new Empresa(
            "11.222.333/0001-44", "Eco Start", "senha789", 
            120, 1, false, 
            5.0, 0.05, 0.02, 4, 92.0
        ));
    }

    // CNPJ e senha do administrador Edenred
    private static final String ADMIN_CNPJ = "00.000.000/0001-00";
    private static final String ADMIN_SENHA = "admin123";

    // Cadastrar nova empresa
    public static void cadastrarEmpresa(Empresa empresa) throws DadosEmpresaException {
        // Validações básicas
        if (empresa.getCnpj() == null || empresa.getCnpj().trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ é obrigatório.");
        }
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new DadosEmpresaException("Nome da empresa é obrigatório.");
        }
        if (empresa.getSenha() == null || empresa.getSenha().trim().isEmpty()) {
            throw new DadosEmpresaException("Senha é obrigatória.");
        }
        if (empresa.getColaboradores() <= 0) {
            throw new DadosEmpresaException("Número de colaboradores deve ser maior que zero.");
        }
        if (empresa.getNumeroBeneficios() <= 0) {
            throw new DadosEmpresaException("Número de benefícios deve ser maior que zero.");
        }
        if (empresa.getVidaUtilCartaoAnos() <= 0) {
            throw new DadosEmpresaException("Vida útil do cartão deve ser maior que zero.");
        }

        // Verificar se CNPJ já existe
        for (Empresa e : empresas) {
            if (e.getCnpj().equals(empresa.getCnpj())) {
                throw new DadosEmpresaException("CNPJ já cadastrado no sistema.");
            }
        }

        empresas.add(empresa);
    }

    // Buscar empresa por CNPJ
    public static Empresa buscarPorCnpj(String cnpj) {
        for (Empresa e : empresas) {
            if (e.getCnpj().equals(cnpj)) {
                return e;
            }
        }
        return null;
    }

    // Autenticar empresa (login)
    public static Empresa autenticar(String cnpj, String senha) throws DadosEmpresaException {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new DadosEmpresaException("CNPJ é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new DadosEmpresaException("Senha é obrigatória.");
        }

        Empresa empresa = buscarPorCnpj(cnpj);
        if (empresa == null) {
            throw new DadosEmpresaException("Empresa não encontrada. Verifique o CNPJ.");
        }
        if (!empresa.getSenha().equals(senha)) {
            throw new DadosEmpresaException("Senha incorreta.");
        }

        return empresa;
    }

    // Verificar se é o admin Edenred
    public static boolean autenticarAdmin(String cnpj, String senha) {
        return ADMIN_CNPJ.equals(cnpj) && ADMIN_SENHA.equals(senha);
    }

    // Listar todas as empresas
    public static ArrayList<Empresa> listarEmpresas() {
        return empresas;
    }

    // Remover empresa por CNPJ
    public static void removerEmpresa(String cnpj) {
        empresas.removeIf(e -> e.getCnpj().equals(cnpj));
    }

    // Atualizar dados de uma empresa
    public static void atualizarEmpresa(String cnpjAntigo, Empresa novosDados) throws DadosEmpresaException {
        for (int i = 0; i < empresas.size(); i++) {
            if (empresas.get(i).getCnpj().equals(cnpjAntigo)) {
                // Se o CNPJ mudou, verifica se o novo já existe
                if (!cnpjAntigo.equals(novosDados.getCnpj())) {
                    if (buscarPorCnpj(novosDados.getCnpj()) != null) {
                        throw new DadosEmpresaException("Novo CNPJ já cadastrado.");
                    }
                }
                empresas.set(i, novosDados);
                return;
            }
        }
        throw new DadosEmpresaException("Empresa não encontrada para atualização.");
    }

    // Retorna o CNPJ do admin para referência
    public static String getAdminCnpj() {
        return ADMIN_CNPJ;
    }
}
