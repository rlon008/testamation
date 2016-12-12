/*
 * Copyright 2016 Ratha Long
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.testamation.core.reader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import nz.co.testamation.core.reader.pdf.HttpClientProvider;
import nz.co.testamation.core.reader.pdf.HttpContextProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonClientImpl implements JsonClient {
    private HttpClientProvider httpClientProvider;
    private HttpContextProvider httpContextProvider;
    private ObjectMapper objectMapper;
    private static final Log logger = LogFactory.getLog( JsonClientImpl.class );

    private interface Parser<T> {
        T parse( HttpResponse response ) throws IOException;
    }

    public JsonClientImpl( HttpClientProvider httpClientProvider, HttpContextProvider httpContextProvider, ObjectMapper objectMapper ) {
        this.httpClientProvider = httpClientProvider;
        this.httpContextProvider = httpContextProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> doGet( String url ) {
        return doGet( url, response -> objectMapper.readValue( response.getEntity().getContent(), new TypeReference<Map<String, Object>>() { } ) );
    }


    @Override
    public <T> T doGet( String url, Class<T> clazz ) {
        return doGet( url, response -> objectMapper.readValue( response.getEntity().getContent(), clazz ) );
    }


    @Override
    public Map<String, Object> doPost( String url, Object jsonBody ) {
        return doPost( url, jsonBody, ImmutableMap.of() );
    }

    @Override
    public Map<String, Object> doPost( String url, Object jsonBody, Map<String, String> headers ) {
        return doPostInternal( url, jsonBody, headers, response ->
            objectMapper.readValue( response.getEntity().getContent(), new TypeReference<Map<String, Object>>() { } )
        );
    }

    @Override
    public <T> T doPost( String url, Object jsonBody, Map<String, String> headers, Class<T> clazz ) {
        return doPostInternal( url, jsonBody, headers, response -> objectMapper.readValue( response.getEntity().getContent(), clazz ) );
    }

    private <T> T doPostInternal( String url, Object jsonBody, Map<String, String> headers, Parser<T> parser ) {
        HttpPost request = new HttpPost( url );
        request.addHeader( "content-type", "application/json" );
        headers.entrySet().stream().forEach( e -> request.addHeader( e.getKey(), e.getValue() ) );
        request.setEntity( getEntity( jsonBody ) );
        return execute( request, parser );

    }

    private StringEntity getEntity( Object jsonBody ) {
        try {
            return new StringEntity( objectMapper.writeValueAsString( jsonBody ) );
        } catch ( UnsupportedEncodingException | JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
    }

    private <T> T doGet( String url, Parser<T> parser ) {
        HttpGet httpGet = new HttpGet( url );
        return execute( httpGet, parser );
    }

    private <T> T execute( HttpUriRequest request, Parser<T> parser ) {
        CloseableHttpResponse response = null;
        HttpContext localContext = httpContextProvider.get();
        try {
            CloseableHttpClient httpClient = httpClientProvider.get();
            response = httpClient.execute( request, localContext );
            return parser.parse( response );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        } finally {
            try {
                if ( response != null ) {
                    response.close();
                }
            } catch ( IOException e ) {
                logger.info( "Unable to close url", e );
            }
        }
    }

}
