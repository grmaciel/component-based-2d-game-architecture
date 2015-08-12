package br.com.furb.tagarela.puzzlegame.model;

import br.com.furb.tagarela.persistence.model.AbstractPojo;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.persistence.model.Symbol;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "puzzle_piece")
public class Piece extends AbstractPojo {
	private static final long serialVersionUID = -6273184054306847842L;

	@DatabaseField(columnName = "id_symbol_origin", foreign = true, foreignAutoRefresh = true, canBeNull = false)
	private Symbol origin;
	
	@DatabaseField(columnName = "id_symbol_destiny", foreign = true, foreignAutoRefresh = true, canBeNull = false)
	private Symbol destiny;
	
	@DatabaseField(columnName = "id_plans", foreign = true, canBeNull = false)
	private Prancha prancha;
	
	@DatabaseField(columnName = "destiny_pos_x", canBeNull = false)
	private Float destinyPosX;

	@DatabaseField(columnName = "destiny_pos_y", canBeNull = false)
	private Float destinyPosY;
	
	@DatabaseField(columnName = "img_layer", canBeNull = true)
	private Integer layer;
	

	public Symbol getOrigin() {
		return origin;
	}

	public void setOrigin(Symbol origin) {
		this.origin = origin;
	}

	public Symbol getDestiny() {
		return destiny;
	}

	public void setDestiny(Symbol destiny) {
		this.destiny = destiny;
	}

	public Float getDestinyPosX() {
		return destinyPosX;
	}

	public void setDestinyPosX(Float destinyPosX) {
		this.destinyPosX = destinyPosX;
	}

	public Float getDestinyPosY() {
		return destinyPosY;
	}

	public void setDestinyPosY(Float destinyPosY) {
		this.destinyPosY = destinyPosY;
	}

	public Prancha getPrancha() {
		return prancha;
	}

	public void setPrancha(Prancha prancha) {
		this.prancha = prancha;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}
}
