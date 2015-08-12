package br.com.furb.tagarela.dto;

import br.com.furb.tagarela.persistence.model.Symbol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SymbolDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6595886559957593853L;
	
	@Expose
	private String name;
	
	@SerializedName("image_representation")
	private String imageStringRepresentation;
	
	@Override
	public String getEntityUrl() {
		return "http://still-scrubland-6051.herokuapp.com/private_symbols/find_symbols_with_ids.json";
	}

	@Override
	public Class<?> getPojoReference() {
		return Symbol.class;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageStringRepresentation() {
		return imageStringRepresentation;
	}

	public void setImageStringRepresentation(String imageStringRepresentation) {
		this.imageStringRepresentation = imageStringRepresentation;
	}
}
