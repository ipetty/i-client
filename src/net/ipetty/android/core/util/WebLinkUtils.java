package net.ipetty.android.core.util;

import net.ipetty.android.core.ui.UserURLSpan;
import android.app.Activity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.TextView;

public class WebLinkUtils {

	private static final String TAG = WebLinkUtils.class.getSimpleName();

	public static void setUserLinkIntercept(Activity activity, TextView tv, String text) {
		CharSequence charSequence = Html.fromHtml(text);
		Spannable sp = (Spannable) charSequence;
		int end = charSequence.length();
		URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
		Log.d(TAG, "length" + urls.length);
		SpannableStringBuilder spannable = new SpannableStringBuilder(sp);
		for (URLSpan url : urls) {
			spannable.setSpan(new UserURLSpan(activity, Integer.valueOf(url.getURL().toString())),
					sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(spannable);
	}

	public static void setUserLinkClickIntercept(Activity activity, TextView tv, String text) {
		setUserLinkIntercept(activity, tv, text);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
