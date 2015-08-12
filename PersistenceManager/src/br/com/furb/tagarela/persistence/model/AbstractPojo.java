package br.com.furb.tagarela.persistence.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractPojo implements IPojo {
	private static final long serialVersionUID = 2488023747329758567L;
	
	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
	protected Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
