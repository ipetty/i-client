package net.ipetty.android.sdk.base;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;

public class CachedClientHttpResponse extends AbstractClientHttpResponse{

	@Override
	public int getRawStatusCode() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStatusText() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void closeInternal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected InputStream getBodyInternal() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
