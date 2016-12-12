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
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface BrowserDriver {

    String getInnerHtml( WebElement element );

    void goBack();

    void runJavascriptWait( String s );

    public void setCookie( Cookie cookie );

    void open( String url );

    void click( String id );

    void click( By by );

    void type( By by, Object inputText );

    String evaluateJavascriptExpression( String javascript );

    Boolean evaluateJavascriptBoolean( String javascript );

    void takeScreenShotOfContent( String filename );

    void focusWindow( String windowName );


    void uploadFile( By by, File file );

    void reset();

    void select( By by );

    void selectRadioByValue( String name, Object value );

    void setCheckBoxState( String name, boolean desiredState );

    void setCheckBoxState( String name, String value, boolean desiredState );

    void selectDropDownByValue( By by, Object value );

    void selectDropDownByIndex( String id, int index );

    void deleteLastCharacter( By by );

    void switchWindow();

    String getCurrentWindow();

    void resetWindow( String activeWindow );

    void acceptAlert();

    void waitFor( Predicate<WebDriver> predicate );

    void waitFor( Predicate<WebDriver> predicate, int timeoutSeconds );

    void waitForClickable( By by );

    void waitForVisible( By by );

    void waitForVisible( By by, int timeoutInSeconds );

    void waitForAnyAjaxToComplete();

    void clearAllCookies();

    Set<Cookie> getAllCookies();

    Cookie getCookie( String cookie );

    File getDownLoadDir();

    List<WebElement> getSelectOptions( By by );

    WebElement findElement( By by );

    List<WebElement> findElements( By by );

    String getPageHtml();

    String getPageTitle();

    boolean isElementPresent( String id );

    String getUrl();

    void refresh();

    void waitForContent( By by, Matcher<String> matcher );

    void switchToFrame( By by );

    void switchToDefaultFrame();
}
