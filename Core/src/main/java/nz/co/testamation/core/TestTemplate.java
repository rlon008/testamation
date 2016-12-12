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

package nz.co.testamation.core;

import com.google.common.base.Predicate;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.client.BrowserDriver;
import nz.co.testamation.core.client.page.BaseUrlProvider;
import nz.co.testamation.core.config.Config;
import nz.co.testamation.core.config.ConfigRunner;
import nz.co.testamation.core.mock.OnGoingExpectationBuilder;
import nz.co.testamation.core.mock.OnGoingExpectationStart;
import nz.co.testamation.core.reader.JsonClient;
import nz.co.testamation.core.reader.pdf.PdfContentReader;
import nz.co.testamation.core.step.Step;
import nz.co.testamation.core.step.StepRunner;
import nz.co.testamation.core.waiting.Task;
import nz.co.testamation.core.waiting.WaitFor;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class TestTemplate {


    @Value("${web.driver.element.notExist.waitTime.seconds:3}")
    private int elementNotExistWaitTime;

    @Autowired
    protected BrowserDriver browserDriver;

    @Autowired
    protected BaseUrlProvider baseUrlProvider;


    @Autowired
    protected ConfigRunner configRunner;

    @Autowired
    protected StepRunner stepRunner;

    @Autowired
    protected PdfContentReader pdfContentReader;

    @Autowired
    protected JsonClient jsonClient;


    public void given() throws Exception {
    }

    public void externalBehaviours() throws Exception {
    }

    public abstract void when() throws Exception;

    public void then() throws Exception {
    }

    public void tearDown() throws Exception {
    }

    public String pageTitle() {
        return browserDriver.getPageTitle();
    }

    public String currentUrl() {
        return browserDriver.getUrl();
    }


    public Map<String, Object> getJson( String relativeUrl ) {
        return jsonClient.doGet( getUrl( relativeUrl ) );
    }

    public <T> T getJson( String relativeUrl, Class<T> responseClazz ) {
        return jsonClient.doGet( getUrl( relativeUrl ), responseClazz );
    }

    public <T> T doPostJson( String relativeUrl, Object request, Map<String, String> requestHeaders, Class<T> responseClazz ) {
        return jsonClient.doPost( getUrl( relativeUrl ), request, requestHeaders, responseClazz );
    }

    public Map<String, Object> doPostJson( String relativeUrl, Object request, Map<String, String> requestHeaders ) {
        return jsonClient.doPost( getUrl( relativeUrl ), request, requestHeaders );
    }

    public Map<String, Object> doPostJson( String relativeUrl, Object request ) {
        return jsonClient.doPost( getUrl( relativeUrl ), request );
    }

    public String pdfContent( By by ) {
        WebElement webElement = pageElement( by );
        String pdfUrl = "";

        if ( webElement.getTagName().equals( "a" ) ) {
            pdfUrl = webElement.getAttribute( "href" );
        } else if ( webElement.getTagName().equals( "iframe" ) ) {
            pdfUrl = webElement.getAttribute( "src" );
        }
        try {
            return pdfContentReader.readText( pdfUrl );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public String pageElementValue( String id ) {
        return pageElementValue( By.id( id ) );
    }

    public String pageElementValue( By by ) {
        return pageElement( by ).getAttribute( "value" );
    }

    public String pageElementText( String id ) {
        return pageElementText( By.id( id ) );
    }

    public String pageElementText( By by ) {
        return pageElement( by ).getText();
    }

    public WebElement pageElement( String id ) {
        return pageElement( By.id( id ) );
    }

    public List<WebElement> getSelectOptions( String selectId ) {
        return getSelectOptions( By.id( selectId ) );
    }

    public List<WebElement> getSelectOptions( By by ) {
        return browserDriver.getSelectOptions( by );
    }

    public boolean pageElementDoesNotExist( By by ) {
        //Wait a shorter amount of time as we dont expect element to appear.
        return pageElement( by, elementNotExistWaitTime ) == null;
    }

    public boolean pageElementDoesNotExist( String id ) {
        return pageElementDoesNotExist( By.id( id ) );
    }

    public WebElement pageElement( By by, int timeoutInSeconds ) {
        try {
            browserDriver.waitForVisible( by, timeoutInSeconds );
        } catch ( org.openqa.selenium.TimeoutException ignored ) {
        }
        return browserDriver.findElement( by );
    }

    public WebElement pageElement( By by ) {
        try {
            browserDriver.waitForVisible( by );
        } catch ( org.openqa.selenium.TimeoutException ignored ) {
        }
        return browserDriver.findElement( by );
    }


    public void assertPageContent( String id, Matcher<String> matcher ) {
        assertPageContent( By.id( id ), matcher );
    }

    public void assertPageContent( By by, Matcher<String> matcher ) {
        try {
            assertThat( pageElementText( by ), matcher );
        } catch ( AssertionError error ) {
            System.out.println( "Failed to match page element content, retrying once only..." );
            try {
                Thread.sleep( 250 );
            } catch ( InterruptedException e ) {
                throw new RuntimeException( e );
            }
            assertThat( pageElementText( by ), matcher );
        }
    }

    public <T> void assertThat( T actual, Matcher<T> matcher ) {
        MatcherAssert.assertThat( actual, matcher );
    }

    public <T> void waitForVisible( By by ) {
        browserDriver.waitForVisible( by );
    }

    public Matcher<Object> notNullValue() {
        return Matchers.notNullValue();
    }

    public Matcher<Object> nullValue() {
        return Matchers.nullValue();
    }

    public void assertField( Object object, String fieldName, Matcher matcher ) {
        MatcherAssert.assertThat( ReflectionUtil.getFieldValue( object, fieldName ), matcher );
    }

    private String getUrl( String url ) {
        return url.startsWith( "http" ) ? url : baseUrlProvider.getBaseUrl() + url;
    }

    public void goToUrl( String url ) {
        browserDriver.open( getUrl( url ) );
    }

    public <T> T given( Step<T> step ) throws Exception {
        return stepRunner.run( step );
    }


    public <T> OnGoingExpectationStart<T> when( OnGoingExpectationBuilder<T> onGoingExpectationBuilder ) {
        return onGoingExpectationBuilder.createExpectation();
    }

    public <T> T given( Config<T> config ) throws Exception {
        return configRunner.apply( config );
    }


    public <T> Matcher<T> equalTo( T operand ) {
        return Matchers.equalTo( operand );
    }

    public <T> T waitFor( Task<T> task, Predicate<T> predicate ) {
        return new WaitFor<>( predicate )
            .when( task )
            .run();
    }
}
