package com.infomovil.infomovil.gui.widget;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.BasicItem;
import com.infomovil.infomovil.gui.common.ListItem;
import com.infomovil.infomovil.gui.common.ViewItem;
import com.infomovil.infomovil.model.DatosUsuario;



public class TableView extends LinearLayout {
	
	public enum TableType {
		TableTypeClassic,
		TableTypeCountry
	}
	
	private int mIndexController = 0;
	private LayoutInflater mInflater;
	private LinearLayout mMainContainer;
	private LinearLayout mListContainer;
	private List<ListItem> mItemList;
	private ClickListener mClickListener;
	private Context ctx;
	private TableType tableType;
	Resources res;
	private boolean editing;
	private DatosUsuario datosUsuario;

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = getResources();
		mItemList = new ArrayList<ListItem>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainContainer = (LinearLayout)  mInflater.inflate(R.layout.list_container, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addView(mMainContainer, params);				
		mListContainer = (LinearLayout) mMainContainer.findViewById(R.id.buttonsContainer);	
		ctx = context;
		datosUsuario = DatosUsuario.getInstance();
	}
	 
	/**
	 * 
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(String title) {
		mItemList.add(new BasicItem(title));
	}
	
	public void addBasicItem(String title, boolean editado) {
		mItemList.add(new BasicItem(title, editado));
	}
	
	/**
	 * 
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(String title, String summary) {
		mItemList.add(new BasicItem(title, summary));
	}
	
	/**
	 * 
	 * @param title
	 * @param summary
	 * @param color
	 */
	public void addBasicItem(String title, String summary, int color) {
		mItemList.add(new BasicItem(title, summary, color));
	}
	
	/**
	 * 
	 * @param drawable
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(int drawable, String title, String summary) {
		mItemList.add(new BasicItem(drawable, title, summary));
	}
	
	/**
	 * 
	 * @param drawable
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(int drawable, String title, String summary, int color) {
		mItemList.add(new BasicItem(drawable, title, summary, color));
	}
	
	/**
	 * 
	 * @param item
	 */
	public void addBasicItem(BasicItem item) {
		mItemList.add(item);
	}
	
	/**
	 * 
	 * @param itemView
	 */
	public void addViewItem(ViewItem itemView) {
		mItemList.add(itemView);
	}
	
	public void commit() {
		mIndexController = 0;
		Typeface myTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/Avenir LT 45 Book.ttf");
		if (tableType == TableType.TableTypeClassic) {
			if(mItemList.size() > 1) {
				//when the list has more than one item
				for(ListItem obj : mItemList) {
					View tempItemView;
					if(mIndexController == 0) {
						tempItemView = mInflater.inflate(R.layout.list_item_top, null);
					}
					else if(mIndexController == mItemList.size()-1) {
						tempItemView = mInflater.inflate(R.layout.list_item_bottom, null);
					}
					else {
						tempItemView = mInflater.inflate(R.layout.list_item_middle, null);
					}
					TextView txtTitulo = (TextView)tempItemView.findViewById(R.id.title);
					txtTitulo.setTypeface(myTypeface);
					setupItem(tempItemView, obj, mIndexController);
					tempItemView.setClickable(obj.isClickable());
					mListContainer.addView(tempItemView);
					mIndexController++;
				}
			}
			else if(mItemList.size() == 1) {
				//when the list has only one item
				View tempItemView = mInflater.inflate(R.layout.list_item_single, null);
				ListItem obj = mItemList.get(0);
				setupItem(tempItemView, obj, mIndexController);
				tempItemView.setClickable(obj.isClickable());
				mListContainer.addView(tempItemView);
			}
		}
		else if(tableType == TableType.TableTypeCountry) {
			if(mItemList.size() > 1) {
				for(ListItem obj : mItemList) {
					View tempItemView;
					if(mIndexController == 0) {
						tempItemView = mInflater.inflate(R.layout.list_item_country_top, null);
					}
					else if(mIndexController == mItemList.size()-1) {
						tempItemView = mInflater.inflate(R.layout.list_item_country_bottom, null);
					}
					else {
						tempItemView = mInflater.inflate(R.layout.list_item_country_midle, null);
					}
					setupBasicItemCountry(tempItemView, (BasicItem)obj, mIndexController);
					tempItemView.setClickable(obj.isClickable());
					mListContainer.addView(tempItemView);
					mIndexController++;
				}
			}
		}
		
	}
	
