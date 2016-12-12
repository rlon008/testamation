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

package nz.co.testamation.common.util;

import com.google.common.collect.ImmutableMap;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class XmlUtil {

    public static Document toDocument( String str ) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Document document = new DocumentBuilderFactoryImpl().newDocumentBuilder().newDocument();
            transformerFactory.newTransformer().transform( new StreamSource( new StringReader( str ) ), new DOMResult( document ) );
            return document;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }


    public static NamespaceContext toNameSpaceContext( Map<String, String> namespace ) {
        return new PrefixMapNamespaceContext( namespace );
    }


    public static XPathExpression compileXPath( String xPathExpression, NamespaceContext namespaceContext ) {
        try {
            final XPath xPath = XPathFactory.newInstance().newXPath();
            if ( namespaceContext != null ) {
                xPath.setNamespaceContext( namespaceContext );
            }
            return xPath.compile( xPathExpression );
        } catch ( XPathExpressionException e ) {
            throw new IllegalArgumentException( "Invalid XPath : " + xPathExpression, e );
        }
    }

    public static XPathExpression compileXPath( String xPathExpression, Map<String, String> namespaceContext ) {
        return compileXPath( xPathExpression, toNameSpaceContext( namespaceContext ) );
    }

    public static String toString( Node node ) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform( new DOMSource( node ), new StreamResult( writer ) );
            return writer.toString();
        } catch ( Exception ex ) {
            throw new RuntimeException( ex );
        }
    }

    public static String toString( DOMResult domResult ) {
        return toString( ( (Document) domResult.getNode() ).getDocumentElement() );
    }

    public static String selectString( Node node, String xPathExpression, ImmutableMap<String, String> namespaces ) {
        try {
            return compileXPath( xPathExpression, namespaces ).evaluate( node );
        } catch ( XPathExpressionException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String selectString( Node node, String xPathExpression ) {
        return selectString( node, xPathExpression, ImmutableMap.<String, String>of() );
    }
}
