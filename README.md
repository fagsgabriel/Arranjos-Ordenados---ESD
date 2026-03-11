# ArranjoOrdenado

Implementacao de uma estrutura de dados **Arranjo Ordenado** em Java, com analise experimental de desempenho das operacoes de insercao e exclusao.

Projeto desenvolvido como atividade academica para a disciplina de Estrutura de Dados.

---

## Funcionalidades

- Insercao mantendo a ordenacao (crescente ou decrescente) via **busca binaria**
- Exclusao de elementos com localizacao por **busca binaria**
- Busca eficiente (`contem`)
- Validacao de duplicatas e capacidade

---

## Experimentos

O projeto inclui experimentos que medem o tempo de execucao (em milissegundos) das operacoes de **insercao** e **exclusao** sob diferentes cenarios, com **100 execucoes** e capacidade de **100.000 elementos**.

### Insercao

| Tipo de Insercao | Ordenacao Crescente | Ordenacao Decrescente |
|---|---|---|
| Crescente | 3,79 +/- 0,81 ms | 1185,42 +/- 514,15 ms |
| Decrescente | 1529,06 +/- 278,22 ms | 3,35 +/- 0,66 ms |
| Aleatoria | 406,70 +/- 222,99 ms | 355,67 +/- 191,87 ms |

### Exclusao

| Pre-preenchimento | Ordenacao Crescente | Ordenacao Decrescente |
|---|---|---|
| Crescente | 665,17 +/- 310,51 ms | 557,40 +/- 48,67 ms |
| Decrescente | 490,72 +/- 146,30 ms | 409,75 +/- 183,71 ms |
| Aleatoria | 187,70 +/- 28,06 ms | 173,74 +/- 9,37 ms |

A significancia estatistica e avaliada pelo **Teste de Wilcoxon** (signed-rank).

---

## Tecnologias

- **Java 11**
- **Maven**
- **JUnit 5** -- testes unitarios
- **Apache Commons Math 3** -- teste estatistico de Wilcoxon

---

## Como executar

```bash
# Compilar o projeto
mvn compile

# Executar os experimentos
mvn exec:java

# Rodar os testes
mvn test
```

Os resultados sao salvos na pasta `resultados/`.

---

## Estrutura do projeto

```
src/
  main/java/br/edu/arranjo/
    ArranjoOrdenado.java            -- Estrutura de dados
    experimento/
      Main.java                     -- Ponto de entrada dos experimentos
      ExperimentoInsercao.java      -- Experimento de insercao
      ExperimentoExclusao.java      -- Experimento de exclusao
      Resultado.java                -- Modelo de resultado
  test/java/br/edu/arranjo/
    ArranjoOrdenadoTest.java        -- Testes unitarios
```
