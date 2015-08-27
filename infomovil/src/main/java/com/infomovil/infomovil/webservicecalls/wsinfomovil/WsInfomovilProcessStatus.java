/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil;

import com.infomovil.infomovil.gui.common.MenuItems;

/**
 * @author Ignaki Dominguez
 *
 */
public enum WsInfomovilProcessStatus
{
	
	ERROR_IO(-100),
	ERROR_PARSER_XML(-200),
	ERROR_WSINFOMOVIL (-300),
	ERROR_GENERICO (-400),
	ERROR_CONEXION(-500),
	EXITO (0),
	ERROR_DESCONOCIDO(1),
	SIN_ELEMENTOS (3),
	SIN_EXITO (4),
	ERROR_INSERTA_USUARIO (5),
	ERROR_LOGIN (6),
	ERROR_INSERTA_IMAGEN (7),
	ERROR_GET_FECHA_EXPIRA (8),
	ERROR_INSERTA_REGISTRO (9),
	ERROR_COMPRA_DOMINIO (10),
	ERROR_TOKEN (11),
	ERROR_DESENCRIPTA (12),
	ERROR_ACTUALIZA (13),
	ERROR_PUBLICAR (14),
	ERROR_DOM_EXISTE (15),
	ERROR_SESION(16),
	
	//Errores de codigos
	ERROR_CODIGO_NOEXISTE(-1),
	ERROR_CODIGO_CAMPANIA_CADUCO(-2),
	ERROR_CODIGO_CAMPANIA_NOACTIVA(-3),
	ERROR_CODIGO_ASOCIADO(-5),
	ERROR_CODIGO_AGOTADO(-6),
	ERROR_CODIGO_CUENTA_NOCAMP(-7),
	ERROR_CODIGO_CUENTA_YAACTIVADA(-8),
	ERROR_CODIGO_PLANPRO(-13);
	
	private int value;
	
	WsInfomovilProcessStatus(int value) { this.value = value; }
	public int getValue() {return this.value;}
	
	
	public static WsInfomovilProcessStatus byValue(int value) {
        for (WsInfomovilProcessStatus s : WsInfomovilProcessStatus.values()) {
            if (s.getValue() == value) {
                return s;
            }
        }
        return null;
    }
	
	
}
