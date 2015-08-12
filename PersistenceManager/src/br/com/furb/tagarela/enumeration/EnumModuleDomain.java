package br.com.furb.tagarela.enumeration;

public enum EnumModuleDomain {
	PUZZLE_GAME("PUZZLE_GAME");
	
	String domain;
	
	private EnumModuleDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
