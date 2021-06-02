package com.mygdx.game;


import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.lines.itschool.MyGame;


public class AndroidLauncher extends AndroidApplication {
static int C=9;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer=false;
		config.useCompass=false;
		DisplayMetrics metrics  = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 double inches = metrics.widthPixels*metrics.heightPixels/metrics.densityDpi;
		 double k =metrics.heightPixels/metrics.widthPixels;
		 double inch=Math.sqrt(inches/k);
		 double physicsdensity=Math.sqrt(k*inch*inch+inch*inch);
		//Gdx.app.log(" "+inches+" "+inch+" "+k+" "+physicsdensity,"ssssssssssssssssssssssssssssssssss");
		//вычисление физичекой диагонали и определеление размера линиий
		 if (physicsdensity>7){
			C=12;
		 } else {
			 C=9;
		 }




		initialize(new MyGame(C,physicsdensity), config);
	}
}
