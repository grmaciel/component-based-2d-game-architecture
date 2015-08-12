package br.com.furb.tagarela.puzzlegame.controller;

import java.sql.SQLException;

import android.content.Context;
import br.com.furb.tagarela.persistence.controller.AbstractController;
import br.com.furb.tagarela.puzzlegame.model.GameConfig;
import br.com.furb.tagarela.puzzlegame.model.Piece;

import com.j256.ormlite.dao.Dao;

public abstract class AbstractPuzzleController extends AbstractController {
	private Dao<GameConfig, Integer> gameConfigDao;
	private Dao<Piece, Integer> pieceDao;

	public AbstractPuzzleController(Context context) {
		super(context);
		this.createCustomTables();
		
	}
	
	private void createCustomTables() {
		getPersistenceManager().createCustomTable(GameConfig.class);
		getPersistenceManager().createCustomTable(Piece.class);		
	}

	public Dao<GameConfig, Integer> getGameConfigDao() throws SQLException {
		if (this.gameConfigDao == null) {
			this.gameConfigDao = getPersistenceManager().getDao(GameConfig.class);
		}
		
		return gameConfigDao;
	}
	
	public Dao<Piece, Integer> getPieceDao() throws SQLException {
		if (this.pieceDao == null) {
			this.pieceDao = getPersistenceManager().getDao(Piece.class);
		}
		
		return pieceDao;
	}
}
