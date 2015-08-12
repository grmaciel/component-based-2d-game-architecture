package br.com.engine.system.render;

import java.text.DecimalFormat;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceView;
import br.com.engine.component.IComponent;
import br.com.engine.game.GameCanvas;

public class CanvasRenderSystem implements IRenderSystem {
	// last time the status was stored
	private long lastStatusStore = 0;
	// the status time counter
	private long statusIntervalTimer = 0l;
	// number of frames skipped since the game started
	private long totalFramesSkipped = 0l;
	// number of frames skipped in a store cycle (1 sec)
	private long framesSkippedPerStatCycle = 0l;

	// number of rendered frames in an interval
	private int frameCountPerStatCycle = 0;
	private long totalFrameCount = 0l;
	// the last FPS values
	private double fpsStore[];
	// the number of times the stat has been read
	private long statsCount = 0;
	// the average FPS since the game started
	private double averageFps = 0.0;

	// desired fps
	private final static int MAX_FPS = 50;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 5;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;

	// Stuff for stats */
	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
	// we'll be reading the stats every second
	private final static int STAT_INTERVAL = 1000; // ms
	// the average will be calculated by storing
	// the last n FPSs
	private final static int FPS_HISTORY_NR = 10;
	
	long beginTime;
	long timeDiff;
	int sleepTime = 0;
	int framesSkipped;
	
	private Paint paint;
	private SurfaceView canvas;
	private RenderWrapper wrapper = new RenderWrapper();
	private CanvasHolder cWrapper = new CanvasHolder();

	public CanvasRenderSystem() {
		this.configurePaint();
	}

	private void configurePaint() {
		paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(false);
		paint.setFilterBitmap(true);
	}

	@Override
	public void render(List<IComponent> components) {
		Canvas c = null;
		try {
			c = canvas.getHolder().lockCanvas();
			cWrapper.canvas = c;
			cWrapper.paint = paint;
			wrapper.setHolder(cWrapper);

			if (c != null) {
				synchronized (canvas.getHolder()) {
					for (IComponent component : components) {
						if (component.isActive()) {
							component.onRender(wrapper);
						}
					}
				}
			}
		} finally {
			if (c != null) {
				canvas.getHolder().unlockCanvasAndPost(c);
			}
		}
	}

	private void storeStats() {
		frameCountPerStatCycle++;
		totalFrameCount++;

		// check the actual time
		statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

		if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
			// calculate the actual frames pers status check interval
			double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

			// stores the latest fps in the array
			fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

			// increase the number of times statistics was calculated
			statsCount++;

			double totalFps = 0.0;
			// sum up the stored fps values
			for (int i = 0; i < FPS_HISTORY_NR; i++) {
				totalFps += fpsStore[i];
			}

			// obtain the average
			if (statsCount < FPS_HISTORY_NR) {
				// in case of the first 10 triggers
				averageFps = totalFps / statsCount;
			} else {
				averageFps = totalFps / FPS_HISTORY_NR;
			}
			// saving the number of total frames skipped
			totalFramesSkipped += framesSkippedPerStatCycle;
			// resetting the counters after a status record (1 sec)
			framesSkippedPerStatCycle = 0;
			statusIntervalTimer = 0;
			frameCountPerStatCycle = 0;

			statusIntervalTimer = System.currentTimeMillis();
			lastStatusStore = statusIntervalTimer;
//			 Log.d("FPS", "Average FPS:" + (int)averageFps);
//			gamePanel.setAvgFps("FPS: " + df.format(averageFps));
		}
	}

	public void setCanvas(GameCanvas canvas) {
		this.canvas = canvas;
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}
}
