package com.infomovil.infomovil.gui.fragment.principal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;

import com.google.android.gms.plus.PlusShare;
import com.infomovil.infomovil.R;
import com.infomovil.infomovil.common.utils.ButtonStyleShow;
import com.infomovil.infomovil.common.utils.CuentaUtils;
import com.infomovil.infomovil.gui.common.AlertView;
import com.infomovil.infomovil.gui.common.AlertView.AlertViewType;
import com.infomovil.infomovil.gui.common.interfaces.AlertViewInterface;
import com.infomovil.infomovil.gui.principal.InfomovilActivity;
import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.model.HorariosModel;
import com.infomovil.infomovil.model.TipoContacto;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_OffertRecordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
import com.madrobot.text.StringUtils;

public class VistaPrevioDominio extends InfomovilActivity {

	private WebView vistaPrevia;	
	Activity context;
	AlertViewInterface delegado;
	String nombreDominio ="", mailto = "";
	boolean vistaPreviaWeb;
	
	
	public String getStringHTML() {
		datosUsuario = DatosUsuario.getInstance();
		Resources res = getResources();

		
		mailto = "<a id=\"link\" href=\"mailto:%1$s?:&subject=Informacion&body=Hola, estoy interesado en recibir mas informacion de tus productos y / o servicios anunciados %2$s\" >";

		try{
			nombreDominio = "en http:// " + getResources().getString(R.string.txtWWWGeneral)+
			datosUsuario.getDomainData().getDomainName()+getResources().getString(R.string.txtTelGeneral);
		}
		catch(Exception e){
			nombreDominio = "";
		}
		

		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
				+ "<html>"
				+ "<head>"
				+ "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\" />"
				+ "<meta http-equiv=\"Cache-Control\" content=\"max-age=3600\"/>"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />"
				
				+ "<link href=\"http://info-movil.net/images/favicon.ico\" rel=\"icon\" type=\"image/x-icon\" />"
				+ "<link href=\"css/NewStyle.css\"  rel=\"stylesheet\" type=\"text/css\"/>"
				+ "<link href=\"css/NewSprite.css\"  rel=\"stylesheet\" type=\"text/css\"/>  "

				+ "<style type=\"text/css\" > .hide { display:none; } </style>"
				+ "<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js\"></script>"
				+ "<script type=\"text/javascript\" src=\"https://maps.google.com/maps/api/js?sensor=false\"></script>"
				+ "<script type=\"text/javascript\" src=\"https://raw.github.com/HPNeo/gmaps/master/gmaps.js\"></script>"
				+ "<script type=\"text/javascript\">"
				+ "function main(){init();} function init() {$('#mapa').toggle('fast');"
				+ "}</script>"
				+ "<script type=\"text/javascript\" async src=\"http://info-movil.net/js/functions.js\"></script>"
				+ "<title>pruebadominio</title>");

		builder.append("<meta name=\"name\" content=\""
				+ datosUsuario.getDomainData().getDomainName()
				+ "\">"
				+ "<meta name=\"descripcion\" content=\""
				+ datosUsuario.getDomainData().getDisplayString()
				+ "\">"
				+ "<meta property=\"og:title\" content=\""
				+ datosUsuario.getDomainData().getDomainName()
				+ "\" />"
				+ "<meta name=\"og:description\" content=\""
				+ datosUsuario.getDomainData().getDisplayString()
				+ "\">"
				+ "<meta property=\"og:image\"  content=\"http://www.info-movil.net/images/infomovil.png\" /></head>");

		String colorAux;
		WS_DomainVO dominio = datosUsuario.getDomainData();
		if (dominio.getColour() == null || dominio.getColour().length() < 7) {
			colorAux = "#BDBDBD";
		} else {
			colorAux = dominio.getColour();
		}
		// colorAux = "#567899";
		String nombreEmpresa = dominio.getTextRecord();
		if (nombreEmpresa == null || nombreEmpresa.equals("(null)")) {
			nombreEmpresa = "Título";
		}

		String descripcionEmpresa = dominio.getDisplayString();// http://info-movil.com/images/no-avatar.png
		if (descripcionEmpresa == null || descripcionEmpresa.equals("(null)")) {
			descripcionEmpresa = "Descripción corta";
		}
		descripcionEmpresa = descripcionEmpresa.replace("\n", "<br />");
		
		
		
		String rutaLogo ="<div id=\"headerLogo\" style=\"width=100%%; border-radius:7px; background:#2e2e2e; padding:2%% 0; margin-bottom:.9%%;\">"
				+ "<table id=\"Logo\" width=\"90%%\" style=\"margin:0 auto;\">"
				+ "<td width=\"45px;\">"
				+ "<img src=\"data:image/png;base64,"
				+ "%1$s"
				+ "\" id=\"photo\">" + "</td><td><div class=\"info\">" + "%2$s"
				+ "</div></td></table></div>";
		
		
		
