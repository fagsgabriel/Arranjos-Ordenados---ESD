package br.edu.arranjo.experimento;

/**
 * Armazena e calcula estatísticas sobre um conjunto de medições de tempo.
 */
public class Resultado {

    private final long[] tempos;
    private final int execucoes;

    public Resultado(int execucoes) {
        this.execucoes = execucoes;
        this.tempos = new long[execucoes];
    }

    /**
     * Registra o tempo de uma execução.
     *
     * @param indice índice da execução (0-based)
     * @param tempo  tempo em nanosegundos
     */
    public void registrar(int indice, long tempo) {
        tempos[indice] = tempo;
    }

    /** @return array com os tempos brutos em nanosegundos */
    public long[] getTempos() {
        return tempos.clone();
    }

    /** @return média em milissegundos */
    public double media() {
        double soma = 0;
        for (long t : tempos) {
            soma += t;
        }
        return (soma / execucoes) / 1_000_000.0;
    }

    /** @return desvio padrão amostral em milissegundos */
    public double desvioPadrao() {
        double m = media() * 1_000_000.0; // converte de volta para ns para cálculo
        double somaQuadrados = 0;
        for (long t : tempos) {
            double diff = t - m;
            somaQuadrados += diff * diff;
        }
        double variancia = somaQuadrados / (execucoes - 1);
        return Math.sqrt(variancia) / 1_000_000.0;
    }

    /**
     * Formata o resultado como "média +/- desvio ms".
     */
    public String formatar() {
        return String.format("%.2f +/- %.4f ms", media(), desvioPadrao());
    }
}
