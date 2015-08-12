package br.com.engine.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import br.com.engine.component.IComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.input.InputSystem;
import br.com.engine.system.logic.LogicSystem;
import br.com.engine.system.message.MessageSystem;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.IRenderSystem;

public class Game {
	boolean running;
	
	private List<IComponent> components = new ArrayList<IComponent>();
	private Scene scene;
	private GameLoop gameLoop;
	private LogicSystem logicSystem = new LogicSystem();
	private InputSystem inputSystem = new InputSystem();
	private MessageSystem messageSystem = new MessageSystem();
	private IRenderSystem renderSystem;
	private List<MessageWrapper> messages = new ArrayList<MessageWrapper>();
	private static Game game;
	
	private Game() {
	}

	/**
	 * Configuração para definir a cena e o 
	 * sistema de renderização
	 * 
	 * @param context
	 * @param scene
	 * @param renderSystem
	 */
	public void configureGame(Context context, Scene scene, IRenderSystem renderSystem) {
		this.scene = scene;
		this.renderSystem = renderSystem;
	}
	
	/**
	 * Inicialização do loop principal em uma thread separada	
	 */
	public void startGame() {
		this.gameLoop = new GameLoop();
		this.gameLoop.setRunning(true);
		this.gameLoop.start();
	}
	
	public void stopGame() {
		boolean retry = true;

		while (retry) {
			try {
				gameLoop.setRunning(false);
				gameLoop.join();
				retry = false;
			} catch (InterruptedException e) {
				Log.d("ERROR", e.getMessage());
			}
		}
	}
	
	public void pauseGame() {
		if (gameLoop != null) {
			gameLoop.setRunning(false);
		}
	}
	
	public void resumeGame() {
		if (gameLoop != null) {
			gameLoop.setRunning(true);
			
			if (gameLoop.isAlive()) {
				Log.d("GAME", "ALIVE");
			} else {
				Log.d("GAME", "DEAD");
			}
		}
	}
	
	public void clearGame() {
		if (gameLoop != null) {
			this.stopGame();
		}
		
		this.scene = new Scene();
		this.renderSystem = null;
		this.components = new ArrayList<IComponent>();
		this.messages = new ArrayList<MessageWrapper>();
		this.logicSystem = new LogicSystem();
		this.inputSystem = new InputSystem();
		this.messageSystem = new MessageSystem();
	}
	
	private void gameLoop() {
		long freeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		Log.i("MEMORY", "Game Loop: " + freeMemory);
		
		List<IComponent> allComponents = getAllComponents();
		messageSystem.sendMessage(messages);
		logicSystem.update(allComponents);
		renderSystem.render(allComponents);
	}
	
	private class GameLoop extends Thread {
		private boolean running;
		
		@Override
		public void run() {
			while (running) {
				gameLoop();
			}
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
		
		public boolean isRunning() {
			return this.running;
		}
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public static Game getInstance() {
		if (game == null) {
			game = new Game();
		}
		return game;
	}
	
	public OnTouchListener getOnTouchListener() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gameLoop != null && gameLoop.isRunning()) {
					inputSystem.onTouch(getAllComponents(), v, event);
				}
				
				return true;
			}
		};
	}
	
	public void sendMessage(MessageWrapper message) {
		messages.add(message);
	}
	
	public void registerMessageListener(IComponent component) {
		messageSystem.registerListener(component);
	}
	
	
	public List<IComponent> getAllComponents() {
		List<GameObject> objs = scene.getGameObjects();
		List<IComponent> components = new ArrayList<IComponent>();
		components.addAll(this.components);
		for (GameObject obj : objs) {
			components.addAll(obj.getComponents());
		}
		
		return components;
	}
	
	public List<IComponent> getComponents() {
		return components;
	}

	public void setComponents(List<IComponent> components) {
		this.components = components;
	}
	
	public void addComponent(IComponent component) {
		this.components.add(component);
	}
}
