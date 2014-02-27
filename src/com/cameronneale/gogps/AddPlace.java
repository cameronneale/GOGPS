package com.cameronneale.gogps;


import java.util.List;

import com.cameronneale.gogps.model.Place;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class AddPlace extends Activity {
	
	public TextView apName;
	public TextView apDesc;
	public TextView apLong;
	public TextView apLat;
	public Switch apDef;
	public SeekBar apCol;
	public View apCb;
	public int ePlaceId = 10;
	public Place ePlace;
	double cLat;
	double cLon;
	boolean isEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		if (isEdit) {
			setContentView(R.layout.activity_edit_place);
			getActionBar().setTitle(R.string.editplace_title);
			initForm();
			ePlaceId = getIntent().getIntExtra("ePlaceId", 0);
			ChangeDB db = new ChangeDB(this);
			ePlace = db.getPlace(ePlaceId);
			if (ePlace.isDefault() == 1) {
				apDef.setChecked(true);
			} else {
				apDef.setChecked(false);
			}
			apName.setText(ePlace.getName());
			apDesc.setText(ePlace.getDesc());
			apLong.setText(""+ePlace.getLong());
			apLat.setText(""+ePlace.getLat());
			apCol.setProgress(ePlace.getColor());
			colorChange();
			
		} else {
			setContentView(R.layout.activity_add_place);
			getActionBar().setTitle(R.string.addplace_title);
			initForm();
			cLat = getIntent().getDoubleExtra("latitude", 0.0);
			cLon = getIntent().getDoubleExtra("longitude", 0.0);
			apLong.setText(""+cLon);
			apLat.setText(""+cLat);
		}

		setupActionBar();
		
		apCol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				colorChange();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
		});
	}
	
	public void showOnMap(View view) {
		Intent sa = new Intent();
	    sa.putExtra("id",ePlaceId);
	    setResult(RESULT_OK,sa);
	    finish();
	    
	}
	
	//バーの色を更新する
	public void colorChange() {
		float hsv[] = {Float.valueOf(apCol.getProgress()),100f,100f};
		apCb.setBackgroundColor(Color.HSVToColor(hsv));
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	//コンポーネントを指定する
	public void initForm() {
		apName = (TextView) findViewById(R.id.apname);
		apDesc = (TextView) findViewById(R.id.apdesc);
		apLong = (TextView) findViewById(R.id.aplong);
		apLat = (TextView) findViewById(R.id.aplat);
		apDef = (Switch) findViewById(R.id.apdef);
		apCol = (SeekBar) findViewById(R.id.apcol);
		apCb = (View) findViewById(R.id.colbar);
	}
	
	//目的地を削除する
	public void deletePlace(View view) {
		ChangeDB db = new ChangeDB(this);
		db.deletePlace(ePlace);
		db.close();
		finish();
	}
	
	//目的地を編集する
	public void editPlace(View view) {
		ChangeDB db = new ChangeDB(this);
		if (apDef.isChecked()) {
			ePlace.setDefault(1);
		} else {
			ePlace.setDefault(0);
		}
		ePlace.setName(""+apName.getText());
		ePlace.setDesc(""+apDesc.getText());
		ePlace.setLong(""+apLong.getText());
		ePlace.setLat(""+apLat.getText());
		ePlace.setColor(apCol.getProgress());
		db.updatePlace(ePlace);
		confirmDefault();
		db.close();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_place, menu);
		return true;
	}

	//ホーム場所？
	public void confirmDefault(){
		if (apDef.isChecked()) {
			ChangeDB db = new ChangeDB(this);
			List<Place> placeList = db.getAllPlaces();
			for(Place p: placeList) {
				if (p.getId() != ePlace.getId()) {
					p.setDefault(0);
					db.updatePlace(p);
				}
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//目的地を追加する
	public void addPlace(View view) {
		int ad = 0;
		if (apDef.isChecked()) {
			ad = 1;
		}
		ChangeDB db = new ChangeDB(this);
		db.addPlace(new Place(String.valueOf(apName.getText()),String.valueOf(apDesc.getText()),String.valueOf(apLong.getText()),String.valueOf(apLat.getText()),ad,apCol.getProgress()));
		db.getAllPlaces();
		
		finish();
	}
}
