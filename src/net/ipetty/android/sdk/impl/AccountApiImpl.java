package net.ipetty.android.sdk.impl;

import java.net.URI;

import net.ipetty.android.sdk.AccountApi;
import net.ipetty.android.sdk.base.ApiBase;
import net.ipetty.android.sdk.base.ApiContext;
import net.ipetty.android.sdk.domain.IpetUser;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author xiaojinghai
 */
public class AccountApiImpl extends ApiBase implements AccountApi {

    public AccountApiImpl(ApiContext context) {
        super(context);
    }

    @Override
    public IpetUser login(String loginName, String password) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("username", loginName);
        request.add("password", password);
        IpetUser user = context.getRestTemplate().postForObject(buildUri("account/login"), request, IpetUser.class);
        context.setIsAuthorized(true);
        context.setCurrUserId(user.getId());
        return user;
    }

    @Override
    public void logout() {
        context.setIsAuthorized(Boolean.FALSE);
        context.setCurrUserId("");
    }

    @Override
    public IpetUser register(String loginName, String password) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.add("username", loginName);
        request.add("password", password);
        IpetUser user = context.getRestTemplate().postForObject(buildUri("account/create"), request, IpetUser.class);
        return user;
    }

    @Override
    public Boolean checkLoginName(String userName) {
        URI uri = buildUri("account/availableUsername", "username", userName);
        Boolean ret = context.getRestTemplate().getForObject(uri, Boolean.class);
        return ret;
    }

    @Override
    public Boolean checkPhone(String phone) {
        URI uri = buildUri("account/availablePhone", "phone", phone);
        Boolean ret = context.getRestTemplate().getForObject(uri, Boolean.class);
        return ret;
    }

    @Override
    public Boolean checkEmail(String email) {
        URI uri = buildUri("account/availableEmail", "email", email);
        Boolean ret = context.getRestTemplate().getForObject(uri, Boolean.class);
        return ret;
    }

    @Override
    public Boolean changePassword(String oldP, String newP) {
        requireAuthorization();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("userId", context.getCurrUserId());
        body.add("oldPassword", oldP);
        body.add("newPassword", newP);
        Boolean ret = context.getRestTemplate().postForObject(buildUri("account/changePassword"), body, Boolean.class);
        return ret;
    }

}
