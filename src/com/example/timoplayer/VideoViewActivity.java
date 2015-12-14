package com.example.timoplayer;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.R.menu;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class VideoViewActivity extends Activity{

	private String path = "/mnt/sdcard/video.flv";
	private VideoView mVideoView;
	private View mVolumeBrightnessLyout;
	private View pauseLayout;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	
	private int mMaxVolunme;
	private int MVolume = -1;
	private float mBrightness = -1f;
	
	private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
	private GestureDetector mGestureDetector;
	private MediaController mMediaController;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Vitamio.isInitialized(this);
		setContentView(R.layout.videoview_main);
		
		
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		mVolumeBrightnessLyout = findViewById(R.id.operation_volume_brightness);
		pauseLayout = findViewById(R.id.pause_layout);
		mOperationBg = (ImageView) findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolunme = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		
		mVideoView.setVideoPath(path);
		mMediaController = new MediaController(this);
		mVideoView.setMediaController(mMediaController);
		mVideoView.requestFocus();

		mGestureDetector = new GestureDetector(this, new MyGestureListener());
		
	}
	
	private Handler mDismissHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mVolumeBrightnessLyout.setVisibility(View.GONE);
		};
	};
	
	private void endGesture(){
		MVolume = -1;
		mBrightness = -1f;	
		
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageAtTime(0,  500);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private class MyGestureListener extends SimpleOnGestureListener{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
//		Ë«»÷È«ÆÁ
			
//			if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM) {
//				mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
//			}else{
//				mLayout++;
//			}
//			if (mVideoView != null) {
//				mVideoView.setVideoLayout(mLayout, 0);
//			}
			
//		Ë«»÷ÔÝÍ£
			
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				pauseLayout.setVisibility(View.VISIBLE);
			}else {
				pauseLayout.setVisibility(View.INVISIBLE);
				mVideoView.start();
			}
			return true;
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			int x = (int) e2.getRawX();
			Display disp = getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();
				
			if (mOldX > windowWidth * 4.0 / 5){
				onVolumeSlide((mOldY - y) / windowHeight);
			}else if (mOldX < windowWidth / 5.0) {
				onBrightnessSlide((mOldY - y) / windowHeight);
			}else {
				onProcessSlide(x - mOldX);
			}
			
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
		
	}
	
		private void onProcessSlide(float percent){
			
			long index, length;
			length = mVideoView.getDuration();
			
			if (percent > 0){
				
				index = (long) (mVideoView.getCurrentPosition() + 1000);
				
				if (index >= length){
					index = length;
				}
			}else{
				index = (long) (mVideoView.getCurrentPosition() - 1000);
				if (index <= 0) {
					index = 0;
				}
			}
			
			mVideoView.seekTo(index);
			
		}
		
		
		private void onVolumeSlide(float percent){
			if (MVolume == -1){
				MVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				if (MVolume < 0) {
					MVolume = 0;
				}
				
				mOperationBg.setImageResource(R.drawable.video_volumn_bg);
				mVolumeBrightnessLyout.setVisibility(View.VISIBLE);
			}
			
			int index = (int) ((percent * mMaxVolunme) + MVolume);
			if (index > mMaxVolunme) {
				index = mMaxVolunme;
			}else if(index < 0){
				index = 0;
			}
			
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
			
			ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
			lp.width = findViewById(R.id.operation_full).getLayoutParams().width*index/mMaxVolunme;
			mOperationPercent.setLayoutParams(lp);
		}
		
		private void onBrightnessSlide(float percent){
			if (mBrightness < 0){
				mBrightness = getWindow().getAttributes().screenBrightness;
				if (mBrightness <= 0.00f){
					mBrightness = 0.50f;
				}
				if (mBrightness < 0.01f) {
					mBrightness = 0.01f;
				}
				
				mOperationBg.setImageResource(R.drawable.video_brightness_bg);
				mVolumeBrightnessLyout.setVisibility(View.VISIBLE);
			}
			WindowManager.LayoutParams lpa = getWindow().getAttributes();
			lpa.screenBrightness = mBrightness + percent;
			if (lpa.screenBrightness > 1.0f) {
				lpa.screenBrightness = 1.0f;
			}else if(lpa.screenBrightness < 0.01f){
				lpa.screenBrightness = 0.01f;
			}
			getWindow().setAttributes(lpa);
			
			ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
			lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width*lpa.screenBrightness);
			mOperationPercent.setLayoutParams(lp);
		}
		
		
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			// TODO Auto-generated method stub
			if (mVideoView != null) {
				mVideoView.setVideoLayout(mLayout, 0);
			}
			super.onConfigurationChanged(newConfig);
		}

		
	

}
