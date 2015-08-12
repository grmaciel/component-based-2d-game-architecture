package br.com.furb.tagarela.activity;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.com.furb.tagarela.R;
import br.com.furb.tagarela.activity.controller.TagarelaController;
import br.com.furb.tagarela.persistence.model.Prancha;
import br.com.furb.tagarela.util.ImageUtil;

public class PlanoSimboloViewerActivity extends Activity {
	private TagarelaController controller = new TagarelaController(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.plano_simbolo_viewer);
		
		this.configureViewData();
	}
	
	private void configureViewData() {
		try {
			PranchaAdapter adapter = new PranchaAdapter(this,
					controller.getPranchaList());
			getListview().setAdapter(adapter);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	private ListView getListview() {
		return (ListView) this.findViewById(R.id.listViewPlanoSimbolo);
	}
	
	private class PranchaAdapter extends ArrayAdapter<Prancha> {

		public PranchaAdapter(Context context, List<Prancha> objects) {
			super(context, R.layout.plano_simbolo_item, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.plano_simbolo_item, parent, false);
			}
			
			Prancha prancha = this.getItem(position);
			
			ImageView img = (ImageView) convertView
					.findViewById(R.id.imgSimboloPlano);
			TextView txt = (TextView) convertView.findViewById(R.id.nomePlano);
			
			txt.setText(prancha.getName());
			
			if (prancha.getSymbol() != null && prancha.getSymbol().getImage() != null) {
				img.setImageBitmap(ImageUtil.decodeImageByte(prancha
						.getSymbol().getImage()));
			}
			
			return convertView;
		}
	}
}