		if (datosUsuario.getImagenLogo() != null
				&& datosUsuario.getImagenLogo().getImagenClobGaleria() != null
				&& datosUsuario.getImagenLogo().getImagenClobGaleria().length() > 2) {
			// rutaLogo = "data:image/png;base64," +
			// datosUsuario.getImagenLogo().getImagenClobGaleria();
			rutaLogo = String.format(rutaLogo, datosUsuario.getImagenLogo().getImagenClobGaleria(),nombreEmpresa);
			
		} else if (datosUsuario.getImagenLogo() != null
				&& datosUsuario.getImagenLogo().getImagenPath() != null
				&& datosUsuario.getImagenLogo().getImagenPath().length() > 2) {
			String img = null;
			try {
				File f = new File(datosUsuario.getImagenLogo().getImagenPath());
				img = Base64.encodeToString(read(f), Base64.DEFAULT);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			datosUsuario.getImagenLogo().setImagenClobGaleria(img);
			// rutaLogo = "data:image/png;base64," + rutaLogo;
//			rutaLogo = "<td><div id=\"header-left\">"
//					+ "<div style=\"width:auto;\">"
//					+ "<img width=\"90px;\" src=\"data:image/png;base64,"
//					+ rutaLogo + "\" id=\"photo\">" + "</div></div></td>";
			rutaLogo = String.format(rutaLogo, img,nombreEmpresa);
			
		} else {
//			rutaLogo = "<td><div id=\"header-left\">"
//					+ "<div style=\"width:auto;\">"
//					+ "<div class=\"usuario\" id=\"photoDef\"></div>"
//					+ "</div></div></td>";
			String imgDefault = "iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA9BJREFUeNrsnMFrE0EUxqfB9dAcugGTgw3EgFRqC4pUi4ii3kT8a0W8KSgiqVER2opFSIXGQyJke6iH5hD3i90SSrLkzbyZ3Z2875JDt2HeL+/NezP7ZpZGo9FjJdLWUgxwJBgEoAAUgAJQJAAFoAAUgCIBuLgAoyhSp8OhigbH489JXQ4CFVZW/n+GoQCEut3fqtfvq0F0rHq9Pul/a7WqqoQrqlatqtXVq4sDEF724+DnGN5FL9MVvBIQb6xdd+6dzgDC03b3vpM9jSp45ubG+tgzvQB4cvJXtT61rYObBnL77pYql5eLC/AgDtXdvX22UNUJ7c2Nm2otDu1CARzGwN5/+Ojc69K88eGD+yqIgeYeIJIE4CF0dVWPE0LzWuM8uyLhdA5/qaP4U1cIZUDkTjKsAAHvzdt3RiG7fW9rDG+aALG10zYK6adPHrFCLOUJXuJ5s4S/1Q1qPowNY8RYcwWQAx6E8oPjGZcQjQEiYbR2PrNk2nlCiyP8Ts/GPGQYszFAzEkDxpBwJYzZZD5lAYjsaJIZp00FHM/MK4y9azj+klnotlm9Aks9jmeoEWQSytoAv3z9xr7CODqr92bJtBacNR/CFqd1IIrkl69eW5ufLhbS2IjAspAb3qRePH+mtW7WAgi3T/OUIgo/GIp46yGM+cI3eMn0oDMXkgF2Ov7BM7GtpPNLeQvw0DJAJI8iFs2U4pq6i3SJ8jCyoW1htyRNWMfaFGxslht2AEYOvM/Vu4x0Gxt2QhivH30X1UYSwLxs0VsNYaKNJSVys5ngIoHkxgsJtooHuvJAkQAUgIUGWF4uLwwUiq3zA7TcpJMrgARbS7a+eBHgkQFWctRaa0tUG0kAwwUAGNoEWKtd8R4g1UYawGp13OHkq2AbdTuNXAdm2RFvWzq2kQE2mw1vAerYRgYIF/exnIFNOrvhWks5NG77Jl2btADiLb5PXghb0jpjrWwm3Ll9yxuAJrZoA0TGqnuQkWGDSWVhtJ2FZpwi14UYu05DERvAgGEAWQpjNz18Y7yhCvcvYlbGmDkWBSWewaxrZ7FMCuZ4rKbHJVgBJuFQBIi6jZTWARYBIjc8doAJxDzWiBiTjYRn7bww3u6jl5rab4ejqanfS+xdwSoD4Gx1fVk9cI2eYxy4xh0JWQh3KCDbBhZrVSd3Jri6L2HSi13dm+D01g7b5z2wLMPxfpdNmpncG4N5MTmFbtpzjbdoyaGcLHaIMr+5CPMkIPZ6fyZuLopm3FwUnt9chJc/gBdkvBaXu7MEoAAUgAJQJAAFoAAUgCIB6F7/BBgAMUcmVi6xazAAAAAASUVORK5CYII=";
//			rutaLogo = "<td><div id=\"header-left\">"
//					+ "<div style=\"width:auto;\">"
//					+ "<img width=\"90px;\" src=\"data:image/png;base64,"
//					+ rutaLogo + "\" id=\"photo\">" + "</div></div></td>";
			
			rutaLogo = String.format(rutaLogo, imgDefault , nombreEmpresa);
			
		}

		builder.append("<body bgcolor = "
				+ colorAux
				+ "><div id=\"container\">"
				+ rutaLogo
				+ "<div id=\"ContenedorGeneral\">"
//				+ "<table id=\"Logo\" width=\"100%\"   >"
//				+ rutaLogo
//				+ "<td width=\"70%\">"
//				+ "<div class=\"info\">"
//				+ "<span id=\"title-domain\">"
//				+ nombreEmpresa
//				+ "</span>"
//				+ "</div> <!-- End Titulo Domain -->"
//				+ "</td>"
//				+ "</table>"
				+ "<div id=\"content\">"
				+ "<div id=\"content-left\" class=\"left-column\">"
				+ "<div id=\"descCorta\">"
				+ "<h2 id=\"shortDesc\" style=\"word-wrap: break-word;\">"
				+ descripcionEmpresa
				+ "<br/>"
				+ "</h2>"
				+ "</div> <!-- End short desc -->"
				+ "</div>"
				+ "<div class=\"division\" style=\" width: 95%; border-top-width: 1px; border-top-style: dotted; border-top-color: rgb(111, 111, 111) !important; margin: 0 auto;\"  ></div>");

		builder.append("<div id=\"contenedor-Contacto\"><table id=\"tablaContactos\" border=\"0\" cellspacing=\"0\" ><td>");
		String telefonoPrincipal = "";
		String emailPrincipal = "";
		if (datosUsuario.getListaContactos() != null
				&& datosUsuario.getListaContactos().size() >= 1) {
			for (int i = 0; i < datosUsuario.getListaContactos().size(); i++) {
				WS_RecordNaptrVO contacto = datosUsuario.getListaContactos()
						.get(i);
				if (contacto.getIdTipoContacto() == 0
						|| contacto.getIdTipoContacto() == 1) {
					telefonoPrincipal = contacto.getCodCountry()
							+ contacto.getNoContacto();
					break;
				}
			}
			for (int i = 0; i < datosUsuario.getListaContactos().size(); i++) {
				WS_RecordNaptrVO contacto = datosUsuario.getListaContactos()
						.get(i);
				if (contacto.getIdTipoContacto() == 3) {
					emailPrincipal = contacto.getNoContacto();
					break;
				}
			}
		}
		Log.d("infoLog", "El telefono es " + telefonoPrincipal);
		// numero telefonico
		if (telefonoPrincipal.length() > 2) {
			builder.append("<a href=\"tel:"
					+ telefonoPrincipal
					+ "\"><div id=\"article\" class=\"llamanos\"></div></a><span id=\"text_icono\"> "
					+ res.getString(R.string.txtLlamanos) + "</span></td>");
		} else {
			builder.append("<a href=\"javascript:void(0);\" onclick=\"noDisponible()\"><div id=\"article\" class=\"llamanos\"></div></a><span id=\"text_icono\"> "
					+ res.getString(R.string.txtLlamanos) + "</span></td>");
		}

		// mapa
		if (datosUsuario.getCoordenadasUbicacion() != null
				&& datosUsuario.getCoordenadasUbicacion().getLatitudeLoc() != 0.0
				&& datosUsuario.getCoordenadasUbicacion().getLongitudeLoc() != 0.0) {
			// builder.append("<td><a href=\"javascript:void(0);\" onclick=\"creariMapa("+
			// datosUsuario.getCoordenadasUbicacion().getLatitudeLoc()+","
			// +datosUsuario.getCoordenadasUbicacion().getLongitudeLoc()
			// +")\"><div id=\"article\" class=\"ubicanos\"></div></a><p id=\"text_icono\" > Ub&iacute;canos</p></td>");
			Log.d("infoLog", "Si genera la ubicacion");
			builder.append("<td><a onclick=\"creariMapa("
					+ datosUsuario.getCoordenadasUbicacion().getLatitudeLoc()
					+ ","
					+ datosUsuario.getCoordenadasUbicacion().getLongitudeLoc()
					+ ")\" href=\"javascript:void(0);\" id=\"verMapa\">"
					+ "<div class=\"ubicanos\" id=\"article\"></div>"
					+ "</a><span id=\"text_icono\"> "
					+ res.getString(R.string.txtUbicanos) + "</span></td>");
		} else {
			Log.d("infoLog", "no genera la ubicacion");
			builder.append("<td><a id=\"verMapa\" href=\"javascript:void(0);\" onclick=\"isNull()\" ><div id=\"article\" class=\"ubicanos\"></div></a><span id=\"text_icono\" >"
					+ res.getString(R.string.txtUbicanos) + "</span><td>");
		}

		// email
		if (emailPrincipal.length() > 2) {
			
			String mail = String.format(mailto, emailPrincipal,nombreDominio);
			
			builder.append("<td>"
					+ mail
					+ "<div id=\"article\" class=\"contactanos\"></div> </a></h2><span id=\"text_icono\"> "
					+ res.getString(R.string.txtContactnos)
					+ "</span></td></table></div></div></div>");
		} else {
			builder.append("<td><a id=\"link \"href=\"javascript:void(0);\" onclick=\"noDisponible()\"><div id=\"article\" class=\"contactanos\"></div> </a></h2><span id=\"text_icono\"> "
					+ res.getString(R.string.txtContactnos)
					+ "</span></td></table></div></div></div>");
		}

		// poniendo div mapa
		if (datosUsuario.getCoordenadasUbicacion() != null
				&& datosUsuario.getCoordenadasUbicacion().getLatitudeLoc() != 0.0
				&& datosUsuario.getCoordenadasUbicacion().getLongitudeLoc() != 0.0) {
			Log.d("infoLog", "Nuevamente genera la ubicacion");
			builder.append("<div id=\"mapa\" class=\"hide\" ><table id=\"head-mapa\" border=0 width=\"100%\"><td  width=\"20%\">"
					+ "<div id=\"iconos\" class=\"ubicacion\"></div><td> <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtUbicacion)
					+ " </div><td width=\"40%\"> <div id=\"boton-contenedor\" >"
					+ "<a  href=\"javascript:void(0);\"  ><div id=\"ocultar-mapa\" class=\"hideP\" ></div></a>  </div></table><div id=\"contenedorMapa\"></div></div>");
		}

		// aqui va el video
		if(CuentaUtils.isCuentaPro() && datosUsuario.getVideoSeleccionado() != null && 
			!StringUtils.isEmpty(datosUsuario.getVideoSeleccionado().getLinkVideo())){
			
			builder.append("<div id=\"contenedor\"> <a  onclick=\"verVideo()\"> <table id=\"head-video\" border=0 width=\"100%\"> <td  width=\"20%\"> ");
			builder.append("<div id=\"iconos\" class=\"video\"><td><div id=\"text_icono_contenedor\"> Video </div> <td width=\"40%\"><div id=\"boton-contenedor\">");
			builder.append("<div id=\"ocultar-video\" class=\"showP\" ></div> </div></table></a><div id=\"movie-panel\"   class=\"hide\">");
			builder.append("<span class=\"align_title_video\"><span style=\"color:#FFFFFF\">.</span></span>");
			builder.append("<iframe id=\"iVideo\" src=\"" + datosUsuario.getVideoSeleccionado().getLinkVideo() + "\"></iframe>");
			builder.append("<span class=\"align_desc_video\"><span style=\"color:#FFFFFF\">.</span></span> </div> </div>");
		}
		

		// agregando la promocion

		if (datosUsuario.getPromocionDominio() != null
				&& datosUsuario.getPromocionDominio().getTitleOffer() != null
				&& datosUsuario.getPromocionDominio().getTitleOffer().length() > 1) {
			WS_OffertRecordVO oferta = datosUsuario.getPromocionDominio();
			String fechaSeleccionada = oferta.getEndDateOffer();
			if (fechaSeleccionada == null) {
				fechaSeleccionada = "";
			}

			builder.append("<div id=\"contenedor\">"
					+ "<table id=\"head-promociones\" border=0 width=\"100%\">"
					+ "<td  width=\"20%\"> <div id=\"iconos\" class=\"promociones\">"
					+ "<td> <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtPromociones)
					+ " </div>"
					+ "<td width=\"40%\"> <div id=\"boton-contenedor\" >"
					+ "<a  href=\"javascript:void(0);\" onclick=\"verOferta()\" ><div id=\"ocultarOffer\" class=\"showP\" ></div></a>"
					+ "</table>" + "<div id=\"offer-panel\" class=\"hide\">"
					+ "<div class=\"wrapper\"> "
					+ "<div style=\"width:100%; margin:0 auto; text-align:center;\">");
			if (oferta.getImageClobOffer().length() > 2) {
				builder.append("<img title=\"Imagen Offer\" id=\"photosOffer\" src=\"data:image/jpg;base64,"
						+ oferta.getImageClobOffer()
						+ "\" style=\"max-width:150px;  margin: 0 auto;\"/>");
			} else {
				builder.append("<div id=\"photosOffer\" class=\"descuento\" style=\"margin: 0 auto;\"></div>");
			}
			builder.append("</div><div id=\"text_icono_Offer\">" + oferta.getTitleOffer() + "</div>" 
					+ "<div id=\"ofertaDiv\">"
					+ "<div id=\"contOffer\">"
					+ "<div id=\"Descripcion\" class=\"text_desc\">"
					+ oferta.getDescOffer() + "</div>"
					+ "<div id=\"Caducidad\" class=\"text_desc\"> "
					+ res.getString(R.string.txtVigencia) + " "
					+ fechaSeleccionada + " </div>"
					+ "<div id=\"Reddem\" class=\"text_desc\"> "
					+ res.getString(R.string.txtComoRedimir) + " "
					+ oferta.getRedeemOffer() + " </div>"
					+ "<div id=\"verTerms\" class=\"text_desc\" src=\".\" style=\"margin-top:3%;\"><a style=\"color: rgb(0, 0, 0);\">  "
					+ res.getString(R.string.txtInfoAdicional) + "</a>"
					+ "<div id=\"Terminos\" class=\"text_desc_terminos\">"
					+ oferta.getTermsOffer() + "</div></div>"
					+ "</div>"
					
					+ "</div></div></div></div>");
		}

		// Galeria de imagenes

		if (datosUsuario.getListaImagenes() != null
				&& datosUsuario.getListaImagenes().size() > 0) {
			builder.append("<div id=\"contenedor\">"
					+ "<table id=\"head-galeria\" border=0 width=\"100%\">"
					+ "<td  width=\"20%\"> <div id=\"iconos\" class=\"imagenes\">"
					+ "</div>"
					+ "<td> <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtGaleriaWeb)
					+ " </div>"
					+ "<td width=\"40%\"> <div id=\"boton-contenedor\">"
					+ "<a  href=\"javascript:void(0);\" onclick=\"verGaleria()\" ><div  id=\"ocultar-galeria\" class=\"showP\" ></div></a></div>"
					+ "</table><div id=\"div-galeria\"class=\"division hide\" ></div>"
					+ "<div id=\"galeria\" class=\"hide\">");

			Vector<WS_ImagenVO> listaImagenes = datosUsuario.getListaImagenes();
			for (int i = 0; i < listaImagenes.size(); i++) {
				WS_ImagenVO imagenAux = listaImagenes.get(i);
				builder.append("<div class=\"image_gallery\" >"
						+ "<img src=\"data:image/png;base64,"
						+ imagenAux.getImagenClobGaleria()
						+ "\" class=\"imagen-galery\"><div id=\"descImagenGaleria\">" + imagenAux.getDescripcionImagen() + "</div></div>");
						
						if(i!=(listaImagenes.size()-1)){
							builder.append("<div class=\"divisionGaleria\" style=\"border-top:solid 1px rgb(111, 111, 111); margin: 3% auto 0 auto; width:94%;\" ></div>");
						}
						
						
				builder.append("<div>&nbsp;</div>");
			}
			builder.append("</div></div>");
		}

		// Perfil
		if (datosUsuario.getArregloPerfil() != null
				&& datosUsuario.getArregloPerfil().size() > 0
				&& (datosUsuario.getEstatusEdicion()[9] == true || datosUsuario
						.getEstatusEdicion()[2] == true)) {
			builder.append("<div id=\"contenedor\"><table id=\"head-perfil\" border=0 width=\"100%\">"
					+ "<td  width=\"20%\"> <div id=\"iconos\" class=\"perfil\">"
					+ "<td> <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtPerfilDescTitulo)
					+ " </div>"
					+ "<td width=\"40%\"> <div id=\"boton-contenedor\" ><a  href=\"javascript:void(0);\" onclick=\"verPerfil()\" ><div id=\"ocultar-perfil\" class=\"showP\" ></div></a></div>"
					+ "</table><div id=\"perfil-panel\" class=\"hide\">");

			Vector<WS_KeywordVO> listaPerfil = datosUsuario.getArregloPerfil();
			String arregloTitulos[] = {
					res.getString(R.string.productoServicio),
					res.getString(R.string.areaServicio),
					res.getString(R.string.horario),
					res.getString(R.string.metodosPago),
					res.getString(R.string.asociaciones),
					res.getString(R.string.biografia),
					res.getString(R.string.negocioProfesion) };
			for (int i = 0; i < listaPerfil.size(); i++) {
				WS_KeywordVO keyPerfil = listaPerfil.get(i);
				if (i == 2) {
					if (keyPerfil.getKeywordValue() != null
							&& keyPerfil.getKeywordValue().length() > 0
							&& datosUsuario.getEstatusPerfil()[2] == true) {
						builder.append("<div id=\"contenedor-data\" >"
								+ "<span id=\"align_desc_cont\">"
								+ arregloTitulos[i]
								+ "</span><br>"
								+ "<div id=\"constenedor-horario\">"
								+ "<table id=\"CSSTableGenerator\" class=\"align_desc_cont_sub\" style=\"margin-left:10%;\">");
						ArrayList<HorariosModel> listaHorarios = new ArrayList<HorariosModel>();
						String[] cadenas = keyPerfil.getKeywordValue().split(
								"\\|");
						Integer index = 1;
						String dia = "";

						for (String cadena : cadenas) {

							if (cadena.isEmpty()) {
								continue;
							}

							if (index % 2 == 0) {// Horario
								String[] horarios = cadena.split("-");
								Log.d("horarios", cadena);

								listaHorarios
										.add(new HorariosModel(dia, horarios[0]
												.trim(), horarios[1].trim()));
							} else {// Dia
								dia = cadena.trim();
							}
							index++;
						}
						for (int j = 0; j < listaHorarios.size(); j++) {
							HorariosModel model = listaHorarios.get(j);
							if (!model.getInicio().equals("00:00")
									|| !model.getCierre().equals("00:00")) {
								builder.append("<td class=\"horarios\">"
										+ model.getDia()
										+ "</td><td class=\"horarios\">"
										+ model.getInicio() + " - "
										+ model.getCierre() + "</td><tr>");
							}
						}
						builder.append("</table><br><div class=\"divisionContacto\" style=\" border-top-width: 1px; border-top-style: solid; border-top-color: rgb(111, 111, 111) !important; margin: 0 auto 10px; \"></div></div></div><div class=\"clr\"></div>");
					}
				} else {
					if (keyPerfil.getKeywordValue() != null) {
						builder.append("<div id=\"contenedor-data\"><span id=\"align_desc_cont\">"
								+ arregloTitulos[i]
								+ "</span><br><div class=\"clr\"></div>"
								+ "<span class=\"align_desc_cont_sub\">"
								+ keyPerfil.getKeywordValue().replace("\n",
										"<br />")
								+ "<br></span><div class=\"clr\"></div><div class=\"divisionContacto\" style=\" border-top-width: 1px; border-top-style: solid; border-top-color: rgb(111, 111, 111) !important; margin: 0 auto 10px; \" ></div></div><div class=\"clr\"></div>");
					}
				}
			}
			builder.append("</div></div>");
		}

		// los contactos

		if (datosUsuario.getListaContactos() != null
				&& datosUsuario.getListaContactos().size() > 0) {
			boolean contactosActivos = false;
			for (WS_RecordNaptrVO contactoAux : datosUsuario
					.getListaContactos()) {
				if (contactoAux.getVisible() == 1) {
					contactosActivos = true;
				}
			}
			if (contactosActivos) {
				builder.append("<div id=\"contenedor\">"
						+ "<table id=\"head-contacto\" border=0 width=\"100%\">"
						+ "<td  width=\"20%\"> <div id=\"iconos\" class=\"contacto\">"
						+ "</div></td>"
						+ "<td> <div id=\"text_icono_contenedor\"> "
						+ res.getString(R.string.txtContacto)
						+ " </div></td>"
						+ "<td width=\"40%\"> <div id=\"boton-contenedor\"  >"
						+ "<a  href=\"javascript:void(0);\" onclick=\"verContacto()\"><div  id=\"ocultar-contacto\" class=\"showP\" ></div></a>"
						+ "</div></td></table><div id=\"body-contacto\" class=\"hide\">");

				Vector<TipoContacto> arrayDatos = datosUsuario
						.getArrayTipoContacto();
				Vector<WS_RecordNaptrVO> arregloContactos = datosUsuario
						.getListaContactos();
				for (int i = 0; i < arregloContactos.size(); i++) {
					String regExp;
					WS_RecordNaptrVO contacto = arregloContactos.get(i);
					if (contacto.getCodCountry() != null
							&& contacto.getCodCountry().length() > 0) {
						regExp = contacto.getCodCountry()
								+ contacto.getNoContacto();
					} else {
						regExp = contacto.getNoContacto();
					}
					if (contacto.getVisible() == 1) {
						if (contacto.getIdTipoContacto() == 0) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-telefono\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ regExp
									+ "</a> <div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a><br/>"
									+ "<div id=\"contenedor-data-boton\">"
									+ "<a href=\"tel:"
									+ regExp
									+ "\" ><input  value=\"Llamar\" type=\"button\"  class=\"btncontact\" > </input></a></div><div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 1) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-cel\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ regExp
									+ "</a> <div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<div id=\"contenedor-data-boton\">"
									+ "<a href=\"tel:"
									+ regExp
									+ "\" ><input  value=\"Llamar\" type=\"button\"  class=\"btncontact\" > </input></a></div><div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 2) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-sms\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ regExp
									+ "</a> <div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<div id=\"contenedor-data-boton\">"
									+ "<a href=\"tel:"
									+ regExp
									+ "\" ><input  value=\"Contactar\" type=\"button\"  class=\"btncontact\" > </input></a></div><div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 3) {
							
							String mail = String.format(mailto, contacto.getLongLabelNaptr(),nombreDominio);
							
							
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-email\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto().toLowerCase()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a> <div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<div id=\"contenedor-data-boton\">"
									+ mail
									+ "<input value=\"Contactar\" type=\"button\"  class=\"btncontact\" > </input></a></div><div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 4) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-fax\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub2\">"
									+ regExp
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<div id=\"contenedor-data-boton\">"
									+ "<a href=\"tel:"
									+ regExp
									+ "\" ><input  value=\"Llamar\" type=\"button\"  class=\"btncontact\" > </input></a></div><div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 5) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-facebook\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"http://"+ contacto.getNoContacto() +"\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 6) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-twitter\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"http://"+ contacto.getNoContacto() +"\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 7) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-google\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"http://"+ contacto.getNoContacto() +"\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 8) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-skype\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"skype://"+ contacto.getNoContacto() +"?call\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 9) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-linkedin\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"http://"+ contacto.getNoContacto() +"\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						} else if (contacto.getIdTipoContacto() == 10) {
							builder.append("<div id=\"contenedor-data\">"
									+ "<div class=\"c-secure-web\" id=\"align_desc_cont\"></div>"
									+ "<a id=\"tilte-contact\">"
									+ arrayDatos.get(
											contacto.getIdTipoContacto())
											.getTexto()
									+ "</a> </img><br>"
									+ "<a class=\"align_desc_cont_sub\">"
									+ contacto.getNoContacto()
									+ "</a><div class=\"clr\"></div>"
									+ "<a id=\"desc-contact\">"
									+ contacto.getLongLabelNaptr()
									+ "</a>"
									+ "<a href=\"http://"+ contacto.getNoContacto() +"\" target=\"_blank\" id=\"link\"> <input id=\"contenedor-data-boton\"  value=\"Contactar\" type=\"button\" class=\"btncontact\" > </input> </a>"
									+ "<div class=\"clr\"></div><div class=\"divisionContacto\" ></div></div>");
						}

					}
				}
				builder.append("</div></div> ");
			}
		}

