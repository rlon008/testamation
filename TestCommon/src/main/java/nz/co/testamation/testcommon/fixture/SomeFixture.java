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

package nz.co.testamation.testcommon.fixture;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class SomeFixture {

    public static String someString( int length ) {
        return RandomStringUtils.randomAlphabetic( length );
    }

    public static String someString() {
        return RandomStringUtils.randomAlphabetic( 7 );
    }

    public static int someInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static long someLong() {
        return ThreadLocalRandom.current().nextLong();
    }


    public static int someIntLessThan( int maxExclusive ) {
        return ThreadLocalRandom.current().nextInt( maxExclusive );
    }


    public static int someYear() {
        return 2000 + someIntLessThan( 11 );
    }

    public static Duration someDuration() {
        return Duration.ofSeconds( someLong() );
    }

    public static String someEmail() {
        return String.format( "%s@%s.com", RandomStringUtils.randomAlphabetic( 5 ), RandomStringUtils.randomAlphabetic( 5 ) ).toLowerCase();
    }

    public static LocalDate someLocalDate() {
        return LocalDate.of(
            someYear(),
            someIntLessThan( 12 ) + 1,
            someIntLessThan( 28 ) + 1
        );
    }

    public static LocalDateTime someDateTime() {
        return LocalDateTime.of(
            someYear(),
            someIntLessThan( 12 ) + 1,
            someIntLessThan( 28 ) + 1,
            someIntBetween( 4, 24 ), // avoid sometimes invalid 2am due to daylight savings
            someIntLessThan( 60 ),
            someIntLessThan( 60 )
        );
    }

    public static Boolean someBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static <E extends Enum> E someEnum( Class<E> enumClazz ) {
        return someValue( enumClazz.getEnumConstants() );
    }

    public static <E extends Enum> E someEnum( Class<E> enumClazz, Predicate<E> predicate ) {
        E result;
        do {
            result = someEnum( enumClazz );
        } while ( !predicate.apply( result ) );
        return result;
    }

    public static <T extends Enum> T someEnumOtherThan( Class<T> enumClazz, T... excluded ) {
        return someValue( Sets.difference( ImmutableSet.copyOf( enumClazz.getEnumConstants() ), ImmutableSet.copyOf( excluded ) ) );
    }

    private static int someInt( int length ) {
        return new Integer( RandomStringUtils.randomNumeric( length ) );
    }

    public static Integer someIntBetween( int minInclusive, int maxExclusive ) {
        return ThreadLocalRandom.current().nextInt( minInclusive, maxExclusive );
    }

    public static String someEmail( String prefix ) {
        return prefix + "_" + someEmail();
    }


    public static BigDecimal someBigDecimal() {
        return new BigDecimal( somePositiveInt() );
    }

    public static BigDecimal someBigDecimalPercentage() {
        return new BigDecimal( someDouble() );
    }

    public static double someDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static byte[] someBytes() {
        return someString().getBytes();
    }

    public static int somePositiveInt() {
        return Math.abs( someInt() );
    }

    public static <T> T someValue( T... values ) {
        return values[ someIntLessThan( values.length ) ];
    }

    public static <T> T someValue( Collection<T> values ) {
        return Iterables.get( values, someIntLessThan( values.size() ) );
    }

    public static String someString( String... choises ) {
        return choises[ SomeFixture.someIntBetween( 0, choises.length ) ];
    }

    public static String someString( Iterable<String> choices ) {
        return someString( Iterables.toArray( choices, String.class ) );
    }

    public static BigInteger someBigInteger() {
        return new BigInteger( String.valueOf( somePositiveInt() ) );
    }

    public static <T> T someThing( T... things ) {
        return things[ someIntBetween( 0, things.length ) ];
    }
}
