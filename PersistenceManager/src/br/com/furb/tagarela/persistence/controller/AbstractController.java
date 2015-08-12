package br.com.furb.tagarela.persistence.controller;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import br.com.furb.tagarela.AbstractTagarelaDBHelper;
import br.com.furb.tagarela.TagarelaDBHelper;
import br.com.furb.tagarela.persistence.model.Category;
import br.com.furb.tagarela.persistence.model.IPojo;
import br.com.furb.tagarela.persistence.model.Module;
import br.com.furb.tagarela.persistence.model.PlanGroup;
import br.com.furb.tagarela.persistence.model.Plano;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.persistence.model.Symbol;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

public abstract class AbstractController {
	private static String LOG_TAG = "PERSIST";
	private AbstractTagarelaDBHelper helper;
	protected Context context;
	
	public AbstractController(Context context) {
		this.context = context;
	}
	
	protected <T extends IPojo, ID> void save(T model, Dao<T, ID> dao) 
			throws SQLException {
		if (model.getId() == null ||
				dao.queryForId((ID) model.getId()) == null) {
			Log.d(LOG_TAG, "INSERTING : " + model.getClass().getSimpleName());
			dao.create(model);
		} else {
			Log.d(LOG_TAG, "UPDATING : " + model.getClass()
					.getSimpleName() + " ID: " + model.getId());
			dao.update(model);
		}
	}
	
	protected AbstractTagarelaDBHelper getPersistenceManager() {
		if (helper == null) {
			Log.d(LOG_TAG, "INSTANCIANDO DB HELPER");
			helper = new TagarelaDBHelper(context);
		}
		return helper;
	}
	
	public <T extends IPojo, ID> Where getDaoWhereBuilder(Dao<T, ID> dao) {
		return dao.queryBuilder().where();
	}
	
	public <T extends IPojo, ID> T getPojoById(ID id, Dao<T, ID> dao) 
			throws SQLException {
		return dao.queryForId(id);
	}
	
	public Plano getPlanoById(Integer id) throws SQLException {
		return this.getPojoById(id, getPlanoDao());
	}
	
	public Prancha getPranchaById(Integer id) throws SQLException {
		return this.getPojoById(id, getPranchaDao());
	}
	
	public Symbol getSymbolById(Integer id) throws SQLException {
		return this.getPojoById(id, getSymbolDao());
	}
	
	public List<Plano> getPlanoList() throws SQLException {
		return this.getPlanoDao().queryForAll();
	}
	
	public List<Prancha> getPranchaList() throws SQLException {
		return this.getPranchaDao().queryForAll();
	}
	
	protected Dao<Category, Integer> getCategoryDao() throws SQLException {
		return getPersistenceManager().getCategoryDao();
	}
	
	protected Dao<Plano, Integer> getPlanoDao() throws SQLException {
		return getPersistenceManager().getPlanoDao();
	}
	
	protected Dao<Prancha, Integer> getPranchaDao() throws SQLException {
		return getPersistenceManager().getPranchaDao();
	}
	
	protected Dao<PlanGroup, Integer> getPlanGroupDao() throws SQLException {
		return getPersistenceManager().getPlanGroupDao();
	}
	
	protected Dao<Symbol, Integer> getSymbolDao() throws SQLException {
		return getPersistenceManager().getSymbolDao();
	}
	
	protected Dao<Module, Integer> getModuleDao() throws SQLException {
		return getPersistenceManager().getModuleDao();
	}
	
	public void save(Plano plano) throws SQLException {
		this.save(plano, getPlanoDao());
	}
	
	public void save(Prancha prancha) throws SQLException {
		this.save(prancha, getPranchaDao());
	}
	
	public void save(Symbol symbol) throws SQLException {
		this.save(symbol, getSymbolDao());
	}
	
	public void save(Module module) throws SQLException {
		this.save(module, getModuleDao());
	}
}
