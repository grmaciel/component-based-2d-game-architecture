package br.com.furb.tagarela.persistence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "group_plan")
public class Plano extends AbstractTraceablePojo {

	private static final long serialVersionUID = -7152222212213038047L;
	
	@DatabaseField(columnName = "name")
	private String name;
	@DatabaseField(columnName = "user_id")
	private String idUser;
	@DatabaseField(columnName = "tipo_plano")
	private String tipoPlano;
	@DatabaseField(columnName = "id_module", foreign = true, canBeNull = true)
	private Module modulo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getTipoPlano() {
		return tipoPlano;
	}

	public void setTipoPlano(String tipoPlano) {
		this.tipoPlano = tipoPlano;
	}

	public Module getModulo() {
		return modulo;
	}

	public void setModulo(Module modulo) {
		this.modulo = modulo;
	}
}