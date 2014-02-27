package com.cameronneale.gogps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.cameronneale.gogps.model.Place;
import com.cameronneale.gogps.model.Visit;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class MapLauncher extends FragmentActivity {
	public int[] visitlist;
	static final int ADD_VISIT = 0;
	GoogleMap gMap;
	MapFragment fm;
	ObjectAnimator a0 = new ObjectAnimator();
	ObjectAnimator a1 = new ObjectAnimator();
	AnimatorSet as = new AnimatorSet();
	View b1;
	View b2;
	View b0;
	LatLng cLoc;
	int su = 0;
	public Double slat;
    public Double slong;
	public boolean menuVis = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		
		b1 = (View) findViewById(R.id.maddPlace);
		b0 = (View) findViewById(R.id.plusBtn);
		MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    	gMap = fm.getMap();
    	gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
 		
        drawMap(true);
 		
        gMap.setOnMapClickListener(new OnMapClickListener() {
        	@Override
        	public void onMapClick(LatLng latlng) {
        		Log.d("LatLon: ",latlng.toString());
        		cLoc = latlng;
        		drawMap(false);
        		gMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(201)).title(genDist(latlng.latitude,latlng.longitude))).showInfoWindow();
        		System.out.println(latlng);
        		showMenu();
        	}
        });
        
        gMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			
			@Override
			public void onCameraChange(CameraPosition arg0) {
				hideMenu();
				if (su == 0) {
	        		 drawMap(false);
	        		 if (gMap.getMyLocation()!=null) su++;
	        		 System.out.println("READY");
	        	 }
			}
			
		});
        
        
        a0.setDuration(1000);
        a1.setDuration(1000);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		drawMap(false);
	}
	
	//距離を計算する
	public double distance(Double lat1, Double lon1, Double lat2, Double lon2) {
		//経度・緯度をレイディオンに変更
		lat1 *= (Math.PI/180);
        lon1 *= (Math.PI/180);
        lat2 *= (Math.PI/180);
        lon2 *= (Math.PI/180);
        //差を計算
        Double dlon = lon2 - lon1;
        Double dlat = lat2 - lat1;
        //アークサインを計算
        Double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
        Double c = 2 * Math.asin(Math.min(1,Math.sqrt(a)));
        //地表距離を計算
        Double dis = 6371 * c;
        return dis;
	}
	
	//現在地から目的地までの距離
		public String genDist(Double lat, Double lon) {
			Location gen = gMap.getMyLocation();
			if (gen==null) return "";
			String sdist = "";
			Double ddist = distance(gen.getLatitude(),gen.getLongitude(),lat,lon);
			if (ddist < 1) {
				sdist = ""+(int)(ddist*1000)+"m";
			} else {
				sdist = ""+(int)(ddist*1)+"km";
			}
			if(getString(R.string.lang).equals("jpa")) {
				return getString(R.string.currentLoc)+"から"+sdist+"";
			} else {
				return ""+sdist+" from "+getString(R.string.currentLoc).toLowerCase();
			}
		}
	
	//最も近い場所を計算
	@SuppressLint("DefaultLocale")
	public String closest(Visit v) {
		Double mdist = 12000.00;
		Double ddist = 12000.00;
		Place cl = null;
		String sdist = "";
		ChangeDB db = new ChangeDB(this);
		List<Place> p = db.getAllPlaces();
		for (Place i: p) {
			ddist = distance(Double.valueOf(v.getLatitude()),Double.valueOf(v.getLongitude()),Double.valueOf(i.getLat()),Double.valueOf(i.getLong()));
			if(mdist > ddist) {
				cl = i;
				mdist = ddist;
			}
		 }
		if (cl == null) return "";
		if (mdist < 1) {
			sdist = ""+(int)(mdist*1000)+"m";
		} else {
			sdist = ""+(int)(mdist*1)+"km";
		}
		if(getString(R.string.lang).equals("jpa")) {
			return cl.getName()+"から"+sdist;
		} else {
			return sdist+" from "+cl.getName();
		}
		
	}
	
	//履歴からデータを読み込む
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 1) {
			visitlist = data.getIntArrayExtra("vl");
			drawMap(false);
		} else if(resultCode == RESULT_OK && requestCode == 2) {
			drawMap(false);
			ChangeDB db = new ChangeDB(this);
			Place p = db.getPlace(data.getIntExtra("id",0));
			fly(Double.valueOf(p.getLat()),Double.valueOf(p.getLong()));
			db.close();
		}
		
	}
	
	//マップを描く／描き直す
	public void drawMap(boolean startUp) {
    	gMap.clear();
        ChangeDB db = new ChangeDB(this);
        List<Place> places = db.getAllPlaces();
        db.close();
    	for (Place i: places) {
    		String psnip = i.getDesc();
    		psnip += " ("+genDist(Double.valueOf(i.getLat()),Double.valueOf(i.getLong()))+")";
    		gMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(i.getLat()),Double.parseDouble(i.getLong()))).title(i.getName()).snippet(psnip).icon(BitmapDescriptorFactory.defaultMarker(i.getColor())));
    	}
    	int v =0;
    	if (visitlist!=null) {
	    	while (visitlist[v]>0) {
	    		Visit i = db.getVisit(visitlist[v]);
	    		if (closest(i)!="")	i.setNearestLocation(closest(i));
	    		System.out.println("FIRST: "+i.toString());
	    		BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_345);
	    		if (i.getVcol() >= 345) {
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_345);
	    		} else if(i.getVcol() >= 325){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_325);
	    		} else if(i.getVcol() >= 285){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_285);
	    		} else if(i.getVcol() >= 225){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_225);
	    		} else if(i.getVcol() >= 160){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_160);
	    		} else if(i.getVcol() >= 80){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_80);
	    		} else if(i.getVcol() >= 45){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_45);
	    		} else if(i.getVcol() >= 20){
	    			icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_icon_20);
	    		}
	    		String snip = i.getMemo();
	    		if (!snip.equals("")) {
	    			snip += " - ";
	    		}
	    		snip += i.getNearestLocation();
	    		gMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(i.getLatitude()),Double.parseDouble(i.getLongitude()))).title(i.getDate()+" ("+getString(R.string.gosa)+": "+i.getGosa()+"m)").snippet(snip).icon(icon));
	    		v++;
	    	}
    	}
    	if (startUp) {
    		Toast msg = Toast.makeText(getApplicationContext(), R.string.info_tap, Toast.LENGTH_LONG);
        	msg.show();
    		flyHome(places);
    	}
	}
	
	
	
	//マップを描き直し、ホームに飛ぶ
	public void goHome(View view) {
		drawMap(true);
	}
	
	//メニューを作る
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_activity_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	 //メニューを見せる
	 public void showMenu() {
		if (!menuVis) {
	 		a0 = ObjectAnimator.ofFloat(b0, "alpha", 0.2f, 1f);
	 		a1 = ObjectAnimator.ofFloat(b1,"alpha", 0.2f,1f);
	 		as.play(a0).with(a1);
	 		as.start();
	 		menuVis = true;
		}
	 }
	 
	//メニューを隠す
	 public void hideMenu() {
		if (menuVis) {
	 		a0 = ObjectAnimator.ofFloat(b0, "alpha", 1f, 0.2f);
	 		a1 = ObjectAnimator.ofFloat(b1, "alpha", 1f,0.2f);
	 		as.play(a0).with(a1);
	 		as.start();
	 		menuVis = false;
		}
	}
	 
	//新しい場所を追加する
	 public void addPlace(View view) {
		 if (menuVis) {
			 Intent addP = new Intent(this,AddPlace.class);
			 addP.putExtra("longitude", cLoc.longitude);
			 addP.putExtra("latitude", cLoc.latitude);
			 startActivity(addP);
		 }
	 }
	 
	 //目的地を見せる
	 public void openPlaces(View view){
		 Intent pl = new Intent(this,Destinations.class);
		 int requestCode = 2;
		 startActivityForResult(pl, requestCode);
	 }
	 
	 //履歴を見せる
	 public void openVisits(View view){
		 Intent vs = new Intent(this,Visits.class);
		 int requestCode = 1;
		 startActivityForResult(vs, requestCode);
	 }
	 
	//マップ表示を「地図」にする
	 public void mapModeNormal(MenuItem item) {
		 gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    	
	 }
	 
	//マップ表示を「航空写真」にする
	 public void mapModeSat(MenuItem item) {
	    	gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    	
	 }
	 
	 //マップで場所に飛ぶ(double)
	 public void fly(double lat, double lon) {
		 LatLng flyTo = new LatLng(lat,lon);
		 gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(flyTo, 15));
	 }
	 
	//マップで場所に飛ぶ(LatLng)
	 public void fly(LatLng latlon) {
		 gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlon, 15));
	 }
	 
	 //ホームに飛ぶ
	 public void flyHome(List<Place> p) {
		 for (Place i: p) {
			 if (i.isDefault()==1) {
				 fly(Double.parseDouble(i.getLat()),Double.parseDouble(i.getLong()));
			 }
		 }
	 }
	 
	 //履歴（現在地）を保存
	 public void saveVisit(View view) {
		 Location cl = gMap.getMyLocation();
		 if (cl==null) {
			 Log.d("ERROR","null location reading, aborted");
			 Toast error1 = Toast.makeText(getApplicationContext(), R.string.null_visit, Toast.LENGTH_LONG);
			 error1.show();
		 } else {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm aa");
		String date = df.format(cal.getTime());
		Log.d("Date",date);
		String vlongitude = Double.toString(cl.getLongitude());
		Log.d("Longitude",vlongitude);
		String vlatitude = Double.toString(cl.getLatitude());
		Log.d("Latitude",vlatitude);
		int gosa = (int)cl.getAccuracy();
		Log.d("Accuracy",""+gosa+"m");
		 
		 Intent av = new Intent(this,AddVisit.class);
		 av.putExtra("date", date);
		 av.putExtra("long", vlongitude);
		 av.putExtra("lat", vlatitude);
		 av.putExtra("gosa", gosa);
		 startActivityForResult(av,ADD_VISIT);
		 }
	 }
	 
	//現在地に飛ぶ
	 public void getCoords(MenuItem item) {
		 Location location = gMap.getMyLocation();
		 if (location!=null) {
			 LatLng myLoc = new LatLng(location.getLatitude(),location.getLongitude());
			 gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 15));
		 }
	 }
}

