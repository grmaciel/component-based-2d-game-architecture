package br.com.furb.tagarela.puzzlegame;

import java.sql.SQLException;

import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.puzzlegame.controller.GameConfigController;
import br.com.furb.tagarela.puzzlegame.model.GameConfig;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ConfigActivity extends Activity {
	private static String PARAM_PRANCHA = "PARAM_ID_PRANCHA";
	
	private GameConfig gameConfig;
	private Prancha prancha;
	private GameConfigController gameConfigController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.config_game);
		this.loadParams();

		this.configureViewListeners();
		this.configureView();
	}

	private void loadParams() {
		Intent intent = getIntent();
		Integer idPrancha = intent.getExtras().getInt(PARAM_PRANCHA);
		
		try {
			this.prancha = getController().getPranchaById(idPrancha);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void configureView() {
		try {
			this.gameConfig = getController().getGameConfigByIdPrancha(prancha.getId());
		
			if (gameConfig != null) {
				getCheckDestacarOrgao().setChecked(gameConfig.getDestacarDestino());
				getCheckLimitarErros().setChecked(gameConfig.getLimiteErros());
				getCheckAtracao().setChecked(gameConfig.getArrastarObjeto());
				getLabelDificuldade().setText(String.valueOf(gameConfig.getDificuldadeEncaixe()));
				getSeekBarDificuldade().setProgress(gameConfig.getDificuldadeEncaixe());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		getSeekBarDificuldade().setMax(90);
	}

	private void configureViewListeners() {
		getSeekBarDificuldade().setOnSeekBarChangeListener(getOnSeekBarChange());
		getBtnConfirmar().setOnClickListener(getOnConfirmarListener());
		getBtnSair().setOnClickListener(getOnSairListener());

	}
	
	private OnClickListener getOnSairListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		};
	}

	private OnClickListener getOnConfirmarListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionConfirmar();
			}
		};
	}
	
	private void actionConfirmar() {
		try {
			GameConfigController controller = new GameConfigController(this);
			
			if (gameConfig == null) {
				gameConfig = new GameConfig();
				gameConfig.setPrancha(prancha);
			}
			
			gameConfig.setDestacarDestino(getCheckDestacarOrgao().isChecked());
			gameConfig.setLimiteErros(getCheckLimitarErros().isChecked());
			gameConfig.setDificuldadeEncaixe(Integer.valueOf(getLabelDificuldade()
					.getText().toString()));
			gameConfig.setArrastarObjeto(getCheckAtracao().isChecked());
			
			controller.save(gameConfig);
			finish();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private OnSeekBarChangeListener getOnSeekBarChange() {
		return new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				getLabelDificuldade().setText(String.valueOf(progress));
			}
		};
	}
	
	public static Intent getIntent(Context context, Integer idPrancha) {
		Intent intent = new Intent(context, ConfigActivity.class);
		Bundle extras = new Bundle();
		extras.putInt(PARAM_PRANCHA, idPrancha);
		intent.putExtras(extras);
		
		return intent;
	}
	
	private GameConfigController getController() {
		if (gameConfigController == null) {
			gameConfigController = new GameConfigController(this);
		}
		
		return gameConfigController;
	}
	

	private TextView getLabelDificuldade() {
		return (TextView) this.findViewById(R.id.configLabelDificuldadeEncaixe);
	}
	
	private CheckBox getCheckDestacarOrgao() {
		return (CheckBox) this.findViewById(R.id.checkBoxDestacarDestino);
	}
	
	private CheckBox getCheckLimitarErros() {
		return (CheckBox) this.findViewById(R.id.checkBoxLimiteErro);
	}
	
	private CheckBox getCheckAtracao() {
		return (CheckBox) this.findViewById(R.id.checkBoxAtracao);
	}

	private SeekBar getSeekBarDificuldade() {
		return (SeekBar) this.findViewById(R.id.configDificuldadeSeekbar);
	}
	
	private Button getBtnConfirmar() {
		return (Button) this.findViewById(R.id.mainBtnConfirmarConfig);
	}
	
	private Button getBtnSair() {
		return (Button) this.findViewById(R.id.mainBtnSairConfig);	
	}
}
