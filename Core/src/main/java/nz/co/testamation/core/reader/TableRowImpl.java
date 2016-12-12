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

import java.util.Map;

public class TableRowImpl implements TableRow {

    private Map<String, Object> map;

    public TableRowImpl( Map<String, Object> map ) {
        this.map = map;
    }

    @Override
    public int getIntColumn( String columnName ) {
        return Integer.valueOf( getColumn( columnName ) );
    }

    @Override
    public String getColumn( String columnName ) {
        return map.get( columnName ).toString();
    }
}
