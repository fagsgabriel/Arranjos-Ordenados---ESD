package br.edu.arranjo.experimento;

import br.edu.arranjo.ArranjoOrdenado;

import java.util.Random;

/**
 * Experimento científico para medir o desempenho da operação de EXCLUSÃO
 * no ArranjoOrdenado com diferentes tipos de ordenação e padrões de entrada.
 *
 * Metodologia:
 * - 100 execuções por combinação
 * - Capacidade: 100.000 elementos (arranjo inicialmente cheio)
 * - Medição com System.nanoTime()
 */
public class ExperimentoExclusao {

    private static final int CAPACIDADE = 100_000;
    private static final int EXECUCOES = 100;

    /** Tipos de padrão de inserção inicial (determina estado do arranjo antes da exclusão) */
    public enum TipoInsercao {
        CRESCENTE, DECRESCENTE, ALEATORIA
    }

    /**
     * Executa o experimento de exclusão para uma combinação de tipo de ordenação
     * e tipo de inserção inicial.
     *
     * @param crescente    true se o ArranjoOrdenado usa ordem crescente
     * @param tipoInsercao padrão com que o arranjo foi preenchido
     * @return Resultado com as 100 medições
     */
    public Resultado executar(boolean crescente, TipoInsercao tipoInsercao) {
        Resultado resultado = new Resultado(EXECUCOES);
        Random random = new Random(42); // seed fixo para reprodutibilidade

        for (int exec = 0; exec < EXECUCOES; exec++) {
            // Cria e preenche o arranjo
            ArranjoOrdenado arranjo = new ArranjoOrdenado(CAPACIDADE, crescente);
            int[] elementosInseridos = preencherArranjo(arranjo, tipoInsercao, random);

            // Embaralha a ordem de exclusão para não ter padrão
            int[] ordemExclusao = embaralhar(elementosInseridos, random);

            long t1 = System.nanoTime();
            for (int e : ordemExclusao) {
                arranjo.excluir(e);
            }
            long t2 = System.nanoTime();

            resultado.registrar(exec, t2 - t1);
        }

        return resultado;
    }

    /**
     * Preenche o arranjo de acordo com o padrão e retorna os elementos inseridos.
     */
    private int[] preencherArranjo(ArranjoOrdenado arranjo, TipoInsercao tipo, Random random) {
        int[] elementos = gerarElementos(tipo, random);
        for (int e : elementos) {
            arranjo.inserir(e);
        }
        return elementos;
    }

    /**
     * Gera 100.000 elementos únicos conforme o padrão.
     */
    private int[] gerarElementos(TipoInsercao tipo, Random random) {
        int[] elementos = new int[CAPACIDADE];

        switch (tipo) {
            case CRESCENTE:
                for (int i = 0; i < CAPACIDADE; i++) {
                    elementos[i] = i;
                }
                break;

            case DECRESCENTE:
                for (int i = 0; i < CAPACIDADE; i++) {
                    elementos[i] = CAPACIDADE - 1 - i;
                }
                break;

            case ALEATORIA:
                elementos = gerarPermutacaoAleatoria(random);
                break;
        }

        return elementos;
    }

    /**
     * Retorna uma cópia do array embaralhada com Fisher-Yates.
     */
    private int[] embaralhar(int[] original, Random random) {
        int[] copia = original.clone();
        for (int i = copia.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = copia[i];
            copia[i] = copia[j];
            copia[j] = tmp;
        }
        return copia;
    }

    /**
     * Gera uma permutação aleatória de [0, CAPACIDADE) usando Fisher-Yates.
     */
    private int[] gerarPermutacaoAleatoria(Random random) {
        int[] perm = new int[CAPACIDADE];
        for (int i = 0; i < CAPACIDADE; i++) {
            perm[i] = i;
        }
        for (int i = CAPACIDADE - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }
        return perm;
    }
}
