package com.example.timoplayer;

import android.graphics.Bitmap;


public class VideoInfo {
	
	private String name;
	private String path;
	private Bitmap bitmap;
	
	public VideoInfo(String name, String path, Bitmap bitmap) {
		this.name = name;
		this.path = path;
		this.bitmap = bitmap;
	}
	
	public VideoInfo(String name, String path) {
		this.name = name;
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
