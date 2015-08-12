package br.com.furb.tagarela.puzzlegame;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import br.com.engine.game.Game;
import br.com.engine.game.GameCanvas;
import br.com.engine.game.Scene;
import br.com.engine.model.GameObject;
import br.com.engine.system.render.CanvasRenderSystem;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.puzzlegame.component.CanvasRenderBitmapComponent;
import br.com.furb.tagarela.puzzlegame.component.DragComponent;
import br.com.furb.tagarela.puzzlegame.component.DragDropComponent;
import br.com.furb.tagarela.puzzlegame.component.GameBackgroundComponent;
import br.com.furb.tagarela.puzzlegame.controller.PuzzleController;
import br.com.furb.tagarela.puzzlegame.model.Piece;
import br.com.furb.tagarela.puzzlegame.util.ImageUtil;

public class StressTestActivity extends Activity {
	private static String PARAM_PRANCHA = "PARAM_ID_PRANCHA";
	private Prancha prancha;
	private PuzzleController puzzleController;
	
	private void loadParams() {
		Intent intent = getIntent();
		
		if (intent != null) {
			Integer idPrancha = intent.getExtras().getInt(PARAM_PRANCHA);
			
			try {
				this.prancha = getController().getPranchaById(idPrancha);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.loadParams();
		
		long freeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		Log.i("MEMORY", "StressTestActivity: " + freeMemory);
		
		Scene scene = this.configureScene();
		this.configureGame(scene);
		
		freeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		Log.i("MEMORY", "StressTestActivity: " + freeMemory);
	}
	
	private Scene configureScene() {
		Log.d("MAIN", "CONFIG SCENE");
		Scene scene = new Scene();
		
		Display display = getWindowManager().getDefaultDisplay();
		
		Game game = Game.getInstance();
		game.addComponent(new GameBackgroundComponent(getResources().getColor(
				R.color.GreenYellow)));
		
		try {
			List<Piece> pieces = getController().getPieceByPranchaId(prancha.getId());

			int altura = 0;
			int x = 0;
			for (int i = 0; i < 200; i++) {
				Bitmap bmpOrigin = ImageUtil.getBitmapFromByte(pieces.get(0).getOrigin().getImage());
				GameObject origin = new GameObject(x, altura);
				origin.addComponent(new CanvasRenderBitmapComponent(origin, bmpOrigin));
				origin.addComponent(new DragComponent(origin, bmpOrigin));	
				scene.addGameObject(origin);
				
				x += bmpOrigin.getWidth()/4;
				
				if (i != 0 && i % 10 == 0) {
					altura += bmpOrigin.getHeight()/4;
					x = 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return scene;
	}
	
	private void configureGame(Scene scene) {
		Game game = Game.getInstance();
		
		CanvasRenderSystem renderSystem = new CanvasRenderSystem();
		
		GameCanvas canvas = new GameCanvas(this);
		
		renderSystem.setCanvas(canvas);
		
		canvas.setOnTouchListener(game.getOnTouchListener());
		
		game.configureGame(this, scene, renderSystem);
		
		setContentView(canvas);
	}
	
	private PuzzleController getController() {
		if (puzzleController == null) {
			puzzleController = new PuzzleController(this);
		}
		
		return puzzleController;
	}
	
	public static Intent getIntent(Context context, Integer idPrancha) {
		Intent intent = new Intent(context, StressTestActivity.class);
		Bundle extras = new Bundle();
		extras.putInt("PARAM_ID_PRANCHA", idPrancha);
		intent.putExtras(extras);
		
		return intent;
	}
}
