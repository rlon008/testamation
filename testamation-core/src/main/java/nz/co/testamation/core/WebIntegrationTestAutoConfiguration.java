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

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.testamation.core.client.BrowserDriver;
import nz.co.testamation.core.client.SeleniumBrowserDriver;
import nz.co.testamation.core.client.page.BaseUrlProvider;
import nz.co.testamation.core.client.page.BaseUrlProviderImpl;
import nz.co.testamation.core.exception.ExceptionRecorder;
import nz.co.testamation.core.exception.ExceptionRecorderImpl;
import nz.co.testamation.core.reader.JsonClient;
import nz.co.testamation.core.reader.JsonClientImpl;
import nz.co.testamation.core.reader.pdf.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.lang.String.format;

@ConditionalOnMissingBean( IntegrationTestRunner.class )
@ConditionalOnWebApplication
@AutoConfigureBefore( WebMvcAutoConfiguration.class )
@Configuration
public class WebIntegrationTestAutoConfiguration {

    @Bean( name = "webDriverDownloadDir" )
    public File downloadDir() {
        try {
            return Files.createTempDirectory( "integration-temp" ).toFile();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    @Bean
    @Autowired
    public WebDriver webDriver(
        @Value( "${web.driver:firefox}" ) String webDriver,
        @Value( "${web.driver.autoDownload.mimeTypes:application/pdf}" ) String autoDownloadMimeTypes,
        @Qualifier( "webDriverDownloadDir" ) File downloadDir
    ) {

        if ( "htmlunit".equals( webDriver ) ) {
            return new HtmlUnitDriver();
        }

        if ( "chrome".equals( webDriver ) ) {
            return new ChromeDriver();
        }

        if ( "firefox".equals( webDriver ) ) {
            FirefoxBinary firefoxBinary = new FirefoxBinary();
            if ( System.getenv( "DISPLAY" ) == null ) {
                firefoxBinary.setEnvironmentProperty( "DISPLAY", ":99" );
            }

            FirefoxProfile profile = new FirefoxProfile();

            profile.setPreference( "browser.download.folderList", 2 ); // Download to: 0 desktop, 1 default download location, 2 custom folder
            profile.setPreference( "browser.download.dir", downloadDir.getAbsolutePath() );
            profile.setPreference( "browser.download.manager.showWhenStarting", false );
            profile.setPreference( "browser.helperApps.alwaysAsk.force", false );
            profile.setPreference( "browser.helperApps.neverAsk.saveToDisk", autoDownloadMimeTypes );
            profile.setPreference( "plugin.disable_full_page_plugin_for_types", "application/pdf" );
            profile.setPreference( "pdfjs.disabled", true );

            return new FirefoxDriver( firefoxBinary, profile );
        }

        if ( "ie".equals( webDriver ) ) {
            return new InternetExplorerDriver();
        }

        if ( "safari".equals( webDriver ) ) {
            return new SafariDriver();
        }

        if ( "opera".equals( webDriver ) ) {
            return new OperaDriver();
        }

        throw new IllegalArgumentException( format( "Web driver %s not supported.", webDriver ) );
    }

    @Bean
    public BaseUrlProvider baseUrlProvider(
        @Value( "${server.host:localhost}" ) String host,
        @Value( "${server.port:8080}" ) int port,
        @Value( "${server.contextPath:/}" ) String contextPath,
        @Value( "${server.base.url:NONE}" ) String baseUrl
    ) {
        if ( "NONE".equals( baseUrl ) ) {
            return new BaseUrlProviderImpl( host, port, contextPath );
        }
        return new BaseUrlProviderImpl( baseUrl );
    }

    @Bean
    public ExceptionRecorder exceptionRecorder() {
        return new ExceptionRecorderImpl();
    }


    @Bean
    @Autowired
    public BrowserDriver browserDriver(
        WebDriver webDriver,
        @Value( "${web.driver.endOfPageElementId:}" ) String endOfPageElementId,
        @Value( "${web.driver.default.waitTime.seconds:10}" ) int defaultWaitTime,
        @Value( "${web.driver.ajax.waitTime.seconds:10}" ) int ajaxWaitTime,
        @Value( "${web.driver.element.waitTime.seconds:5}" ) int elementWaitTime,
        @Qualifier( "webDriverDownloadDir" ) File downloadDir
    ) {
        return new SeleniumBrowserDriver( webDriver, endOfPageElementId, defaultWaitTime, ajaxWaitTime, elementWaitTime, downloadDir );
    }

    @Bean
    public HttpClientProvider httpClientProvider() {
        return new DefaultHttpClientProvider();
    }

    @Bean
    @Autowired
    public HttpContextProvider browserCookieHttpContextProvider( BrowserDriver browserDriver ) {
        return new BrowserCookieHttpContextProvider( browserDriver );
    }

    @Bean
    @Autowired
    public PdfContentReader pdfContentReader( HttpClientProvider httpClientProvider, HttpContextProvider httpContextProvider ) {
        return new PdfContentReaderImpl( httpClientProvider, httpContextProvider );
    }

    @Bean
    @Autowired
    public JsonClient jsonClient( HttpClientProvider httpClientProvider, HttpContextProvider httpContextProvider ) {
        return new JsonClientImpl( httpClientProvider, httpContextProvider, new ObjectMapper() );
    }

    @Bean
    @Autowired
    public IntegrationTestRunner integrationTestRunner( ApplicationContext applicationContext ) {
        return new SpringIntegrationTestRunner( applicationContext );
    }

}
