package com.my.lovelytv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Data_Adapter extends BaseAdapter {

    ImageLoader imgLoader;
    Context context;
    LayoutInflater inflater;
    private List<Data_get_or_set> livetvItems;

    public Data_Adapter(Context context, List<Data_get_or_set> arraylist)
    {
        imgLoader = new ImageLoader(context);
        this.context = context;
        this.livetvItems = arraylist;
    }
 
    @Override
	public int getCount() {
		return livetvItems.size();
	}
    
   
    @Override
	public View getView(int position, View view, ViewGroup parent) 
    {
		// TODO Auto-generated method stub
       
        LayoutInflater abc = ((Activity) context).getLayoutInflater();
		View itemView = abc.inflate(R.layout.home_page_item, null,true);
		final Data_get_or_set m = livetvItems.get(position);


        ImageView image1 = itemView.findViewById(R.id.image);
        TextView name = itemView.findViewById(R.id.name);

        Picasso.with(context).load(m.image()).transform(new CircleTransform()).into(image1);
        name.setText(m.name());

        return itemView;
    }

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}