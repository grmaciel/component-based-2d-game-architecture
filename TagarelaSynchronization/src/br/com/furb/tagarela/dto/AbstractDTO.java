package br.com.furb.tagarela.dto;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class AbstractDTO implements IDTO {

	private static final long serialVersionUID = 2010305334647033901L;

	@Expose
	protected Integer id;
	
	@Expose
	@SerializedName("created_at")
	protected Date createdAt;

	@Expose
	@SerializedName("updated_at")
	protected Date updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
