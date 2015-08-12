package br.com.furb.tagarela.puzzlegame;

import br.com.engine.game.Game;
import br.com.engine.game.GameCanvas;
import br.com.engine.game.Scene;
import br.com.engine.model.GameObject;
import br.com.engine.system.render.CanvasRenderSystem;
import br.com.furb.tagarela.puzzlegame.component.TestComponent;
import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.createGame();
	}

	private void createGame() {
		// Criando um game object nas coordenadas x: 100 y: 100
		GameObject obj1 = new GameObject(100, 100);
		// Adicionando comportamento do TestComponent
		obj1.addComponent(new TestComponent());
		// Criando e adicionandos objetos a uma cena
		Scene scene1 = new Scene();
		scene1.addGameObject(obj1);
		// Definindo o sistema de renderizcao do Canvas 
		CanvasRenderSystem renderSystem = new CanvasRenderSystem();
		// Utilizando o Canvas padrão da Engine
		GameCanvas canvas = new GameCanvas(this);
		renderSystem.setCanvas(canvas);		
		// Configurando a inicializacao do jogo
		Game.getInstance().configureGame(this, scene1, renderSystem);
		// Definindo o conteúdo da activity com o canvas
		setContentView(canvas);
	}
	
}
