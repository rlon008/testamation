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

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static nz.co.testamation.common.util.DateUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DateUtilTest {
    LocalDate monday = LocalDate.of( 2016, 1, 11 );
    LocalDate friday = LocalDate.of( 2016, 1, 15 );
    LocalDate mondayAfter = LocalDate.of( 2016, 1, 18 );
    LocalDate wednesdayAfter = LocalDate.of( 2016, 1, 20 );


    @Test
    public void daysBetweenExcludingIgnoringNothing() throws Exception {
        assertThat( daysBetween( monday, friday, ImmutableList.of() ), equalTo( 4L ) );
    }

    @Test
    public void daysBetweenExcludingIgnoringDaysOutsideRange() throws Exception {
        assertThat( daysBetween( monday, friday, ImmutableList.of( DayOfWeek.SATURDAY ) ), equalTo( 4L ) );
    }

    @Test
    public void daysBetweenExcludingIgnoringTuesday() throws Exception {
        assertThat( daysBetween( monday, friday, ImmutableList.of( DayOfWeek.TUESDAY ) ), equalTo( 3L ) );
    }

    @Test
    public void daysBetweenExcludingIgnoringMultipleDays() throws Exception {
        assertThat( daysBetween( monday, friday, ImmutableList.of( DayOfWeek.MONDAY, DayOfWeek.TUESDAY ) ), equalTo( 2L ) );
    }

    @Test
    public void workingDays() throws Exception {
        assertThat( weekDaysBetween( monday, mondayAfter ), equalTo( 5L ) );
        assertThat( weekDaysBetween( friday, wednesdayAfter ), equalTo( 3L ) );
    }

    @Test
    public void dateInPastIgnoresWeekendAndHolidays() {
        LocalDate otherHoliday = LocalDate.of( 2016, 3, 9 );
        LocalDate goodFriday = LocalDate.of( 2016, 3, 25 );
        LocalDate easterMonday = LocalDate.of( 2016, 3, 28 );
        LocalDate endOfMonth = LocalDate.of( 2016, 3, 31 );

        assertThat( getDateInThePast( endOfMonth, 10, ImmutableList.of( goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 15 ) ) );
        assertThat( getDateInThePast( endOfMonth, 16, ImmutableList.of( goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 7 ) ) );
        assertThat( getDateInThePast( endOfMonth, 16, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 4 ) ) );
        assertThat( getDateInThePast( endOfMonth, 1, ImmutableList.of( goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 30 ) ) );
    }

    @Test
    public void dateInFutureIgnoresWeekendAndHolidays() {
        LocalDate otherHoliday = LocalDate.of( 2016, 3, 9 );
        LocalDate goodFriday = LocalDate.of( 2016, 3, 25 );
        LocalDate easterMonday = LocalDate.of( 2016, 3, 28 );

        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 15 ), 10, ImmutableList.of( goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 31 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 15 ), 9, ImmutableList.of( goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 30 ) ) );

        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 1, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 5 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 2, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 8 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 3, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 9 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 4, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 11 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 5, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 12 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 6, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 15 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 7, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 16 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 8, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 17 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 9, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 18 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 10, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 19 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 11, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 22 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 12, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 23 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 13, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 24 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 14, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 25 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 15, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 30 ) ) );
        assertThat( getDateInTheFuture( LocalDate.of( 2016, 3, 4 ), 16, ImmutableList.of( otherHoliday, goodFriday, easterMonday ) ), equalTo( LocalDate.of( 2016, 3, 31 ) ) );

    }


    @Test
    public void getDayOfMonthFormattedDateBlackBoxTest() throws Exception {

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 2, 23 ) ),
            Matchers.equalTo( "this 23rd day of February 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 2, 13 ) ),
            Matchers.equalTo( "this 13th day of February 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 1, 1 ) ),
            Matchers.equalTo( "this 1st day of January 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 1, 11 ) ),
            Matchers.equalTo( "this 11th day of January 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 1, 21 ) ),
            Matchers.equalTo( "this 21st day of January 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 7, 12 ) ),
            Matchers.equalTo( "this 12th day of July 2010" )
        );

        MatcherAssert.assertThat(
            DateUtil.getDayOfMonthFormattedDate( LocalDate.of( 2010, 8, 22 ) ),
            Matchers.equalTo( "this 22nd day of August 2010" )
        );

    }

    @Test
    public void workingDayBetweenCountTheNumberOfWeekDays() throws Exception {
        MatcherAssert.assertThat(
            DateUtil.workingDaysBetween( LocalDate.of( 2016, 7, 14 ), LocalDate.of( 2016, 7, 20 ), ImmutableList.of() ),
            Matchers.equalTo( 4L )
        );
    }

    @Test
    public void workingDaysExcludeHolidays() throws Exception {
        MatcherAssert.assertThat(
            DateUtil.workingDaysBetween( LocalDate.of( 2016, 7, 14 ), LocalDate.of( 2016, 7, 20 ), ImmutableList.of(
                    LocalDate.of( 2016, 7, 14 ),
                    LocalDate.of( 2016, 7, 16 ),
                    LocalDate.of( 2016, 7, 18 )
                )
            ),
            Matchers.equalTo( 2L )
        );
    }

    @Test(expected = IllegalStateException.class)
    public void workingDayBetweenBlowUpIfEndDateIsBeforeStartDate() throws Exception {
        DateUtil.workingDaysBetween( LocalDate.of( 2016, 7, 14 ), LocalDate.of( 2016, 7, 13 ), ImmutableList.of() );
    }
}