public class Instrucoes {

    // Atributos principais para controle de instruções
    BlocoDeControleDeProcessos bcp;  // Bloco de Controle de Processos associado
    int quantum;  // Valor do quantum
    int cont;  // Contador de chamadas de tempo
    private int tempo, instrucoesExecutadas;  // Tempo de execução e quantidade de instruções executadas

    // Construtor que recebe o BCP e o quantum
    public Instrucoes(BlocoDeControleDeProcessos bcp, int quantum) {
        this.bcp = bcp;
        this.quantum = quantum;
    }

    // Método que processa as instruções do programa
    public void processaInstrucoes() throws Exception {
        System.out.println("\n-------->>\n\t EXEC. PROGRAMA: " + bcp.getNomePrograma());

        instrucoesExecutadas = 0;  // Zera o contador de instruções executadas

        // Itera sobre o número de ciclos definidos pelo quantum
        for (int i = 0; i < quantum; i++) {

            // Recupera a instrução atual a partir do contador de programa
            String instrucao = bcp.getInstrucoesDoPrograma().get(bcp.getContador());

            System.out.println("\t\tINSTRUCAO EXECUTADA: " + instrucao);
            bcp.incrementaContador();  // Incrementa o contador de instruções do programa
            instrucoesExecutadas++;  // Incrementa o número de instruções executadas

            // Verifica se a instrução afeta o registrador X
            if (instrucao.startsWith("X"))
                this.instrucaoX(Integer.parseInt(instrucao.substring(instrucao.indexOf("=") + 1)));

            // Verifica se a instrução afeta o registrador Y
            if (instrucao.startsWith("Y"))
                this.instrucaoY(Integer.parseInt(instrucao.substring(instrucao.indexOf("=") + 1)));

            // Trata instruções de entrada e saída (E/S)
            if (instrucao.equals("E/S")) {
                if (cont == 0) {
                    this.tempo = i + 1;  // Armazena o tempo da primeira ocorrência de E/S
                    this.cont++;
                }
                chamadaAoSO();  // Chama o Sistema Operacional para E/S
            }

            // Instrução de comunicação (COM)
            if (instrucao.equals("COM")) 
                instrucaoCOM();

            // Instrução de saída do programa
            if (instrucao.equals("SAIDA")) {
                if (cont == 0) {
                    this.tempo = i + 1;  // Armazena o tempo da primeira ocorrência de saída
                    this.cont++;
                    System.out.println("\nTERMINOU ->>>> " + "NOME: " + bcp.getNomePrograma() + " TEMPO: " + (bcp.getTempoEspera() + this.tempo) + "\n");
                }
                throw new Exception("SAIDA");  // Lança exceção indicando o término do processo
            }
        }

        // Se não houve chamadas de E/S ou SAIDA, define o tempo de execução completo
        if (cont == 0) 
            this.tempo = quantum;
        
        this.cont = 0;  // Reseta o contador para a próxima execução
    }

    // Método que processa instruções que afetam o registrador X
    public void instrucaoX(int x) {
        bcp.setRegistradorX(x);  // Define o valor de X no BCP
    }

    // Método que processa instruções que afetam o registrador Y
    public void instrucaoY(int y) {
        bcp.setRegistradorY(y);  // Define o valor de Y no BCP
    }

    // Método que chama o Sistema Operacional para E/S, bloqueando o processo
    public void chamadaAoSO() throws Exception {
        bcp.setEstadoProcesso("Bloqueado");  // Define o estado do processo como bloqueado
        throw new Exception("E/S");  // Lança exceção indicando uma operação de E/S
    }

    // Método vazio que pode ser utilizado para instruções de comunicação
    public void instrucaoCOM() {}

    // Verifica se o programa terminou suas instruções
    public boolean terminou() {
        return bcp.getContador() >= bcp.getInstrucoesDoPrograma().size();  // Verifica se o contador chegou ao fim
    }

    // Retorna o tempo de execução do processo
    public int getTempo() {
        return this.tempo;
    }

    // Retorna o número de instruções executadas
    public int getInstrucoesExecutadas() {
        return instrucoesExecutadas;
    }
}