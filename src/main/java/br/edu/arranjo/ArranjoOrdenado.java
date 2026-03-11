package br.edu.arranjo;

import java.util.NoSuchElementException;

/**
 * Estrutura de dados ArranjoOrdenado para inteiros.
 * Mantém os elementos sempre ordenados (crescente ou decrescente)
 * usando busca binária para inserção e exclusão eficientes.
 */
public class ArranjoOrdenado {

    private final int[] dados;
    private final boolean crescente;
    private int tamanho;

    /**
     * Cria um ArranjoOrdenado vazio.
     *
     * @param capacidade número máximo de elementos
     * @param crescente  true para ordem crescente, false para decrescente
     */
    public ArranjoOrdenado(int capacidade, boolean crescente) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser positiva.");
        }
        this.dados = new int[capacidade];
        this.crescente = crescente;
        this.tamanho = 0;
    }

    /**
     * Insere um valor mantendo a ordenação.
     * Usa busca binária para encontrar a posição de inserção.
     *
     * @param valor valor a inserir
     * @throws IllegalStateException    se o arranjo estiver cheio
     * @throws IllegalArgumentException se o valor já existir
     */
    public void inserir(int valor) {
        if (estaCheia()) {
            throw new IllegalStateException("ArranjoOrdenado está cheio.");
        }

        int pos = posicaoInsercao(valor);

        // Verifica duplicata
        if (pos < tamanho && dados[pos] == valor) {
            throw new IllegalArgumentException("Valor duplicado: " + valor);
        }

        // Desloca elementos para abrir espaço
        System.arraycopy(dados, pos, dados, pos + 1, tamanho - pos);
        dados[pos] = valor;
        tamanho++;
    }

    /**
     * Remove um valor do arranjo.
     * Usa busca binária para localizar o elemento.
     *
     * @param valor valor a remover
     * @throws NoSuchElementException se o valor não for encontrado
     */
    public void excluir(int valor) {
        int pos = buscaBinaria(valor);

        if (pos < 0) {
            throw new NoSuchElementException("Valor não encontrado: " + valor);
        }

        // Desloca elementos para fechar o gap
        System.arraycopy(dados, pos + 1, dados, pos, tamanho - pos - 1);
        tamanho--;
    }

    /**
     * Verifica se um valor está no arranjo.
     *
     * @param valor valor a buscar
     * @return true se encontrado
     */
    public boolean contem(int valor) {
        return buscaBinaria(valor) >= 0;
    }

    /** @return número de elementos armazenados */
    public int tamanho() {
        return tamanho;
    }

    /** @return true se não há elementos */
    public boolean estaVazio() {
        return tamanho == 0;
    }

    /** @return true se chegou à capacidade máxima */
    public boolean estaCheia() {
        return tamanho == dados.length;
    }

    /** @return capacidade máxima do arranjo */
    public int capacidade() {
        return dados.length;
    }

    /**
     * Retorna o elemento na posição informada (0-indexed).
     *
     * @param indice índice do elemento
     * @return valor na posição
     */
    public int get(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return dados[indice];
    }

    // -------------------------------------------------------------------------
    // Métodos privados
    // -------------------------------------------------------------------------

    /**
     * Busca binária adaptada para ordenação crescente ou decrescente.
     *
     * @return índice do elemento se encontrado, -1 caso contrário
     */
    private int buscaBinaria(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;
            int cmp = comparar(dados[meio], valor);

            if (cmp == 0) {
                return meio;
            } else if (cmp < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    /**
     * Determina a posição correta para inserção usando busca binária.
     *
     * @return índice onde o valor deve ser inserido
     */
    private int posicaoInsercao(int valor) {
        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;
            int cmp = comparar(dados[meio], valor);

            if (cmp < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return inicio;
    }

    /**
     * Comparação levando em conta a ordenação configurada.
     * Para crescente: compara normalmente (a - b).
     * Para decrescente: inverte (b - a).
     */
    private int comparar(int a, int b) {
        if (crescente) {
            return Integer.compare(a, b);
        } else {
            return Integer.compare(b, a);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tamanho; i++) {
            sb.append(dados[i]);
            if (i < tamanho - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
