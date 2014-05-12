package net.ipetty.android.sdk;

import java.util.List;

import net.ipetty.android.sdk.domain.IpetFavor;
import net.ipetty.android.sdk.domain.IpetPhoto;

/**
 * 赞API
 * 
 * @author xiaojinghai
 */
public interface FavorApi {

	public IpetPhoto favor(String photoId, String text);

	public IpetPhoto unfavor(String photoId);

	public List<IpetFavor> list(String photoId);

}
