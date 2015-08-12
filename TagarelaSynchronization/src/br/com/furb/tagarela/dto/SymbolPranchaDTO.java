package br.com.furb.tagarela.dto;

import com.google.gson.annotations.SerializedName;
/*
 * Workaround pra fazer a primeira busca dos symbosl pra trazer os ids
 * mantendo o padrão da url na classe
 */
public class SymbolPranchaDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6595886559957593853L;
	
	@SerializedName("plans_id")
	private String idPrancha;
	
	@SerializedName("private_symbols_id")
	private String idSymbol;
	

	@Override
	public String getEntityUrl() {
		return "http://still-scrubland-6051.herokuapp.com/symbol_plans";
	}

	@Override
	public Class<?> getPojoReference() {
		return null;
	}

	public String getIdPrancha() {
		return idPrancha;
	}

	public void setIdPrancha(String idPrancha) {
		this.idPrancha = idPrancha;
	}

	public String getIdSymbol() {
		return idSymbol;
	}

	public void setIdSymbol(String idSymbol) {
		this.idSymbol = idSymbol;
	}
}
