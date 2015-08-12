package br.com.furb.tagarela.puzzlegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameMenuActivity extends Activity {
	private static String PARAM_PRANCHA = "PARAM_ID_PRANCHA";
	private Integer idPrancha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.game_menu);
		
		loadParams();

		this.configureViewListeners();
	}

	private void loadParams() {
		Intent intent = getIntent();
		this.idPrancha = intent.getExtras().getInt(PARAM_PRANCHA);
	}

	private void configureViewListeners() {
		getBtnNovoJogo().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				actionNovoJogo();
			}
		});
		
		getBtnConfiguracoes().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionConfiguracoes();
			}
		});
		
		getBtnSair().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionSair();
			}
		});
	}
	
	private void actionNovoJogo() {
		Intent intent = GameActivity.getIntent(this, idPrancha);
		startActivity(intent);
	}
	
	private void actionConfiguracoes() {
		Intent intent = ConfigActivity.getIntent(this, idPrancha);
		startActivity(intent);
	}
	
	public static Intent getIntent(Context context, Integer idPrancha) {
		Intent intent = new Intent(context, GameMenuActivity.class);
		Bundle extras = new Bundle();
		extras.putInt("PARAM_ID_PRANCHA", idPrancha);
		intent.putExtras(extras);
		
		return intent;
	}
	
	private void actionSair() {
		this.finish();
	}

	private Button getBtnNovoJogo() {
		return (Button) this.findViewById(R.id.mainBtnNovoJogo);
	}

	private Button getBtnConfiguracoes() {
		return (Button) this.findViewById(R.id.mainBtnConfiguracoes);
	}

	private Button getBtnSair() {
		return (Button) this.findViewById(R.id.mainBtnSair);
	}

}
