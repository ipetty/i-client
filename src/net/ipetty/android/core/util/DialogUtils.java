package net.ipetty.android.core.util;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.ui.ModDialogItem;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogUtils {
	public final static String TAG = "DialogUtils";

	public static Dialog modPopupDialog(Context context, List<ModDialogItem> items, Dialog d) {
		if (d != null && d.isShowing()) {
			return d;
		}
		final Dialog dialog = new Dialog(context, R.style.PopDialog);
		dialog.setContentView(R.layout.dialog_mod_pop);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.items);

		for (ModDialogItem item : items) {
			View v = dialog.getLayoutInflater().inflate(R.layout.dialog_mod_pop_item, layout, false);
			View line = v.findViewById(R.id.line);
			TextView tx = (TextView) v.findViewById(R.id.text);
			TextView val = (TextView) v.findViewById(R.id.value);
			ImageView img = (ImageView) v.findViewById(R.id.image);
			if (item.getIconId() == null) {
				img.setVisibility(View.GONE);
			}
			if (items.indexOf(item) == (items.size() - 1)) {
				line.setVisibility(View.GONE);
			}

			tx.setText(item.getText());
			val.setText(item.getValue());
			v.setOnClickListener(item.getOnClickListener());
			layout.addView(v);
		}
		if (dialog != null) {
			dialog.show();
		}
		return dialog;
	}

	public static Dialog bottomPopupDialog(Activity context, OnClickListener[] listeners, int id, String title, Dialog d) {
		if (d != null && d.isShowing()) {
			return d;
		}

		final Dialog dialog = new Dialog(context, R.style.BottomPopDialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics(context);
		params.x = 0;
		params.y = dm.heightPixels;
		window.setAttributes(params);
		window.setBackgroundDrawableResource(R.drawable.dialog_bottom_background);

		View itemView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_pop, null);
		dialog.setContentView(itemView);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		TextView item1 = (TextView) itemView.findViewById(R.id.item1);
		TextView item2 = (TextView) itemView.findViewById(R.id.item2);
		TextView item3 = (TextView) itemView.findViewById(R.id.item3);
		TextView item4 = (TextView) itemView.findViewById(R.id.item4);
		TextView item5 = (TextView) itemView.findViewById(R.id.item5);
		TextView titleView = (TextView) itemView.findViewById(R.id.title);
		String itemValues[] = context.getResources().getStringArray(id);
		TextView[] items = new TextView[] { item1, item2, item3, item4, item5 };

		for (int i = 0; i < items.length; i++) {
			if (i < listeners.length) {
				items[i].setText(itemValues[i]);
				items[i].setOnClickListener(listeners[i]);
			} else {
				items[i].setVisibility(View.GONE);
			}
		}

		if (title == null) {
			titleView.setVisibility(View.GONE);
		} else {
			titleView.setText(title);
		}

		TextView cancelView = (TextView) itemView.findViewById(R.id.cancel);
		cancelView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		if (dialog != null) {
			dialog.show();
		}

		return dialog;

	}
}
