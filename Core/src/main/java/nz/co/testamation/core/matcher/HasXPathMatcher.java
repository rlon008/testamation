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

package nz.co.testamation.core.matcher;

import nz.co.testamation.common.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.*;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

public class HasXPathMatcher extends TypeSafeMatcher<Node> {
    private static final Log logger = LogFactory.getLog( HasXPathMatcher.class );

    private Matcher<String>[] valueMatchers;

    private final XPathExpression compiledXPath;
    private final String xpathString;

    public HasXPathMatcher( String xPathExpression ) {
        this( xPathExpression, null );
    }

    public HasXPathMatcher( String xPathExpression, final Map<String, String> prefixNamespaceMap ) {
        this.xpathString = xPathExpression;
        compiledXPath = XmlUtil.compileXPath( xPathExpression, prefixNamespaceMap );
    }

    public boolean matchesSafely( Node item ) {
        String xPathResult;
        boolean matched;
        try {
            if ( hasMatchers() ) {
                xPathResult = (String) compiledXPath.evaluate( item, XPathConstants.STRING );
                matched = getMatcher().matches( xPathResult );
            } else {
                matched = (Boolean) compiledXPath.evaluate( item, XPathConstants.BOOLEAN );
                xPathResult = Boolean.toString( matched );
            }
        } catch ( XPathExpressionException e ) {
            xPathResult = "Failed to execute xpath: " + xpathString;
            matched = false;
        }

        if ( !matched ) {
            String matcherDescription = null;
            if ( getMatcher() != null ) {
                Description description = new StringDescription();
                getMatcher().describeTo( description );
                matcherDescription = description.toString();
            }
            logger.info( String.format(
                "XPath failed: %s%s",
                xpathString,
                matcherDescription != null ? ",  actual: '" + xPathResult + "', expected: " + matcherDescription : ""
            ) );
        }
        return matched;
    }

    private Matcher<String> getMatcher() {
        if ( !hasMatchers() ) {
            return null;
        }
        return Matchers.allOf( valueMatchers );
    }

    private boolean hasMatchers() {
        return valueMatchers != null && valueMatchers.length > 0;
    }

    public void describeTo( Description description ) {
        description.appendText( "an XML document with XPath " ).appendText( xpathString );
        if ( hasMatchers() ) {
            description.appendText( " " ).appendDescriptionOf( getMatcher() );
        }
    }

    public HasXPathMatcher that( Matcher<String>... matchers ) {
        this.valueMatchers = matchers;
        return this;
    }

    public static HasXPathMatcher hasXPathOf( String xPathExpression, Map<String, String> namespaces ) {
        return new HasXPathMatcher( xPathExpression, namespaces );
    }

    public static HasXPathMatcher hasXPathOf( String xPathExpression ) {
        return new HasXPathMatcher( xPathExpression );
    }

}
