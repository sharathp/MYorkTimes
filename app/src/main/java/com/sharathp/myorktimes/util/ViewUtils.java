package com.sharathp.myorktimes.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class ViewUtils {

    public static void setToolbarTitleFont(final Context context, final TextView textView) {
        final Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Pacifico.ttf");
        // Assign the typeface to the view
        textView.setTypeface(font);
    }
}
