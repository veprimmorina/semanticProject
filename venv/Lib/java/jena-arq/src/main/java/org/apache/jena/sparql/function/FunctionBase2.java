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

package org.apache.jena.sparql.function;

import java.util.List ;

import org.apache.jena.atlas.lib.Lib ;
import org.apache.jena.query.QueryBuildException ;
import org.apache.jena.sparql.ARQInternalErrorException ;
import org.apache.jena.sparql.expr.ExprEvalException ;
import org.apache.jena.sparql.expr.ExprList ;
import org.apache.jena.sparql.expr.NodeValue ;

/** Support for functions with two arguments. */
public abstract class FunctionBase2 extends FunctionBase {

    @Override
    public void checkBuild(String uri, ExprList args) {
        if ( args.size() != 2 )
            throw new QueryBuildException("Function '" + Lib.className(this) + "' takes two arguments");
    }

    @Override
    public final NodeValue exec(List<NodeValue> args) {
        if ( args == null )
            throw new ARQInternalErrorException(Lib.className(this) + ": Null args list");
        if ( args.size() != 2 )
            throw new ExprEvalException(Lib.className(this) + ": Wrong number of arguments: Wanted 2, got " + args.size());
        NodeValue v1 = args.get(0);
        NodeValue v2 = args.get(1);
        return exec(v1, v2);
    }

    public abstract NodeValue exec(NodeValue v1, NodeValue v2);
}
