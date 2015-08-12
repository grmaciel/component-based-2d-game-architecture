package br.com.furb.tagarela;

import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public abstract class AbstractDBHelper extends OrmLiteSqliteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;

	public AbstractDBHelper(String databaseName, Context context) {
		super(context, databaseName, null, DATABASE_VERSION);
	}
}
