package com.infomovil.infomovil.gui.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.infomovil.infomovil.gui.adapter.ItemSelectAdapter;
import com.infomovil.infomovil.model.ItemSelectModel;

public class ItemListView extends GridView {
	

	public ItemListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ItemListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ItemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setVideos(List<ItemSelectModel> items) {
		ItemSelectAdapter adapter = new ItemSelectAdapter(getContext(), items);
		setAdapter(adapter);
	}


}
