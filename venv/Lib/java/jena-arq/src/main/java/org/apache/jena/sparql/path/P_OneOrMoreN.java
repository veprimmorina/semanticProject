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

package org.apache.jena.sparql.path ;

import org.apache.jena.sparql.util.NodeIsomorphismMap ;

/** One or more - all results */
public class P_OneOrMoreN extends P_Path1 {
    public P_OneOrMoreN(Path path) {
        super(path) ;
    }

    @Override
    public boolean equalTo(Path path2, NodeIsomorphismMap isoMap) {
        if ( path2 instanceof P_OneOrMoreN other )
            return getSubPath().equalTo(other.getSubPath(), isoMap) ;
        return false ;
    }

    @Override
    public int hashCode() {
        return hashOneOrMoreN ^ getSubPath().hashCode() ;
    }

    @Override
    public void visit(PathVisitor visitor) {
        visitor.visit(this) ;
    }
}
