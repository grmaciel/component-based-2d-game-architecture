package br.com.furb.tagarela.puzzlegame.database;

import android.content.Context;
import br.com.furb.tagarela.AbstractTagarelaDBHelper;
import br.com.furb.tagarela.puzzlegame.model.GameConfig;
import br.com.furb.tagarela.puzzlegame.model.Piece;

public class PuzzleDBHelper extends AbstractTagarelaDBHelper {
	public PuzzleDBHelper(Context context) {
		super(context);
		
		this.createCustomTable(GameConfig.class);
		this.createCustomTable(Piece.class);
	}
}
