package br.com.furb.tagarela.puzzlegame.controller;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import br.com.furb.tagarela.puzzlegame.model.GameConfig;

import com.j256.ormlite.stmt.Where;

public class GameConfigController extends AbstractPuzzleController {
	
	public GameConfigController(Context context) {
		super(context);
	}
	
	public void save(GameConfig config) throws SQLException {
		this.save(config, getGameConfigDao());
	}
	
	public List<GameConfig> getGameConfigList() throws SQLException {
		return this.getGameConfigDao().queryForAll();
	}
	
	@SuppressWarnings("rawtypes")
	public GameConfig getGameConfigByIdPrancha(Integer idPrancha) throws SQLException {
		Where where = getDaoWhereBuilder(getGameConfigDao());
		where.eq("id_prancha", idPrancha);
		GameConfig gameConfig = (GameConfig) where.queryForFirst();
		
		return gameConfig;
	}
}