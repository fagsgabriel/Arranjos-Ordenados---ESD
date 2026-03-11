package br.edu.arranjo.experimento;

import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Ponto de entrada dos experimentos científicos.
 * Executa os experimentos de inserção e exclusão, imprime as tabelas
 * e salva os resultados em arquivos texto.
 */
public class Main {

    private static final String DIR_RESULTADOS = "resultados";

    public static void main(String[] args) throws IOException {
        // Garante que o diretório de resultados existe
        Files.createDirectories(Paths.get(DIR_RESULTADOS));

        System.out.println("==========================================================");
        System.out.println("  EXPERIMENTOS - ARRANJOORDENADO");
        System.out.println("  Capacidade: 100.000 | Execuções: 100");
        System.out.println("==========================================================\n");

        // ---- Experimento de Inserção ----
        System.out.println("Executando experimento de INSERÇÃO...");
        System.out.println("(Isso pode levar alguns minutos)\n");
        executarExperimentoInsercao();

        // ---- Experimento de Exclusão ----
        System.out.println("\nExecutando experimento de EXCLUSÃO...");
        System.out.println("(Isso pode levar alguns minutos)\n");
        executarExperimentoExclusao();

        System.out.println("\nResultados salvos em: " + DIR_RESULTADOS + "/");
    }

    // -------------------------------------------------------------------------
    // Experimento de Inserção
    // -------------------------------------------------------------------------

    private static void executarExperimentoInsercao() throws IOException {
        ExperimentoInsercao exp = new ExperimentoInsercao();

        // Combinações: [tipo inserção] x [ordenação crescente/decrescente]
        Resultado[][] resultados = new Resultado[3][2];

        ExperimentoInsercao.TipoInsercao[] tipos = {
            ExperimentoInsercao.TipoInsercao.CRESCENTE,
            ExperimentoInsercao.TipoInsercao.DECRESCENTE,
            ExperimentoInsercao.TipoInsercao.ALEATORIA
        };

        for (int t = 0; t < tipos.length; t++) {
            System.out.printf("  Tipo de inserção: %-12s ", tipos[t]);
            resultados[t][0] = exp.executar(true,  tipos[t]); // crescente
            System.out.print("[crescente OK] ");
            resultados[t][1] = exp.executar(false, tipos[t]); // decrescente
            System.out.println("[decrescente OK]");
        }

        // Imprime e salva tabela
        String tabela = montarTabelaInsercao(resultados);
        System.out.println("\n" + tabela);

        // Extra: Wilcoxon entre inserção crescente vs decrescente (para cada tipo de inserção)
        String wilcoxon = calcularWilcoxonInsercao(resultados, tipos);
        System.out.println(wilcoxon);

        salvarArquivo(DIR_RESULTADOS + "/insercao.txt", tabela + "\n" + wilcoxon);
    }

    private static String montarTabelaInsercao(Resultado[][] resultados) {
        String[] linhas = {"Crescente   ", "Decrescente ", "Aleatória   "};
        StringBuilder sb = new StringBuilder();
        sb.append("=== EXPERIMENTO: INSERÇÃO ===\n");
        sb.append(String.format("%-28s | %-26s | %-26s%n",
            "Tipo de Inserção", "Ordenação Crescente", "Ordenação Decrescente"));
        sb.append("-".repeat(86)).append("\n");

        for (int t = 0; t < 3; t++) {
            sb.append(String.format("%-28s | %-26s | %-26s%n",
                linhas[t],
                resultados[t][0].formatar(),
                resultados[t][1].formatar()));
        }
        return sb.toString();
    }

    private static String calcularWilcoxonInsercao(
            Resultado[][] resultados,
            ExperimentoInsercao.TipoInsercao[] tipos) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n=== TESTE DE WILCOXON — INSERÇÃO ===\n");
        sb.append("(Crescente vs Decrescente para cada tipo de inserção)\n");
        sb.append(String.format("%-14s | %-12s | %-10s | %s%n",
            "Tipo Inserção", "Estatística W", "p-value", "Conclusão"));
        sb.append("-".repeat(65)).append("\n");

        WilcoxonSignedRankTest wilcoxon = new WilcoxonSignedRankTest();

