import java.io.File;
import java.io.IOException;
import java.util.*;

public class LeituraPrograma {

    // Scanner para leitura de arquivos e variável para armazenar o quantum
    private static Scanner entrada;
    private static int quantum;

    /**
     * Lê todos os programas na pasta "programas" e retorna uma lista de blocos de controle de processos.
     * Apenas arquivos ".txt" que não sejam "quantum.txt" e "prioridades.txt" serão lidos.
     * @return lista de programas lidos como blocos de controle de processos.
     */
    public static List<BlocoDeControleDeProcessos> programasLidos() {
        List<BlocoDeControleDeProcessos> listaDeProgramas = new ArrayList<>();
        File diretorio = new File("programas");  // Diretório contendo os arquivos de programas

        // Percorre cada arquivo na pasta "programas"
        for (File file : diretorio.listFiles()) {
            try {
                // Verifica se o arquivo é um programa de entrada válido (exclui arquivos específicos)
                if (file.getName().endsWith(".txt") && !file.getName().equals("quantum.txt") && !file.getName().equals("prioridades.txt")) {
                    entrada = new Scanner(file);

                    // Cria um bloco de controle de processo para cada programa
                    BlocoDeControleDeProcessos blocoDoPrograma = new BlocoDeControleDeProcessos(lerDados(), Integer.parseInt(file.getName().replace(".txt", "")));
                    listaDeProgramas.add(blocoDoPrograma);  // Adiciona o programa à lista
                    fecharArquivo();  // Fecha o arquivo após a leitura
                }
            } catch (IOException erroES) {
                // Captura erros de leitura de arquivo
                System.err.println("Erro ao abrir o arquivo. Finalizando.");
                System.exit(1);
            }
        }

        // Ordena a lista de programas lidos
        Collections.sort(listaDeProgramas);
        return listaDeProgramas;
    }

    /**
     * Lê os dados de um arquivo e retorna uma lista de comandos/instruções.
     * @return lista de comandos lidos do arquivo.
     */
    public static List<String> lerDados() {
        List<String> listaDeComandos = new ArrayList<>();

        try {
            // Lê linha por linha do arquivo até o fim
            while (entrada.hasNext()) {
                String comando = entrada.nextLine();
                listaDeComandos.add(comando);  // Adiciona cada comando à lista
            }
        } catch (NoSuchElementException erroElemento) {
            // Erro ao ler um elemento do arquivo
            System.err.println("Arquivo com problemas. Finalizando.");
            entrada.nextLine();
        } catch (IllegalStateException erroEstado) {
            // Erro ao acessar o arquivo
            System.err.println("Erro ao ler o arquivo. Finalizando.");
        }

        return listaDeComandos;
    }

    /**
     * Carrega as prioridades dos processos a partir do arquivo "prioridades.txt".
     * As prioridades são atribuídas a cada processo da lista de programas.
     * @param listaDeProgramas lista de processos onde as prioridades serão atribuídas.
     */
    public static void carregarPrioridades(List<BlocoDeControleDeProcessos> listaDeProgramas) {
        try {
            File prioridadeFile = new File("programas/prioridades.txt");
            Scanner scanner = new Scanner(prioridadeFile);
            int i = 0;

            // Lê cada linha do arquivo de prioridades e aplica a cada processo
            while (scanner.hasNextLine() && i < listaDeProgramas.size()) {
                int prioridade = Integer.parseInt(scanner.nextLine().trim());
                BlocoDeControleDeProcessos processo = listaDeProgramas.get(i);
                processo.setPrioridade(prioridade);
                processo.setCreditos(prioridade);  // Inicialmente, os créditos são iguais à prioridade
                i++;
            }
            scanner.close();
        } catch (Exception e) {
            // Captura erros ao carregar prioridades
            System.err.println("Erro ao carregar prioridades. Finalizando.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Lê o valor do quantum a partir de um arquivo especificado.
     * @param fileName nome do arquivo que contém o valor do quantum.
     * @return o valor do quantum lido do arquivo.
     */
    public static int getQuantumFile(String fileName) {
        try {
            // Abre o arquivo de quantum
            File quantumFile = new File("programas/" + fileName);
            entrada = new Scanner(quantumFile);
            quantum = Integer.parseInt(lerDados().get(0));  // Lê o primeiro valor como o quantum

            return quantum;
        } catch (Exception e) {
            // Captura erros ao abrir o arquivo de quantum
            System.err.println("Erro ao abrir o quantum. Finalizando.");
            System.exit(1);
            return 0;
        }
    }

    /**
     * Fecha o arquivo atualmente aberto.
     */
    public static void fecharArquivo() {
        if (entrada != null)
            entrada.close();
    }

    /**
     * Apaga todos os arquivos de log gerados no diretório atual.
     */
    public static void apagarLogs() {
        File diretorio = new File(".");  // Diretório atual

        // Percorre todos os arquivos no diretório
        for (File file : diretorio.listFiles()) {
            // Verifica se o arquivo é um log e o exclui
            if (file.getName().startsWith("log") && file.getName().contains(".txt"))
                file.delete();
        }
    }
}