		// La direccion
		if (datosUsuario.getArregloDireccion() != null
				&& datosUsuario.getArregloDireccion().size() > 0
				&& datosUsuario.getEstatusEdicion()[10] == true) {
			Vector<WS_KeywordVO> arregloDireccion = datosUsuario
					.getArregloDireccion();
			builder.append("<div id=\"contenedor\"><table id=\"head-direccion\" border=0 width=\"100%\">"
					+ "<td  width=\"20%\"> <div id=\"iconos\"class=\"direccion\">"
					+ "</div>"
					+ "<td> <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtTituloDireccion)
					+ " </div><td width=\"40%\"> <div id=\"boton-contenedor\" ><a  href=\"javascript:void(0);\" onclick=\"verDireccion()\"><div  id=\"ocultar-direccion\" class=\"showP\" ></div></a>"
					+ "</div></table><div id=\"direccion-panel\" class=\"hide\">");

			for (int i = 0; i < arregloDireccion.size(); i++) {
				WS_KeywordVO keyModel = arregloDireccion.get(i);
				builder.append("<div id=\"contenedor-data\"><span class=\"align_desc_cont_sub\">"
						+ keyModel.getKeywordValue() + "</span></div>");
			}
			builder.append("<div>&nbsp;</div></div></div>");

		}

