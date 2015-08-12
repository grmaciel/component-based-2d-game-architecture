package br.com.furb.tagarela.persistence.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "module")
public class Module extends AbstractPojo {
	private static final long serialVersionUID = 3083612344882642325L;
	
	@DatabaseField(columnName = "nome")
	private String nome;
	@DatabaseField(columnName = "dominio")
	private String dominio;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
}
