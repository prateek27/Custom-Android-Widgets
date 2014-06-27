package com.example.upclosesettings;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	Button f, m;
	Button km, mi;
	TextView genderTV, showMeValueTV, limitSearchValueTV;
	SeekBar distanceSB;
	CheckBox menCB, womenCB;
	int distance = 100;
	float distanceInMiles;
	boolean distance_in_km = true, distance_in_miles = false;
	String showMe = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		f = (Button) findViewById(R.id.female_button);
		m = (Button) findViewById(R.id.male_button);
		km = (Button) findViewById(R.id.km_button);
		mi = (Button) findViewById(R.id.miles_button);
		genderTV = (TextView) findViewById(R.id.genderTV);
		limitSearchValueTV = (TextView) findViewById(R.id.limitSearchValueTV);
		distanceSB = (SeekBar) findViewById(R.id.distanceSB);
		distanceSB.setProgress(distance);
		menCB = (CheckBox) findViewById(R.id.menCB);
		womenCB = (CheckBox) findViewById(R.id.womenCB);
		showMeValueTV = (TextView) findViewById(R.id.showMeValueTV);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.female_button:
			f.setBackgroundResource(R.drawable.round_corners_blue);
			f.setTextColor(Color.WHITE);
			m.setBackgroundResource(R.drawable.round_corners);
			m.setTextColor(Color.BLACK);
			genderTV.setText("Female");
			break;
		case R.id.male_button:
			m.setBackgroundResource(R.drawable.round_corners_blue);
			m.setTextColor(Color.WHITE);
			f.setBackgroundResource(R.drawable.round_corners);
			f.setTextColor(Color.BLACK);
			genderTV.setText("Male");
			break;

		case R.id.km_button:
			km.setBackgroundResource(R.drawable.round_corners_blue);
			km.setTextColor(Color.WHITE);
			mi.setBackgroundResource(R.drawable.round_corners);
			mi.setTextColor(Color.BLACK);
			distance = distanceSB.getProgress();

			if (distance_in_miles) {
				distance = (int) (distance / 1.609);
				distance_in_miles = false;
				distance_in_km = true;
			}
			limitSearchValueTV.setText(String.format("%d Km", distance));
			distanceSB.setProgress(distance);
			break;

		case R.id.miles_button:
			mi.setBackgroundResource(R.drawable.round_corners_blue);
			mi.setTextColor(Color.WHITE);
			km.setBackgroundResource(R.drawable.round_corners);
			km.setTextColor(Color.BLACK);
			distance = distanceSB.getProgress();

			if (distance_in_km) {
				distance = (int) ((distance) * 1.609);
				distance_in_km = false;
				distance_in_miles = true;
			}
			distance = Math.min(distance, 100);
			distanceSB.setProgress((int) distance);
			limitSearchValueTV.setText(String.format("%d Mi", (int) distance));
			break;

		case R.id.menCB:
			if (menCB.isChecked()) {
				showMe = "Men";
			}
			if (menCB.isChecked() && womenCB.isChecked()) {
				showMe = "Men,Women";
			} else {
				showMe = "";
			}
			showMeValueTV.setText(showMe);
			break;
		
		case R.id.womenCB:
			if (womenCB.isChecked()) {
				showMe = "Women";
			}
			if (menCB.isChecked() && womenCB.isChecked()) {
				showMe = "Men,Women";
			} else
				showMe = "";
			showMeValueTV.setText(showMe);
			break;
		}

		distanceSB.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				
				if (distance_in_km)
					limitSearchValueTV.setText(String.format("%d Km",
							distanceSB.getProgress()));
				else
					limitSearchValueTV.setText(String.format("%d Mi",
							distanceSB.getProgress()));

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

		});

	}
}