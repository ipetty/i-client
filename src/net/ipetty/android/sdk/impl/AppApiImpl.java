package net.ipetty.android.sdk.impl;

import java.net.URI;

import net.ipetty.android.sdk.AppApi;
import net.ipetty.android.sdk.base.ApiBase;
import net.ipetty.android.sdk.base.ApiContext;
import net.ipetty.android.sdk.domain.IpetAppUpdate;

import org.springframework.util.LinkedMultiValueMap;

public class AppApiImpl extends ApiBase implements AppApi {

    public AppApiImpl(ApiContext context) {
        super(context);
    }

    @Override
    public IpetAppUpdate checkAppVersion(String appKey) {
        URI uri = buildUri("app/checkUpdate");
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        body.add("appKey", "ipet");
        IpetAppUpdate ipetAppUpdate = context.getRestTemplate().postForObject(uri, body, IpetAppUpdate.class);
        return ipetAppUpdate;
    }

}
