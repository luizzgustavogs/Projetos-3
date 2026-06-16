package com.projetos3.edenred.repository;

import com.projetos3.edenred.model.RelatorioDigitalizacao;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class RelatorioDigitalizacaoRepository {

    private final AtomicLong sequence = new AtomicLong(1);
    private final List<RelatorioDigitalizacao> relatorios = new CopyOnWriteArrayList<>();

    public RelatorioDigitalizacao save(RelatorioDigitalizacao relatorio) {
        if (relatorio.getId() == null) {
            relatorio.setId(sequence.getAndIncrement());
        }
        relatorios.removeIf(item -> item.getId().equals(relatorio.getId()));
        relatorios.add(relatorio);
        return relatorio;
    }

    public long count() {
        return relatorios.size();
    }

    public List<RelatorioDigitalizacao> findTop10ByOrderByDataGeracaoDesc() {
        return relatoriosOrdenados().stream()
                .limit(10)
                .toList();
    }

    public List<RelatorioDigitalizacao> findByEmpresaCnpjOrderByDataGeracaoDesc(String empresaCnpj) {
        return relatoriosOrdenados().stream()
                .filter(relatorio -> relatorio.getEmpresaCnpj().equals(empresaCnpj))
                .toList();
    }

    private List<RelatorioDigitalizacao> relatoriosOrdenados() {
        return new ArrayList<>(relatorios).stream()
                .sorted(Comparator.comparing(RelatorioDigitalizacao::getDataGeracao).reversed())
                .toList();
    }
}
