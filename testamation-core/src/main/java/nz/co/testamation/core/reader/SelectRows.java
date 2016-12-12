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

package nz.co.testamation.core.reader;

import com.google.common.collect.Lists;
import org.springframework.jdbc.core.JdbcTemplate;

public class SelectRows implements SelectQuery<SQLResult> {
    private static final String SELECT_SQL_TEMPLATE = "select * from %s %s";

    private final String tableName;
    private WhereClauseBuilder where = new WhereClauseBuilder();


    public SelectRows( String tableName ) {
        this.tableName = tableName;
    }

    public SelectRows where( String column, String value ) {
        where.and( column, value );
        return this;
    }

    @Override
    public SQLResult select( JdbcTemplate jdbcTemplate ) {
        return new SQLResult(
            Lists.transform( jdbcTemplate.queryForList( getSql() ), TableRowImpl::new )
        );
    }

    protected String getSql() {
        return String.format( SELECT_SQL_TEMPLATE, tableName, where.toString() );
    }


    @Override
    public String toString() {
        return String.format( "SelectRows{table=%s, query=%s}", tableName, getSql() );
    }

}