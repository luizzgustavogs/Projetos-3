package com.projetos3.edenred.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculadoraMateriaisTest {

    @Test
    public void deveriaCalcularPlasticoCorretamente() {
        double resultado = CalculadoraMateriais.calcularPlastico(10);
        assertEquals(50.0, resultado);
    }

    @Test
    public void deveriaRetornarZeroQuandoCartoesNegativos() {
        double resultado = CalculadoraMateriais.calcularPlastico(-5);
        assertEquals(0.0, resultado);
    }

    @Test
    public void deveriaCalcularLogisticaComSucesso() {
        double resultado = CalculadoraMateriais.calcularLogistica(10, 100.0);
        assertEquals(150.0, resultado);
    }

    @Test
    public void deveriaFormatarParaGramas() {
        String resultado = CalculadoraMateriais.formatarPeso(500.0);
        assertEquals("500,0 g", resultado);
    }

    @Test
    public void deveriaFormatarParaQuilos() {
        String resultado = CalculadoraMateriais.formatarPeso(2500.0);
        assertEquals("2,50 kg", resultado);
    }
}