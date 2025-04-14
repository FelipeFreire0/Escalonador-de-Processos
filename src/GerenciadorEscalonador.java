import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar o escalonamento de processos e suas operações.
 */
public class GerenciadorEscalonador {

    // Atributos principais do Gerenciador de Escalonador
    private int quantum, instrucoesExecutadas, trocas, totalProgramas;
    private Instrucoes instrucao = null;
    private String log;

    // Listas que armazenam os processos prontos, bloqueados e a tabela de processos
    protected List<BlocoDeControleDeProcessos> listaDeProntos = new ArrayList<>();
    protected List<BlocoDeControleDeProcessos> listaDeBloqueados = new ArrayList<>();
    protected List<BlocoDeControleDeProcessos> tabelaDeProcessos = new ArrayList<>();

    // Construtor que recebe um valor de quantum
    public GerenciadorEscalonador(int quantum) {
        this.quantum = quantum;
    }

    // Construtor que lê o valor do quantum de um arquivo
    public GerenciadorEscalonador(String quantumFile) {
        this.quantum = LeituraPrograma.getQuantumFile(quantumFile);
    }

    // Método responsável por carregar os programas na tabela de processos
    public void carregandoProgramas() {
        tabelaDeProcessos = LeituraPrograma.programasLidos();
        totalProgramas = tabelaDeProcessos.size();
        
        for (BlocoDeControleDeProcessos processo : tabelaDeProcessos) {
            listaDeProntos.add(processo);

            String log = "Carregando " + processo.getNomePrograma();
            Log.gravarArquivoLog(quantum, log);
        }
    }

    // Método responsável por executar o escalonador utilizando o algoritmo de créditos
    public void executaEscalonadorPorCreditos() {
        System.out.println("INICIANDO ESCALONAMENTO POR CRÉDITOS - QUANTUM = " + quantum);

        while (listaDeProntos.size() > 0 || listaDeBloqueados.size() > 0) {
            try {
                if (listaDeProntos.size() > 0) {
                    listaDeProntos.sort((p1, p2) -> p2.getCreditos() - p1.getCreditos()); // Ordena os processos por créditos
                    BlocoDeControleDeProcessos programa = listaDeProntos.get(0);
                    instrucao = new Instrucoes(programa, quantum);

                    if (!instrucao.terminou()) {
                        log = "Executando " + programa.getNomePrograma();
                        Log.gravarArquivoLog(quantum, log);

                        programa.setEstadoProcesso("Executando");
                        instrucao.processaInstrucoes();

                        instrucoesExecutadas += instrucao.getInstrucoesExecutadas();
                        trocas++;  

                        programa.decrementaCredito();
                        if (programa.getCreditos() == 0) {
                            redistribuirCreditos();
                        }

                        log = "Interrompendo " + programa.getNomePrograma() + " após " + instrucao.getInstrucoesExecutadas() + " instruções";
                        Log.gravarArquivoLog(quantum, log);
                    }
                    listaDeProntos.add(listaDeProntos.remove(0)); 
                    decrementaTempoEspera_Bloqueados();
                } else {
                    decrementaTempoEspera_Bloqueados();
                }
            } catch (Exception e) {
                if (e.getMessage().equals("E/S")) executaES();
                if (e.getMessage().equals("SAIDA")) executaSaida();
                decrementaTempoEspera_Bloqueados();
            } finally {
                imprimeListaDeProntos();
                imprimeListaDeBloqueados();
                imprimeTabelaProcessos();
            }
        }
    }

    // Redistribui os créditos dos processos prontos
    private void redistribuirCreditos() {
        for (BlocoDeControleDeProcessos processo : listaDeProntos) {
            processo.setCreditos(processo.getPrioridade());
        }
    }

