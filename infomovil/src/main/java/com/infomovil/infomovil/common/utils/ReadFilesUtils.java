package com.infomovil.infomovil.common.utils;

/**
 * @author Sergio Sánchez Flores
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.res.Resources;
import android.util.Log;

import com.infomovil.infomovil.R;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.model.CallingCodes;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.TipoContacto;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
//import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

public class ReadFilesUtils {
	
	public static ArrayList<CallingCodes> leerArchivoCodigos(Resources res) {
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		CallingCodes callAux = null;
		ArrayList<CallingCodes> listaPaises = new ArrayList<CallingCodes>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(res.getAssets().open("callingCodes.xml"));
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName("item");
			for(int i = 0; i < items.getLength(); i++) {
				CallingCodes itemCodes = new CallingCodes();
				Node itemActual = items.item(i);
				NodeList codeData = itemActual.getChildNodes();
				for(int j = 0; j < codeData.getLength(); j++) {
					Node dato = codeData.item(j);
					String etiqueta = dato.getNodeName();
					if (etiqueta.equals("key")) {
						String value = dato.getFirstChild().getNodeValue();
						itemCodes.setCountryCode(value);
						Locale loc = new Locale("",value);
						itemCodes.setCountryName(com.infomovil.infomovil.common.utils.
								StringUtils.eliminarAcentos(loc.getDisplayCountry()));
					}
					else if(etiqueta.equals("code")) {
						itemCodes.setPhoneCode(dato.getFirstChild().getNodeValue());
					}
				}
				if (itemCodes.getCountryName().equals(res.getString(R.string.tituloMexico)) || itemCodes.getCountryName().equals(res.getString(R.string.tituloMexicoAcento))) {
					callAux = itemCodes;
				}
				else {
					listaPaises.add(itemCodes);
				}

				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Collections.sort(listaPaises,  new Comparator<CallingCodes>() {
			@Override
			public int compare(CallingCodes c1, CallingCodes c2) {
				return c1.getCountryName().compareTo(c2.getCountryName());
			}
		});
		if (callAux != null) {
			listaPaises.add(0, callAux);
		}
		datosUsuario.setArrayCodigos(listaPaises);
		
		return listaPaises;
	}
	
	public static Vector<TipoContacto> leerArchivo(Resources res) {
		Vector<TipoContacto> listaTipo = new Vector<TipoContacto>();
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder(); 
				Document dom;
				if (res.getString(R.string.strLanguage).equals("en")) {
					dom = builder.parse(res.getAssets().open("tipoContactoEn.xml"));
				}
				else {
					dom = builder.parse(res.getAssets().open("tipoContacto.xml"));
				}
				Element root = dom.getDocumentElement();
				NodeList items = root.getElementsByTagName("dict");
				for(int i = 0; i < items.getLength(); i++) {
					TipoContacto contactoActual = new TipoContacto();
					Node itemActual = items.item(i);
					NodeList codeData = itemActual.getChildNodes();
					for(int j = 0; j < codeData.getLength(); j++) {
						Node dato = codeData.item(j);
						String etiqueta = dato.getNodeName();
						if(etiqueta.equals("ejemplo")) {
							contactoActual.setPlaceholder(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("mensaje")) {
							contactoActual.setMensaje(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("expresion")) {
							contactoActual.setExpresion(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("subcategoria")) {
							contactoActual.setSubcategoria(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("servicio")) {
							contactoActual.setServicio(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("text")) {
							contactoActual.setTexto(dato.getFirstChild().getNodeValue());
						}
						else if (etiqueta.equals("image")) {
							contactoActual.setImagen(dato.getFirstChild().getNodeValue());
						}
					}
					listaTipo.add(contactoActual);
				}
			}
			catch (IOException e) {
				Log.d("infoLog", "Ocurrio una IOException "+ e.toString());
				e.printStackTrace();
			}
			catch (Exception e) {
				Log.d("infoLog", "Ocurrio una Exception "+ e.toString());
				e.printStackTrace();
			}
			datosUsuario = DatosUsuario.getInstance();
			datosUsuario.setArrayTipoContacto(listaTipo);
		return listaTipo;
		
	}
	
	public static Vector<WS_RecordNaptrVO> acomodaContactos (Vector<WS_RecordNaptrVO> contactoLista) {
		ArrayList<CallingCodes> arrayCodes = new ArrayList<CallingCodes>();
		Vector<TipoContacto> vectorTipo;
		Vector<WS_RecordNaptrVO> vectorAuxiliar = new Vector<WS_RecordNaptrVO>();
		DatosUsuario datos = DatosUsuario.getInstance();
		if(datos.getArrayCodigos() == null || datos.getArrayCodigos().size() == 0) {
			arrayCodes = ReadFilesUtils.leerArchivoCodigos(InfomovilApp.getApp().getResources());
		}
		else {
			arrayCodes = datos.getArrayCodigos();
		}
		if(datos.getArrayTipoContacto() == null || datos.getArrayTipoContacto().size() == 0) {
			vectorTipo = ReadFilesUtils.leerArchivo(InfomovilApp.getApp().getResources());
		}
		else {
			vectorTipo = datos.getArrayTipoContacto();
		}
		Vector<WS_RecordNaptrVO> listaContactos = contactoLista;
		for (int i=0; i < listaContactos.size(); i++) {
			try {
			WS_RecordNaptrVO contactoActual = listaContactos.get(i);
			String arrAux[] = contactoActual.getRegExp().split(":");
			if(arrAux.length > 1) {
				contactoActual.setNoContacto(arrAux[1]);
				if (contactoActual.getNoContacto().startsWith("//")) {
					contactoActual.setNoContacto(contactoActual.getNoContacto().substring(2));
				}
			} 
			else {
				contactoActual.setNoContacto(contactoActual.getRegExp());
			}
			for (int j = 0; j < vectorTipo.size(); j++) {
				try {
				TipoContacto tipoContacto = vectorTipo.get(j);
				if (contactoActual.getServicesNaptr().equals(tipoContacto.getServicio())) {
					if(tipoContacto.getServicio().equals("E2U+web:http")) {
						if (tipoContacto.getSubcategoria().equals(contactoActual.getSubCategory())) {
							contactoActual.setIdTipoContacto(j);
						}
						
					}
					else {
						contactoActual.setIdTipoContacto(j);
					}
				}
				}
				catch (Exception e) {
					e.printStackTrace();
					Log.d("infoLog", "Seguro aqui esta el error 44");
				}
			}
			if (contactoActual.getServicesNaptr().startsWith("E2U+voice") || contactoActual.getServicesNaptr().startsWith("E2U+sms") || contactoActual.getServicesNaptr().startsWith("E2U+fax")) {
				if (contactoActual.getServicesNaptr().startsWith("tel:")) {
					contactoActual.setNoContacto(contactoActual.getNoContacto().substring(4));
				}
				Vector<String> ccTestArray = new Vector<String>();
				int maxLength = contactoActual.getNoContacto().length();
				if (maxLength > 3) {
					ccTestArray.add(contactoActual.getNoContacto().substring(0, 4)); 
					
				}
				if (maxLength > 2) {
					ccTestArray.add(contactoActual.getNoContacto().substring(0, 3));
				}
				if (maxLength > 1) {
					ccTestArray.add(contactoActual.getNoContacto().substring(0, 2));
				} 
				finCiclo:
				for (int k = 0; k < arrayCodes.size(); k++) {
					try {
					String shortCC = ccTestArray.lastElement();
					CallingCodes call = arrayCodes.get(k);
					if (call.getPhoneCode().startsWith(shortCC)) {
						for (String matchCC : ccTestArray) {
							if (call.getPhoneCode().equals(matchCC)) {
								contactoActual.setCodCountry(matchCC);
//								Locale loc = new Locale("", call.ge)
								contactoActual.setPais(call.getCountryName());
								contactoActual.setNoContacto(contactoActual.getNoContacto().substring(matchCC.length()));
								break finCiclo;
							}   
						}
					}
					}
					catch (Exception e) {
						e.printStackTrace();
						Log.d("infoLog", "Seguro aqui esta el error");
					}
				}
			}
			vectorAuxiliar.add(contactoActual);
		}catch (Exception e) {
			e.printStackTrace();
			Log.d("infoLog", "Seguro aqui esta el error 555"); 
		}
		}
		Vector<WS_RecordNaptrVO> arrAux = new Vector<WS_RecordNaptrVO>();
		for (int i = 0; i < vectorAuxiliar.size(); i++) {
			WS_RecordNaptrVO recNaptr = vectorAuxiliar.get(i);
			if (recNaptr.getIdTipoContacto() == 0 || recNaptr.getIdTipoContacto() == 1 || recNaptr.getIdTipoContacto() == 2 || recNaptr.getIdTipoContacto() == 4) {
				recNaptr.setRegExp(vectorTipo.get(recNaptr.getIdTipoContacto()).getExpresion()+ recNaptr.getCodCountry() +recNaptr.getNoContacto()+"!");
			}
			else {
				recNaptr.setRegExp(vectorTipo.get(recNaptr.getIdTipoContacto()).getExpresion()+recNaptr.getNoContacto()+"!");
			}
			arrAux.add(recNaptr);
		}
//		datos.setListaContactos(vectorAuxiliar);
		return arrAux;
		
	} 
	
	/****
	 * 
	 * @param res
	 * @param keyword
	 * @return
	 */
	public static boolean modificoHorarios(Resources res, WS_KeywordVO keyword){	
		
		String tmphorariosCero = "|%s|00:00 - 00:00|%s|00:00 - 00:00|%s|00:00 - 00:00|%s|00:00 - 00:00|"
				+ "%s|00:00 - 00:00|%s|00:00 - 00:00|%s|00:00 - 00:00|";
	
		String horariosCero = String.format(tmphorariosCero,
				res.getString(R.string.horarioLun),
				res.getString(R.string.horarioMar),
				res.getString(R.string.horarioMie),
				res.getString(R.string.horarioJue),
				res.getString(R.string.horarioVie),
				res.getString(R.string.horarioSab),
				res.getString(R.string.horarioDom));
		
		String horariosCero2 = String.format(tmphorariosCero,
				res.getString(R.string.horarioLun),
				res.getString(R.string.horarioMar),
				res.getString(R.string.horarioMie2),
				res.getString(R.string.horarioJue),
				res.getString(R.string.horarioVie),
				res.getString(R.string.horarioSab),
				res.getString(R.string.horarioDom));	
		
		String horariosCero3 = String.format(tmphorariosCero,
				res.getString(R.string.horarioLun),
				res.getString(R.string.horarioMar),
				res.getString(R.string.horarioMie3),
				res.getString(R.string.horarioJue),
				res.getString(R.string.horarioVie),
				res.getString(R.string.horarioSab2),
				res.getString(R.string.horarioDom));
		
		String horariosCero4 = String.format(tmphorariosCero,
				res.getString(R.string.horarioLun),
				res.getString(R.string.horarioMar),
				res.getString(R.string.horarioMie2),
				res.getString(R.string.horarioJue),
				res.getString(R.string.horarioVie),
				res.getString(R.string.horarioSab2),
				res.getString(R.string.horarioDom));
		
		String lang = Locale.getDefault().getLanguage();
		
		if(lang.equals("en")){
			keyword.setKeywordValue(keyword.getKeywordValue().replace("Lun","Mon").replace("Mar","Thu")
					.replace(res.getString(R.string.horarioMie),"Wen").replace("Jue","Tue")
					.replace("Vie","Fri").replace(res.getString(R.string.horarioSab),"Sat")
					.replace("Dom","Sun"));
		}
		else if(lang.equals("es")){
			keyword.setKeywordValue(keyword.getKeywordValue().replace("Mon","Lun").replace("Thu","Mar")
					.replace("Wen",res.getString(R.string.horarioMie)).replace("Tue","Jue")
					.replace("Fri","Vie").replace("Sat",res.getString(R.string.horarioSab))
					.replace("Sun","Dom"));
		}
		
		return !keyword.getKeywordValue().equals(horariosCero) &&
				!keyword.getKeywordValue().equals(horariosCero2) &&
				!keyword.getKeywordValue().equals(horariosCero3) && 
				!keyword.getKeywordValue().equals(horariosCero4);	
	}
	
	
