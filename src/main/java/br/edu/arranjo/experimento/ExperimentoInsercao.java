package br.edu.arranjo.experimento;

import br.edu.arranjo.ArranjoOrdenado;

import java.util.Random;

/**
 * Experimento científico para medir o desempenho da operação de INSERÇÃO
 * no ArranjoOrdenado com diferentes tipos de ordenação e padrões de entrada.
 *
 * Metodologia:
 * - 100 execuções por combinação
 * - Capacidade: 100.000 elementos
 * - Medição com System.nanoTime()
 */
public class ExperimentoInsercao {

    private static final int CAPACIDADE = 100_000;
    private static final int EXECUCOES = 100;

    /** Tipos de padrão de inserção */
    public enum TipoInsercao {
        CRESCENTE, DECRESCENTE, ALEATORIA
    }

    /**
     * Executa o experimento de inserção para uma combinação de tipo de ordenação
     * e tipo de inserção.
     *
     * @param crescente    true se o ArranjoOrdenado usa ordem crescente
     * @param tipoInsercao padrão dos elementos inseridos
     * @return Resultado com as 100 medições
     */
    public Resultado executar(boolean crescente, TipoInsercao tipoInsercao) {
        Resultado resultado = new Resultado(EXECUCOES);
        Random random = new Random(42); // seed fixo para reprodutibilidade

        for (int exec = 0; exec < EXECUCOES; exec++) {
            ArranjoOrdenado arranjo = new ArranjoOrdenado(CAPACIDADE, crescente);

            // Gera os elementos de acordo com o padrão
            int[] elementos = gerarElementos(tipoInsercao, random);

            long t1 = System.nanoTime();
            for (int e : elementos) {
                arranjo.inserir(e);
            }
            long t2 = System.nanoTime();

            resultado.registrar(exec, t2 - t1);
        }

        return resultado;
    }

    /**
     * Gera um array de 100.000 elementos únicos conforme o padrão solicitado.
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
                // Gera permutação aleatória de 0..CAPACIDADE-1 (garante unicidade)
                elementos = gerarPermutacaoAleatoria(random);
                break;
        }

        return elementos;
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
