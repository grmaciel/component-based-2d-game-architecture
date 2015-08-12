package br.com.furb.tagarela.persistence.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "private_symbols")
public class Symbol extends AbstractTraceablePojo {

	private static final long serialVersionUID = -583306847171721231L;
	
	@DatabaseField(columnName = "name")
	private String name;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = "image")
	private byte[] image;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = "sound")
	private byte[] sound;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public byte[] getSound() {
		return sound;
	}
	public void setSound(byte[] sound) {
		this.sound = sound;
	}
	
	
	
	
}
