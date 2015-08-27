package com.infomovil.infomovil.gui.common;

public enum MenuItems {
	
	COLOR_FONDO(0),
	NOMBRE_EMPRESA(1),
	LOGO(2),
	NEGOCIO_PROFESION(3),
	DESCRIPCION_CORTA(4),
	CONTACTO(5),	
	MAPA(6),
	VIDEO(7),
	PROMOCIONES(8),
	GALERIA_IMAGENES(9),
	PERFIL(10),
	DIRECCION(11),
	INFORMACION_ADICIONAL(12),
	PUBLICAR(13);
	
	private int value;
	
	private MenuItems(int value){
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static MenuItems byValue(int value) {
        for (MenuItems m : MenuItems.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        return null;
    }
	
	
}
