package br.com.furb.tagarela.puzzlegame.controller;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import br.com.furb.tagarela.enumeration.EnumModuleDomain;
import br.com.furb.tagarela.persistence.model.Module;
import br.com.furb.tagarela.persistence.model.Plano;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.puzzlegame.model.Piece;

import com.j256.ormlite.stmt.QueryBuilder;

public class PuzzleController extends AbstractPuzzleController {

	public PuzzleController(Context context) {
		super(context);
	}
	
	public void save(Piece piece) throws SQLException {
		this.save(piece, getPieceDao());
	}
	
	public Module getModuleByDomain(EnumModuleDomain module) throws SQLException {
		QueryBuilder<Module, Integer> moduleQb = getModuleDao().queryBuilder();
		moduleQb.where().eq("dominio", module.getDomain());
		
		return moduleQb.queryForFirst();
	}
	
	public Plano getPlanoByModule(EnumModuleDomain module) throws SQLException {
		QueryBuilder<Module, Integer> moduleQb = getModuleDao().queryBuilder();
		moduleQb.where().eq("dominio", module.getDomain());
		QueryBuilder<Plano, Integer> planoQb = getPlanoDao().queryBuilder();
		planoQb.join(moduleQb);
		
		return planoQb.queryForFirst();
	}
	
	public List<Prancha> getPranchaPuzzleModule() throws SQLException {
		QueryBuilder<Module, Integer> moduleQb = getModuleDao().queryBuilder();
		moduleQb.where().eq("dominio", EnumModuleDomain.PUZZLE_GAME.getDomain());
		QueryBuilder<Plano, Integer> planoQb = getPlanoDao().queryBuilder();
		planoQb.join(moduleQb);
		QueryBuilder<Prancha, Integer> pranchaQb = getPranchaDao().queryBuilder();
		pranchaQb.join(planoQb);
		
		return pranchaQb.query();
	}
	
	public List<Piece> getPieceByPranchaId(Integer idPrancha) throws SQLException {
		QueryBuilder<Prancha, Integer> pranchaQb = getPranchaDao().queryBuilder();
		pranchaQb.where().eq("id", idPrancha);
		QueryBuilder<Piece, Integer> pieceQb = getPieceDao().queryBuilder();
		pieceQb.join(pranchaQb);
		
		return pieceQb.query();
	}
}
