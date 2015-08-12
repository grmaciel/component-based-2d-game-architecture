package br.com.furb.tagarela.puzzlegame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import br.com.engine.game.Game;
import br.com.engine.game.GameCanvas;
import br.com.engine.game.Scene;
import br.com.engine.model.GameObject;
import br.com.engine.system.render.CanvasRenderSystem;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.puzzlegame.component.AttractComponent;
import br.com.furb.tagarela.puzzlegame.component.ButtonComponent;
import br.com.furb.tagarela.puzzlegame.component.CanvasRenderBitmapComponent;
import br.com.furb.tagarela.puzzlegame.component.DragDropComponent;
import br.com.furb.tagarela.puzzlegame.component.GameBackgroundComponent;
import br.com.furb.tagarela.puzzlegame.component.ScrollComponent;
import br.com.furb.tagarela.puzzlegame.component.ScrollObjectHolder;
import br.com.furb.tagarela.puzzlegame.component.SoundPlayComponent;
import br.com.furb.tagarela.puzzlegame.controller.GameConfigController;
import br.com.furb.tagarela.puzzlegame.controller.PuzzleController;
import br.com.furb.tagarela.puzzlegame.model.GameConfig;
import br.com.furb.tagarela.puzzlegame.model.Piece;
import br.com.furb.tagarela.puzzlegame.util.ImageUtil;

public class GameActivity extends Activity {
	private static String PARAM_PRANCHA = "PARAM_ID_PRANCHA";
	
