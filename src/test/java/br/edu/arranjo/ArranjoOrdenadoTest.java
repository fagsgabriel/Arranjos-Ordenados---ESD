package br.edu.arranjo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ArranjoOrdenado usando JUnit 5.
 */
@DisplayName("ArranjoOrdenado")
class ArranjoOrdenadoTest {

    // =========================================================================
    // Testes de criação
    // =========================================================================

    @Test
    @DisplayName("Cria arranjo vazio com capacidade correta")
    void testCriacaoVazio() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        assertEquals(0, a.tamanho());
        assertEquals(10, a.capacidade());
        assertTrue(a.estaVazio());
        assertFalse(a.estaCheia());
    }

    @Test
    @DisplayName("Lança exceção para capacidade inválida")
    void testCapacidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new ArranjoOrdenado(0, true));
        assertThrows(IllegalArgumentException.class, () -> new ArranjoOrdenado(-1, true));
    }

    // =========================================================================
    // Testes de inserção crescente
    // =========================================================================

    @Test
    @DisplayName("Inserção crescente: elementos ficam em ordem")
    void testInsercaoCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(5);
        a.inserir(2);
        a.inserir(8);
        a.inserir(1);
        a.inserir(9);

        assertEquals(5, a.tamanho());
        assertEquals(1, a.get(0));
        assertEquals(2, a.get(1));
        assertEquals(5, a.get(2));
        assertEquals(8, a.get(3));
        assertEquals(9, a.get(4));
    }

    @Test
    @DisplayName("Inserção crescente: elemento menor que todos vai para posição 0")
    void testInsercaoNoInicioCrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(10);
        a.inserir(5);
        a.inserir(1);

        assertEquals(1,  a.get(0));
        assertEquals(5,  a.get(1));
        assertEquals(10, a.get(2));
    }

    // =========================================================================
    // Testes de inserção decrescente
    // =========================================================================

    @Test
    @DisplayName("Inserção decrescente: elementos ficam em ordem inversa")
    void testInsercaoDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, false);
        a.inserir(5);
        a.inserir(2);
        a.inserir(8);
        a.inserir(1);
        a.inserir(9);

        assertEquals(5, a.tamanho());
        assertEquals(9, a.get(0));
        assertEquals(8, a.get(1));
        assertEquals(5, a.get(2));
        assertEquals(2, a.get(3));
        assertEquals(1, a.get(4));
    }

    @Test
    @DisplayName("Inserção decrescente: elemento maior vai para posição 0")
    void testInsercaoNoInicioDecrescente() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, false);
        a.inserir(1);
        a.inserir(5);
        a.inserir(10);

        assertEquals(10, a.get(0));
        assertEquals(5,  a.get(1));
        assertEquals(1,  a.get(2));
    }

    // =========================================================================
    // Testes de inserção aleatória
    // =========================================================================

    @Test
    @DisplayName("Inserção aleatória: arranjo crescente permanece ordenado")
    void testInsercaoAleatoriaOrdemCrescentePreservada() {
        ArranjoOrdenado a = new ArranjoOrdenado(100, true);
        Random rng = new Random(0);

        // Gera 100 valores únicos aleatórios
        java.util.Set<Integer> inseridos = new java.util.HashSet<>();
        while (inseridos.size() < 100) {
            inseridos.add(rng.nextInt(10000));
        }
        for (int v : inseridos) {
            a.inserir(v);
        }

        // Verifica que está crescente
        for (int i = 0; i < a.tamanho() - 1; i++) {
            assertTrue(a.get(i) <= a.get(i + 1),
                "Posição " + i + ": " + a.get(i) + " > " + a.get(i + 1));
        }
    }

    @Test
    @DisplayName("Inserção aleatória: arranjo decrescente permanece ordenado")
    void testInsercaoAleatoriaOrdemDecrescentePreservada() {
        ArranjoOrdenado a = new ArranjoOrdenado(100, false);
        Random rng = new Random(0);

        java.util.Set<Integer> inseridos = new java.util.HashSet<>();
        while (inseridos.size() < 100) {
            inseridos.add(rng.nextInt(10000));
        }
        for (int v : inseridos) {
            a.inserir(v);
        }

        // Verifica que está decrescente
        for (int i = 0; i < a.tamanho() - 1; i++) {
            assertTrue(a.get(i) >= a.get(i + 1),
                "Posição " + i + ": " + a.get(i) + " < " + a.get(i + 1));
        }
    }

    // =========================================================================
    // Testes de exceções na inserção
    // =========================================================================

    @Test
    @DisplayName("Inserção em arranjo cheio lança IllegalStateException")
    void testInsercaoArranjoCheia() {
        ArranjoOrdenado a = new ArranjoOrdenado(3, true);
        a.inserir(1);
        a.inserir(2);
        a.inserir(3);

        assertTrue(a.estaCheia());
        assertThrows(IllegalStateException.class, () -> a.inserir(4));
    }

    @Test
    @DisplayName("Inserção de valor duplicado lança IllegalArgumentException")
    void testInsercaoDuplicada() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(10);

        assertThrows(IllegalArgumentException.class, () -> a.inserir(10));
    }

    // =========================================================================
    // Testes de exclusão
    // =========================================================================

    @Test
    @DisplayName("Exclusão: remove elemento existente e mantém ordenação")
    void testExclusaoExistente() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1);
        a.inserir(3);
        a.inserir(5);

        a.excluir(3);

        assertEquals(2, a.tamanho());
        assertEquals(1, a.get(0));
        assertEquals(5, a.get(1));
        assertFalse(a.contem(3));
    }

    @Test
    @DisplayName("Exclusão: remove o primeiro elemento")
    void testExclusaoPrimeiro() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1);
        a.inserir(2);
        a.inserir(3);

        a.excluir(1);

        assertEquals(2, a.get(0));
        assertEquals(2, a.tamanho());
    }

    @Test
    @DisplayName("Exclusão: remove o último elemento")
    void testExclusaoUltimo() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1);
        a.inserir(2);
        a.inserir(3);

        a.excluir(3);

        assertEquals(2, a.get(a.tamanho() - 1));
        assertEquals(2, a.tamanho());
    }

    @Test
    @DisplayName("Exclusão de elemento inexistente lança NoSuchElementException")
    void testExclusaoInexistente() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1);
        a.inserir(2);

        assertThrows(NoSuchElementException.class, () -> a.excluir(99));
    }

    @Test
    @DisplayName("Exclusão de elemento em arranjo vazio lança NoSuchElementException")
    void testExclusaoArranjoVazio() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);

        assertThrows(NoSuchElementException.class, () -> a.excluir(1));
    }

    // =========================================================================
    // Testes de busca
    // =========================================================================

    @Test
    @DisplayName("contem(): retorna true para elemento presente")
    void testContemTrue() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(7);
        a.inserir(3);
        a.inserir(15);

        assertTrue(a.contem(7));
        assertTrue(a.contem(3));
        assertTrue(a.contem(15));
    }

    @Test
    @DisplayName("contem(): retorna false para elemento ausente")
    void testContemFalse() {
        ArranjoOrdenado a = new ArranjoOrdenado(10, true);
        a.inserir(7);

        assertFalse(a.contem(1));
        assertFalse(a.contem(100));
        assertFalse(a.contem(0));
    }

    @Test
    @DisplayName("contem(): retorna false após exclusão do elemento")
    void testContemAposExclusao() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(42);
        assertTrue(a.contem(42));

        a.excluir(42);
        assertFalse(a.contem(42));
    }

    // =========================================================================
    // Testes de estado
    // =========================================================================

    @Test
    @DisplayName("estaVazio(): correto após inserções e exclusões")
    void testEstaVazio() {
        ArranjoOrdenado a = new ArranjoOrdenado(3, true);
        assertTrue(a.estaVazio());

        a.inserir(1);
        assertFalse(a.estaVazio());

        a.excluir(1);
        assertTrue(a.estaVazio());
    }

    @Test
    @DisplayName("estaCheia(): correto ao atingir a capacidade")
    void testEstaCheia() {
        ArranjoOrdenado a = new ArranjoOrdenado(2, true);
        assertFalse(a.estaCheia());

        a.inserir(1);
        assertFalse(a.estaCheia());

        a.inserir(2);
        assertTrue(a.estaCheia());

        a.excluir(1);
        assertFalse(a.estaCheia());
    }

    @Test
    @DisplayName("get(): lança IndexOutOfBoundsException para índice inválido")
    void testGetIndiceInvalido() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(1);

        assertThrows(IndexOutOfBoundsException.class, () -> a.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(1));
    }

    // =========================================================================
    // Testes de reinserção após exclusão
    // =========================================================================

    @Test
    @DisplayName("Reinserção após exclusão funciona corretamente")
    void testReinsercaoAposExclusao() {
        ArranjoOrdenado a = new ArranjoOrdenado(5, true);
        a.inserir(10);
        a.inserir(20);
        a.excluir(10);
        a.inserir(10); // reinsere

        assertTrue(a.contem(10));
        assertEquals(2, a.tamanho());
        assertEquals(10, a.get(0));
        assertEquals(20, a.get(1));
    }
}
