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

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DateUtil {

    public static <T extends Comparable> T getMax( Collection<T> collection ) {
        Optional<T> max = collection.stream().max( ( o1, o2 ) -> o1.compareTo( o2 ) );
        return max.isPresent() ? max.get() : null;
    }

    public static long daysBetween( LocalDate start, LocalDate end, List<DayOfWeek> ignore ) {
        return Stream.iterate( start, d -> d.plusDays( 1 ) )
            .limit( start.until( end, ChronoUnit.DAYS ) )
            .filter( d -> !ignore.contains( d.getDayOfWeek() ) )
            .count();
    }

    public static long weekDaysBetween( LocalDate start, LocalDate end ) {
        return daysBetween( start, end, ImmutableList.of( DayOfWeek.SATURDAY, DayOfWeek.SUNDAY ) );
    }

    public static LocalDate getDateInThePast( LocalDate endDate, int requiredNumberWorkingDays, List<LocalDate> holidays ) {
        LocalDate start = endDate.minusDays( requiredNumberWorkingDays );
        long workingDaysBetween = workingDaysBetween( start, endDate, holidays );
        while ( workingDaysBetween < requiredNumberWorkingDays ) {
            start = start.minusDays( 1 );
            workingDaysBetween = workingDaysBetween( start, endDate, holidays );
        }
        return start;
    }

    public static LocalDate getDateInTheFuture( LocalDate start, int requiredNumberWorkingDays, List<LocalDate> holidays ) {
        LocalDate end = start.plusDays( requiredNumberWorkingDays );
        long workingDaysBetween = workingDaysBetween( start, end, holidays );
        while ( workingDaysBetween < requiredNumberWorkingDays ) {
            end = end.plusDays( 1 );
            workingDaysBetween = workingDaysBetween( start, end, holidays );
        }
        return end;
    }

    public static long workingDaysBetween( LocalDate start, LocalDate endDate, List<LocalDate> holidays ) {
        if ( endDate.isBefore( start ) ) {
            throw new IllegalStateException( String.format(
                "Expected endDate(%s) to be the same or after startDate(%s)",
                start, endDate
            ) );
        }
        return Stream.iterate( start, d -> d.plusDays( 1 ) )
            .limit( start.until( endDate, ChronoUnit.DAYS ) )
            .filter( d -> !ImmutableList.of( DayOfWeek.SATURDAY, DayOfWeek.SUNDAY ).contains( d.getDayOfWeek() ) )
            .filter( d -> !holidays.contains( d ) )
            .count();
    }

    public static String getDayOfMonthSuffix( LocalDate date ) {
        int n = date.getDayOfMonth();
        if ( n >= 11 && n <= 13 ) {
            return "th";
        }

        switch ( n % 10 ) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getDayOfMonthFormattedDate( LocalDate date ) {
        return String.format(
            "this %s day of %s %s",
            date.getDayOfMonth() + DateUtil.getDayOfMonthSuffix( date ),
            CaseFormat.UPPER_UNDERSCORE.to( CaseFormat.UPPER_CAMEL, date.getMonth().toString() ),
            date.getYear()
        );
    }
}
