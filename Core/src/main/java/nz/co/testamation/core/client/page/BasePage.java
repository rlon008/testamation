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

package nz.co.testamation.core.client.page;

import nz.co.testamation.common.util.StringUtil;
import nz.co.testamation.core.client.BrowserDriver;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class BasePage<T extends BasePage> {

    @Autowired
    protected BrowserDriver browserDriver;

    @Autowired
    protected BaseUrlProvider baseUrlProvider;


    private final String pageUriTemplate;
    private String[] pageUriTemplateParams;

    public BasePage( String pageUriTemplate, String... pageUriTemplateParams ) {
        this.pageUriTemplate = pageUriTemplate;
        this.pageUriTemplateParams = pageUriTemplateParams;
    }

    protected BasePage() {
        this( null );
    }


    public T visit() {
        if ( pageUriTemplate == null ) {
            throw new RuntimeException( "Page cannot be visited as no endpoint has been provided" );
        }
        browserDriver.open( baseUrlProvider.getBaseUrl() + String.format( pageUriTemplate, pageUriTemplateParams ) );
        return (T) this;
    }

    public T type( By by, Object value ) {
        browserDriver.type( by, StringUtil.toNotNullString( value ) );
        return (T) this;
    }

    public T type( String id, Object value ) {
        return type( By.id( id ), value );
    }

    public T click( By by ) {
        browserDriver.click( by );
        return (T) this;
    }

    public T click( String id ) {
        return click( By.id( id ) );
    }

    public T selectDropDownByValue( String id, Object value ) {
        browserDriver.selectDropDownByValue( By.id( id ), value );
        return (T) this;
    }

    public T waitForContent( By by, Matcher<String> matcher ) {
        browserDriver.waitForContent( by, matcher );
        return (T) this;
    }


    public T uploadFile( By by, File file ) {
        browserDriver.uploadFile( by, file );
        return (T) this;
    }


}
