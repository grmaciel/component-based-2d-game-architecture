package br.com.furb.tagarela.persistence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "plans")
public class Prancha extends AbstractTraceablePojo {

	private static final long serialVersionUID = 1L;

	@DatabaseField(columnName = "name")
	private String name;

	@DatabaseField(columnName = "layout")
	private Integer layout;

	@DatabaseField(columnName = "description")
	private String description;

	@DatabaseField(columnName = "user_id")
	private String idUser;

	@DatabaseField(columnName = "patient_id")
	private String idPatient;

	@DatabaseField(columnName = "id_plano", foreign = true)
	private Plano plano;

	@DatabaseField(columnName = "id_symbol", foreign = true, foreignAutoRefresh = true)
	private Symbol symbol;
	
	@DatabaseField(columnName = "id_icon", foreign = true, foreignAutoRefresh = true)
	private Symbol icon;

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

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Symbol getIcon() {
		return icon;
	}

	public void setIcon(Symbol icon) {
		this.icon = icon;
	}
}