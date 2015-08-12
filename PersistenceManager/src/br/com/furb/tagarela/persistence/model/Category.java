package br.com.furb.tagarela.persistence.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category {
	@DatabaseField(id = true)
	private Integer id;
	
	@DatabaseField(columnName = "name")
	private String name;
	
	@DatabaseField(columnName = "created_by")
	private Integer createdBy;
	
	@DatabaseField(columnName = "created_at")
	private Date creationDate;
	
	@DatabaseField(columnName = "updated_at")
	private Date updateDate;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
