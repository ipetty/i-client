package net.ipetty.android.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class DeviceUtils {

    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static UUID uuid;

    /**
     * 设备的UUID http://luhuajcdd.iteye.com/blog/1608746
     *
     *
     * @see http://code.google.com/p/android/issues/detail?id=10603
     *
     * @return a UUID
     */
    public static synchronized UUID getDeviceUUID(Context context) {
        if (uuid == null) {

            if (uuid == null) {
                final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {
                    uuid = UUID.fromString(id);
                } else {
                    final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                        } else {
                            final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                }
            }
        }

        return uuid;

    }

    public static final int REQUEST_CODE_PICK_IMAGE = 50;
    public static final int REQUEST_CODE_TAKE_IMAGE = 51;

    public static void chooserSysPics(Context context) {
        if (context == null) {
            return;
        }
        Intent localIntent = getChooserSysPicsIntent();
        ((Activity) context).startActivityForResult(localIntent, REQUEST_CODE_PICK_IMAGE);
    }

    public static void chooserSysPics(Fragment fragment) {
        if (fragment.getActivity() == null) {
            return;
        }
        Intent localIntent = getChooserSysPicsIntent();
        fragment.startActivityForResult(localIntent, REQUEST_CODE_PICK_IMAGE);
    }

    public static Intent getChooserSysPicsIntent() {
        Intent localIntent = new Intent();
        localIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        localIntent.setType("image/*");
        localIntent.setAction("android.intent.action.GET_CONTENT");
        return localIntent;
    }

    // takePicture
    public static void takePicture(Context context) {
        takePicture(context, null, null);
    }

    public static void takePicture(Context context, String path, String filename) {
        if (context == null) {
            return;
        }
        Intent intent = getPhotoIntent(path, filename);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);

    }

    public static void takePicture(Fragment fragment) {
        takePicture(fragment, null, null);
    }

    public static void takePicture(Fragment fragment, String path, String filename) {
        if (fragment.getActivity() == null) {
            return;
        }
        Intent intent = getPhotoIntent(path, filename);
        fragment.startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);
    }

    public static Intent getPhotoIntent(String path, String filename) {
        if (StringUtils.isEmpty(path)) {
            path = PathUtils.getCarmerDir();
        }

        if (StringUtils.isEmpty(filename)) {
            filename = System.currentTimeMillis() + ".jpg";
        }
        File file = new File(path, filename);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

}
