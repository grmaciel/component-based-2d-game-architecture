package br.com.furb.tagarela.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanoPranchaDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;
	
	@Expose
	@SerializedName("plan_id")
	private String idPrancha;
	
	@Expose
	@SerializedName("group_id")
	private String idPlano;

	@Override
	public String getEntityUrl() {
		return "http://still-scrubland-6051.herokuapp.com/group_plan_relationships/index.json";
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

	public String getIdPlano() {
		return idPlano;
	}

	public void setIdPlano(String idPlano) {
		this.idPlano = idPlano;
	}
}
