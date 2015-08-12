package br.com.furb.tagarela.dto;

import br.com.furb.tagarela.persistence.model.Plano;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanoDTO extends AbstractDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5047616052315714242L;
	@Expose
	private String name;

	@Expose
	@SerializedName("user_id")
	private String idUser;

	@Expose
	@SerializedName("group_plan_type")
	private String tipoPlano;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getEntityUrl() {
		return "http://still-scrubland-6051.herokuapp.com/group_plans/index.json";
	}

	@Override
	public Class<?> getPojoReference() {
		return Plano.class;
	}

	public String getTipoPlano() {
		return tipoPlano;
	}

	public void setTipoPlano(String tipoPlano) {
		this.tipoPlano = tipoPlano;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
}