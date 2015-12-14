package com.example.timoplayer;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private List<VideoInfo> lists;
	private Context context;
	
	public MyAdapter(List<VideoInfo> lists, Context context) {
		this.lists = lists;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public VideoInfo getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
			holder = new ViewHolder();
			holder.nametv = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.nametv.setText(lists.get(position).getName()+lists.get(position).getPath());
		holder.image.setImageBitmap(getVideoThumbnail(lists.get(position).getPath()));
		
		return convertView;
	}

	private static class ViewHolder{
		TextView nametv;
		ImageView image;
	}
	
	
	public Bitmap getVideoThumbnail(String filePath) {  
        Bitmap bitmap = null;  
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
            retriever.setDataSource(filePath);  
            bitmap = retriever.getFrameAtTime();  
        }   
        catch(IllegalArgumentException e) {  
            e.printStackTrace();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }   
        finally {  
            try {  
                retriever.release();  
            }   
            catch (RuntimeException e) {  
                e.printStackTrace();  
            }  
        }  
        return bitmap;  
    }  
}
