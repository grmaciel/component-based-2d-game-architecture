package br.com.furb.tagarela.activity;

import java.sql.SQLException;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import br.com.engine.model.GameObject;
import br.com.furb.tagarela.R;
import br.com.furb.tagarela.activity.controller.TagarelaController;
import br.com.furb.tagarela.controller.TagarelaSynchronizationController;
import br.com.furb.tagarela.enumeration.EnumModuleDomain;
import br.com.furb.tagarela.listener.IOnSynchronizationError;
import br.com.furb.tagarela.listener.IOnSynchronizationFinish;
import br.com.furb.tagarela.persistence.model.Module;
import br.com.furb.tagarela.persistence.model.Plano;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.persistence.model.Symbol;
import br.com.furb.tagarela.puzzlegame.GameHomeActivity;
import br.com.furb.tagarela.puzzlegame.GameMenuActivity;
import br.com.furb.tagarela.puzzlegame.StressTestActivity;
import br.com.furb.tagarela.puzzlegame.component.CanvasRenderBitmapComponent;
import br.com.furb.tagarela.puzzlegame.component.DragDropComponent;
import br.com.furb.tagarela.puzzlegame.controller.PuzzleController;
import br.com.furb.tagarela.puzzlegame.model.Piece;
import br.com.furb.tagarela.puzzlegame.util.ImageUtil;

