package com.infomovil.infomovil.gui.fragment.principal;

import android.app.Activity;

import com.infomovil.infomovil.model.DatosUsuario;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_DomainVO;
import com.infomovil.infomovil.webservicecalls.wsinfomovil.model.WS_RecordNaptrVO;
import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;

public class VistaPreviaDominio {
	
	
	public String getHtml(Activity activity){
		AndroidTemplates loader = new AndroidTemplates(activity.getBaseContext());
		Theme theme = new Theme(loader);
		Chunk html = theme.makeChunk("vistaPreviaDominio#infomovil");
		DatosUsuario datosUsuario = DatosUsuario.getInstance();
		
		WS_DomainVO dominioVO = datosUsuario.getDomainData();
		
		html.set("domainName", dominioVO.getDomainName());
		html.set("displaySting", dominioVO.getDisplayString());
		
		String imagenLogo="";
		
		if (datosUsuario.getImagenLogo() != null
				&& datosUsuario.getImagenLogo().getImagenClobGaleria() != null
				&& datosUsuario.getImagenLogo().getImagenClobGaleria().length() > 2) {
			imagenLogo = datosUsuario.getImagenLogo().getImagenClobGaleria();
		} else {
			imagenLogo = "iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJ"
					+ "lYWR5ccllPAAAA9BJREFUeNrsnMFrE0EUxqfB9dAcugGTgw3EgFRqC4pUi4ii3kT8a0W8KSgiqVER2opFSIXGQyJke6iH5hD3"
					+ "i90SSrLkzbyZ3Z2875JDt2HeL+/NezP7ZpZGo9FjJdLWUgxwJBgEoAAUgAJQJAAFoAAUgCIBuLgAoyhSp8OhigbH489JXQ4CFVZW/n"
					+ "+GoQCEut3fqtfvq0F0rHq9Pul/a7WqqoQrqlatqtXVq4sDEF724+DnGN5FL9MVvBIQb6xdd+6dzgDC03b3vpM9jSp45ubG+tgzvQB4cvJ"
					+ "XtT61rYObBnL77pYql5eLC/AgDtXdvX22UNUJ7c2Nm2otDu1CARzGwN5/+Ojc69K88eGD+yqIgeYeIJIE4CF0dVWPE0LzWuM8uyLhdA5/qa"
					+ "P4U1cIZUDkTjKsAAHvzdt3RiG7fW9rDG+aALG10zYK6adPHrFCLOUJXuJ5s4S/1Q1qPowNY8RYcwWQAx6E8oPjGZcQjQEiYbR2PrNk2nlCiyP8Ts/GPGQYszFAzEkDxpBwJYzZZD5lAYjsaJIZp00FHM/MK4y9azj+klnotlm9Aks9jmeoEWQSytoAv3z9xr7CODqr92bJtBacNR/CFqd1IIrkl69eW5ufLhbS2IjAspAb3qRePH+mtW7WAgi3T/OUIgo/GIp46yGM+cI3eMn0oDMXkgF2Ov7BM7GtpPNLeQvw0DJAJI8iFs2U4pq6i3SJ8jCyoW1htyRNWMfaFGxslht2AEYOvM/Vu4x0Gxt2QhivH30X1UYSwLxs0VsNYaKNJSVys5ngIoHkxgsJtooHuvJAkQAUgIUGWF4uLwwUiq3zA7TcpJMrgARbS7a+eBHgkQFWctRaa0tUG0kAwwUAGNoEWKtd8R4g1UYawGp13OHkq2AbdTuNXAdm2RFvWzq2kQE2mw1vAerYRgYIF/exnIFNOrvhWks5NG77Jl2btADiLb5PXghb0jpjrWwm3Ll9yxuAJrZoA0TGqnuQkWGDSWVhtJ2FZpwi14UYu05DERvAgGEAWQpjNz18Y7yhCvcvYlbGmDkWBSWewaxrZ7FMCuZ4rKbHJVgBJuFQBIi6jZTWARYBIjc8doAJxDzWiBiTjYRn7bww3u6jl5rab4ejqanfS+xdwSoD4Gx1fVk9cI2eYxy4xh0JWQh3KCDbBhZrVSd3Jri6L2HSi13dm+D01g7b5z2wLMPxfpdNmpncG4N5MTmFbtpzjbdoyaGcLHaIMr+5CPMkIPZ6fyZuLopm3FwUnt9chJc/gBdkvBaXu7MEoAAUgAJQJAAFoAAUgCIB6F7/BBgAMUcmVi6xazAAAAAASUVORK5CYII=";
		}
		
		html.set("imagenLogo",imagenLogo);
		html.set("nombreEmpresa", dominioVO.getTextRecord());
		
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
		
		html.set("telefonoPrincipal",telefonoPrincipal);
		html.set("emailPrincipal",emailPrincipal);
		
		html.set("latitude", datosUsuario.getCoordenadasUbicacion().getLatitudeLoc());
		html.set("longitude", datosUsuario.getCoordenadasUbicacion().getLongitudeLoc());
		
		html.set("promocionTitulo");
		
		
		
		
		
		return html.toString();
	}
	

}
