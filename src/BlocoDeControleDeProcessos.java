import java.util.*;

/**
 * Classe responsável por gerenciar a lista de processos que estão na tabela de processos.
 * O BCP (Bloco de Controle de Processos) armazena as informações de um processo e será utilizado
 * em uma tabela de processos representada por um ArrayList.
 */

public class BlocoDeControleDeProcessos implements Comparable<BlocoDeControleDeProcessos>{

    // Atributos principais do bloco de controle de processos
	private int contadorPrograma, tempoEspera, nomeArquivo;
	private String nomePrograma, estadoProcesso;
	private int[] registradores = new int[2]; // Registradores X e Y
	private List<String> instrucoesDoPrograma = new ArrayList<>();
	private int prioridade;
	private int creditos;
	
    // Construtor padrão
	public BlocoDeControleDeProcessos(){}; 

    // Construtor com lista de instruções e nome de arquivo
	public BlocoDeControleDeProcessos(List<String> instrucoesDoPrograma, int nomeArquivo){
		this.instrucoesDoPrograma = instrucoesDoPrograma;
		this.nomePrograma = instrucoesDoPrograma.remove(0); // Primeiro item é o nome do programa			
		this.estadoProcesso = "Pronto"; // Estado inicial do processo
		this.nomeArquivo = nomeArquivo;
	}

    // Método para imprimir lista de instruções do programa
	public void imprimeListaInstrucoes(){ 
		for(String x : instrucoesDoPrograma){ 
			System.out.println(x);
		}
	}

	// Método para comparação de blocos baseado no nome do arquivo
	@Override
	public int compareTo(BlocoDeControleDeProcessos bloco){
		return nomeArquivo - bloco.nomeArquivo;
	}

    // Representação em string do BCP
	@Override
	public String toString(){
		return nomePrograma + " - PC: [" + contadorPrograma + "/" + instrucoesDoPrograma.size() + "] - Tempo Espera: " + tempoEspera + " - Estado: " + estadoProcesso + "\n";
	}

	// Getters e setters

	public int getRegistradorX(){
		return this.registradores[0];
	}

	public int getRegistradorY(){
		return this.registradores[1];
	}

	public void setRegistradorX(int x){
		this.registradores[0] = x;
	}

	public void setRegistradorY(int y){
		this.registradores[1] = y;
	}
			
	public List<String> getInstrucoesDoPrograma(){
		return this.instrucoesDoPrograma;
	}

	public int getContador(){
		return this.contadorPrograma;
	}

	public void setContador(int valor){ 
		this.contadorPrograma = valor;
	}

	public String getNomePrograma(){
		return this.nomePrograma;
	}
	
	public int getTempoEspera(){
		return this.tempoEspera;
	}
	
	public void setTempoEspera(int tempo){ 
		this.tempoEspera += tempo;
	}

	public void decrementaTempoEspera(){
		this.tempoEspera--;
	}

	public void incrementaContador(){ 
		this.contadorPrograma++;
	}

	public void setEstadoProcesso(String novoEstado){
		estadoProcesso = novoEstado;
	}

	public int getNomeArquivo(){
		return nomeArquivo;
	}

	public int getPrioridade() {
		return prioridade;
	}
	
	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
	
	public int getCreditos() {
		return creditos;
	}
	
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	
	public void decrementaCredito() {
		this.creditos--;
	}
    

}