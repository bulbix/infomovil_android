package com.infomovil.infomovil.gui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.StringUtils;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.model.CallingCodes;

public class CountryListAdapter extends ArrayAdapter<CallingCodes> implements
		Filterable {

	private Context context;
	private List<CallingCodes> arrayCodes = new ArrayList<CallingCodes>();
	private Filter mFilter;
	private String tipoVista;

	public CountryListAdapter(Context context, List<CallingCodes> arrayCodes, String tipoVista) {
		super(context, R.layout.list_item_country_midle, arrayCodes);
		this.context = context;
		this.arrayCodes = arrayCodes;
		this.tipoVista = tipoVista;
		Log.d("infoLog", "Creando el adapter de la lista");
	}
	@Override
	public int getCount() {
		return arrayCodes.size();
	}
	@Override
	public CallingCodes getItem(int position) {
		return arrayCodes.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		
			if (rowView == null ) {//|| position == 1
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.list_item_country_midle,
						parent, false);
//			}
				holder = new ViewHolder();
				holder.txtPais = (TextWithFont) rowView
						.findViewById(R.id.labelPais);
				holder.txtCodigo = (TextWithFont) rowView
						.findViewById(R.id.labelCodigoPais);
				if (this.tipoVista.equals("publicar")) {
					holder.txtCodigo.setVisibility(View.GONE);
				}
				rowView.setTag(holder);
			} else {
				holder = (ViewHolder) rowView.getTag();
			}
				if (arrayCodes.size() > position) {
//					if (arrayCodes.get(position) != null) {
					holder.txtPais.setText(arrayCodes.get(position).getCountryName());
					holder.txtCodigo.setText(arrayCodes.get(position).getPhoneCode());
//					}
//					else {
//						txtPais.setText("");
//						txtCodigo.setText("");
//					}
				}
			
			
	 
			rowView.setFocusable(false);
			rowView.setClickable(false);
			return rowView;
			
		
	}
	
	public CountryListAdapter getAdapter() {
		return this;
	}

	public List<CallingCodes> getCallingCodes() {
		return arrayCodes;
	}
	
	public void setCallingCodes(List<CallingCodes> paises) {
		arrayCodes = paises;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ViewHolder {
		TextWithFont txtPais, txtCodigo;
	}


	private class ArrayFilter extends Filter {

		private final Object lock = new Object();
		private List<CallingCodes> listaOriginal;

		@SuppressLint("DefaultLocale") 
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
//			clear();
			if (listaOriginal == null) {
				synchronized (lock) {
					listaOriginal = new ArrayList<CallingCodes>(arrayCodes);
				}
			}

			if (constraint == null || constraint.length() == 0) {
				synchronized (lock) {
					List<CallingCodes> list = new ArrayList<CallingCodes>(
							listaOriginal);
					results.values = list;
					results.count = list.size();
				}
			} else {
				final String prefixString = StringUtils.stripAccents(constraint.toString().toLowerCase());
				List<CallingCodes> values = listaOriginal;
				int count = values.size();
				ArrayList<CallingCodes> newValues = new ArrayList<CallingCodes>();
				for (int i = 0; i < count; i++) {
					CallingCodes code = values.get(i);
					if (StringUtils.stripAccents(code.getCountryName().toLowerCase())
							.contains(prefixString)) {
						newValues.add(code);
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			arrayCodes = (List<CallingCodes>) results.values;
			if (results.count > 0) {
			     getAdapter().notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}
}
