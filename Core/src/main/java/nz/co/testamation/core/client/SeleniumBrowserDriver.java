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

package nz.co.testamation.core.client;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import nz.co.testamation.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

public class SeleniumBrowserDriver implements BrowserDriver {

    interface Work {
        void execute();
    }

    private static final Logger logger = LoggerFactory.getLogger( SeleniumBrowserDriver.class );


    private static final String BACKSPACE_CHARACTER = "\b";


    private final WebDriver driver;
    private final String endOfPageElementId;
    private final File tempDir;

    private final int ajaxWaitTimeInSeconds;
    private final int defaultWaitTimeInSeconds;
    private final int elementWaitTimeInSeconds;


    private static File createTempFile() {
        try {
            return Files.createTempDirectory( "integration-test-" ).toFile();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public SeleniumBrowserDriver( WebDriver driver ) {
        this( driver, null, 10, 10, 5 );
    }

    public SeleniumBrowserDriver( WebDriver driver, String endOfPageElementId, int ajaxWaitTimeInSeconds, int defaultWaitTimeInSeconds, int elementWaitTimeInSeconds ) {
        this( driver, endOfPageElementId, ajaxWaitTimeInSeconds, defaultWaitTimeInSeconds, elementWaitTimeInSeconds, createTempFile() );
    }

    public SeleniumBrowserDriver( WebDriver driver, String endOfPageElementId, int ajaxWaitTimeInSeconds, int defaultWaitTimeInSeconds, int elementWaitTimeInSeconds, File tempDir ) {
        this.driver = driver;
        this.endOfPageElementId = endOfPageElementId;
        this.ajaxWaitTimeInSeconds = ajaxWaitTimeInSeconds;
        this.defaultWaitTimeInSeconds = defaultWaitTimeInSeconds;
        this.elementWaitTimeInSeconds = elementWaitTimeInSeconds;
        this.tempDir = tempDir;
    }

    @Override
    public void reset() {
        clearAllCookies();
        for ( File file : tempDir.listFiles() ) {
            file.delete();
        }
    }


    @Override
    public void select( By by ) {
        System.out.println( "clicking " + by.toString() );
        waitBeforeClick( by );
    }

    @Override
    public void selectRadioByValue( String name, Object value ) {
        By by = By.cssSelector( String.format( "input[name='%s'][value='%s']", name, StringUtil.toNotNullString( value ) ) );
        System.out.println( "clicking " + by.toString() );
        waitBeforeClick( by );
    }

    private void waitBeforeClick( By by ) {
        waitForVisible( by );
        try {
            findElement( by ).click();
        } catch ( WebDriverException ex ) {
            if ( ex.getMessage().contains( "Element is not clickable" ) ) {
                System.out.println( "Element is not clickable, retrying..." );
                try {
                    Thread.sleep( 250 );
                    findElement( by ).click();
                } catch ( InterruptedException e ) {
                    throw new RuntimeException( e );
                }
            }

        }
    }

    @Override
    public void setCheckBoxState( String name, boolean desiredState ) {
        final WebElement element = findElement( By.name( name ) );
        if ( element.isSelected() != desiredState ) {
            element.click();
        }
    }

    @Override
    public void setCheckBoxState( String name, String value, boolean desiredState ) {
        final WebElement element = findElement( By.cssSelector( String.format( "input[name='%s'][value='%s']", name, value ) ) );
        if ( element.isSelected() != desiredState ) {
            element.click();
        }
    }

    @Override
    public void selectDropDownByValue( By by, Object value ) {
        for ( WebElement option : getSelectOptions( by ) ) {
            if ( StringUtil.toNotNullString( value ).equals( option.getAttribute( "value" ) ) ) {
                option.click();
                break;
            }
        }
    }

    @Override
    public void selectDropDownByIndex( String id, int index ) {
        final List<WebElement> webElements = getSelectOptions( By.id( id ) );
        if ( webElements.size() <= index ) {
            throw new IllegalStateException( String.format( "Attempt to select element %s of drop down with id '%s' but it only has %s elements", index, id, webElements.size() ) );
        }
        webElements.get( index ).click();
    }

    @Override
    public void deleteLastCharacter( By by ) {
        findElement( by ).sendKeys( BACKSPACE_CHARACTER );
    }

    @Override
    public void switchWindow() {
        final String currentWindow = getCurrentWindow();
        for ( String window : driver.getWindowHandles() ) {
            if ( !currentWindow.equals( window ) ) {
                getDriver().switchTo().window( window );
            }
        }
    }

    @Override
    public String getCurrentWindow() {
        if ( driver == null ) {
            return null;
        }
        return getDriver().getWindowHandle();
    }

    @Override
    public void resetWindow( String activeWindow ) {
        if ( activeWindow == null ) {
            return;
        }
        for ( String window : getDriver().getWindowHandles() ) {
            if ( !activeWindow.equals( window ) ) {
                getDriver().switchTo().window( window );
                getDriver().close();
            }
        }
        getDriver().switchTo().window( activeWindow );
    }

    @Override
    public void acceptAlert() {
        driver.switchTo().alert().accept();

    }

    @Override
    public void waitFor( Predicate<WebDriver> predicate ) {
        waitFor( predicate, defaultWaitTimeInSeconds );
    }

    @Override
    public List<WebElement> getSelectOptions( By by ) {
        return new Select( findElement( by ) ).getOptions();
    }

    public WebElement findElement( By by ) {
        List<WebElement> elements = findElements( by );
        logger.debug( "Find element " + by + " found " + elements.size() );
        if ( elements.isEmpty() ) {
            return null;
        }
        return Iterables.getFirst( elements, null );
    }


    public List<WebElement> findElements( By by ) {
        final List<WebElement> elements = getDriver().findElements( by );

        if ( elements.isEmpty() ) {
            try {
                final WebElement element = getDriver().findElement( by );
                elements.add( element );
            } catch ( NoSuchElementException ignore ) {
            }
        }
        return elements;
    }

    @Override
    public String getInnerHtml( WebElement element ) {
        return element.getAttribute( "innerHTML" );

    }

    public void goBack() {
        getDriver().navigate().back();
    }

    public void runJavascriptWait( final String s ) {
        getJavascriptExecutor().executeScript( s );
    }

    public void setCookie( Cookie cookie ) {
        getDriver().manage().deleteCookieNamed( cookie.getName() );
        getDriver().manage().addCookie( cookie );
    }

    public void open( final String url ) {
        executeAndWaitForPageLoad( () -> getDriver().navigate().to( url ) );
    }

    public void click( String id ) {
        click( By.id( id ) );
    }

    public void click( final By by ) {
        System.out.println( "clicking " + by.toString() );
        executeAndWaitForPageLoad( () -> waitBeforeClick( by ) );
    }

    private void executeAndWaitForPageLoad( Work work ) {
        work.execute();
        if ( StringUtils.isNotBlank( endOfPageElementId ) ) {
            new WebDriverWait( getDriver(), defaultWaitTimeInSeconds )
                .until( ( WebDriver input ) -> !input.findElements( By.id( endOfPageElementId ) ).isEmpty() );
        }
    }


    @Override
    public void uploadFile( final By fileInput, final File file ) {
        driver.findElement( fileInput ).sendKeys( file.getAbsolutePath() );
    }

    public String getPageHtml() {
        return getDriver().getPageSource();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }

    public boolean isElementPresent( final String id ) {
        return !getDriver().findElements( By.id( id ) ).isEmpty();
    }

    public void type( final By by, final Object inputText ) {
        System.out.println( String.format( "typing '%s' into '%s'", inputText, by ) );
        waitForVisible( by );
        WebElement element = findElement( by );
        element.clear();
        element.sendKeys( StringUtil.toNotNullString( inputText ) );
    }

    public String getUrl() {
        return getDriver().getCurrentUrl();
    }

    public void refresh() {
        getDriver().navigate().refresh();
    }

    @Override
    public void waitForContent( By by, Matcher<String> matcher ) {
        waitFor( driver -> matcher.matches( findElement( by ).getText() ) );
    }

    @Override
    public void switchToFrame( By by ) {
        getDriver().switchTo().frame( findElement( by ) );
    }

    @Override
    public void switchToDefaultFrame() {
        getDriver().switchTo().defaultContent();
    }

    public String evaluateJavascriptExpression( String javascript ) {
        return (String) getJavascriptExecutor().executeScript( javascript );
    }

    @Override
    public Boolean evaluateJavascriptBoolean( String javascript ) {
        return (Boolean) getJavascriptExecutor().executeScript( javascript );
    }

    public void takeScreenShotOfContent( String filename ) {
        if ( getDriver() instanceof TakesScreenshot ) {
            try {
                FileCopyUtils.copy( ( (TakesScreenshot) getDriver() ).getScreenshotAs( OutputType.BYTES ), new File( filename ) );
            } catch ( IOException e ) {
                throw new RuntimeException( e );
            }
        }
    }

    @Override
    public void focusWindow( String windowName ) {
        getDriver().switchTo().window( windowName );
    }

    @Override
    public void waitFor( Predicate<WebDriver> predicate, int timeoutSeconds ) {
        new WebDriverWait( getDriver(), timeoutSeconds ).until( predicate );
    }

    @Override
    public void waitForClickable( By by ) {
        new WebDriverWait( getDriver(), elementWaitTimeInSeconds ).until(
            ExpectedConditions.elementToBeClickable( by )
        );
    }

    @Override
    public void waitForVisible( By by ) {
        try {
            waitForVisible( by, elementWaitTimeInSeconds );
        } catch ( StaleElementReferenceException ex ) {
            //retry once more for StaleElement
            waitForVisible( by, elementWaitTimeInSeconds );
        }
    }

    @Override
    public void waitForVisible( By by, int timeoutInSeconds ) {
        new WebDriverWait( getDriver(), timeoutInSeconds ).until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy( by )
        );
    }

    private JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    private WebDriver getDriver() {
        return driver;
    }


    @Override
    public void waitForAnyAjaxToComplete() {
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        waitFor(
            input -> evaluateJavascriptBoolean( "return (typeof Ajax == 'undefined' || Ajax.activeRequestCount === 0) && (typeof jQuery == 'undefined' || jQuery.active === 0)" ),
            ajaxWaitTimeInSeconds
        );
    }

    @Override
    public void clearAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    @Override
    public Set<Cookie> getAllCookies() {
        return getDriver().manage().getCookies();
    }

    @Override
    public Cookie getCookie( String cookieName ) {
        return getDriver().manage().getCookieNamed( cookieName );
    }

    @Override
    public File getDownLoadDir() {
        return tempDir;
    }

}
