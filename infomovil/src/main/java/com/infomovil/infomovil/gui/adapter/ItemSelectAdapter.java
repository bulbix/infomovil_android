
package com.infomovil.infomovil.gui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.widget.URLImageView;
import com.infomovil.infomovil.model.ItemSelectModel;
import com.squareup.picasso.Picasso;

public class ItemSelectAdapter extends BaseAdapter {
	
	List<ItemSelectModel> videosList;
	private LayoutInflater inflate;
	private Context context;
	
	public ItemSelectAdapter(Context context, List<ItemSelectModel> videos) {
		videosList = videos;
		this.notifyDataSetChanged();
		this.context = context;
		inflate = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return videosList.size();
	}

	@Override
	public Object getItem(int position) {
		return videosList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflate.inflate(R.layout.list_item_layout, null);
		}
		ImageView thumb = (ImageView) convertView.findViewById(R.id.userVideoThumbImageView);
		TextWithFont title = (TextWithFont) convertView.findViewById(R.id.userVideoTitleTextView);
		ItemSelectModel video = videosList.get(position);
		Picasso.with(context).load(video.getLinkImagen()).into(thumb);
		title.setText(video.getTitulo());
		return convertView;
	}

}
