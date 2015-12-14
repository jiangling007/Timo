package com.example.timoplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SpalishActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler x = new Handler();
		x.postDelayed(new splashhander(), 2000);
	}
	
	class splashhander implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startActivity(new Intent(SpalishActivity.this,ListViewActivity.class));
			SpalishActivity.this.finish();
		}
	}
}
