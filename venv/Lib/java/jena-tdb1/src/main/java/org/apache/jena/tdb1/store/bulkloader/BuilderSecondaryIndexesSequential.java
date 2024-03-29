/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.tdb1.store.bulkloader;

import org.apache.jena.atlas.lib.Timer ;
import org.apache.jena.tdb1.store.tupletable.TupleIndex;

public class BuilderSecondaryIndexesSequential implements BuilderSecondaryIndexes
{
    private LoadMonitor monitor ;

    public BuilderSecondaryIndexesSequential(LoadMonitor monitor) { this.monitor = monitor ; } 
    
    // Create each secondary indexes, doing one at a time.
    @Override
    public void createSecondaryIndexes(TupleIndex   primaryIndex ,
                                       TupleIndex[] secondaryIndexes)
    {
        Timer timer = new Timer() ;
        timer.startTimer() ;

        for ( TupleIndex index : secondaryIndexes )
        {
            if ( index != null )
            {
                long time1 = timer.readTimer() ;
                LoaderNodeTupleTable.copyIndex(primaryIndex.all(), new TupleIndex[]{index}, index.getMappingStr(), monitor) ;
                long time2 = timer.readTimer() ;
                //                if ( printTiming )
                //                    printf("Time for %s indexing: %.2fs\n", index.getLabel(), (time2-time1)/1000.0) ;
//                if ( printTiming )
//                    printer.println() ;
            }  
        }
    }
}