		// otra informacion

		if (datosUsuario.getArregloInformacionAdicional() != null
				&& datosUsuario.getArregloInformacionAdicional().size() > 0) {
			builder.append("<div id=\"contenedor\"><table id=\"head-otherInfo\" border=0 width=\"100%\"><td width=\"20%\"> <div id=\"iconos\" class=\"infoAd\">"
					+ "</div></td><td > <div id=\"text_icono_contenedor\"> "
					+ res.getString(R.string.txtInfoAdicional)
					+ " </div></td><td width=\"40%\"> <div id=\"boton-contenedor\" >"
					+ "<a  href=\"javascript:void(0);\" onclick=\"verOtherInfo()\"><div  id=\"ocultar-otherInfo\" class=\"showP\" ></div></a>"
					+ "</div></td></table><div id=\"info-panel\" class=\"hide\">");

			Vector<WS_KeywordVO> listaInfo = datosUsuario
					.getArregloInformacionAdicional();
			for (int i = 0; i < listaInfo.size(); i++) {
				WS_KeywordVO keyInfo = listaInfo.get(i);
				builder.append("<div id=\"contenedor-data\"><a class=\"align_desc_cont\">"
						+ keyInfo.getKeywordField()
						+ "</a></div>"
						+ "<div id=\"contenedor-data\"><span class=\"align_desc_cont_sub\">"
						+ keyInfo.getKeywordValue().replace("\n", "<Br />")
						+ "</span>");
				
				if(i!=(listaInfo.size()-1)){
					builder.append("<div class=\"divisionContacto\" ></div>");
				}
				
				builder.append("</div>");
				
			}
			builder.append("<div>&nbsp;</div></div></div>");
		}

//		builder.append("<div id=\"contenedor\" >"
//				+ "<div id=\"compartir\"><div class=\"clr\"></div>"
//				+ "<div id=\"compartirText\">Compartir</div>"
//				+ "<table id=\"links-Compartir\" width=\"100%\">"
//				+ "<td width=\"16.6%\"><a href=\"http://www.facebook.com/sharer.php?u=http://www."
//				+ dominio.getDomainName()
//				+ ".tel\"\" target=\"blank\"\"><div id=\"iconos\" class=\"facebook\" ></div></a><td>"
//				+ "<td width=\"16.6%\"><a href=\"https://plus.google.com/share?url=http://www."
//				+ dominio.getDomainName()
//				+ ".tel\" target=\"blank\"\"><div id=\"iconos\" class=\"google\">  </div></a><td>"
//				+ "<td width=\"16.6%\"><a href=\"http://www.twitter.com/intent/tweet?text=Hola%20visita%20este%20dominio%20http://www."
//				+ dominio.getDomainName()
//				+ ".tel\" target=\"blank\"><div id=\"iconos\" class=\"twitter\"></div></a></td>"
//				+ "<td width=\"16.6%\"><a href=\"mailto:mail@mail.com&subject=Ya%20estoy%20en%20linea%20con%20www.info-movil.com&body=Ya%20estoy%20en%20linea%20con%20www.info-movil.com%0A%0AVisita%20mi%20sitio:%20www."
//				+ dominio.getDomainName()
//				+ ".tel%0A%0A%0A%0A%0A%0A%0A%0A<h1>!Ponte%20en%20linea%20con%20Infomovil!</h1>%0A!Crea%20tu%20cuenta%20gratis%20ahora!%0A%0ALiga%20de%20descarga\"><div id=\"iconos\" class=\"email\"></div></a></td>"
//				+ "<td width=\"16.6%\"><a href=\"sms:?body=Ya%20estoy%20en%20linea%20con%20info-movil.com.%20Visita%20mi%20sitio%20web%20http://www."
//				+ dominio.getDomainName()
//				+ ".tel\"\"><div id=\"iconos\" class=\"sms\"></div></a></td>"
//				+ "<td width=\"16.6%\"><a  href=\"whatsapp://send?text=Ya%20estoy%20en%20linea%20con%20www.info-movil.com%0A%0AVisita%20mi%20sitio:%20www."
//				+ dominio.getDomainName()
//				+ ".tel\"\" target=\"blank\"\"><div id=\"iconos\" class=\"whatsapp\">  </div></a></td></table>"
//				+ "<div class=\"clr\"></div></div></div></body></html>");

