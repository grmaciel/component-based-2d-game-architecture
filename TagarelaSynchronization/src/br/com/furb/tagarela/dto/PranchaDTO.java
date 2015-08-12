package br.com.furb.tagarela.dto;

import br.com.furb.tagarela.persistence.model.Prancha;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PranchaDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6166508082795535698L;
	
	@Expose
	private String name;
	
	@Expose
	private Integer layout;
	
	@Expose
	private String description;
	
	@Expose
	@SerializedName("user_id")
	private String idUser;
	
	@Expose
	@SerializedName("patient_id")
	private String idPatient;
	
	@Override
	public String getEntityUrl() {
		return "http://still-scrubland-6051.herokuapp.com/plans";
	}

	@Override
	public Class<?> getPojoReference() {
		return Prancha.class;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdPatient() {
		return idPatient;
	}

	public void setIdPatient(String idPatient) {
		this.idPatient = idPatient;
	}
}
