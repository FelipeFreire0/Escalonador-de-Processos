public class Escalonador {  
// Método principal que decide entre executar o escalonador ou a simulação
public static void main(String[] args) {
    executaEscalonador(); // Executa o escalonador
}

// Método que executa o escalonador de processos com base no arquivo de quantum
private static void executaEscalonador() {
    System.out.println("Iniciando escalonador...");

    // Apaga logs anteriores antes de iniciar o escalonador
    LeituraPrograma.apagarLogs();
    
    // Cria um novo gerenciador de escalonador com o arquivo de quantum
    GerenciadorEscalonador escalonador = new GerenciadorEscalonador("quantum.txt");

    // Carrega os programas no escalonador
    escalonador.carregandoProgramas();

    // Carrega as prioridades para a lista de processos prontos
    LeituraPrograma.carregarPrioridades(escalonador.listaDeProntos); 

    // Executa o escalonador utilizando o algoritmo de créditos
    escalonador.executaEscalonadorPorCreditos();

    // Gera o log final da execução
    escalonador.logFinal();
}

}