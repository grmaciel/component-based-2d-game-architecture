package br.com.furb.tagarela.puzzlegame;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.puzzlegame.controller.PuzzleController;
import br.com.furb.tagarela.puzzlegame.util.ImageUtil;

public class GameHomeActivity extends Activity {
	private PuzzleController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.puzzle_home);
		
		this.configureGridLayout();
		
		long freeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		Log.i("MEMORY", "GameHomeActivity: " + freeMemory);
	}
	
	private void configureGridLayout() {
		try {
			PuzzleAppAdapter adapter = new PuzzleAppAdapter(
					getPuzzleController().getPranchaPuzzleModule());
			getHomeGrid().setAdapter(adapter);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private GridView getHomeGrid() {
		return (GridView) this.findViewById(R.id.puzzleHomeGrid);
	}
	
	private class PuzzleAppAdapter extends BaseAdapter {
		private List<Prancha> games = Collections.emptyList();
		
		public PuzzleAppAdapter(List<Prancha> games) {
			this.games = games;
		}

		@Override
		public int getCount() {
			return games.size();
		}

		@Override
		public Prancha getItem(int position) {
			return games.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) GameHomeActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.puzzle_home_item, parent, false);
				
				final Prancha prancha = this.getItem(position);
				
				ImageView imgPuzzle = (ImageView) convertView.findViewById(R.id.puzzleHomeItemImg);
				TextView txtPuzzle = (TextView) convertView.findViewById(R.id.puzzleHomeItemTxt);
				
				imgPuzzle.setImageBitmap(ImageUtil.getBitmapFromByte(prancha
						.getIcon().getImage()));
				
				txtPuzzle.setText(prancha.getDescription());
				
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						actionOnPuzzleClicked(prancha.getId());
					}
				});
			}
			
			return convertView;
		}
	}
	
	private void actionOnPuzzleClicked(Integer idPrancha) {
		Intent intent = GameMenuActivity.getIntent(this, idPrancha);
		this.startActivity(intent);
	}
	
	private PuzzleController getPuzzleController() {
		if (controller == null){
			controller = new PuzzleController(this);
		}
		
		return controller;
	}
}
