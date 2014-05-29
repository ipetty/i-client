package net.ipetty.android.core.ui;

import android.view.View.OnClickListener;

public class ModDialogItem {
	private Integer iconId;
	private String text;
	private OnClickListener onClickListener;

	public Integer getIconId() {
		return iconId;
	}

	public ModDialogItem(Integer iconId, String text, OnClickListener onClickListener) {
		super();
		this.iconId = iconId;
		this.text = text;
		this.onClickListener = onClickListener;
	}

	public void setIconId(Integer iconId) {
		this.iconId = iconId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
}
