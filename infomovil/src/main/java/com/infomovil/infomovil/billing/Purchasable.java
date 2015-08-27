package com.infomovil.infomovil.billing;

import java.math.BigDecimal;

import com.infomovil.infomovil.billing.util.IabResult;
import com.infomovil.infomovil.billing.util.Purchase;

public interface Purchasable {
	
	void compraCorrecta(IabResult result, Purchase info, String tipo, BigDecimal precio);
	void compraFallida(String tipo);
	
}