public static Map<String,Boolean> buscaEditados(Resources res) {
		
		Map<String,Boolean> result = new HashMap<String,Boolean>();
		boolean arregloEdicion[] = new boolean[13];
		
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		
		WS_DomainVO domAux = datosUsuario.getDomainData();
		
		if (datosUsuario.getDomainData().getColour() != null && datosUsuario.getDomainData().getColour().length() > 5) {
			arregloEdicion[0] = true;
		}
		
		
		if (domAux != null) {
			if (domAux.getTextRecord()!= null && domAux.getTextRecord().length() > 0 && !domAux.getTextRecord().equals("(null)") && !domAux.getTextRecord().equals(res.getString(R.string.tituloTituloAcento))) {
				result.put(res.getString(R.string.txtNombreoEmpresa),true);
				arregloEdicion[1] = true;
			}
			if (domAux.getDisplayString() != null && domAux.getDisplayString().length() > 0 && !domAux.getDisplayString().equals(" ") && !domAux.getDisplayString().equals("(null)") && !domAux.getDisplayString().equals("Título")) {
				result.put(res.getString(R.string.txtDescripcionCortaTitulo),true);
				arregloEdicion[4] = true;
			}
			if (domAux.getColour() != null && domAux.getColour().length() > 3) {
				datosUsuario.setEligioColor(true);
			}
		}
		WS_ImagenVO imagenAux = datosUsuario.getImagenLogo();
		if (imagenAux != null && ((imagenAux.getUrl() != null && imagenAux.getUrl().length() > 1) || (imagenAux.getImagenPath() != null && imagenAux.getImagenPath().length() > 1))) {
			result.put(res.getString(R.string.txtLogo),true);
			arregloEdicion[2] = true;
		}
		
		if(datosUsuario.getListaContactos() != null && datosUsuario.getListaContactos().size() > 0) {
			result.put(res.getString(R.string.txtContacto),true);
			arregloEdicion[5] = true;
		}		
		
		if(datosUsuario.getCoordenadasUbicacion() != null && datosUsuario.getCoordenadasUbicacion().getLatitudeLoc() != 0.0) {
			result.put(res.getString(R.string.txtMapaTitutlo),true);
			result.put(res.getString(R.string.txtMapaTituloOpcional),true);
			arregloEdicion[6] = true;
		}
		
		if(datosUsuario.getVideoSeleccionado()!= null && !StringUtils.isEmpty(datosUsuario.getVideoSeleccionado().getLinkVideo())) {
			result.put(res.getString(R.string.txtTituloVideo),true);
			arregloEdicion[7] = true;
		}		
		
		if(datosUsuario.getPromocionDominio() != null && datosUsuario.getPromocionDominio().getIdOffer() > 0) {
			result.put(res.getString(R.string.txtPromociones),true);
			arregloEdicion[8] = true;
		}
		if(datosUsuario.getListaImagenes() != null && datosUsuario.getListaImagenes().size() > 0) {
			result.put(res.getString(R.string.txtGaleriaImagenesTitulo),true);
			arregloEdicion[9] = true;
		}
		if (datosUsuario.getArregloPerfil() != null) {
			Vector<WS_KeywordVO> auxPerfil = datosUsuario.getArregloPerfil();
			boolean arrAuxPerfil[] = {false, false, false, false, false, false};
			
			for (int i = 0; i < auxPerfil.size(); i++) {
				
				WS_KeywordVO auxKey = auxPerfil.get(i);		
				
				if(auxKey.getKeywordValue() != null){
					
					if(auxKey.getKeywordValue().length() > 0)  {
						
						if(auxKey.getKeywordField().equals("np"))  {
							result.put(res.getString(R.string.negocioProfesion),true);
							arregloEdicion[3] = true;
						}
						else if(auxKey.getKeywordField().equals("oh"))  {
							
							if(	modificoHorarios(res,auxKey)){
								arrAuxPerfil[i] = true;
								result.put(res.getString(R.string.txtPerfilTitulo),true);
								arregloEdicion[10] = true;
							}
							else {
								arrAuxPerfil[i] = false; 
							}
						}
						else{
							arrAuxPerfil[i] = true;
							result.put(res.getString(R.string.txtPerfilTitulo),true);
							arregloEdicion[10] = true;							
						}
						
					}					
				}
			}
			
			String arrayTitulosPerfil[] = {res.getString(R.string.productoServicio),
					res.getString(R.string.areaServicio), 
					res.getString(R.string.horario),
					res.getString(R.string.metodosPago), 
					res.getString(R.string.asociaciones), 
					res.getString(R.string.biografia) };
			
			int index = 0;
			for(boolean estatusPerfil: arrAuxPerfil){
				result.put(arrayTitulosPerfil[index++],estatusPerfil);
			}
			
			datosUsuario.setEstatusPerfil(arrAuxPerfil);
		}
		if (datosUsuario.getArregloDireccion() != null) {
			Vector<WS_KeywordVO> auxDireccion = datosUsuario.getArregloDireccion();
			for (int i = 0; i < auxDireccion.size(); i++) {
				WS_KeywordVO auxKey = auxDireccion.get(i);
				if (auxKey.getKeywordValue() != null && auxKey.getKeywordValue().length() > 0) {
					result.put(res.getString(R.string.txtTituloDireccion),true);
					arregloEdicion[11] = true;
					break;
				}
			}
		}
		if(datosUsuario.getArregloInformacionAdicional() != null && datosUsuario.getArregloInformacionAdicional().size() > 0) {
			result.put(res.getString(R.string.txtTituloInfoAdicional),true);
			arregloEdicion[12] = true;
		}
		
		datosUsuario.setEstatusEdicion(arregloEdicion);
		
		return result;
	}
}
