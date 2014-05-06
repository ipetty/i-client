package net.ipetty.android.utils;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.ui.model.ModDialogItem;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
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
		dialog.setContentView(R.layout.mod_dialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.items);

		for (ModDialogItem item : items) {
			View v = dialog.getLayoutInflater().inflate(R.layout.mod_dialog_item, layout, false);
			TextView tx = (TextView) v.findViewById(R.id.text);
			ImageView img = (ImageView) v.findViewById(R.id.image);
			if (item.getIconId() == null) {
				img.setVisibility(View.GONE);
			}
			tx.setText(item.getText());
			v.setOnClickListener(item.getOnClickListener());
			layout.addView(v);
		}
		if (dialog != null) {
			dialog.show();
		}
		return dialog;
	}
}
