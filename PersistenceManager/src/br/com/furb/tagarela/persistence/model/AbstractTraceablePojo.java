package br.com.furb.tagarela.persistence.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractTraceablePojo extends AbstractPojo {

	private static final long serialVersionUID = -6364738954673192366L;
	
	@DatabaseField(columnName = "created_at")
	protected Date createdAt;

	@DatabaseField(columnName = "updated_at")
	protected Date updatedAt;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
