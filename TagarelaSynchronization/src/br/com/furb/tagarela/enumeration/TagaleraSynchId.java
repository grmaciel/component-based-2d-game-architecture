package br.com.furb.tagarela.enumeration;

public enum TagaleraSynchId {
	IMPORTACAO_PLANO_PRANCHA(0);
	
	private int id;

	private TagaleraSynchId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
