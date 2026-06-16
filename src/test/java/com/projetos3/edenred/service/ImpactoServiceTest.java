package com.projetos3.edenred.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.projetos3.edenred.model.Empresa;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ImpactoServiceTest {

    private final ImpactoService impactoService = new ImpactoService();

    @Test
    public void deveriaCalcularBeneficioAcumuladoConformePercentualDigital() {
        Empresa empresa = new Empresa(
                "12.345.678/0001-99", "TechCorp Solucoes", "senha123",
                5000, 3, true,
                5.0, 0.15, 0.10, 8, 20.0
        );

        Map<String, Object> simulacao20 = impactoService.simularImpacto(empresa, 20);
        Map<String, Object> simulacao40 = impactoService.simularImpacto(empresa, 40);

        assertEquals("150,00 kg", simulacao20.get("tabCo2Atual"));
        assertEquals("150,00 kg", simulacao20.get("tabCo2Sim"));
        assertEquals("150,00 kg", simulacao20.get("cardCo2"));
        assertEquals("0,0 g", simulacao20.get("co2"));

        assertEquals("300,00 kg", simulacao40.get("tabCo2Sim"));
        assertEquals("300,00 kg", simulacao40.get("cardCo2"));
        assertEquals("150,00 kg", simulacao40.get("co2"));
    }

    @Test
    public void deveriaCalcularMateriaisETransacoesPelaFormulaDaTabela() {
        Empresa empresa = new Empresa(
                "12.345.678/0001-99", "TechCorp Solucoes", "senha123",
                5000, 3, true,
                5.0, 0.15, 0.10, 8, 20.0
        );

        Map<String, Object> simulacao40 = impactoService.simularImpacto(empresa, 40);

        assertEquals("15,00 kg", simulacao40.get("cardPlastico"));
        assertEquals("384,00 kg", simulacao40.get("cardPapel"));
        assertEquals("80,64 kg", simulacao40.get("cardCo2Transacional"));
        assertEquals("399,00 kg", simulacao40.get("cardResiduos"));

        assertEquals("7,50 kg", simulacao40.get("plastico"));
        assertEquals("192,00 kg", simulacao40.get("papel"));
        assertEquals("40,32 kg", simulacao40.get("co2Transacional"));
        assertEquals("199,50 kg", simulacao40.get("residuos"));
    }

    @Test
    public void deveriaCalcularImpactoAtualPelosCartoesFisicosRestantes() {
        Empresa empresa = new Empresa(
                "12.345.678/0001-99", "TechCorp Solucoes", "senha123",
                5000, 3, true,
                5.0, 0.15, 0.10, 8, 20.0
        );

        Map<String, String> impactoAtual = impactoService.calcularImpactoAtual(empresa);

        assertEquals("4.000", impactoAtual.get("totalCartoes"));
        assertEquals("600,00 kg", impactoAtual.get("co2"));
        assertEquals("30,00 kg", impactoAtual.get("plastico"));
        assertEquals("160,00 kg", impactoAtual.get("co2Transporte"));
    }
}
