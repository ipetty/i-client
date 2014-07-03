package net.ipetty.android.core;

import java.util.HashMap;

import net.ipetty.R;

public class Constant {

	// 文件服务器地址
	public static final String FILE_SERVER_BASE = "http://api.ipetty.tk";

	// API服务器地址
	public static final String API_SERVER_BASE = "http://api.ipetty.tk/api";

	public static final Integer EMPTY_USER_ID = -1;

	public final static String INTENT_PHOTO_PATH_KEY = "publish_photo_path";
	public final static String INTENT_PHOTO_OUT_PATH_KEY = "publish_photo_out_path";
	public final static String INTENT_IMAGE_ORIGINAL_KEY = "url";
	public final static String INTENT_FEED_ID_KEY = "feedId";
	public final static String INTENT_USER_ID_KEY = "id";
	public final static String INTENT_PET_ID_KEY = "petId";

	// BROADCAST
	public final static String BROADCAST_INTENT_FEED_PUBLISH = "BROADCAST_INTENT_FEED_PUBLISH";
	public final static String BROADCAST_INTENT_FEED_COMMENT = "BROADCAST_INTENT_FEED_COMMENT";
	public final static String BROADCAST_INTENT_FEED_FAVORED = "BROADCAST_INTENT_FEED_FAVORED";

	public static final String FEEDVO_JSON_SERIALIZABLE = "FEEDVO_JSON_SERIALIZABLE";

	public final static float HOME_HEAD_BG_SCALE = 1.5f;

	public final static float COMPRESS_IMAGE_MAX_WIDTH = 960f;
	public final static float COMPRESS_IMAGE_MAX_HEIGHT = 1280f;
	public final static float COMPRESS_IMAGE_MIN_WIDTH = 64f;
	public final static float COMPRESS_IMAGE_MIN_HEIGHT = 64f;
	public final static int COMPRESS_IMAGE_KB = 100;

	// PET_FAMILY
	public final static String PET_FAMILY_DOG_KEY = "dog";
	public final static String PET_FAMILY_CAT_KEY = "cat";
	public final static String PET_FAMILY_OTHER_KEY = "other";

	public static HashMap<String, Integer> PET_FAMILY_RES_MAP = new HashMap<String, Integer>();

	static {
		PET_FAMILY_RES_MAP.put(PET_FAMILY_DOG_KEY, R.drawable.default_main_header_bg_dog);
		PET_FAMILY_RES_MAP.put(PET_FAMILY_CAT_KEY, R.drawable.default_main_header_bg_cat);
		PET_FAMILY_RES_MAP.put(PET_FAMILY_OTHER_KEY, R.drawable.default_main_header_bg_other);
	}
}
