package it.unimib.bdf.assignment3;

public class Alimento {
	private String nome;
	
	public Alimento(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the nome
	 */
	private String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return "Aliemento [nome=" + nome + "]";
	}
	
	

}
