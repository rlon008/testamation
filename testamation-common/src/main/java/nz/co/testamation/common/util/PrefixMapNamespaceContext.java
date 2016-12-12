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

import org.apache.commons.lang3.StringUtils;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class PrefixMapNamespaceContext implements NamespaceContext {

    final Map<String, String> prefixNamespaceMap;

    public PrefixMapNamespaceContext( Map<String, String> prefixNamespaceMap ) {
        this.prefixNamespaceMap = prefixNamespaceMap;
    }

    public String getNamespaceURI( String prefix ) {
        switch ( prefix ) {
            case XMLConstants.XML_NS_PREFIX:
                return XMLConstants.XML_NS_URI;
            case XMLConstants.XMLNS_ATTRIBUTE:
                return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
            default:
                return StringUtils.defaultString( prefixNamespaceMap.get( prefix ), XMLConstants.NULL_NS_URI );
        }
    }

    public String getPrefix( String namespaceURI ) {
        Iterator<String> prefixes = getPrefixes( namespaceURI );
        if ( prefixes.hasNext() ) {
            return prefixes.next();
        }
        return null;
    }

    public Iterator<String> getPrefixes( String namespaceURI ) {
        return prefixNamespaceMap
            .entrySet()
            .stream()
            .filter( entry -> namespaceURI.equals( entry.getValue() ) )
            .map( Map.Entry::getKey )
            .collect( Collectors.toList() ).iterator();
    }

}
