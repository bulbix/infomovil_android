package at.bookworm.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import at.bookworm.R;

/**
 * @author Grantland Chew <grantlandchew@gmail.com>
 */
public class TabControlButton extends SegmentedControlButton {

    public TabControlButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.tabControlButtonStyle);
        init();
    }
    
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Avenir LT 45 Book.ttf");
        setTypeface(tf);
    }
}
