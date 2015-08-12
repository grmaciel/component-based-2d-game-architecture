package br.com.furb.tagarela.puzzlegame.model;

import java.io.Serializable;

import br.com.furb.tagarela.persistence.model.IPojo;
import br.com.furb.tagarela.persistence.model.Prancha;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "game_config")
public class GameConfig implements IPojo {
	
	private static final long serialVersionUID = -2966622255578036462L;

	@DatabaseField(generatedId = true)
	private Integer id;
	
	@DatabaseField(columnName = "id_prancha", canBeNull = false, foreign = true)
	private Prancha prancha;
	
	@DatabaseField(columnName = "flg_destacar_destino")
	private Boolean destacarDestino;
	
	@DatabaseField(columnName = "flg_limite_erros")
	private Boolean limiteErros;
	
	@DatabaseField(columnName = "flg_arrastar")
	private Boolean arrastarObjeto;
	
	public Boolean getArrastarObjeto() {
		return arrastarObjeto;
	}

	public void setArrastarObjeto(Boolean arrastarObjeto) {
		this.arrastarObjeto = arrastarObjeto;
	}

	@DatabaseField(columnName = "val_dificuldade_encaixe")
	private int dificuldadeEncaixe;

	@Override
	public Serializable getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getDestacarDestino() {
		return destacarDestino;
	}

	public void setDestacarDestino(Boolean destacarDestino) {
		this.destacarDestino = destacarDestino;
	}

	public Boolean getLimiteErros() {
		return limiteErros;
	}

	public void setLimiteErros(Boolean limiteErros) {
		this.limiteErros = limiteErros;
	}

	public int getDificuldadeEncaixe() {
		return dificuldadeEncaixe;
	}

	public void setDificuldadeEncaixe(int dificuldadeEncaixe) {
		this.dificuldadeEncaixe = dificuldadeEncaixe;
	}

	public Prancha getPrancha() {
		return prancha;
	}

	public void setPrancha(Prancha prancha) {
		this.prancha = prancha;
	}
}
