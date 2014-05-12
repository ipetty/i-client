package net.ipetty.android.sdk.impl;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import net.ipetty.android.sdk.UserApi;
import net.ipetty.android.sdk.base.ApiBase;
import net.ipetty.android.sdk.base.ApiContext;
import net.ipetty.android.sdk.domain.IpetUser;
import net.ipetty.android.sdk.domain.IpetUserUpdate;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;

/**
 * 
 * @author xiaojinghai
 */
public class UserApiImpl extends ApiBase implements UserApi {

	public UserApiImpl(ApiContext context) {
		super(context);
	}

	@Override
	public IpetUser getUser(String userId) {
		IpetUser user = context.getRestTemplate().getForObject(ApiContext.API_SERVER_BASE + "user/{id}",
				IpetUser.class, userId);
		return user;
	}

	@Override
	public List<IpetUser> getUsers(String ids) {
		URI uri = buildUri("user/listByIds", "ids", ids);
		IpetUser[] users = context.getRestTemplate().getForObject(uri, IpetUser[].class);
		List<IpetUser> list = Arrays.asList(users);
		return list;
	}

	@Override
	public IpetUser updateUserInfo(IpetUserUpdate update) {
		requireAuthorization();
		update.setId(context.getCurrUserId());
		IpetUser ret = context.getRestTemplate().postForObject(ApiContext.API_SERVER_BASE + "user/updateInfo", update,
				IpetUser.class);
		return ret;
	}

	@Override
	public IpetUser updateAvatar(FileSystemResource avatarFile) {
		requireAuthorization();
		String url = ApiContext.API_SERVER_BASE + "user/uploadAvatar";
		LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
		body.add("userId", context.getCurrUserId());
		body.add("file", avatarFile);
		IpetUser ret = context.getRestTemplate().postForObject(url, body, IpetUser.class);
		return ret;
	}

	@Override
	public IpetUser updateAvatar(File avatarFile) {
		FileSystemResource fsr = new FileSystemResource(avatarFile);
		IpetUser ret = updateAvatar(fsr);
		return ret;
	}

	@Override
	public IpetUser updateAvatar(String avatarFilePath) {
		File f = new File(avatarFilePath);
		IpetUser ret = updateAvatar(f);
		return ret;
	}

}
