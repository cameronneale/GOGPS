package com.cameronneale.gogps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cameronneale.gogps.model.Place;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class Destinations extends Activity {
	private ListView listview;
	public List<Place> placeList;
	public static ArrayList<String> an = new ArrayList<String>();
	public static ArrayList<Integer> linkid = new ArrayList<Integer>();
	public ArrayAdapter<String> adapter;
	public ChangeDB db;
	int plid = 0;
	int i;
	int id[] = new int[2000];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_destinations);
		setupActionBar();
		
		db = new ChangeDB(this);
		listview = (ListView) findViewById(android.R.id.list);
		listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v,int pos,long idn){
				Place p = db.getPlace(id[pos]);
				editPlace(p.getId());
			}
		});
		
		getList();
		db.close();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("ATTEMPTING ACTIVITY");
		if(resultCode == RESULT_OK && requestCode == 2) {
			plid = data.getIntExtra("id",0);
			System.out.println("STORING DATA"+plid);
			Intent sa = new Intent();
		    sa.putExtra("id",plid);
		    setResult(RESULT_OK,sa);    
		    finish();
		}
		
	}
	
	public void editPlace(int pid) {
		Intent editIn = new Intent(getApplicationContext(),AddPlace.class);
		editIn.putExtra("isEdit", true);
		editIn.putExtra("ePlaceId",pid);
		startActivityForResult(editIn,2);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getList();
	}

	public void getList() {
		db = new ChangeDB(this);
		
		placeList = db.getAllPlaces();
		an.clear();
		Arrays.fill(id, 0);
		
		i = 0;
		for(Place p: placeList) {
			if (p.getDesc().equals("")) {
				an.add(p.getName());
			} else {
				an.add(p.getName() + " (" + p.getDesc() + ")");
			}
			id[i] = p.getId();
			i++;
		}
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,an);
		listview.setAdapter(adapter);
		
		db.close();
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
