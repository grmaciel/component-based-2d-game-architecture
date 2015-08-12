package br.com.furb.tagarela;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.furb.tagarela.persistence.model.Category;
import br.com.furb.tagarela.persistence.model.IPojo;
import br.com.furb.tagarela.persistence.model.Module;
import br.com.furb.tagarela.persistence.model.PlanGroup;
import br.com.furb.tagarela.persistence.model.Plano;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.persistence.model.Symbol;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public abstract class AbstractTagarelaDBHelper extends AbstractDBHelper {
	private static final String DATABASE_NAME = "tagarela.db";
	
	private Dao<Category, Integer> categoryDao;
	private Dao<Plano, Integer> planDao;
	private Dao<Prancha, Integer> pranchaDao;
	private Dao<PlanGroup, Integer> planGroupDao;
	private Dao<Symbol, Integer> symbolDao;
	private Dao<Module, Integer> moduleDao;

	public AbstractTagarelaDBHelper(Context context) {
		super(DATABASE_NAME, context);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource conn) {
		try {
		Log.i(AbstractDBHelper.class.getName(), "onCreate Tagarela Core Tables");
			TableUtils.createTable(conn, Category.class);
			TableUtils.createTable(conn, Plano.class);
			TableUtils.createTable(conn, Prancha.class);
			TableUtils.createTable(conn, Symbol.class);
			TableUtils.createTable(conn, Module.class);
		} catch (SQLException e) {
			Log.e(AbstractDBHelper.class.getName(), "ERROR CREATING CORE TABLES " + e.getMessage());
		}
	}
	
	public <T extends IPojo> void createCustomTable(Class<T> pojo) {
		try {
			TableUtils.createTableIfNotExists(getConnectionSource(), pojo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
	}
	
	public Dao<Category, Integer> getCategoryDao() throws SQLException {
		if (this.categoryDao == null) {
			this.categoryDao = this.getDao(Category.class);
		}
		
		return categoryDao;
	}
	
	public Dao<Plano, Integer> getPlanoDao() throws SQLException {
		if (this.planDao == null) {
			this.planDao = this.getDao(Plano.class);
		}
		
		return planDao;
	}
	
	public Dao<Prancha, Integer> getPranchaDao() throws SQLException {
		if (this.pranchaDao == null) {
			this.pranchaDao = this.getDao(Prancha.class);
		}
		
		return pranchaDao;
	}
	
	public Dao<PlanGroup, Integer> getPlanGroupDao() throws SQLException {
		if (this.planGroupDao == null) {
			this.planGroupDao = this.getDao(PlanGroup.class);
		}
		
		return planGroupDao;
	}
	
	public Dao<Symbol, Integer> getSymbolDao() throws SQLException {
		if (this.symbolDao == null) {
			this.symbolDao = this.getDao(Symbol.class);
		}
		
		return symbolDao;
	}
	
	public Dao<Module, Integer> getModuleDao() throws SQLException {
		if (this.moduleDao == null) {
			this.moduleDao = this.getDao(Module.class);
		}
		
		return moduleDao;
	}
}
