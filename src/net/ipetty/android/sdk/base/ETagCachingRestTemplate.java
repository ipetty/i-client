/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ipetty.android.sdk.base;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * {@link RestTemplate} implementing caching based on HTTP Etag header.
 * 
 * @author Oliver Gierke
 */
public class ETagCachingRestTemplate extends RestTemplate {

    private static final String ETAG_HEADER = "ETag";
    private static final String IF_NONE_MATCH_HEADER = "If-None-Match";

    private Cache cache = new Cache();


    @Override
    protected <T> T doExecute(URI url, HttpMethod method,
            RequestCallback requestCallback,
            ResponseExtractor<T> responseExtractor) throws RestClientException {

        if (isPurgingRequest(method)) {

            synchronized (cache) {
                cache.remove(url);
            }

            return super.doExecute(url, method, requestCallback,
                    responseExtractor);
        }

        return super.doExecute(url, method, new DelegatingRequestCallback(url,
                requestCallback), new DelegatingResponseExtractor<T>(url,
                method, responseExtractor));
    }


    private boolean isCacheableRequest(HttpMethod method) {

        return GET.equals(method);
    }


    private boolean isPurgingRequest(HttpMethod method) {

        return Arrays.asList(POST, PUT, DELETE).contains(method);
    }

    /**
     * Applies an {@value ETagCachingRestTemplate#IF_NONE_MATCH_HEADER} header
     * to the request if there we already have an instance of the requested
     * resource in the cache.
     * 
     * @author Oliver Gierke
     */
    private class DelegatingRequestCallback implements RequestCallback {

        private URI uri;
        private RequestCallback callback;


        public DelegatingRequestCallback(URI uri, RequestCallback callback) {

            Assert.notNull(uri);

            this.uri = uri;
            this.callback = callback;
        }


        public void doWithRequest(ClientHttpRequest request) throws IOException {

            // Do we have a cached object? Apply header...
            synchronized (cache) {
                if (cache.hasEntry(uri)) {
                    request.getHeaders().add(IF_NONE_MATCH_HEADER,
                            cache.getEtag(uri));
                }
            }

            if (null != callback) {
                callback.doWithRequest(request);
            }
        }
    }

    private class DelegatingResponseExtractor<T> implements
            ResponseExtractor<T> {

        private URI uri;
        private HttpMethod method;
        private ResponseExtractor<T> extractor;


        public DelegatingResponseExtractor(URI uri, HttpMethod method,
                ResponseExtractor<T> extractor) {

            Assert.notNull(uri);

            this.uri = uri;
            this.method = method;
            this.extractor = extractor;
        }


        /**
         * Returns a cached instance if the response returns 304 and we already
         * have a cached instance available. Puts the extracted resource into
         * the cache on 200.
         */
        @SuppressWarnings("unchecked")
        public T extractData(ClientHttpResponse response) throws IOException {

            HttpHeaders headers = response.getHeaders();

            boolean isNotModified =
                    NOT_MODIFIED.equals(response.getStatusCode());

            synchronized (cache) {
                // Return cached instance on 304
                if (isNotModified && cache.hasEntry(uri)) {
                    return (T) cache.get(uri);
                }
            }

            T result = extractor.extractData(response);

            // Put into cache if ETag returned
            if (isCacheableRequest(method) && headers.containsKey(ETAG_HEADER)) {

                synchronized (cache) {
                    cache.put(new CacheEntry(headers.getFirst(ETAG_HEADER),
                            uri, result));
                }
            }

            return result;
        }
    }

    private static class CacheEntry {

        private String etag;
        private URI uri;
        private Object resource;


        /**
         * @param etag
         * @param uri
         * @param resource
         */
        public CacheEntry(String etag, URI uri, Object resource) {

            Assert.notNull(etag);
            Assert.notNull(uri);

            this.etag = etag;
            this.uri = uri;
            this.resource = resource;
        }


        public String getEtag() {

            return etag;
        }


        public URI getUri() {

            return uri;
        }


        public Object getResource() {

            return resource;
        }


        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {

            StringBuffer buffer = new StringBuffer();
            buffer.append("URI: ").append(uri);
            buffer.append(" - ETag: ").append(etag);
            buffer.append(" - Resource: ").append(resource);

            return buffer.toString();
        }


        @Override
        public boolean equals(Object obj) {

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof CacheEntry)) {
                return false;
            }

            CacheEntry that = (CacheEntry) obj;

            boolean uriAndEtag =
                    this.uri.equals(that.uri) && this.etag.equals(that.etag);

            return uriAndEtag
                    && (null == this.resource ? null == that.resource
                            : this.resource.equals(that.resource));
        }


        @Override
        public int hashCode() {

            int result = 31;

            result += 17 * etag.hashCode();
            result += 17 * uri.hashCode();

            result += null == resource ? 0 : resource.hashCode();

            return result;
        }
    }

    private static class Cache {

        private Map<URI, CacheEntry> entries =
                new WeakHashMap<URI, CacheEntry>();


        public void put(CacheEntry entry) {

            entries.put(entry.getUri(), entry);
        }


        public Object get(URI uri) {

            return entries.get(uri).getResource();
        }


        public String getEtag(URI uri) {

            return entries.get(uri).getEtag();
        }


        public boolean hasEntry(URI uri) {

            return entries.containsKey(uri);
        }


        public void remove(URI uri) {

            entries.remove(uri);
        }
    }
}
