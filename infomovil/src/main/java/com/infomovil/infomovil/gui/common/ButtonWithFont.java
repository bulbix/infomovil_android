package com.infomovil.infomovil.gui.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonWithFont extends Button {
	
	public ButtonWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonWithFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
        setTypeface(tf);
    }
}
