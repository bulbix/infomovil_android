package com.infomovil.infomovil.fragment.gui.nombrar;


import com.infomovil.infomovil.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class TestFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static TestFragment newInstance(String content) {
        TestFragment fragment = new TestFragment();
        fragment.mContent = content;

        return fragment;
    }

    private String mContent = "???";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView webView = new WebView(getActivity());
        //TextView text = new TextView(getActivity());
        //text.setGravity(Gravity.CENTER);
        //text.setText(mContent);
        //text.setTextSize(13 * getResources().getDisplayMetrics().scaledDensity);
        //text.setPadding(20, 20, 20, 20);
        //text.setTextColor(getResources().getColor(R.color.colorFuenteAzul));
        webView.getSettings().setDefaultFontSize(19);
        webView.loadDataWithBaseURL(null, mContent, "text/html", "utf-8", null);
        

        LinearLayout layout = new LinearLayout(getActivity());
        int px = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        layout.setLayoutParams(new LayoutParams(px, LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(webView);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
        	layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_border));
        }
        else {
        	layout.setBackground(getResources().getDrawable(R.drawable.rounded_border));
        }
        

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
