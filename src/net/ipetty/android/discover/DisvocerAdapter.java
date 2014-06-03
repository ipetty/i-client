package net.ipetty.android.discover;

import java.util.List;

import net.ipetty.R;
import net.ipetty.android.core.util.AnimUtils;
import net.ipetty.android.core.util.AppUtils;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DisvocerAdapter extends BaseAdapter {
	public final static String TAG = "DisvocerAdapter";
	private LayoutInflater mInflater;
	private Context context;
	private List list = null; // 这个就本地dataStore
	DisplayImageOptions options;

	public DisvocerAdapter(Context context) {
		super();
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		options = AppUtils.getCacheImageBublder().showImageForEmptyUri(R.drawable.default_image).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 18;
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	private class GridHolder {
		ImageView image;
		TextView findTextView;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		GridHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_discover_item, null);

			holder = new GridHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			LayoutParams ps = (LayoutParams) holder.image.getLayoutParams();
			DisplayMetrics dm = AnimUtils.getDefaultDisplayMetrics((Activity) context);
			int width = dm.widthPixels / 3 - AnimUtils.dip2px(context, 5);
			ps.height = width;
			holder.image.setLayoutParams(ps);
			convertView.setTag(holder);
		} else {
			holder = (GridHolder) convertView.getTag();
		}
		String str = "http://image.zcool.com.cn/img3/19/24/m_1330054314883.jpg";
		ImageLoader.getInstance().displayImage(str, holder.image, options);

		return convertView;
	}
}
