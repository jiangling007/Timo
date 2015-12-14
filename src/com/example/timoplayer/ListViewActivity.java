package com.example.timoplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewActivity extends Activity implements OnItemClickListener,OnClickListener{
	private ListView lv;
	private MyAdapter adapter;
	private List<VideoInfo> lists = new ArrayList<VideoInfo>();
	private Button choose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_layout);
		
		GetVideo();
		
		lv = (ListView) findViewById(R.id.list_view);
//		choose = (Button) findViewById(R.id.choose);
		adapter = new MyAdapter(lists, this);
		
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
//		choose.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String path = adapter.getItem(position).getPath();
		
		Intent intent = new Intent(ListViewActivity.this,VideoViewActivity.class);
		intent.putExtra("path", path);
		
		startActivity(intent);
	}
	
	public void GetVideo(){
		String name, path;
		VideoInfo videoInfo;

		Cursor cursor = getContentResolver().query(  
		        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,  
		        MediaStore.Video.Media.DEFAULT_SORT_ORDER); 
		System.out.println("cursor: "+cursor.getCount());
		if(cursor.moveToFirst()){  
            do{  
            	System.out.println("geshuwei   "+cursor.getCount());
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
    			path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                  
    			videoInfo = new VideoInfo(name, path);
    			Log.d("finish", name+"  "+path);
    			lists.add(videoInfo);   
                  
            }while(cursor.moveToNext());  
        }  
		
		cursor.close();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showFileChooser();
	}

	private void showFileChooser() {  
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	    intent.setType("*/*");  
	    intent.addCategory(Intent.CATEGORY_OPENABLE);  
	    try {  
	        startActivityForResult(Intent.createChooser(intent, "请选择要打开的文件"),  
	                1);  
	    } catch (android.content.ActivityNotFoundException ex) {  
	        // Potentially direct the user to the Market with a Dialog  
	        Toast.makeText(getApplicationContext(), "请安装文件管理器", Toast.LENGTH_SHORT)  
	                .show();  
	    }  
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if (resultCode == Activity.RESULT_OK) {  
            String path = data.getDataString();  
            
            Intent intent = new Intent(ListViewActivity.this,VideoViewActivity.class);
    		intent.putExtra("path", path);
    		
    		startActivity(intent);
		}
	}
	
}
