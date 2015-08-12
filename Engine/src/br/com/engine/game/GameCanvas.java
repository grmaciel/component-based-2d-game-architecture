package br.com.engine.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder;

	public GameCanvas(Context context) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
		this.setFocusable(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("", "SURFACE CREATED");
		Game.getInstance().startGame();
		Log.i("", "Aceleração de Hardware: " + this.isHardwareAccelerated());
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("CANVAS", "SURFACE DESTRYOED");
		Game.getInstance().stopGame();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}
}
