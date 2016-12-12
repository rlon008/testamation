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

package nz.co.testamation.core.mock.requestmatcher;

import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matcher;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SoapRequestMatcher<T extends SoapRequestMatcher> extends XmlRequestMatcher<T> {

    private static final ImmutableMap<String, String> SOAP_NS = ImmutableMap.of(
        "soap", "http://schemas.xmlsoap.org/soap/envelope/"
    );
    private static final String SOAP_BODY_XPATH = "/soap:Envelope/soap:Body";

    public SoapRequestMatcher( String soapAction ) {
        withMethodThat( equalTo( HttpMethod.POST ) );
        withHeader( "SOAPAction", containsString( soapAction ) );
    }

    public SoapRequestMatcher(){
        withMethodThat( equalTo( HttpMethod.POST ) );
    }

    public T withPayLoadThat( String xPath, Map<String, String> namespaces, Matcher<String> valueMatcher ) {
        ImmutableMap<String, String> mergedNamespaces = ImmutableMap.<String, String>builder().putAll( SOAP_NS ).putAll( namespaces ).build();
        return withXmlThat( SOAP_BODY_XPATH + xPath, mergedNamespaces, valueMatcher );
    }

    public T withPayLoadThat( String xPath, Matcher<String> valueMatcher ) {
        return withPayLoadThat( xPath, ImmutableMap.of(), valueMatcher );
    }


}
