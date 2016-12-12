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

package nz.co.testamation.core.reader.pdf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;

public class PdfContentReaderImpl implements PdfContentReader {
    private static final Log logger = LogFactory.getLog( PdfContentReaderImpl.class );
    private HttpClientProvider httpClientProvider;
    private HttpContextProvider httpContextProvider;

    public PdfContentReaderImpl( HttpClientProvider httpClientProvider, HttpContextProvider httpContextProvider ) {
        this.httpClientProvider = httpClientProvider;
        this.httpContextProvider = httpContextProvider;
    }


    @Override
    public String readText( String pdfUrl ) throws IOException {
        CloseableHttpResponse response = null;
        HttpContext localContext = httpContextProvider.get();
        try {
            CloseableHttpClient httpClient = httpClientProvider.get();
            HttpGet httpGet = new HttpGet( pdfUrl );
            response = httpClient.execute( httpGet, localContext );
            return getPdfText( response );
        } finally {
            try {
                if ( response != null ) {
                    response.close();
                }
            } catch ( IOException e ) {
                logger.info( "Unable to close Pdf Url", e );
            }
        }
    }

    private String getPdfText( CloseableHttpResponse response ) throws IOException {
        PDDocument load = PDDocument.load( response.getEntity().getContent() );
        try {
            return new PDFTextStripper().getText( load ).replaceAll( "\\s+", " " );
        } finally {
            load.close();
        }
    }

}
