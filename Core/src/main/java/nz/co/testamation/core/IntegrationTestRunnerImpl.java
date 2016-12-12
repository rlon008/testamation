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

import com.google.common.collect.Lists;
import nz.co.testamation.core.config.Config;
import nz.co.testamation.core.lifecycle.DynamicProxyLifeCyclePhaseFactory;
import nz.co.testamation.core.lifecycle.LifeCyclePhase;
import nz.co.testamation.core.lifecycle.LifeCyclePhaseFactory;
import nz.co.testamation.core.lifecycle.annotation.TestLifeCycle;
import nz.co.testamation.core.step.Step;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class IntegrationTestRunnerImpl implements IntegrationTestRunner {

    private final ApplicationContext applicationContext;
    private final LifeCyclePhaseFactory lifeCyclePhaseFactory;

    private List<LifeCyclePhase> resets = Lists.newArrayList();
    private List<LifeCyclePhase> afterSteps = Lists.newArrayList();
    private List<LifeCyclePhase> afterGivens = Lists.newArrayList();
    private List<LifeCyclePhase> afterWhens = Lists.newArrayList();
    private List<LifeCyclePhase> afterExternalBehaviours = Lists.newArrayList();
    private Stack<LifeCyclePhase> tearDowns = new Stack<>();

    public IntegrationTestRunnerImpl( ApplicationContext applicationContext, LifeCyclePhaseFactory lifeCyclePhaseFactory ) {
        this.applicationContext = applicationContext;
        this.lifeCyclePhaseFactory = lifeCyclePhaseFactory;

    }

    public IntegrationTestRunnerImpl( ApplicationContext applicationContext ) {
        this( applicationContext, new DynamicProxyLifeCyclePhaseFactory() );
    }

    private void init() {
        resets.clear();
        afterSteps.clear();
        afterGivens.clear();
        afterExternalBehaviours.clear();
        afterWhens.clear();
        tearDowns.clear();

        Map<String, Object> lifecycleBeans = applicationContext.getBeansWithAnnotation( TestLifeCycle.class );
        for ( Object bean : lifecycleBeans.values() ) {
            resets.addAll( lifeCyclePhaseFactory.createReset( bean ) );
            afterSteps.addAll( lifeCyclePhaseFactory.createAfterStep( bean ) );
            afterGivens.addAll( lifeCyclePhaseFactory.createAfterGiven( bean ) );
            afterExternalBehaviours.addAll( lifeCyclePhaseFactory.createAfterExternalBehaviour( bean ) );
            afterWhens.addAll( lifeCyclePhaseFactory.createAfterWhen( bean ) );
            List<LifeCyclePhase> tearDowns = lifeCyclePhaseFactory.createTearDown( bean );
            for ( LifeCyclePhase tearDown : tearDowns ) {
                this.tearDowns.push( tearDown );

            }
        }
    }

    @Override
    public void run( TestTemplate test ) throws Exception {
        init();
        applicationContext.getAutowireCapableBeanFactory().autowireBean( test );
        tearDowns.push( lifeCyclePhaseFactory.createTearDown( test ) );
        try {
            reset();
            test.given();
            afterGiven();
            test.externalBehaviours();
            afterExternalBehaviour();
            test.when();
            afterWhen();
            test.then();
        } finally {
            tearDowns();
        }
    }


    @Override
    public <T> T run( Step<T> step ) throws Exception {
        applicationContext.getAutowireCapableBeanFactory().autowireBean( step );
        T result = step.run();
        lifeCyclePhaseFactory.createTearDown( step ).forEach( tearDowns::push );
        afterSteps();
        return result;
    }

    @Override
    public <T> T apply( Config<T> config ) throws Exception {
        applicationContext.getAutowireCapableBeanFactory().autowireBean( config );
        T result = config.apply();
        lifeCyclePhaseFactory.createTearDown( config ).forEach( tearDowns::push );
        return result;
    }


    private void afterExternalBehaviour() throws Exception {
        for ( LifeCyclePhase phase : afterExternalBehaviours ) {
            phase.run();
        }
    }

    private void afterGiven() throws Exception {
        for ( LifeCyclePhase phase : afterGivens ) {
            phase.run();
        }
    }

    private void afterSteps() throws Exception {
        for ( LifeCyclePhase phase : afterSteps ) {
            phase.run();
        }
    }

    private void tearDowns() throws Exception {
        while( !tearDowns.isEmpty() && tearDowns.peek() !=null ) {
            tearDowns.pop().run();
        }
    }

    private void reset() throws Exception {
        for ( LifeCyclePhase phase : resets ) {
            phase.run();
        }
    }

    private void afterWhen() throws Exception {
        for ( LifeCyclePhase verify : afterWhens ) {
            verify.run();
        }
    }

}