        for (int t = 0; t < tipos.length; t++) {
            double[] crescente   = toDoubleArray(resultados[t][0].getTempos());
            double[] decrescente = toDoubleArray(resultados[t][1].getTempos());

            try {
                double w = wilcoxon.wilcoxonSignedRank(crescente, decrescente);
                double p = wilcoxon.wilcoxonSignedRankTest(crescente, decrescente, false);
                String conclusao = p < 0.05
                    ? "Diferença significativa (p < 0,05)"
                    : "Sem diferença significativa";
                sb.append(String.format("%-14s | %-13.2f | %-10.4f | %s%n",
                    tipos[t], w, p, conclusao));
            } catch (Exception e) {
                sb.append(String.format("%-14s | Erro ao calcular Wilcoxon: %s%n",
                    tipos[t], e.getMessage()));
            }
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Experimento de Exclusão
    // -------------------------------------------------------------------------

    private static void executarExperimentoExclusao() throws IOException {
        ExperimentoExclusao exp = new ExperimentoExclusao();

        Resultado[][] resultados = new Resultado[3][2];

        ExperimentoExclusao.TipoInsercao[] tipos = {
            ExperimentoExclusao.TipoInsercao.CRESCENTE,
            ExperimentoExclusao.TipoInsercao.DECRESCENTE,
            ExperimentoExclusao.TipoInsercao.ALEATORIA
        };

        for (int t = 0; t < tipos.length; t++) {
            System.out.printf("  Pré-preenchimento: %-12s ", tipos[t]);
            resultados[t][0] = exp.executar(true,  tipos[t]);
            System.out.print("[crescente OK] ");
            resultados[t][1] = exp.executar(false, tipos[t]);
            System.out.println("[decrescente OK]");
        }

        String tabela = montarTabelaExclusao(resultados);
        System.out.println("\n" + tabela);

        String wilcoxon = calcularWilcoxonExclusao(resultados, tipos);
        System.out.println(wilcoxon);

        salvarArquivo(DIR_RESULTADOS + "/exclusao.txt", tabela + "\n" + wilcoxon);
    }

    private static String montarTabelaExclusao(Resultado[][] resultados) {
        String[] linhas = {"Crescente   ", "Decrescente ", "Aleatória   "};
        StringBuilder sb = new StringBuilder();
        sb.append("=== EXPERIMENTO: EXCLUSÃO ===\n");
        sb.append(String.format("%-28s | %-26s | %-26s%n",
            "Pré-preenchimento", "Ordenação Crescente", "Ordenação Decrescente"));
        sb.append("-".repeat(86)).append("\n");

        for (int t = 0; t < 3; t++) {
            sb.append(String.format("%-28s | %-26s | %-26s%n",
                linhas[t],
                resultados[t][0].formatar(),
                resultados[t][1].formatar()));
        }
        return sb.toString();
    }

    private static String calcularWilcoxonExclusao(
            Resultado[][] resultados,
            ExperimentoExclusao.TipoInsercao[] tipos) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n=== TESTE DE WILCOXON — EXCLUSÃO ===\n");
        sb.append("(Crescente vs Decrescente para cada pré-preenchimento)\n");
        sb.append(String.format("%-18s | %-12s | %-10s | %s%n",
            "Pré-preenchimento", "Estatística W", "p-value", "Conclusão"));
        sb.append("-".repeat(70)).append("\n");

        WilcoxonSignedRankTest wilcoxon = new WilcoxonSignedRankTest();

        for (int t = 0; t < tipos.length; t++) {
            double[] crescente   = toDoubleArray(resultados[t][0].getTempos());
            double[] decrescente = toDoubleArray(resultados[t][1].getTempos());

            try {
                double w = wilcoxon.wilcoxonSignedRank(crescente, decrescente);
                double p = wilcoxon.wilcoxonSignedRankTest(crescente, decrescente, false);
                String conclusao = p < 0.05
                    ? "Diferença significativa (p < 0,05)"
                    : "Sem diferença significativa";
                sb.append(String.format("%-18s | %-13.2f | %-10.4f | %s%n",
                    tipos[t], w, p, conclusao));
            } catch (Exception e) {
                sb.append(String.format("%-18s | Erro: %s%n",
                    tipos[t], e.getMessage()));
            }
        }
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // Utilitários
    // -------------------------------------------------------------------------

    private static double[] toDoubleArray(long[] longs) {
        double[] d = new double[longs.length];
        for (int i = 0; i < longs.length; i++) {
            d[i] = longs[i];
        }
        return d;
    }

    private static void salvarArquivo(String caminho, String conteudo) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caminho))) {
            pw.println(conteudo);
        }
        System.out.println("  -> Salvo em: " + caminho);
    }
}
