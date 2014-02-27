package com.cameronneale.gogps;

import com.cameronneale.gogps.model.Visit;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class AddVisit extends Activity {
public String dDate;
public String dLong;
public String dLat;
public int dGosa;
public String dMemo;
public int dCol;
public Visit eVisit;

public TextView avDate;
public TextView avLat;
public TextView avLong;
public TextView avGosa;
public TextView avMemo;
public SeekBar avCol;
public View avColBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupActionBar();
		
		//編集・追加
		if (getIntent().getBooleanExtra("isEdit", false)) {
			setContentView(R.layout.activity_edit_visit);
			getActionBar().setTitle(R.string.editvisit_title);
			ChangeDB db = new ChangeDB(this);
			eVisit = db.getVisit(getIntent().getIntExtra("eVisitId", 0));
			dDate = eVisit.getDate();
			dLong = eVisit.getLongitude();
			dLat = eVisit.getLatitude();
			dGosa = eVisit.getGosa();
			dMemo = eVisit.getMemo();
			dCol = eVisit.getVcol();
			db.close();
		} else {
			setContentView(R.layout.activity_add_visit);
			getActionBar().setTitle(R.string.addvisit_title);
			dDate = getIntent().getStringExtra("date");
			dLong = getIntent().getStringExtra("long");
			dLat = getIntent().getStringExtra("lat");
			dGosa = getIntent().getIntExtra("gosa",0);
			dMemo = "";
			dCol = 1;
		}
		avDate = (TextView) findViewById(R.id.avdate);
		avLat = (TextView) findViewById(R.id.avlat);
		avLong = (TextView) findViewById(R.id.avlong);
		avGosa = (TextView) findViewById(R.id.avgosa);
		avMemo = (TextView) findViewById(R.id.avmemo);
		avCol = (SeekBar) findViewById(R.id.avcol);
		avColBar = (View) findViewById(R.id.avcolbar);
		avDate.setText(dDate);
		avDate.setKeyListener(null);
		avLat.setText(dLat);
		avLat.setKeyListener(null);
		avLong.setText(dLong);
		avLong.setKeyListener(null);
		avGosa.setText(""+dGosa+"m");
		avGosa.setKeyListener(null);
		avMemo.setText(dMemo);
		avCol.setProgress(dCol);
		colorChange();
		
		
		avCol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				colorChange();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	//バーの色を更新
	public void colorChange() {
		float hsv[] = {Float.valueOf(avCol.getProgress()),100f,100f};
		avColBar.setBackgroundColor(Color.HSVToColor(hsv));
	}

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_visit, menu);
		return true;
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

	//履歴を追加する
	public void addVisit(View view) {
		ChangeDB db = new ChangeDB(this);
		Visit v = new Visit();
		v.setDate(String.valueOf(avDate.getText()));
		v.setGosa(dGosa);
		v.setLatitude(String.valueOf(avLat.getText()));
		v.setLongitude(String.valueOf(avLong.getText()));
		v.setMemo(String.valueOf(avMemo.getText()));
		v.setVcol(avCol.getProgress());
		db.addVisit(v);
		db.getAllVisits();
		db.close();
		finish();
	}
	
	//履歴を保存する
	public void saveVisit(View view) {
		ChangeDB db = new ChangeDB(this);
		eVisit.setMemo(""+avMemo.getText());
		eVisit.setVcol(avCol.getProgress());
		db.updateVisit(eVisit);
		db.close();
		finish();
	}
	
	//履歴を削除する
	public void deleteVisit(View view) {
		ChangeDB db = new ChangeDB(this);
		db.deleteVisit(eVisit);
		db.close();
		finish();
	}
}
