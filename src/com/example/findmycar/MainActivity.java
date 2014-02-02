package com.example.findmycar;

import java.text.ParseException;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;


public class MainActivity extends FragmentActivity implements LocationListener {

	public static boolean saved = false;
	public static ParkedLocation parkedLocation = null;
	public static MySQLiteHelper datasource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		datasource = new MySQLiteHelper(this);
	}

	public void onClickBtn1(View v) throws ParseException
    {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    } 
	public void onClickBtn2(View v)
    {
        Intent intent = new Intent(this, FindActivity.class);
        startActivity(intent);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
