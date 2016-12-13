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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import nz.co.testamation.common.util.ReflectionUtil;
import nz.co.testamation.core.config.Config;
import nz.co.testamation.core.lifecycle.LifeCyclePhase;
import nz.co.testamation.core.lifecycle.LifeCyclePhaseFactory;
import nz.co.testamation.core.lifecycle.annotation.TestLifeCycle;
import nz.co.testamation.core.step.Step;
import nz.co.testamation.testcommon.fixture.SomeFixture;
import nz.co.testamation.testcommon.template.MockitoTestTemplate;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Stack;

public class SpringIntegrationTestRunnerImplTest {

    abstract class Template extends MockitoTestTemplate {

        ApplicationContext applicationContext = mock( ApplicationContext.class );
        LifeCyclePhaseFactory lifeCyclePhaseFactory = mock( LifeCyclePhaseFactory.class );
        AutowireCapableBeanFactory autowireCapableBeanFactory = mock( AutowireCapableBeanFactory.class );


        SpringIntegrationTestRunner integrationTestRunner = new SpringIntegrationTestRunner( applicationContext, lifeCyclePhaseFactory );

        TestTemplate test = mock( TestTemplate.class );
        Object lifeCycle1 = mock( Object.class );
        Object lifeCycle2 = mock( Object.class );
        LifeCyclePhase afterWhen1 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterWhen2 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterWhen3 = mock( LifeCyclePhase.class );

        LifeCyclePhase afterGiven1 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterGiven2 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterGiven3 = mock( LifeCyclePhase.class );

        LifeCyclePhase afterExternalBehaviour1 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterExternalBehaviour2 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterExternalBehaviour3 = mock( LifeCyclePhase.class );

        LifeCyclePhase reset1 = mock( LifeCyclePhase.class );
        LifeCyclePhase reset2 = mock( LifeCyclePhase.class );
        LifeCyclePhase reset3 = mock( LifeCyclePhase.class );

        LifeCyclePhase testTearDown = mock( LifeCyclePhase.class );
        LifeCyclePhase tearDown1 = mock( LifeCyclePhase.class );
        LifeCyclePhase tearDown2 = mock( LifeCyclePhase.class );
        LifeCyclePhase tearDown3 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterStep1 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterStep2 = mock( LifeCyclePhase.class );
        LifeCyclePhase afterStep3 = mock( LifeCyclePhase.class );
    }

    @Test
    public void happyDay() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                given( applicationContext.getAutowireCapableBeanFactory() ).thenReturn( autowireCapableBeanFactory );

                given( applicationContext.getBeansWithAnnotation( TestLifeCycle.class ) ).thenReturn(
                    ImmutableMap.of(
                        SomeFixture.someString(), lifeCycle1,
                        SomeFixture.someString(), lifeCycle2
                    )
                );

                given( lifeCyclePhaseFactory.createTearDown( test ) ).thenReturn(  testTearDown );

                given( lifeCyclePhaseFactory.createReset( lifeCycle1 ) ).thenReturn( ImmutableList.of( reset1, reset2 ) );
                given( lifeCyclePhaseFactory.createAfterGiven( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterGiven1, afterGiven2 ) );
                given( lifeCyclePhaseFactory.createAfterWhen( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterWhen1, afterWhen2 ) );
                given( lifeCyclePhaseFactory.createAfterExternalBehaviour( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterExternalBehaviour1, afterExternalBehaviour2 ) );
                given( lifeCyclePhaseFactory.createTearDown( lifeCycle1 ) ).thenReturn( ImmutableList.of( tearDown1, tearDown2 ) );
                given( lifeCyclePhaseFactory.createAfterStep( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterStep1, afterStep2 ) );

