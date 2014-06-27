package net.ipetty.android.core;

public class Constant {

	// 文件服务器地址
	public static final String FILE_SERVER_BASE = "http://api.ipetty.tk";

	// API服务器地址
	public static final String API_SERVER_BASE = "http://api.ipetty.tk/api";

	public final static String INTENT_PHOTO_PATH_KEY = "publish_photo_path";
	public final static String INTENT_PHOTO_OUT_PATH_KEY = "publish_photo_out_path";
	public final static String INTENT_IMAGE_ORIGINAL_KEY = "url";
	public final static String INTENT_FEED_ID_KEY = "feedId";
	public final static String INTENT_USER_ID_KEY = "id";

	// BROADCAST
	public final static String BROADCAST_INTENT_FEED_COMMENT = "BROADCAST_INTENT_FEED_COMMENT";
	public final static String BROADCAST_INTENT_FEED_FAVORED = "BROADCAST_INTENT_FEED_FAVORED";

	public static final String FEEDVO_JSON_SERIALIZABLE = "FEEDVO_JSON_SERIALIZABLE";

	public final static float HOME_HEAD_BG_SCALE = 1.5f;

	public final static float COMPRESS_IMAGE_MAX_WIDTH = 960f;
	public final static float COMPRESS_IMAGE_MAX_HEIGHT = 1280f;
	public final static float COMPRESS_IMAGE_MIN_WIDTH = 64f;
	public final static float COMPRESS_IMAGE_MIN_HEIGHT = 64f;
	public final static int COMPRESS_IMAGE_KB = 100;

}
