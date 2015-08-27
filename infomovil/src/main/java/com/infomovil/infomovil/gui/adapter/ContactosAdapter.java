package com.infomovil.infomovil.gui.adapter;

import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.widget.sortlist.DragSortListView.EnableContactListener;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.TipoContacto;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;

@SuppressLint("ResourceAsColor")
public class ContactosAdapter extends ArrayAdapter<WS_RecordNaptrVO> {
	
	private List<WS_RecordNaptrVO> adapter = new Vector<WS_RecordNaptrVO>();
	private Context context;
	private boolean editar;
	private EnableContactListener contactDelegate;
	RelativeLayout layoutCelda;
	
	public ContactosAdapter(Context context, List<WS_RecordNaptrVO> adapter, boolean editar, EnableContactListener delegate) {
		super(context, R.layout.celda_contacto_layout, adapter);
		this.context = context;
		this.adapter = adapter;
		this.setEditar(editar);
		this.contactDelegate = delegate;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		Resources res = context.getResources();
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		Vector<TipoContacto> vectorTipo = datosUsuario.getArrayTipoContacto();
		if (rowView == null) {
			LayoutInflater mInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = mInflate.inflate(R.layout.celda_contacto_layout, parent, false);
			layoutCelda = (RelativeLayout) rowView.findViewById(R.id.layout_celda);
			
			
			holder = new ViewHolder();
			holder.imagenTipo = (ImageView) rowView.findViewById(R.id.imagenTipo);
			//RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(60,80);
			
			//holder.imagenTipo.setLayoutParams(parms);
			holder.txtNumero = (TextWithFont) rowView.findViewById(R.id.labelValorContacto);
			holder.txtDescripcion = (TextWithFont) rowView.findViewById(R.id.labelDescripcionContacto);
			holder.botonActivo = (ToggleButton) rowView.findViewById(R.id.toggleActivar);
			holder.imagenEdicion = (ImageView) rowView.findViewById(R.id.drag_handle);
			rowView.setTag(holder);
		}
		else {
			holder = (ViewHolder) rowView.getTag();
		}
		WS_RecordNaptrVO contacto = adapter.get(position);
		int resId = res.getIdentifier(vectorTipo.get(contacto.getIdTipoContacto()).getImagen(), "drawable", context.getPackageName());
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			holder.imagenTipo.setBackgroundDrawable(res.getDrawable(resId));
		}
		else {
			holder.imagenTipo.setBackground(res.getDrawable(resId));
		}

		if (contacto.getIdTipoContacto() == 1) {
			if (contacto.getCodCountry().equals("+52")) {
				holder.txtNumero.setText(contacto.getNoContacto().substring(1,contacto.getNoContacto().length()));
			}else{
				holder.txtNumero.setText(contacto.getNoContacto());
			}
		}else{
			holder.txtNumero.setText(contacto.getNoContacto());
		}
		
		holder.txtDescripcion.setText(contacto.getLongLabelNaptr());
		if (contacto.getVisible() == 1) {
			holder.botonActivo.setChecked(true);
		}
		else {
			holder.botonActivo.setChecked(false);
		}
		if (editar) {
			holder.imagenEdicion.setVisibility(View.VISIBLE);
			holder.botonActivo.setVisibility(View.GONE);
		}
		else {
			holder.imagenEdicion.setVisibility(View.GONE);
			holder.botonActivo.setVisibility(View.VISIBLE);
		}
		final int posAux = position;
		holder.botonActivo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "text clicked" + posAux, Toast.LENGTH_SHORT).show();
				contactDelegate.enable(posAux);
			}
		});
		
		rowView.setFocusable(false);
		rowView.setClickable(false);
		rowView.setFocusableInTouchMode(false);
		
		if(CuentaUtils.isDowngrade() ){
			int maxNumContactosGratis = CuentaUtils.getStatusDomain(context.getResources().getString(R.string.txtContacto),
					datosUsuario.getItemsDominioGratuito()).getEstatusItem();
			if(position >= maxNumContactosGratis){
				rowView.setBackgroundColor(Color.parseColor("#D3D3D3"));
				holder.botonActivo.setEnabled(false); 
			}
			else{
				rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
				holder.botonActivo.setEnabled(true); 
			}
		}
		
		
		return rowView;
	}
	
	
	private class ViewHolder {
		ImageView imagenTipo, imagenEdicion;
		TextWithFont txtNumero, txtDescripcion;
		ToggleButton botonActivo;
	}
	
	public List<WS_RecordNaptrVO> getListAdapter() {
		return this.adapter;
	}
	
	public void activarContacto(View v) {
		Log.d("infoLog", "Desactivando/Activando Contacto");
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}
}