		return builder.toString();
	}

	public byte[] read(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

	@Override
	public void borrarDatosOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBotonEliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void initCreate() {
		cargarLayout(R.layout.vista_previa_dominio_layout);
		acomodarBotones(ButtonStyleShow.ButtonStyleShowNone);
		vistaPreviaWeb = getIntent().getBooleanExtra("vistaPrevia",
				false);
		delegado = this;
		context = this;
		vistaPrevia = (WebView) findViewById(R.id.webViewDominio);
		vistaPrevia.setWebChromeClient(new WebChromeClient());
		vistaPrevia.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d("infoLog", "la Url es " + url);
//				 String mailToRegexp ="mailto\\:([^?]+)\\?{0,1}((subject\\=([^&]+))|(body\\=([^&]+))|(bcc\\=([^&]+))|(cc\\=([^&]+)))*";
//				 Pattern mailToPattern = Pattern.compile(mailToRegexp);
//				 Matcher mailToMatcher = mailToPattern.matcher(url);
//				 if(mailToMatcher.find()){
//					 Log.d("infoLog", "Entrando a email");
//					 String email = mailToMatcher.group(1);
//					 if (email.equals("mail")) {
//						 email = "";
//					 }
//					 String subject = mailToMatcher.group(4);
//					 String body = mailToMatcher.group(6);
//					 String bcc = mailToMatcher.group(8);
//					 String cc = mailToMatcher.group(10);
//					
//					 Intent intent = new Intent(Intent.ACTION_SEND);
//					
//					 intent.setType("message/rfc822");
//					 intent.putExtra(Intent.EXTRA_EMAIL, email);
//					 intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//					 intent.putExtra(Intent.EXTRA_TEXT, body);
//					 intent.putExtra(Intent.EXTRA_BCC, bcc);
//					 intent.putExtra(Intent.EXTRA_CC, cc);
//					
//					 startActivity(Intent.createChooser(intent, "Send Email"));
//					
//					 return true;
//				 }
				if (url.startsWith("tel")) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse(url));
					startActivity(intent);
				}
				else if (url.startsWith("whatsapp")) {
					compartirWhatsapp(url);
				}
				else if (url.startsWith("sms")) {
					compartirSms(url);
				}
				else if (url.startsWith("mailto")) {
					url = url.replaceFirst("mailto:", "");
			        //
			        String theEmail = "",
			            theEmailCC = "",
			            theEmailBCC = "",
			            theSubject = "",
			            theBody = "";
			        Boolean hasEmail = true,
			            hasEmailCC = url.contains("&cc="),
			            hasEmailBCC = url.contains("&bcc="),
			            hasSubject = url.contains("&subject="),
			            hasBody = url.contains("&body=");
			        int posEmail = 0,
			            posEmailCC = hasEmailCC ? url.indexOf("&cc=") : 0,
			            posEmailBCC = hasEmailBCC ? url.indexOf("&bcc=") : 0,
			            posSubject = hasSubject ? url.indexOf("&subject=") : 0,
			            posBody = hasBody ? url.indexOf("&body=") : 0;
			        //
			        if        (hasEmail    && hasEmailCC ) { theEmail    = url.substring(posEmail, posEmailCC - posEmail);
			        } else if (hasEmail    && hasEmailBCC) { theEmail    = url.substring(posEmail, posEmailBCC - posEmail);
			        } else if (hasEmail    && hasSubject ) { theEmail    = url.substring(posEmail, posSubject - posEmail);
			        } else if (hasEmail    && hasBody    ) { theEmail    = url.substring(posEmail, posBody - posEmail);
			        } else if (hasEmail                  ) { theEmail    = url;
			        } else {                               /*theEmail    = url;*/ }

//			        if        (hasEmailCC  && hasEmailBCC) { theEmailCC  = url.substring(posEmailCC, posEmailBCC - posEmailCC);
//			        } else if (hasEmailCC  && hasSubject ) { theEmailCC  = url.substring(posEmailCC, posSubject - posEmailCC);
//			        } else if (hasEmailCC  && hasBody    ) { theEmailCC  = url.substring(posEmailCC, posBody - posEmailCC);
//			        } else if (hasEmailCC                ) { theEmailCC  = url.substring(posEmailCC);
//			        } else {                               /*theEmailCC  = url.substring(posEmailCC);*/ }
//			        theEmailCC = theEmailCC.replace("&cc=", "");

//			        if        (hasEmailBCC && hasSubject ) { theEmailBCC = url.substring(posEmailBCC, posSubject - posEmailBCC);
//			        } else if (hasEmailBCC && hasBody    ) { theEmailBCC = url.substring(posEmailBCC, posBody - posEmailBCC);
//			        } else if (hasEmailBCC               ) { theEmailBCC = url.substring(posEmailBCC);
//			        } else {                               /*theEmailBCC = url.substring(posEmailBCC);*/ }
//			        theEmailBCC = theEmailBCC.replace("&bcc=", "");

			        if        (hasSubject  && hasBody    ) { theSubject  = url.substring(posSubject, posBody);
			        } else if (hasSubject                ) { theSubject  = url.substring(posSubject);
			        } else {                               /*theSubject  = url.substring(posSubject);*/ }
			        theSubject = theSubject.replace("&subject=", "");

			        if        (hasBody                   ) { theBody     = url.substring(posBody);
			        } else {                               /*theBody     = url.substring(posBody);*/ }
			        theBody = theBody.replace("&body=", "");
			        
			        if (theEmail.equals("mail@mail.com")) {
			        	theEmail = "";
			        }
			        theSubject = theSubject.replace("%20", " ");
			        theBody = theBody.replace("%20", " ").replace("%0A", "\n");
			        //
			        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			        emailIntent.setType("message/rfc822");
			        //
			        emailIntent.putExtra(Intent.EXTRA_EMAIL, theEmail);
			        if (hasSubject) { emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, theSubject); }
			        if (hasBody) { emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, theBody); }
			        //
			        startActivity(emailIntent);
				}
				else {
					if (url.contains("facebook")) {
						compartirFacebook(url);
					}
					else if (url.contains("twitter")) {
						compartirTwitter(url);
					}
					else if (url.contains("google")) {
						compartirGoogle(url);
					}
				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				alerta = new AlertView(context,
						AlertViewType.AlertViewTypeActivity, getResources().getString(R.string.txtCargandoDefault));
				alerta.setDelegado(delegado);
				alerta.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				if (alerta != null)
					alerta.dismiss();
			}
		});
		vistaPrevia.getSettings().setJavaScriptEnabled(true);
		vistaPrevia.getSettings().setPluginState(PluginState.ON);
		
	
	}

	@Override
	public void initResume() {
		ocultarMenuInferior();
		
		if (vistaPreviaWeb) {
			acomodarVistaConTitulo(
					getResources().getString(R.string.tituloVistaPrevia),
					R.drawable.plecaverde);
			vistaPrevia.loadDataWithBaseURL("file:///android_asset/", getStringHTML(), "text/html", "utf-8", null);
			// vistaPrevia.loadUrl("http://info-movil.com/" +
			// DatosUsuario.getInstance().getDomainData().getDomainName());
		} else {
			acomodarVistaConTitulo(
					getResources().getString(R.string.tituloEjemplo),
					R.drawable.plecamorada);
			
			String language = Locale.getDefault().getLanguage(); 
			
			if(language.equals("es")){
				vistaPrevia.loadUrl("file:///android_asset/indexInfo.html");
			}
			else if(language.equals("en")){
				vistaPrevia.loadUrl("file:///android_asset/indexInfoEn.html");
			}
		}		
	}

	@Override
	public void guardarInformacion() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resultActivityCall(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acomodaVista() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//			vistaPrevia.loadUrl("about:blank");
//		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@SuppressWarnings("unused")
	public void compartirWhatsapp(String url){
		datosUsuario = DatosUsuario.getInstance();
		Intent waIntent = new Intent(Intent.ACTION_SEND);
		waIntent.setType("text/plain");
		waIntent.setPackage("com.whatsapp");
		url = url.replace("%20", " ").replace("whatsapp://send?text=", "").replace("%0A", " ");
		
		if (waIntent != null) {
			waIntent.putExtra(Intent.EXTRA_TEXT, url);//
		    startActivity(Intent.createChooser(waIntent, "Share with"));
		} else {
		   // Toast.makeText(this, "WhatsApp no instalado", Toast.LENGTH_SHORT).show();
			AlertView alertaError = new AlertView(this, AlertViewType.AlertViewTypeInfo, getResources().getString(R.string.noWhatsapp));
			alertaError.setDelegado(this);
			alertaError.show();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void compartirSms(String url) {
		//String number = "12346556";  // The number on which you want to send SMS  
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","", null)));
		url = url.replace("%20", " ").replace("sms:?body=", "");
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) //At least KitKat
	    {
	        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); //Need to change the build to API 19

	        Intent sendIntent = new Intent(Intent.ACTION_SEND);
	        sendIntent.setType("text/plain");
	        sendIntent.putExtra(Intent.EXTRA_TEXT,url);

	        if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
	        {
	            sendIntent.setPackage(defaultSmsPackageName);
	        }
	       startActivity(sendIntent);

	    }
	    else //For early versions, do what worked for you before.
	    {
	        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	        sendIntent.setData(Uri.parse("sms:"));
	        sendIntent.putExtra("sms_body", url);
	        startActivity(sendIntent);
	    }

	}
	
	public void compartirFacebook(String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType(HTTP.PLAIN_TEXT_TYPE);
				share.putExtra(Intent.EXTRA_TITLE, " ");
				share.putExtra(Intent.EXTRA_TEXT, " ");
				share.setPackage("com.facebook.katana");
				
				PackageManager packageManager = getPackageManager();
				List<ResolveInfo> activities = packageManager.queryIntentActivities(share, 0);

				if (activities.size() > 0) {
					startActivity(Intent.createChooser(share, " "));
				}else{
					startActivity(browserIntent);
					
				}
	}
	
	
	
	public void compartirTwitter(String url){
		
		datosUsuario = DatosUsuario.getInstance();
		url.replace(" ", "%20").replace("\n", "%0A");
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

	public void compartirGoogle(String url){
		
		datosUsuario = DatosUsuario.getInstance();
		Intent shareIntent = new PlusShare.Builder(this)
        .setType("text/plain")
        .setText(" ")
        .setContentUrl(Uri.parse(url))
        .getIntent();

		startActivityForResult(shareIntent, 0);
	}
}