    // Executa as operações de entrada e saída (E/S) do processo
    private void executaES() {
        BlocoDeControleDeProcessos programaES = listaDeProntos.get(0);

        log = "Interrompendo " + programaES.getNomePrograma() + " após " + instrucao.getInstrucoesExecutadas() + " instruções";
        System.out.println(log);
        Log.gravarArquivoLog(quantum, log);
        
        log = "E/S iniciada em " + programaES.getNomePrograma();
        System.out.println(log);
        Log.gravarArquivoLog(quantum, log);            

        instrucoesExecutadas += instrucao.getInstrucoesExecutadas();
        trocas++;                

        programaES.setTempoEspera(2);
        programaES.setEstadoProcesso("Bloqueado");

        listaDeBloqueados.add(listaDeProntos.remove(0));        
    }

    // Executa o processo de saída
    private void executaSaida() {
        BlocoDeControleDeProcessos programaSaida = listaDeProntos.remove(0);

        int posicao = tabelaDeProcessos.indexOf(programaSaida);
        tabelaDeProcessos.remove(posicao);

        System.out.println(programaSaida.getNomePrograma() + " REMOVIDO da lista de PRONTOS e da TABELA PROCESSOS!");

        instrucoesExecutadas += instrucao.getInstrucoesExecutadas();
        trocas++;

        log = programaSaida.getNomePrograma() + " terminado. X=" + programaSaida.getRegistradorX() + ". Y=" + programaSaida.getRegistradorY();
        Log.gravarArquivoLog(quantum, log);
    }

    // Decrementa o tempo de espera dos processos bloqueados
    private void decrementaTempoEspera_Bloqueados() {
        for (BlocoDeControleDeProcessos processo : listaDeBloqueados) {
            processo.decrementaTempoEspera();
        }

        retiraZerados_Bloqueados();
    }

    // Remove processos que não estão mais bloqueados
    private void retiraZerados_Bloqueados() {
        for (BlocoDeControleDeProcessos processo : listaDeBloqueados) {
            if (processo.getTempoEspera() == 0) {
                int posicao = listaDeBloqueados.indexOf(processo);
                listaDeBloqueados.get(posicao).setEstadoProcesso("Pronto");
                listaDeProntos.add(listaDeBloqueados.remove(posicao));    
                retiraZerados_Bloqueados();
            }

            if (listaDeBloqueados.size() == 0) return;
        }
    }

    // Calcula e retorna a média de trocas de contexto
    public double getMediaTrocasContexto() {
        System.out.println("MEDIA DE TROCAS:  " + (trocas/ totalProgramas) + " / QTD DE TROCAS: " + trocas + " / QTD PROGRAMAS: " + totalProgramas);  
        return ((double) trocas / totalProgramas);        
    }

    // Calcula e retorna a média de instruções por troca de contexto
    public double getMediaInstrucoes() {
        System.out.println("MEDIA DE INSTRUCOES:  " + (instrucoesExecutadas / trocas) + " / QTD DE INSTRUCOES: " + instrucoesExecutadas + " / QTD TROCAS: " + trocas); 
        return ((double) instrucoesExecutadas / trocas);
    }

    // Imprime a tabela de processos
    public void imprimeTabelaProcessos() {
        System.out.println("\n\n---- TABELA DE PROCESSOS");
        for (BlocoDeControleDeProcessos processo : tabelaDeProcessos)
            System.out.print(processo);
    }  

    // Imprime a lista de processos prontos
    public void imprimeListaDeProntos() {
        System.out.println("\n\n---- LISTA DE PRONTOS");
        for (BlocoDeControleDeProcessos processo : listaDeProntos)
            System.out.print(processo);
    }

    // Imprime a lista de processos bloqueados
    public void imprimeListaDeBloqueados() {
        System.out.println("\n---- LISTA DE BLOQUEADOS");
        for (BlocoDeControleDeProcessos processo : listaDeBloqueados)
            System.out.print(processo);
    }

    // Registra as informações finais em log
    public void logFinal() {
        String log;

        log = "MEDIA DE TROCAS: " + getMediaTrocasContexto();
        Log.gravarArquivoLog(quantum, log);

        log = "MEDIA DE INSTRUCOES: " + getMediaInstrucoes();
        Log.gravarArquivoLog(quantum, log);

        log = "QUANTUM: " + quantum;
        Log.gravarArquivoLog(quantum, log);
    }
}