package com.infomovil.infomovil.model;

/**
 * @author Sergio Sánchez Flores
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infomovil.infomovil.app.InfomovilApp;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_ImagenVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_InfoUsuarioVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_KeywordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_LocalizacionVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_OffertRecordVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RespuestaVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_StatusDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UserDomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_UsuarioDominiosVO;

 
public class DatosUsuario {
	
	private static DatosUsuario _datosUsuario;
	private Context context;
	
	private ItemSelectModel videoSeleccionado;
	private String jsonVideo;
	private Vector<WS_StatusDomainVO> itemsDominio;
	private Vector<WS_StatusDomainVO> itemsDominioGratuito;
	private WS_OffertRecordVO promocionDominio;
	private Vector<TipoContacto> arrayTipoContacto;
	private Vector<WS_RecordNaptrVO> listaContactos;
	private WS_ImagenVO imagenLogo;
	private Vector<WS_ImagenVO> listaImagenes; // IDM
	private Vector<WS_KeywordVO> arregloDireccion;
	private Vector<WS_KeywordVO> arregloPerfil;
	private Vector<WS_KeywordVO> arregloInformacionAdicional;
	private WS_LocalizacionVO coordenadasUbicacion;
	private Vector<WS_UsuarioDominiosVO> usuarioDominios;
	private boolean estatusEdicion[];
	private boolean estatusPerfil[];
	private boolean eligioColor;
	private boolean existeLogin;
	private ArrayList<CallingCodes> arrayCodigos;
	private WS_DomainVO domainData;
	private String nombreUsuario;
	private String password,tipoPlan,cancelaDescripcion;
	private String token = "";
	private String statusDominio;
	private String descripcionDominio;
	private String fechaInicio;
	private String fechaFin;
	private String fechaInicioTel;
	private String fechaFinTel;
	private String codeCamp;
	private long domainid;
	private WS_InfoUsuarioVO infoUsr;
	private String nombreDominio;
	private WS_UserDomainVO userDomainData;
	private String[] clavesPerfil;
	private String rutaPromocion;
	private boolean agregoImagenPromocion;
	private CompraModel compra;
	private String domainType;
	private String redSocial;
	private WS_RespuestaVO respuestaVOActual;
	private WS_UsuarioDominiosVO usuarioDominioVOActual;
	private String suscritoCompra;
	private String tipoPlanCompra;
	private String canal;
	private String campania;
	private String tipoUsuario;
	private String hashMovilizaSitio;
	
	private GraphUser facebookInformation;
	
	private String precioMensual;
	private String precioAnual;
	private String precioTel;
	
	private List<Catalogo> catalogoDominios;
	private Catalogo catDominioActual;
	
	
	private DatosUsuario() {
		init();
	}
	
	/**
	 * Inicializa variables
	 */
	public void init(){
		if(clavesPerfil == null){
			clavesPerfil  = new String[]{"pos","sas","oh","pm","tpa","mnu","np"};
		}
		
		if(arregloPerfil == null){
			// Se crea el arreglo vacio de 7 elementos para el perfil
			Vector<WS_KeywordVO> listaPerfil = new Vector<WS_KeywordVO>(7);
			WS_KeywordVO temp_KeywordVO;
			for (int i = 0; i < 7 ; i++)
			{
				temp_KeywordVO = new WS_KeywordVO();
				temp_KeywordVO.setKeywordField(clavesPerfil[i]);
				listaPerfil.add(temp_KeywordVO);
			}
			this.setArregloPerfil(listaPerfil);
		}
		
		if(domainData == null){
			this.setDomainData(new WS_DomainVO());
		}
		
	}
	
	public static synchronized DatosUsuario getInstance() {
		if(_datosUsuario == null) {
			_datosUsuario = new DatosUsuario();
		}
		
		return _datosUsuario;
	} 
	
	public void saveData() {
		context = InfomovilApp.getApp();
		SharedPreferences prefs = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
		Editor edit = prefs.edit();
		Gson gson = new Gson();
		
		edit.putString("videoSeleccionado", gson.toJson(videoSeleccionado));
		edit.putString("jsonVideo", jsonVideo);
		edit.putString("itemsDominio", gson.toJson(itemsDominio));
		edit.putString("itemsDominioGratuito", gson.toJson(itemsDominioGratuito));
		edit.putString("promocionDominio", gson.toJson(promocionDominio));
		edit.putString("arrayTipoContacto", gson.toJson(arrayTipoContacto));
		edit.putString("listaContactos", gson.toJson(listaContactos));
		edit.putString("imagenLogo", gson.toJson(imagenLogo));
		edit.putString("listaImagenes", gson.toJson(listaImagenes));
		edit.putString("arregloDireccion", gson.toJson(arregloDireccion));
		edit.putString("arregloPerfil", gson.toJson(arregloPerfil));
		edit.putString("arregloInformacionAdicional", gson.toJson(arregloInformacionAdicional));
		edit.putString("coordenadasUbicacion", gson.toJson(coordenadasUbicacion));
		edit.putString("usuarioDominios", gson.toJson(usuarioDominios));
		edit.putString("estatusEdicion", gson.toJson(estatusEdicion));
		edit.putString("estatusPerfil", gson.toJson(estatusPerfil));
		edit.putBoolean("eligioColor", eligioColor);
		edit.putBoolean("existeLogin", existeLogin);
		edit.putString("arrayCodigos", gson.toJson(arrayCodigos));
		edit.putString("domainData", gson.toJson(domainData));
		edit.putString("nombreUsuario",nombreUsuario);
		edit.putString("password", password);
		edit.putString("tipoPlan", tipoPlan);
		edit.putString("cancelaDescripcion", cancelaDescripcion);
		edit.putString("token", token);
		edit.putString("statusDominio", statusDominio);
		edit.putString("descripcionDominio", descripcionDominio);
		edit.putString("fechaInicio", fechaInicio);
		edit.putString("fechaFin", fechaFin);
		edit.putString("fechaInicioTel", fechaInicioTel);
		edit.putString("fechaFinTel", fechaFinTel);
		edit.putString("codeCamp", codeCamp);
		edit.putLong("domainid", domainid);
		edit.putString("infoUsr", gson.toJson(infoUsr));
		edit.putString("nombreDominio", nombreDominio);
		edit.putString("userDomainData", gson.toJson(userDomainData));
		edit.putString("clavesPerfil", gson.toJson(clavesPerfil));
		edit.putString("rutaPromocion", rutaPromocion);
		edit.putBoolean("agregoImagenPromocion", agregoImagenPromocion);
		edit.putString("compra", gson.toJson(compra));
		edit.putString("domainType", domainType);
		edit.putString("redSocial", redSocial);
		edit.putString("respuestaVOActual", gson.toJson(respuestaVOActual));
		edit.putString("usuarioDominioVOActual", gson.toJson(usuarioDominioVOActual));
		edit.putString("suscritoCompra", suscritoCompra);
		edit.putString("tipoPlanCompra", tipoPlanCompra);
		edit.putString("canal", canal);
		edit.putString("campania", campania);
		edit.putString("tipoUsuario", tipoUsuario);
		edit.putString("hashMovilizaSitio", hashMovilizaSitio);
		
		edit.putString("tipoInfomovil", InfomovilApp.tipoInfomovil);
		edit.putString("urlSitio", InfomovilApp.urlInfomovil);
		
		edit.putString("catDominioActual", gson.toJson(catDominioActual));
		
//		edit.putString("precioMensual", precioMensual);
//		edit.putString("precioAnual", precioAnual);
		
		edit.commit();
	}
	
	public void readData() {
		context = InfomovilApp.getApp();
		SharedPreferences prefs = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		
		videoSeleccionado = gson.fromJson(prefs.getString("videoSeleccionado",""), ItemSelectModel.class);
		jsonVideo = prefs.getString("jsonVideo","");
		itemsDominio = gson.fromJson(prefs.getString("itemsDominio",""), new TypeToken<Vector<WS_StatusDomainVO>>() {}.getType());
		itemsDominioGratuito = gson.fromJson(prefs.getString("itemsDominioGratuito",""), new TypeToken<Vector<WS_StatusDomainVO>>() {}.getType());
		promocionDominio = gson.fromJson(prefs.getString("promocionDominio",""), WS_OffertRecordVO.class);
		arrayTipoContacto = gson.fromJson(prefs.getString("arrayTipoContacto",""), new TypeToken<Vector<TipoContacto>>() {}.getType());
		listaContactos = gson.fromJson(prefs.getString("listaContactos",""), new TypeToken<Vector<WS_RecordNaptrVO>>() {}.getType());
		imagenLogo = gson.fromJson(prefs.getString("imagenLogo",""), WS_ImagenVO.class);
		
		listaImagenes = gson.fromJson(prefs.getString("listaImagenes",""), new TypeToken<Vector<WS_ImagenVO>>() {}.getType());
		arregloDireccion = gson.fromJson(prefs.getString("arregloDireccion",""),new TypeToken<Vector<WS_KeywordVO>>() {}.getType());
		arregloPerfil = gson.fromJson(prefs.getString("arregloPerfil",""), new TypeToken<Vector<WS_KeywordVO>>() {}.getType());
		
		
		arregloInformacionAdicional = gson.fromJson(prefs.getString("arregloInformacionAdicional",""), new TypeToken<Vector<WS_KeywordVO>>() {}.getType());
		coordenadasUbicacion = gson.fromJson(prefs.getString("coordenadasUbicacion",""), WS_LocalizacionVO.class);
		usuarioDominios = gson.fromJson(prefs.getString("usuarioDominios",""), new TypeToken<Vector<WS_UsuarioDominiosVO>>() {}.getType());
		
		
		estatusEdicion = gson.fromJson(prefs.getString("estatusEdicion",""), boolean[].class);
		estatusPerfil = gson.fromJson(prefs.getString("estatusPerfil",""), boolean[].class);
		eligioColor = prefs.getBoolean("eligioColor", false);
		existeLogin = prefs.getBoolean("existeLogin", false);
		arrayCodigos = gson.fromJson(prefs.getString("arrayCodigos",""),new TypeToken<ArrayList<CallingCodes>>() {}.getType());
		domainData = gson.fromJson(prefs.getString("domainData",""), WS_DomainVO.class);
		
		
		nombreUsuario = prefs.getString("nombreUsuario","");
		password = prefs.getString("password","");
		tipoPlan = prefs.getString("tipoPlan","");
		token = prefs.getString("token","");
		statusDominio = prefs.getString("statusDominio","");
		descripcionDominio = prefs.getString("descripcionDominio","");
		fechaInicio = prefs.getString("fechaInicio","");
		fechaFin = prefs.getString("fechaFin","");
		fechaInicioTel = prefs.getString("fechaInicioTel","");
		fechaFinTel = prefs.getString("fechaFinTel","");
		codeCamp = prefs.getString("codeCamp","");
		
	
		domainid = prefs.getLong("domainid",0);
		infoUsr = gson.fromJson(prefs.getString("infoUsr",""), WS_InfoUsuarioVO.class);
		nombreDominio = prefs.getString("nombreDominio","");
		userDomainData = gson.fromJson(prefs.getString("userDomainData",""), WS_UserDomainVO.class);
		clavesPerfil = gson.fromJson(prefs.getString("clavesPerfil",""), String[].class);
		rutaPromocion = prefs.getString("rutaPromocion","");
		agregoImagenPromocion = prefs.getBoolean("agregoImagenPromocion", false);
		compra = gson.fromJson(prefs.getString("compra",""), CompraModel.class);
		
		domainType = prefs.getString("domainType","");
		redSocial = prefs.getString("redSocial","");
		
		respuestaVOActual = gson.fromJson(prefs.getString("respuestaVOActual",""), WS_RespuestaVO.class);
		usuarioDominioVOActual = gson.fromJson(prefs.getString("usuarioDominioVOActual",""), WS_UsuarioDominiosVO.class);
		
		suscritoCompra = prefs.getString("suscritoCompra","");
		tipoPlanCompra = prefs.getString("tipoPlanCompra","");
		
		canal = prefs.getString("canal","");
		campania = prefs.getString("campania","");
		tipoUsuario = prefs.getString("tipoUsuario","");
		hashMovilizaSitio = prefs.getString("hashMovilizaSitio","");
		
		InfomovilApp.tipoInfomovil = prefs.getString("tipoInfomovil", "recurso");
		InfomovilApp.urlInfomovil = prefs.getString("urlSitio", "");
		
		catDominioActual = gson.fromJson(prefs.getString("catDominioActual",""), Catalogo.class);
		
//		precioMensual = prefs.getString("precioMensual","");
//		precioAnual = prefs.getString("precioAnual","");
		
		
		init();
		
	}
	
	public void borrarDatos() {
		context = InfomovilApp.getApp();
		
		SharedPreferences prefs = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
		if(prefs!= null){
			prefs.edit().clear().commit();
		}
		
		prefs = context.getSharedPreferences("Cuenta", Context.MODE_PRIVATE);
		if(prefs!= null){
			prefs.edit().clear().commit();
		}
		
		videoSeleccionado = new ItemSelectModel();
		jsonVideo = "";
		itemsDominio = new Vector<WS_StatusDomainVO>();
		itemsDominioGratuito = new Vector<WS_StatusDomainVO>();
		promocionDominio = new WS_OffertRecordVO();
		arrayTipoContacto = new Vector<TipoContacto>();
		listaContactos = new Vector<WS_RecordNaptrVO>(); 
		imagenLogo = new WS_ImagenVO();
		listaImagenes = new Vector<WS_ImagenVO>(); // IDM
		arregloDireccion = new Vector<WS_KeywordVO>();
		arregloPerfil = new Vector<WS_KeywordVO>();
		arregloInformacionAdicional = new Vector<WS_KeywordVO>();
		coordenadasUbicacion = new WS_LocalizacionVO();
		usuarioDominios = new Vector<WS_UsuarioDominiosVO>();
		
		estatusEdicion = null;
		estatusPerfil = null;
		eligioColor = false;
		existeLogin = false;
		
		domainData = new WS_DomainVO();
		nombreUsuario = "";
		password = "";
		tipoPlan ="";
		cancelaDescripcion = "";
		token = "";
		statusDominio ="";
		descripcionDominio="";
		fechaInicio = "";
		fechaFin = "";
		fechaInicioTel="";
		fechaFinTel="";
		codeCamp="";
		domainid = 0;
		infoUsr = new WS_InfoUsuarioVO();
		
		nombreDominio="";
		
		userDomainData = new WS_UserDomainVO();
		
		rutaPromocion = "";
		agregoImagenPromocion = false;
		compra = new CompraModel();
		domainType="";
		redSocial="";
		respuestaVOActual = new WS_RespuestaVO();
		usuarioDominioVOActual = new WS_UsuarioDominiosVO();
		suscritoCompra="";
		tipoPlanCompra="";
		
		canal = "";
		campania = "";
		tipoUsuario = "";
		hashMovilizaSitio = "";
		
		catalogoDominios = new ArrayList<Catalogo>();
		catDominioActual = new Catalogo();
		
		
	}
	
	public ItemSelectModel getVideoSeleccionado() {
		return videoSeleccionado;
	}

	public void setVideoSeleccionado(ItemSelectModel videoSeleccionado) {
		this.videoSeleccionado = videoSeleccionado;
	}

	public String getJsonVideo() {
		return jsonVideo;
	}

	public void setJsonVideo(String jsonVideo) {
		this.jsonVideo = jsonVideo;
	}

	public Vector<WS_StatusDomainVO> getItemsDominio() {
		return itemsDominio;
	}

	public void setItemsDominio(Vector<WS_StatusDomainVO> itemsDominio) {
		this.itemsDominio = itemsDominio;
	}

	public WS_OffertRecordVO getPromocionDominio() {
		return promocionDominio;
	}

	public void setPromocionDominio(WS_OffertRecordVO promocionDominio) {
		this.promocionDominio = promocionDominio;
	}

	public Vector<TipoContacto> getArrayTipoContacto() {
		return arrayTipoContacto;
	}

	public void setArrayTipoContacto(Vector<TipoContacto> arrayTipoContacto) {
		this.arrayTipoContacto = arrayTipoContacto;
	}

	public WS_ImagenVO getImagenLogo() {
		return imagenLogo;
	}

	public void setImagenLogo(WS_ImagenVO imagenLogo) {
		this.imagenLogo = imagenLogo;
	} 

	public Vector<WS_RecordNaptrVO> getListaContactos() {
		return listaContactos;
	}

	public void setListaContactos(Vector<WS_RecordNaptrVO> listaContactos) {
		this.listaContactos = listaContactos;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	
	public String getNombreDominio() {
		return nombreDominio;
	}

	public void setNombreDominio(String nombreDominio) {
		this.nombreDominio = nombreDominio;
	}

	public String getTipoPlan() {
		return tipoPlan;
	}

	public void setTipoPlan(String tipoPlan) {
		this.tipoPlan = tipoPlan;
	}

	public String getCancelaDescripcion() {
		return cancelaDescripcion;
	}

	public void setCancelaDescripcion(String cancelaDescripcion) {
		this.cancelaDescripcion = cancelaDescripcion;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getDomainid() {
		return domainid;
	}

	public void setDomainid(long domainid) {
		this.domainid = domainid;
	}

	public Vector<WS_KeywordVO> getArregloDireccion() {
		return arregloDireccion;
	}

	public void setArregloDireccion(Vector<WS_KeywordVO> arregloDireccion) {
		this.arregloDireccion = arregloDireccion;
	}

	public Vector<WS_KeywordVO> getArregloPerfil() {
		return arregloPerfil;
	}

	public void setArregloPerfil(Vector<WS_KeywordVO> arregloPerfil) {
		this.arregloPerfil = arregloPerfil;
	}

	public Vector<WS_KeywordVO> getArregloInformacionAdicional() {
		return arregloInformacionAdicional;
	}

	public void setArregloInformacionAdicional(Vector<WS_KeywordVO> arregloInformacionAdicional) {
		this.arregloInformacionAdicional = arregloInformacionAdicional;
	}

	public WS_LocalizacionVO getCoordenadasUbicacion() {
		return coordenadasUbicacion;
	}

	public void setCoordenadasUbicacion(WS_LocalizacionVO coordenadasUbicacion) {
		this.coordenadasUbicacion = coordenadasUbicacion;
	}

	public boolean[] getEstatusEdicion() {
		return estatusEdicion;
	}

	public void setEstatusEdicion(boolean estatusEdicion[]) {
		this.estatusEdicion = estatusEdicion;
	}

	public boolean[] getEstatusPerfil() {
		return estatusPerfil;
	}

	public void setEstatusPerfil(boolean estatusPerfil[]) {
		this.estatusPerfil = estatusPerfil;
	}

	public Vector<WS_ImagenVO> getListaImagenes() {
		return listaImagenes;
	}

	public void setListaImagenes(Vector<WS_ImagenVO> listaImagenes) {
		this.listaImagenes = listaImagenes;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatusDominio() {
		return statusDominio;
	}

	public void setStatusDominio(String statusDominio) {
		this.statusDominio = statusDominio;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public WS_InfoUsuarioVO getInfoUsr() {
		return infoUsr;
	}

	public void setInfoUsr(WS_InfoUsuarioVO infoUsr) {
		this.infoUsr = infoUsr;
	}

	public WS_DomainVO getDomainData() {
		return domainData;
	}

	public void setDomainData(WS_DomainVO domainData) {
		this.domainData = domainData;
	}

	public WS_UserDomainVO getUserDomainData() {
		return userDomainData;
	}

	public void setUserDomainData(WS_UserDomainVO userDomainData) {
		this.userDomainData = userDomainData;
	}

	public boolean isEligioColor() {
		return eligioColor;
	}

	public void setEligioColor(boolean eligioColor) {
		this.eligioColor = eligioColor;
	}

	public boolean isExisteLogin() {
		return existeLogin;
	}
        
	public void setExisteLogin(boolean existeLogin) {
		this.existeLogin = existeLogin;
	}

	public ArrayList<CallingCodes> getArrayCodigos() {
		return arrayCodigos;
	}

	public void setArrayCodigos(ArrayList<CallingCodes> arrayCodigos) {
		this.arrayCodigos = arrayCodigos;
	}

	public String getRutaPromocion() {
		return rutaPromocion;
	}

	public void setRutaPromocion(String rutaPromocion) {
		this.rutaPromocion = rutaPromocion;
	}

	public boolean isAgregoImagenPromocion() {
		return agregoImagenPromocion;
	}

	public void setAgregoImagenPromocion(boolean agregoImagenPromocion) {
		this.agregoImagenPromocion = agregoImagenPromocion;
	}

	public CompraModel getCompra() {
		return compra;
	}

	public void setCompra(CompraModel compra) {
		this.compra = compra;
	}

	public String getRedSocial() {
		return redSocial;
	}

	public void setRedSocial(String redSocial) {
		this.redSocial = redSocial;
	}

	public WS_RespuestaVO getRespuestaVOActual() {
		return respuestaVOActual;
	}

	public void setRespuestaVOActual(WS_RespuestaVO respuestaVOActual) {
		this.respuestaVOActual = respuestaVOActual;
	}

	public String getCodeCamp() {
		return codeCamp;
	}

	public void setCodeCamp(String codeCamp) {
		this.codeCamp = codeCamp;
	}

	public String getFechaInicioTel() {
		return fechaInicioTel;
	}

	public void setFechaInicioTel(String fechaInicioTel) {
		this.fechaInicioTel = fechaInicioTel;
	}

	public String getFechaFinTel() {
		return fechaFinTel;
	}

	public void setFechaFinTel(String fechaFinTel) {
		this.fechaFinTel = fechaFinTel;
	}

	public String getDomainType() {
		return domainType;
	}

	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	public Vector<WS_UsuarioDominiosVO> getUsuarioDominios() {
		return usuarioDominios;
	}

	public void setUsuarioDominios(Vector<WS_UsuarioDominiosVO> usuarioDominios) {
		this.usuarioDominios = usuarioDominios;
	}

	public WS_UsuarioDominiosVO getUsuarioDominioVOActual() {
		return usuarioDominioVOActual;
	}

	public void setUsuarioDominioVOActual(
			WS_UsuarioDominiosVO usuarioDominioVOActual) {
		this.usuarioDominioVOActual = usuarioDominioVOActual;
	}

	public String getDescripcionDominio() {
		return descripcionDominio;
	}

	public void setDescripcionDominio(String descripcionDominio) {
		this.descripcionDominio = descripcionDominio;
	}

	public Vector<WS_StatusDomainVO> getItemsDominioGratuito() {
		return itemsDominioGratuito;
	}

	public void setItemsDominioGratuito(
			Vector<WS_StatusDomainVO> itemsDominioGratuito) {
		this.itemsDominioGratuito = itemsDominioGratuito;
	}

	public String getSuscritoCompra() {
		return suscritoCompra;
	}

	public void setSuscritoCompra(String suscritoCompra) {
		this.suscritoCompra = suscritoCompra;
	}

	public String getTipoPlanCompra() {
		return tipoPlanCompra;
	}

	public void setTipoPlanCompra(String tipoPlanCompra) {
		this.tipoPlanCompra = tipoPlanCompra;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getCampania() {
		return campania;
	}

	public void setCampania(String campania) {
		this.campania = campania;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public GraphUser getFacebookInformation() {
		return facebookInformation;
	}

	public void setFacebookInformation(GraphUser facebookInformation) {
		this.facebookInformation = facebookInformation;
	}

	public String getPrecioMensual() {
		return precioMensual;
	}

	public void setPrecioMensual(String precioMensual) {
		this.precioMensual = precioMensual;
	}

	public String getPrecioAnual() {
		return precioAnual;
	}

	public void setPrecioAnual(String precioAnual) {
		this.precioAnual = precioAnual;
	}

	public String getPrecioTel() {
		return precioTel;
	}

	public void setPrecioTel(String precioTel) {
		this.precioTel = precioTel;
	}

	public String getHashMovilizaSitio() {
		return hashMovilizaSitio;
	}

	public void setHashMovilizaSitio(String hashMovilizaSitio) {
		this.hashMovilizaSitio = hashMovilizaSitio;
	}

	public List<Catalogo> getCatalogoDominios() {
		return catalogoDominios;
	}

	public void setCatalogoDominios(List<Catalogo> catalogoDominios) {
		this.catalogoDominios = catalogoDominios;
	}

	public Catalogo getCatDominioActual() {
		return catDominioActual;
	}

	public void setCatDominioActual(Catalogo catDominioActual) {
		this.catDominioActual = catDominioActual;
	}
	
}
