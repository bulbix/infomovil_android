package com.infomovil.infomovil.gui.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextWithFont extends TextView {

	public TextWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextWithFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
        setTypeface(tf);
    }


}