	private void setupItem(View view, ListItem item, int index) {
		if(item instanceof BasicItem) {
			BasicItem tempItem = (BasicItem) item;
			setupBasicItem(view, tempItem, mIndexController);
		}
		else if(item instanceof ViewItem) {
			ViewItem tempItem = (ViewItem) item;
			setupViewItem(view, tempItem, mIndexController);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param item
	 * @param index
	 */
	private void setupBasicItem(View view, BasicItem item, int index) {
		if(item.isEditado()) {
			((ImageView) view.findViewById(R.id.image)).setBackgroundResource(R.drawable.check); //setBackgroundResource(item.getDrawable());
			((TextView) view.findViewById(R.id.title)).setTextColor(res.getColor(R.color.colorFuenteAzul));
		}
		else {
			((ImageView) view.findViewById(R.id.image)).setBackgroundResource(R.drawable.circulocheck);
			((TextView) view.findViewById(R.id.title)).setTextColor(res.getColor(R.color.colorFuenteVerde));
		}
		if(item.getSubtitle() != null) {
			((TextView) view.findViewById(R.id.subtitle)).setText(item.getSubtitle());
		} 
		
		else { 
			((TextView) view.findViewById(R.id.subtitle)).setVisibility(View.GONE);
		}		
		((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
//		if(item.getColor() > -1) {
//			((TextView) view.findViewById(R.id.title)).setTextColor(item.getColor());
//		}
		
//		if(!CuentaProUtils.isCuentaPro() && item.getTitle().equalsIgnoreCase("Video")) {
//			view.setVisibility(View.GONE);
//		}
		
		if(item.getTitle().equalsIgnoreCase(getResources().getString(R.string.txtColorFondo))) {
			view.setVisibility(View.GONE);
		}
		
		
		
		view.setTag(index);
		if(item.isClickable()) {
			view.setOnClickListener( new View.OnClickListener() {
	
				@Override
				public void onClick(View view) {
					if(mClickListener != null)
						mClickListener.onClick((Integer) view.getTag());
				}
				
			});	
		}
		else {
			((ImageView) view.findViewById(R.id.chevron)).setVisibility(View.GONE);
		}
	}
	
	private void setupBasicItemCountry(View view, BasicItem item, int index) {
		((TextView)view.findViewById(R.id.labelPais)).setText(item.getTitle());
		((TextView)view.findViewById(R.id.labelCodigoPais)).setVisibility(View.VISIBLE);
		((TextView)view.findViewById(R.id.labelCodigoPais)).setText(item.getSubtitle());
		view.setTag(index);
		if(item.isClickable()) {
			view.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(mClickListener != null)
						mClickListener.onClick((Integer) view.getTag());
				}
				
			});	
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param itemView
	 * @param index
	 */
	private void setupViewItem(View view, ViewItem itemView, int index) {
		if(itemView.getView() != null) {
			LinearLayout itemContainer = (LinearLayout) view.findViewById(R.id.itemContainer);
			itemContainer.removeAllViews();
			//itemContainer.removeAllViewsInLayout();
			itemContainer.addView(itemView.getView());
			
			if(itemView.isClickable()) {
		        	itemContainer.setTag(index);
		               	itemContainer.setOnClickListener( new View.OnClickListener() {
		                	@Override
		                    	public void onClick(View view) {
		                        	if(mClickListener != null)
		                            		mClickListener.onClick((Integer) view.getTag());
					}
		                });
		           }
		}
	}
	
	public void startEditing() {
		
	}
	
	
	public interface ClickListener {		
		void onClick(int index);		
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCount() {
		return mItemList.size();
	}
	
	/**
	 * 
	 */
	public void clear() {
		mItemList.clear();
		mListContainer.removeAllViews();
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setClickListener(ClickListener listener) {
		this.mClickListener = listener;
	}
	
	/**
	 * 
	 */
	public void removeClickListener() {
		this.mClickListener = null;
	}

	public TableType getTableType() {
		return tableType;
	}

	public void setTableType(TableType tableType) {
		this.tableType = tableType;
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

}
