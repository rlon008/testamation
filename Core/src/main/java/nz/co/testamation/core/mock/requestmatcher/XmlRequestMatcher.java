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
import com.google.common.collect.Lists;
import nz.co.testamation.common.util.StringConcat;
import nz.co.testamation.common.util.XmlUtil;
import nz.co.testamation.core.mock.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

import static nz.co.testamation.core.matcher.HasXPathMatcher.hasXPathOf;

public class XmlRequestMatcher<T extends XmlRequestMatcher> extends HttpRequestMatcher<T> {

    private final List<Matcher<Node>> xmlMatchers = Lists.newArrayList();
    protected Document requestXml;

    @Override
    public boolean matches( HttpServletRequestWrapper request ) throws Exception {
        if ( !super.matches( request ) ) {
            return false;
        }
        requestXml = getDocument( request );

        for ( Matcher<Node> xmlMatcher : xmlMatchers ) {
            if ( !xmlMatcher.matches( requestXml ) ) {
                return false;
            }
        }

        return true;
    }

    protected Document getDocument( HttpServletRequestWrapper request ) {
        if ( StringUtils.isNotBlank( request.getBody() ) ) {
            return XmlUtil.toDocument( request.getBody() );
        }
        return null;
    }


    public T withXmlThat( Matcher<Node>... xmlMatchers ) {
        this.xmlMatchers.addAll( Lists.newArrayList( xmlMatchers ) );
        return (T) this;
    }


    public T withXmlThat( String xPath, Map<String, String> namespaces, Matcher<String> valueMatcher ) {
        return withXmlThat( hasXPathOf( xPath, namespaces ).that( valueMatcher ) );
    }

    public T withXmlThat( String xPath, Matcher<String> valueMatcher ) {
        return withXmlThat( xPath, ImmutableMap.of(), valueMatcher );
    }


    @Override
    public String toString() {
        StringConcat concat = new StringConcat( " " );
        concat.append( super.toString() );
        if ( !xmlMatchers.isEmpty() ) {
            for ( Matcher<Node> xmlMatcher : xmlMatchers ) {
                concat.append( "\n and " + xmlMatcher.toString() );
            }
        }

        return concat.toString();
    }


}
