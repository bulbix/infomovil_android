package com.infomovil.infomovil.gui.common.interfaces;

import java.util.List;

import com.infomovil.infomovil.model.ItemSelectModel;

public interface Progressable {
	
	void hideDialog();
	void showDialog();
	void execute(List<ItemSelectModel> videos);

}
