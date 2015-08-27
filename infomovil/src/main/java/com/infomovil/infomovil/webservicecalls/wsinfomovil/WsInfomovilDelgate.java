/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil;


/**
 * @author Ignaki Dominguez
 *
 */
public interface WsInfomovilDelgate
{
	public void respuestaCompletada(WSInfomovilMethods metodo, long milisegundo, WsInfomovilProcessStatus status);
	
	public void respuestaObj(WSInfomovilMethods metodo, WsInfomovilProcessStatus status, Object obj);
	
	public void respuestaErronea(WsInfomovilProcessStatus status, Exception e);
}
