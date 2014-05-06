package net.ipetty.android.ui;

import java.util.ArrayList;
import java.util.List;

import net.ipetty.R;
import net.ipetty.android.ui.event.BackClickListener;
import net.ipetty.android.ui.model.ModDialogItem;
import net.ipetty.android.utils.DialogUtils;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {
	private Dialog sexDialog;
	private Dialog typeDialog;
	private TextView btnSexy;
	private TextView btnType;
	private List<ModDialogItem> sexyItems;
	private List<ModDialogItem> typeItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/* action bar */
		ImageView btnBack = (ImageView) this.findViewById(R.id.action_bar_left_image);
		TextView text = (TextView) this.findViewById(R.id.action_bar_title);
		text.setText(this.getResources().getString(R.string.title_activity_register));
		btnBack.setOnClickListener(new BackClickListener(this));

		sexyItems = new ArrayList<ModDialogItem>();
		sexyItems.add(new ModDialogItem(null, "男生", sexOnClick));
		sexyItems.add(new ModDialogItem(null, "女生", sexOnClick));
		sexyItems.add(new ModDialogItem(null, "男女生", sexOnClick));

		btnSexy = (TextView) this.findViewById(R.id.sexy);
		btnSexy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sexDialog = DialogUtils.modPopupDialog(RegisterActivity.this, sexyItems, sexDialog);
			}
		});

		typeItems = new ArrayList<ModDialogItem>();
		typeItems.add(new ModDialogItem(null, "汪星人", typeOnClick));
		typeItems.add(new ModDialogItem(null, "喵星人", typeOnClick));
		typeItems.add(new ModDialogItem(null, "水星人", typeOnClick));
		typeItems.add(new ModDialogItem(null, "冷星人", typeOnClick));
		typeItems.add(new ModDialogItem(null, "异星人", typeOnClick));

		btnType = (TextView) this.findViewById(R.id.type);
		btnType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				typeDialog = DialogUtils.modPopupDialog(RegisterActivity.this, typeItems, typeDialog);
			}
		});

	}

	private OnClickListener sexOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
			btnSexy.setText(text);
			sexDialog.cancel();
		}
	};

	private OnClickListener typeOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String text = ((TextView) v.findViewById(R.id.text)).getText().toString();
			btnType.setText(text);
			typeDialog.cancel();
		}
	};
	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.register, menu); return true; }
	 */

}
