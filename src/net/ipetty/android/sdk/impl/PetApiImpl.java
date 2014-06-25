package net.ipetty.android.sdk.impl;

import java.util.Arrays;
import java.util.List;

import net.ipetty.android.core.Constant;
import net.ipetty.android.sdk.core.ApiBase;
import net.ipetty.sdk.PetApi;
import net.ipetty.vo.PetVO;
import android.content.Context;

/**
 * PetApiImpl
 * 
 * @author luocanfeng
 * @date 2014年6月25日
 */
public class PetApiImpl extends ApiBase implements PetApi {

	public PetApiImpl(Context context) {
		super(context);
	}

	private static final String URI_NEW_PET = "/newpet";

	/**
	 * 新增宠物
	 */
	public PetVO save(PetVO pet) {
		super.requireAuthorization();
		return getRestTemplate().postForObject(buildUri(URI_NEW_PET), pet, PetVO.class);
	}

	private static final String URI_GET_BY_ID = "/pet/id/{id}";

	/**
	 * 根据ID获取宠物
	 */
	public PetVO getById(Integer id) {
		return getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_GET_BY_ID, PetVO.class, id);
	}

	private static final String URI_GET_BY_UID = "/pet/uid/{uid}";

	/**
	 * 根据uid获取宠物
	 */
	public PetVO getByUid(int uid) {
		return getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_GET_BY_UID, PetVO.class, uid);
	}

	private static final String URI_GET_BY_UNIQUE_NAME = "/pet/{uniqueName}";

	/**
	 * 根据爱宠唯一标识获取宠物
	 */
	public PetVO getByUniqueName(String uniqueName) {
		return getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_GET_BY_UNIQUE_NAME, PetVO.class,
				uniqueName);
	}

	private static final String URI_LIST_BY_USER_ID = "/pets/{userId}";

	/**
	 * 获取指定用户的所有宠物
	 */
	public List<PetVO> listByUserId(Integer userId) {
		return Arrays.asList(getRestTemplate().getForObject(Constant.API_SERVER_BASE + URI_LIST_BY_USER_ID,
				PetVO[].class, userId));
	}

	private static final String URI_UPDATE = "/pet/update";

	/**
	 * 更新宠物信息
	 */
	public PetVO update(PetVO pet) {
		super.requireAuthorization();
		return getRestTemplate().postForObject(buildUri(URI_UPDATE), pet, PetVO.class);
	}

}
