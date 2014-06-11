package net.ipetty.android.sdk.core;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * 异常处理
 *
 * @author xiaojinghai
 */
public class ApiExceptionHandler implements ResponseErrorHandler {

    private static final String TAG = "ApiExceptionHandler";

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return hasError(response.getStatusCode());

    }

    protected boolean hasError(HttpStatus statusCode) {
        return (statusCode.series() == HttpStatus.Series.CLIENT_ERROR || statusCode.series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Charset charset = Charset.forName("utf-8");
        byte[] body = getResponseBody(response);
        HttpStatus statusCode = response.getStatusCode();
        Log.i(TAG, "statusCode:" + statusCode.series());
        throw new APIException();
//        switch (statusCode.series()) {
//            case CLIENT_ERROR:
//            	Log.i(TAG, "throw HttpClientErrorException:");
//                throw new HttpClientErrorException(statusCode, response.getStatusText(), body,
//                        charset);
//            case SERVER_ERROR:
//            	Log.i(TAG, "throw HttpServerErrorException:");
//                throw new HttpServerErrorException(statusCode, response.getStatusText(), body,
//                        charset);
//            default:
//            	Log.i(TAG, "throw RestClientException:");
//                throw new RestClientException("Unknown status code [" + statusCode + "]");
//        }
    }

    private byte[] getResponseBody(ClientHttpResponse response) {
        try {
            InputStream responseBody = response.getBody();
            if (responseBody != null) {
                return FileCopyUtils.copyToByteArray(responseBody);
            }
        } catch (IOException ex) {
            // ignore
        }
        return new byte[0];
    }

}
