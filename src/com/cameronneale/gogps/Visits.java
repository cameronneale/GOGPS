package com.cameronneale.gogps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cameronneale.gogps.model.Visit;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

@SuppressLint("SimpleDateFormat")
public class Visits extends Activity {
	
	private ListView listview;
	public List<Visit> visitList;
	public static ArrayList<String> an = new ArrayList<String>();
	public static ArrayList<Integer> linkid = new ArrayList<Integer>();
	public ArrayAdapter<String> adapter;
	public ChangeDB db;
	public TextView vdate;
	public Calendar c = Calendar.getInstance();
	public SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	public SimpleDateFormat dfd = new SimpleDateFormat("yyyy/MM/dd hh:mm aa");
	int i;
	public int id[] = new int[2000];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visits);

		setupActionBar();
		db = new ChangeDB(this);
		
		listview = (ListView) findViewById(android.R.id.list);
		
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int pos,long idn){
				Visit v = db.getVisit(id[pos]);
				editVisit(v.getId());
			}
		});
		
		Calendar c = Calendar.getInstance();
		vdate = (TextView) findViewById(R.id.vdate);
		vdate.setText(""+df.format(c.getTime()));
		getList();
		db.close();
	}

	//翌日を表示
	public void nextDay(View view) {
		c.add(Calendar.DATE, 1);
		vdate.setText(""+df.format(c.getTime()));
		getList();
	}
	
	//前日を表示
	public void prevDay(View view) {
		c.add(Calendar.DATE, -1);
		vdate.setText(""+df.format(c.getTime()));
		getList();
	}
	
	//履歴を編集
	public void editVisit(int pid) {
		Intent editIn = new Intent(getApplicationContext(),AddVisit.class);
		editIn.putExtra("isEdit", true);
		editIn.putExtra("eVisitId",pid);
		startActivity(editIn);
	}
	
	//マップに戻り、当日の履歴を表示
	public void showMap(View view) {
		Intent sa = new Intent();
	    sa.putExtra("vl",id);
	    setResult(RESULT_OK,sa);    
	    finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getList();
	}
	
	//当てはめる履歴を表示
	public void getList() {
		db = new ChangeDB(this);
		
		Log.d("Attempting:","Read all visits");
		visitList = db.getAllVisits();
		an.clear();
		Arrays.fill(id, 0);
		Date sdate = new Date();
		i = 0;
		if (df.format(sdate).equals(""+vdate.getText())) {
			Log.d("OPERATION","MATCH!");
		} else {
			Log.d("OPERATION","SUCCESS!");
		}
		for(Visit v: visitList) {
			try {
				sdate = dfd.parse(v.getDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (df.format(sdate).equals(""+vdate.getText())) {
				if (v.getMemo().equals("")) {
					an.add(v.getDate());
				} else {
					an.add(v.getDate() + " (" + v.getMemo() + ")");
				}
				id[i] = v.getId();
				i++;
			}
		}
		
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,an);
		listview.setAdapter(adapter);
		
		
	}
	
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.destinations, menu);
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

}
