package com.infomovil.infomovil.gui.adapter;

import java.io.File;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.android.lib.toolbox.ToolBox;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.common.utils.StreamUtils;
import com.infomovil.infomovil.gui.common.TextWithFont;
import com.infomovil.infomovil.gui.fragment.editar.GaleriaPaso2Activity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;

public class ImagenesAdapter extends ArrayAdapter<WS_ImagenVO> {
	
	private List<WS_ImagenVO> adapter = new Vector<WS_ImagenVO>();
	private Context context;
	private boolean editar;
	RelativeLayout layoutCelda;

	public ImagenesAdapter(Context context, List<WS_ImagenVO> adapter) {
		super(context, R.layout.celda_galeria_layout, adapter);
		this.context = context;
		this.adapter = adapter;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		ViewHolder holder;
		if (rowView == null) {
			LayoutInflater mInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = mInflate.inflate(R.layout.celda_galeria_layout, parent, false);
			layoutCelda = (RelativeLayout) rowView.findViewById(R.id.layout_celda);
			holder = new ViewHolder();
			
			holder.imagenView = (ImageView) rowView.findViewById(R.id.bitmapImagenGaleria);
			holder.tituloImagen = (TextWithFont) rowView.findViewById(R.id.textoTituloFoto);
			holder.imagenEdicion = (ImageView) rowView.findViewById(R.id.drag_handleImage);
			rowView.setTag(holder);
		}
		else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		WS_ImagenVO imagenActual = adapter.get(position);
		if (imagenActual.getImagenPath() != null && imagenActual.getImagenPath().length() > 0) {
			try {
				holder.imagenView.setImageBitmap(ToolBox.media_getBitmapFromFile(new File(imagenActual.getImagenPath())));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//holder.imagenView.setImageBitmap(StreamUtils.decodeBase64(imagenActual.getImagenClobGaleria()));
			new StreamUtils.DownloadImageTask(holder.imagenView, context, imagenActual).execute(imagenActual.getUrl());
			
		}
		holder.tituloImagen.setText(imagenActual.getDescripcionImagen());
		if (editar) {
			holder.imagenEdicion.setVisibility(View.VISIBLE);
		}
		else {
			holder.imagenEdicion.setVisibility(View.GONE);
		}
		rowView.setFocusable(false);
		rowView.setClickable(false);
		rowView.setFocusableInTouchMode(false);
		
		
		if(CuentaUtils.isDowngrade() ){
			int maxNumImagenesGratis = CuentaUtils.getStatusDomain(context.getString(R.string.txtGaleriaImagenesTitulo),
					datosUsuario.getItemsDominioGratuito()).getEstatusItem();
			if(position >= maxNumImagenesGratis){
				rowView.setBackgroundColor(Color.parseColor("#D3D3D3"));
			}
			else{
				rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}
		
		
		
		return rowView;
	}
	
	public List<WS_ImagenVO> getListAdapter() {
		return adapter;
	}
	
	public void setListAdapter(List<WS_ImagenVO> array) {
		adapter = array;
	}
	 
	
	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}


	private class ViewHolder {
		ImageView imagenView, imagenEdicion;
		TextWithFont tituloImagen, widthLabel, heightLabel;
	}

}