	private GameCanvas canvas;
	private GameConfig gameConfig;
	public static float bBoxReduceRatio = 0.0f;
	public static Boolean destacarDestino;
	private Prancha prancha;
	private PuzzleController puzzleController;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.loadParams();
		this.loadConfig();
		this.createGame();
	}
	
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

	private void loadConfig() {
		try {
			GameConfigController controller = new GameConfigController(this);
			gameConfig = controller.getGameConfigByIdPrancha(prancha.getId());
			if (gameConfig != null) {
				bBoxReduceRatio = gameConfig.getDificuldadeEncaixe() / 100.0f;
				destacarDestino = gameConfig.getDestacarDestino();
			} else {
				destacarDestino = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createGame() {
		Game.getInstance().clearGame();
		Scene scene = this.configureScene();
		this.configureGame(scene);
	}
	
	private Scene configureScene() {
		Log.d("MAIN", "CONFIG SCENE");
		Scene humanBody = new Scene();
		try {
			this.createGameObjectsFromDatabase(humanBody);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.createUIButtons(humanBody);
		
		return humanBody;
	}
	
	private void createGameObjectsFromDatabase(Scene scene) throws SQLException {
		Display display = getWindowManager().getDefaultDisplay();
		createImageBackground(scene, display);
		List<Piece> pieces = getController().getPieceByPranchaId(prancha.getId());
		List<ScrollObjectHolder> scrollChilds = new ArrayList<ScrollObjectHolder>();
		boolean humanBody = isHumanBodyGame();
		int i = 0;

		for (Piece p : pieces) {
			Bitmap bmpDestiny = ImageUtil
					.getBitmapFromByte(p.getDestiny().getImage());
			GameObject destiny = createDestinyGameObject(p, bmpDestiny);
			Bitmap bmpOrigin = ImageUtil
					.getBitmapFromByte(p.getOrigin().getImage());
			GameObject origin = createOriginGameObject(bmpDestiny, 
					destiny, bmpOrigin);
			
			configureHumanBodySound(humanBody, i, origin);
			scene.addGameObject(origin);
			scene.addGameObject(destiny);
			
			Game.getInstance().registerMessageListener(
					destiny.getComponents().get(0));
			scrollChilds.add(new ScrollObjectHolder(origin, 
					bmpOrigin.getWidth(), 
					bmpOrigin.getHeight()));
			i++;
		}
		
		this.configureScroll(scrollChilds);
	}

	private GameObject createOriginGameObject(Bitmap bmpDestiny,
			GameObject destiny, Bitmap bmpOrigin) {
		GameObject origin = new GameObject();
		origin.addComponent(new CanvasRenderBitmapComponent(origin, bmpOrigin));
		origin.addComponent(new DragDropComponent(origin, bmpOrigin, destiny, bmpDestiny));
		
		if (gameConfig != null && gameConfig.getArrastarObjeto() != null && gameConfig.getArrastarObjeto()) {
			origin.addComponent(new AttractComponent(origin, destiny));
		}
		origin.setLayer(1);
		
		return origin;
	}

	private GameObject createDestinyGameObject(Piece p, Bitmap bmpDestiny) {
		GameObject destiny = new GameObject(p.getDestinyPosX().intValue(),
				p.getDestinyPosY().intValue());
		destiny.addComponent(new CanvasRenderBitmapComponent(destiny, bmpDestiny));
		destiny.setLayer(0);
		return destiny;
	}

	private void configureHumanBodySound(boolean humanBody, int i,
			GameObject origin) {
		if (i == 0 && humanBody) {
			origin.addComponent(new SoundPlayComponent(MediaPlayer.create(this, 
					R.raw.pulmao)));
		} else if (i == 2 && humanBody) {
			origin.addComponent(new SoundPlayComponent(MediaPlayer.create(this, 
					R.raw.coracao)));
		}
	}

	private boolean isHumanBodyGame() {
		boolean humanBody = false;
		
		if (prancha.getDescription().equals("Corpo Humano")) {
			humanBody = true;
		}
		return humanBody;
	}

	private void createImageBackground(Scene scene, Display display) {
		Bitmap bodyCounter = ImageUtil.getBitmapFromByte(prancha.getSymbol().getImage());
		GameObject body = new GameObject(display.getWidth()/2 - bodyCounter.getWidth()/2, 20, -1);
		body.addComponent(new CanvasRenderBitmapComponent(body, bodyCounter));
		scene.addGameObject(body);
	}
	
	private void configureScroll(List<ScrollObjectHolder> scrollChilds) {
		Log.d("MAIN", "CONFIG SCROLL");
		Display display = getWindowManager().getDefaultDisplay();
		
		ScrollComponent scroll = new ScrollComponent(scrollChilds, display.getWidth(), display.getHeight());
		
		Game.getInstance().registerMessageListener(scroll);
		
		Game game = Game.getInstance();
		game.addComponent(new GameBackgroundComponent(getResources().getColor(R.color.SlateBlue)));
		game.addComponent(scroll);
	}
	
	
	private void createUIButtons(Scene scene) {
		GameObject restartButton = new GameObject(30, 30);
		Bitmap restart = ImageUtil.getBitmapFromRes(this, R.drawable.restart);
		restartButton.addComponent(new CanvasRenderBitmapComponent(restartButton, restart));
		restartButton.addComponent(new ButtonComponent(restartButton, 
				restart, 
				getOnRestartClickListener()));
		
		scene.addGameObject(restartButton);
	}
	
	private void configureGame(Scene scene) {
		Game game = Game.getInstance();
		
		CanvasRenderSystem renderSystem = new CanvasRenderSystem();
		
		if (this.canvas == null) {
			this.canvas = new GameCanvas(this);
		}
		
		renderSystem.setCanvas(canvas);
		
		canvas.setOnTouchListener(game.getOnTouchListener());
		
		game.configureGame(this, scene, renderSystem);
		
		setContentView(canvas);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	private OnClickListener getOnRestartClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Game.getInstance().clearGame();
				Game.getInstance().addComponent(new GameBackgroundComponent(Color.WHITE));
				
				CanvasRenderSystem renderSystem = new CanvasRenderSystem();
				renderSystem.setCanvas(canvas);
				
				Log.d("MAIN", "PRE START");
				Game.getInstance().configureGame(GameActivity.this, new Scene(), renderSystem);
				Log.d("MAIN", "CREATE");
				createGame();
			}
		};
	}
	
	private PuzzleController getController() {
		if (puzzleController == null) {
			puzzleController = new PuzzleController(this);
		}
		
		return puzzleController;
	}
	
	public static Intent getIntent(Context context, Integer idPrancha) {
		Intent intent = new Intent(context, GameActivity.class);
		Bundle extras = new Bundle();
		extras.putInt("PARAM_ID_PRANCHA", idPrancha);
		intent.putExtras(extras);
		
		return intent;
	}
}
