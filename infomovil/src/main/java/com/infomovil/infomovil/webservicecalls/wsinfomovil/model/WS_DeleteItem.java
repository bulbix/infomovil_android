/**
 * 
 */
package com.infomovil.infomovil.webservicecalls.wsinfomovil.model;

/**
 * @author Ignaki Dominguez
 *
 */
public class WS_DeleteItem
{
	private long idDomain;
	private int indexItem;
	
	public WS_DeleteItem(long idDomain, int indexItem)
	{
		this.idDomain	= idDomain;
		this.indexItem	= indexItem;
	}
	
	public long getIdDomain() {
		return idDomain;
	}

	public int getIndexItem() {
		return indexItem;
	}
}
