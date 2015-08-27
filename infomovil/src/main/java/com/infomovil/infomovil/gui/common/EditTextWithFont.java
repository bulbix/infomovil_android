package com.infomovil.infomovil.gui.common;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

public class EditTextWithFont extends EditText {

	public EditTextWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithFont(Context context) {
        super(context);
        init();
    }

    private void init() {
    	
    	if(this.getInputType() == 131073){
    		this.setGravity(Gravity.TOP);
    	}
    	
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
        setTypeface(tf);
        
        
        InputFilter filter = new InputFilter() {          
        	@Override
        	public CharSequence filter(CharSequence source, int start, int end,
        			Spanned dest, int dstart, int dend) {
        		boolean keepOriginal = true;
        		StringBuilder sb = new StringBuilder(end - start);
        		for (int i = start; i < end; i++) {
        			char c = source.charAt(i);
        			if (isAsciiPrintable(c)) // put your condition here
        				sb.append(c);
        			else
        				keepOriginal = false;
        		}
        		if (keepOriginal)
        			return null;
        		else {
        			if (source instanceof Spanned) {
        				SpannableString sp = new SpannableString(sb);
        				TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
        				return sp;
        			} else {
        				return sb;
        			}           
        		}
        	}
        };
        
        setFilters(new InputFilter[]{filter});
        
        
    }
    
    public static boolean isAsciiPrintable(char ch) {
        return (ch >= 32 && ch < 255) || (ch == 13) || (ch == 10);
    }

}
