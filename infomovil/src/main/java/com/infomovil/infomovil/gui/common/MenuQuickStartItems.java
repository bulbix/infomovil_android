package com.infomovil.infomovil.gui.common;

public enum MenuQuickStartItems {
	
	NOMBRE_EMPRESA(0),
	DESCRIPCION_CORTA(1),
	CONTACTO(2),
	PRODUCTOS_SERVICIOS(3),
	MAPA(4),
	PUBLICAR(5);
	
	private int value;
	
	private MenuQuickStartItems(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static MenuQuickStartItems byValue(int value) {
        for (MenuQuickStartItems m : MenuQuickStartItems.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        return null;
    }

}