public class MainActivity extends ActionBarActivity {
	private TagarelaSynchronizationController synchController;
	private TagarelaController controller = new TagarelaController(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn = (Button) this.findViewById(R.id.btnTagarela);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
					Intent intent = new Intent(MainActivity.this, GameHomeActivity.class);
					startActivity(intent);
			}
		});
		
		synchController = new TagarelaSynchronizationController(this, controller, new IOnSynchronizationFinish() {
			
			@Override
			public void onSynchronizationFinish() {
						Toast.makeText(MainActivity.this,
								"Importa��o finalizada com sucesso.",
								Toast.LENGTH_LONG).show();
			}
		}, new IOnSynchronizationError() {
			@Override
			public void onSynchronizationError(Exception e) {
				Log.d("SYNC", "ERROR: " + Log.getStackTraceString(e));
			}
		});
		
		Button planoSimbolo = (Button) this.findViewById(R.id.btnPrint);
		planoSimbolo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, PlanoSimboloViewerActivity.class);
				startActivity(intent);
			}
		});
		
		long freeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		Log.i("MEMORY", "MainActivity: " + freeMemory);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		
		switch (id) {
		case R.id.action_settings:
			try {
				synchController.importarNucleoTagarela();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		case R.id.menuImportarPuzzle:
			try {
				simulatePuzzleServerImport();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void simulatePuzzleServerImport() throws SQLException {
		Date current = new Date();
		PuzzleController controller = new PuzzleController(this);
		
		if (controller.getModuleByDomain(EnumModuleDomain.PUZZLE_GAME) != null) {
			Log.d("", "Módulo já criado.");
			return;
		}
		this.createPuzzleHumanBody(controller, current);
		this.createPuzzlePalavras(controller, current);
		Toast.makeText(this, "Jogo de encaixe criado com sucesso.",
				Toast.LENGTH_LONG).show();
	}

	private void createPuzzleHumanBody(PuzzleController controller, Date current) throws SQLException {
		Module module = new Module();
		module.setNome("Puzzle Module");
		module.setDominio(EnumModuleDomain.PUZZLE_GAME.getDomain());
		controller.save(module);
		
		Plano planoPuzzle = new Plano();
		planoPuzzle.setId(1500);
		planoPuzzle.setName("Plano Puzzle");
		planoPuzzle.setIdUser("GILSON_TCC");
		planoPuzzle.setUpdatedAt(current);
		planoPuzzle.setCreatedAt(current);
		planoPuzzle.setModulo(module);
		controller.save(planoPuzzle);
		
		Symbol background = new Symbol();
		background.setName("Body Background");
		background.setCreatedAt(current);
		background.setUpdatedAt(current);
		background.setImage(ImageUtil.getByteArrayFromBitmap(ImageUtil
				.getBitmapFromRes(this, R.drawable.body_contour)));
		controller.save(background);
		
		Symbol icon = new Symbol();
		icon.setName("Human Body Icon");
		icon.setCreatedAt(current);
		icon.setUpdatedAt(current);
		icon.setImage(ImageUtil.getByteArrayFromBitmap(ImageUtil
				.getBitmapFromRes(this, R.drawable.human_body_icon)));
		controller.save(icon);
		
		Prancha prancha = new Prancha();
		prancha.setDescription("Corpo Humano");
		prancha.setIdUser("GILSON_TCC");
		prancha.setUpdatedAt(current);
		prancha.setCreatedAt(current);
		prancha.setSymbol(background);
		prancha.setPlano(planoPuzzle);
		prancha.setIcon(icon);
		controller.save(prancha);
		
		Display display = getWindowManager().getDefaultDisplay();
		DragDropObjHolder lungsHolder = this.createDragDropGameObjects(display,0, 160, R.drawable.pulmoes);
		DragDropObjHolder brainHolder = this.createDragDropGameObjects(display, 0, 30, R.drawable.cerebro);
		DragDropObjHolder heartHolder = this.createDragDropGameObjects(display, 0, 220, R.drawable.coracao);
		DragDropObjHolder liverHolder = this.createDragDropGameObjects(display, -30, 350, R.drawable.figado);
		DragDropObjHolder kidneyHolder = this.createDragDropGameObjects(display, 0, 450, R.drawable.rins);
		DragDropObjHolder largeIntestinHolder = this.createDragDropGameObjects(display, 0, 400, R.drawable.intestino_grosso);
		DragDropObjHolder stomach = this.createDragDropGameObjects(display, 30, 330, R.drawable.estomago);
		
		this.createPiece(prancha, lungsHolder, controller);
		this.createPiece(prancha, brainHolder, controller);
		this.createPiece(prancha, heartHolder, controller);
		this.createPiece(prancha, liverHolder, controller);
		this.createPiece(prancha, kidneyHolder, controller);
		this.createPiece(prancha, largeIntestinHolder, controller);
		this.createPiece(prancha, stomach, controller);
	}

	private void createPuzzlePalavras(PuzzleController controller, Date current) 
			throws SQLException {
		Symbol background = new Symbol();
		background.setName("Bola Background");
		background.setCreatedAt(current);
		background.setUpdatedAt(current);
		background.setImage(ImageUtil.getByteArrayFromBitmap(ImageUtil
				.getBitmapFromRes(this, R.drawable.bola_fundo)));
		
		controller.save(background);
		
		Symbol icon = new Symbol();
		icon.setName("Words Icon");
		icon.setCreatedAt(current);
		icon.setUpdatedAt(current);
		icon.setImage(ImageUtil.getByteArrayFromBitmap(ImageUtil
				.getBitmapFromRes(this, R.drawable.words_icon)));
		
		controller.save(icon);
		
		Prancha prancha = new Prancha();
		prancha.setDescription("Palavras");
		prancha.setIdUser("GILSON_TCC");
		prancha.setUpdatedAt(current);
		prancha.setCreatedAt(current);
		prancha.setSymbol(background);
		prancha.setPlano(controller.getPlanoByModule(EnumModuleDomain
				.PUZZLE_GAME));
		prancha.setIcon(icon);
		
		controller.save(prancha);
		
		Display display = getWindowManager().getDefaultDisplay();
		DragDropObjHolder bHolder = this.createDragDropGameObjects(display, -250, 500, R.drawable.b);
		DragDropObjHolder oHolder = this.createDragDropGameObjects(display, -70, 500, R.drawable.o);
		DragDropObjHolder lHolder = this.createDragDropGameObjects(display, 130, 500, R.drawable.l);
		DragDropObjHolder aHolder = this.createDragDropGameObjects(display, 280, 500, R.drawable.a);
		
		this.createPiece(prancha, bHolder, controller);
		this.createPiece(prancha, oHolder, controller);
		this.createPiece(prancha, lHolder, controller);
		this.createPiece(prancha, aHolder, controller);
	}
	
	private DragDropObjHolder createDragDropGameObjects(Display display, int xAdjust, int y, int bitmapRes) {
		DragDropObjHolder holder = new DragDropObjHolder();
		
		Bitmap bmp = ImageUtil.getBitmapFromRes(this, bitmapRes);
		Bitmap destinyBmp = ImageUtil.getBorderOfImage(bmp);
		
		
		@SuppressWarnings("deprecation")
		GameObject destiny = new GameObject(display.getWidth()/2 - destinyBmp.getWidth()/2 + xAdjust, y);
		destiny.addComponent(new CanvasRenderBitmapComponent(destiny, destinyBmp));
		
		GameObject origin = new GameObject();
		origin.addComponent(new CanvasRenderBitmapComponent(origin, bmp));
		origin.addComponent(new DragDropComponent(origin, bmp, destiny, destinyBmp));
		
		holder.origin = origin;
		holder.originBmp = bmp;
		holder.destiny = destiny;
		holder.destinyBmp = destinyBmp;
		
		return holder;
	}
	
	private void createPiece(Prancha prancha, DragDropObjHolder holder,
			PuzzleController controller) throws SQLException {
		Symbol origin = createSymbol(holder.originBmp);
		Symbol destiny = createSymbol(holder.destinyBmp);
		
		controller.save(origin);
		controller.save(destiny);
		
		Piece piece = new Piece();
		piece.setOrigin(origin);
		piece.setDestiny(destiny);
		piece.setDestinyPosX(holder.destiny.getX());
		piece.setDestinyPosY(holder.destiny.getY());
		piece.setPrancha(prancha);
		
		controller.save(piece);
	}
	
	private Symbol createSymbol(Bitmap bmp) {
		Date current = new Date();
		
		Symbol symbol = new Symbol();
		symbol.setUpdatedAt(current);
		symbol.setCreatedAt(current);
		symbol.setImage(ImageUtil.getByteArrayFromBitmap(bmp));
		
		return symbol;
	}
	
	private class DragDropObjHolder {
		@SuppressWarnings("unused")
		public GameObject origin;
		public Bitmap originBmp;
		public GameObject destiny;
		public Bitmap destinyBmp;
	}
}
