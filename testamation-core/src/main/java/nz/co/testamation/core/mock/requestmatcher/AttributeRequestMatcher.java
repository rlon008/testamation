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

import com.google.common.collect.Lists;
import nz.co.testamation.core.mock.HttpServletRequestWrapper;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static org.hamcrest.Matchers.hasItemInArray;

public abstract class AttributeRequestMatcher<T extends AttributeRequestMatcher> extends HttpRequestMatcher<T> {

    private static Logger logger = LoggerFactory.getLogger( AttributeRequestMatcher.class );
    private final Map<String, Matcher> attributeMatchers = new HashMap<>();
    private final List<String> excludeAttributes = Lists.newArrayList();
    private Map<String, Object> actualAttributes;
    private Matcher<Integer> attributeSizeMatcher;


    @Override
    public boolean matches( HttpServletRequestWrapper request ) throws Exception {
        if ( !super.matches( request ) ) {
            return false;
        }

        if ( !matchRequestAttributes( request ) ) {
            return false;
        }

        return true;
    }

    private boolean matchRequestAttributes( HttpServletRequestWrapper request ) {

        actualAttributes = parseRequest( request );
        for ( String excludeAttribute : excludeAttributes ) {
            if ( actualAttributes.containsKey( excludeAttribute ) ) {
                logger.error( String.format( "Expected request attributes to NOT contain key %s. Actual was: %s", excludeAttribute, actualAttributes ) );
                return false;
            }
        }

        for ( Map.Entry<String, Matcher> attribute : attributeMatchers.entrySet() ) {

            if ( !actualAttributes.containsKey( attribute.getKey() ) ) {
                logger.error( String.format( "Expected request attributes to contain key %s. Actual was: %s", attribute.getKey(), actualAttributes ) );
                return false;
            }

            Object requestValue = actualAttributes.get( attribute.getKey() );

            if ( attribute.getValue() != null ) {
                if ( !matches( attribute.getKey(), attribute.getValue(), requestValue ) ) {
                    return false;
                }
            }
        }
        if ( attributeSizeMatcher != null ) {
            if ( !attributeSizeMatcher.matches( actualAttributes.size() ) ) {
                logger.error( String.format( "Expected number of attributes to be %s, but was %s ", attributeSizeMatcher, actualAttributes.size() ) );
                return false;
            }
        }

        return true;
    }

    private boolean matches( String key, Matcher matcher, Object requestValue ) {

        if ( requestValue.getClass().isArray() ) {
            if ( hasItemInArray( matcher ).matches( requestValue ) ) {
                return true;
            }
        }

        if ( matcher.matches( requestValue ) ) {
            return true;
        }
        logger.error( String.format( "Expected attribute key %s to be %s. Actual was: %s", key, matcher, requestValue ) );
        return false;
    }

    public T withAttribute( String name, Matcher<?> matcher ) {
        attributeMatchers.put( name, matcher );
        return (T) this;
    }

    public T withoutAttribute( String name ) {
        excludeAttributes.add( name );
        return (T) this;
    }

    @Override
    public String toString() {
        return format( "%s\n Attributes: %s, Attributes size: %s", super.toString(), attributeMatchers, attributeSizeMatcher );
    }

    public T withNumberOfAttributes( Matcher<Integer> attributeSizeMatcher ) {
        this.attributeSizeMatcher = attributeSizeMatcher;
        return (T) this;
    }

    protected abstract Map<String, Object> parseRequest( HttpServletRequestWrapper request );

    protected Map<String, Object> getActualAttributes() {
        return actualAttributes;
    }
}
