package br.com.furb.tagarela.openglexample;

import android.app.Activity;
import android.os.Bundle;
import br.com.engine.game.Game;
import br.com.engine.game.Scene;
import br.com.engine.model.GameObject;
import br.com.engine.system.render.OpenGLRenderSystem;

public class GameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		OpenGLRenderSystem renderSystem = new OpenGLRenderSystem(this);
		Scene cena1 = new Scene();
		
		GameObject teste = new GameObject();
//		teste.addComponent(new SquareDrawComponent());
		cena1.addGameObject(teste);
		
		Game game = Game.getInstance();
		game.configureGame(this, cena1, renderSystem);
		setContentView(renderSystem.getSurfaceView());
	}

}
