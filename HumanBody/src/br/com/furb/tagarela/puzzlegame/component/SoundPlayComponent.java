package br.com.furb.tagarela.puzzlegame.component;

import java.io.IOException;

import android.media.MediaPlayer;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.enumeration.EnumMessageId;

public class SoundPlayComponent extends AbstractComponent {
	private MediaPlayer player;

	public SoundPlayComponent(MediaPlayer player) {
		this.player = player;
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onTouchListener(MotionEvent event) {
	}

	@Override
	public void onRender(RenderWrapper render) {
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		if (wrapper.getId() == EnumMessageId.DRAG_OBJECT_SELECTED.ordinal()) {
			player.seekTo(0);
			player.start();
		} else if (wrapper.getId() == EnumMessageId.STOP_DRAG.ordinal()) {
			if (player.isPlaying()) {
				player.stop();
				
				try {
					player.prepare();
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