                given( lifeCyclePhaseFactory.createReset( lifeCycle2 ) ).thenReturn( ImmutableList.of( reset3 ) );
                given( lifeCyclePhaseFactory.createAfterGiven( lifeCycle2 ) ).thenReturn( ImmutableList.of( afterGiven3 ) );
                given( lifeCyclePhaseFactory.createAfterWhen( lifeCycle2 ) ).thenReturn( ImmutableList.of( afterWhen3 ) );
                given( lifeCyclePhaseFactory.createAfterStep( lifeCycle2 ) ).thenReturn( ImmutableList.of( afterStep3 ) );
                given( lifeCyclePhaseFactory.createAfterExternalBehaviour( lifeCycle2 ) ).thenReturn( ImmutableList.of( afterExternalBehaviour3) );
                given( lifeCyclePhaseFactory.createTearDown( lifeCycle2 ) ).thenReturn( ImmutableList.of( tearDown3 ) );

            }

            @Override
            protected void when() throws Exception {
                integrationTestRunner.run( test );
            }

            @Override
            protected void then() throws Exception {
                verifyInOrder( autowireCapableBeanFactory ).autowireBean( test );
                verifyInOrder( reset1 ).run();
                verifyInOrder( reset2 ).run();
                verifyInOrder( reset3 ).run();
                verifyInOrder( test ).given();

                verifyInOrder( afterGiven1 ).run();
                verifyInOrder( afterGiven2 ).run();
                verifyInOrder( afterGiven3 ).run();

                verifyInOrder( test ).externalBehaviours();

                verifyInOrder( afterExternalBehaviour1 ).run();
                verifyInOrder( afterExternalBehaviour2 ).run();
                verifyInOrder( afterExternalBehaviour3 ).run();

                verifyInOrder( test ).when();
                verifyInOrder( afterWhen1 ).run();
                verifyInOrder( afterWhen2 ).run();
                verifyInOrder( afterWhen3 ).run();
                verifyInOrder( test ).then();
                verifyInOrder( testTearDown ).run();
                verifyInOrder( tearDown3 ).run();
                verifyInOrder( tearDown2 ).run();
                verifyInOrder( tearDown1 ).run();


                assertField( integrationTestRunner, "afterSteps", equalTo( ImmutableList.of( afterStep1, afterStep2, afterStep3 ) ) );
            }

        }.run();

    }

    @Test( expected = RuntimeException.class )
    public void tearDownsAreAlwaysRunEvenIfException() throws Exception {

        new Template() {

            @Override
            protected void given() throws Exception {
                given( applicationContext.getAutowireCapableBeanFactory() ).thenReturn( autowireCapableBeanFactory );
                given( applicationContext.getBeansWithAnnotation( TestLifeCycle.class ) ).thenReturn(
                    ImmutableMap.of( SomeFixture.someString(), lifeCycle1 )
                );
                given( lifeCyclePhaseFactory.createTearDown( lifeCycle1 ) ).thenReturn( ImmutableList.of( tearDown1, tearDown2 ) );
                given( lifeCyclePhaseFactory.createTearDown( test ) ).thenReturn( tearDown3 );

                doThrow( new RuntimeException( "sdfdsf" ) ).when( test ).when();
            }

            @Override
            protected void when() throws Exception {
                integrationTestRunner.run( test );
            }

            @Override
            protected void then() throws Exception {
                verifyInOrder( autowireCapableBeanFactory ).autowireBean( test );
                verifyInOrder( test ).given();
                verifyInOrder( test ).externalBehaviours();
                verifyInOrder( test ).when();
                verifyInOrder( tearDown3 ).run();
                verifyInOrder( tearDown2 ).run();
                verifyInOrder( tearDown1 ).run();

            }

        }.run();

    }


    @Test
    public void reRunningTestResetStandardPhases() throws Exception {

        new Template() {

            LifeCyclePhase reset1 = mock( LifeCyclePhase.class );

            @Override
            protected void given() throws Exception {
                given( applicationContext.getAutowireCapableBeanFactory() ).thenReturn( autowireCapableBeanFactory );

                given( applicationContext.getBeansWithAnnotation( TestLifeCycle.class ) ).thenReturn(
                    ImmutableMap.of(
                        SomeFixture.someString(), lifeCycle1
                    )
                );

                given( lifeCyclePhaseFactory.createReset( lifeCycle1 ) ).thenReturn( ImmutableList.of( reset1 ) );
                given( lifeCyclePhaseFactory.createAfterWhen( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterWhen1 ) );
                given( lifeCyclePhaseFactory.createTearDown( lifeCycle1 ) ).thenReturn( ImmutableList.of( tearDown1 ) );
                given( lifeCyclePhaseFactory.createAfterStep( lifeCycle1 ) ).thenReturn( ImmutableList.of( afterStep1 ) );
                given( lifeCyclePhaseFactory.createTearDown( test ) ).thenReturn( testTearDown );

            }

            @Override
            protected void when() throws Exception {
                integrationTestRunner.run( test );
                integrationTestRunner.run( test );
            }

            @Override
            protected void then() throws Exception {
                verifyInOrder( autowireCapableBeanFactory ).autowireBean( test );
                verify( reset1, Mockito.times( 2 ) ).run();
                verify( afterWhen1, Mockito.times( 2 ) ).run();
                verify( tearDown1, Mockito.times( 2 ) ).run();
                verify( testTearDown, Mockito.times( 2 ) ).run();
                assertField( integrationTestRunner, "afterSteps", equalTo( ImmutableList.of( afterStep1 ) ) );

            }

        }.run();

    }

    @Test
    public void runStepHappyDay() throws Exception {

        new Template() {
            Step step = mock( Step.class );
            Object expected = new Object();
            Stack<LifeCyclePhase> tearDowns = mock( Stack.class );

            Object actual;

            @Override
            protected void given() throws Exception {
                given( applicationContext.getAutowireCapableBeanFactory() ).thenReturn( autowireCapableBeanFactory );
                ReflectionUtil.setField( integrationTestRunner, "afterSteps", ImmutableList.of( afterStep1, afterStep2 ) );
                given( lifeCyclePhaseFactory.createTearDown( step ) ).thenReturn( ImmutableList.of( tearDown1, tearDown2 ) );

                ReflectionUtil.setField( integrationTestRunner, "tearDowns", tearDowns );
                given( step.run() ).thenReturn( expected );
            }

            @Override
            protected void when() throws Exception {
                actual = integrationTestRunner.run( step );

            }

            @Override
            protected void then() throws Exception {
                verifyInOrder( autowireCapableBeanFactory ).autowireBean( step );
                verifyInOrder( step ).run();
                verifyInOrder( tearDowns ).push(  tearDown1 );
                verifyInOrder( tearDowns ).push(  tearDown2 );
                verifyInOrder( afterStep1 ).run();
                verifyInOrder( afterStep2 ).run();
                assertThat( actual, equalTo( expected ) );
            }

        }.run();

    }


    @Test
    public void runConfigHappyDay() throws Exception {

        new Template() {
            Config config = mock( Config.class );
            Object expected = new Object();
            Object actual;
            Stack<LifeCyclePhase> tearDowns = mock( Stack.class );


            @Override
            protected void given() throws Exception {
                given( applicationContext.getAutowireCapableBeanFactory() ).thenReturn( autowireCapableBeanFactory );
                given( config.apply() ).thenReturn( expected );
                given( lifeCyclePhaseFactory.createTearDown( config ) ).thenReturn( ImmutableList.of( tearDown1, tearDown2 ) );
                ReflectionUtil.setField( integrationTestRunner, "tearDowns", tearDowns );

            }

            @Override
            protected void when() throws Exception {
                actual = integrationTestRunner.apply( config );

            }

            @Override
            protected void then() throws Exception {
                verifyInOrder( autowireCapableBeanFactory ).autowireBean( config );
                verifyInOrder( config ).apply();
                verifyInOrder( tearDowns ).push(  tearDown1 );
                verifyInOrder( tearDowns ).push(  tearDown2 );
                assertThat( actual, equalTo( expected ) );
            }

        }.run();

    }


}