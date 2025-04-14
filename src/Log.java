import java.io.*;

public class Log {

    /**
     * Método estático que grava uma linha de log em um arquivo de log específico.
     * O arquivo é nomeado de acordo com o valor do quantum.
     * @param quantum Valor do quantum utilizado para nomear o arquivo de log.
     * @param log String que contém a mensagem a ser gravada no log.
     */
    static void gravarArquivoLog(int quantum, String log) {

        // Formata o nome do arquivo de log com base no quantum, adicionando um zero à esquerda se necessário
        String numQuantum = quantum < 10 ? "0" + quantum : Integer.toString(quantum);
        String arquivoLog = "log" + numQuantum + ".txt";

        try {
            // Abre o arquivo de log para escrita, permitindo leitura e escrita ("rw")
            RandomAccessFile arq = new RandomAccessFile(arquivoLog, "rw");

            // Cria um Writer para adicionar conteúdo ao arquivo de log em modo append
            Writer csv = new BufferedWriter(new FileWriter(arquivoLog, true));
            csv.append(log + "\n");  // Adiciona a linha de log e uma quebra de linha
            csv.close();  // Fecha o Writer após a gravação

        } catch (Exception e) {
            // Captura qualquer exceção ocorrida durante a gravação no log
            System.out.println(" ERRO NA GRAVAÇÃO DO LOG ");
            e.printStackTrace();
        }
    }
}