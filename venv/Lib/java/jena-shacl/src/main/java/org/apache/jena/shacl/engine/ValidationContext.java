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

package org.apache.jena.shacl.engine;

import org.apache.jena.atlas.io.IndentedWriter;
import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.riot.system.ErrorHandler;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.shacl.parser.Constraint;
import org.apache.jena.shacl.parser.Shape;
import org.apache.jena.shacl.sys.ShaclSystem;
import org.apache.jena.shacl.validation.ReportItem;
import org.apache.jena.shacl.validation.ValidationListener;
import org.apache.jena.shacl.validation.event.ValidationEvent;
import org.apache.jena.sparql.path.Path;

import java.util.function.Supplier;

public class ValidationContext {

    public static boolean VERBOSE = false;

    private final ValidationReport.Builder validationReportBuilder = ValidationReport.create();
    private boolean verbose = false;
    private boolean seenValidationReportEntry = false;
    private final Shapes shapes;
    private final Graph dataGraph;
    private boolean strict = false;
    private final ValidationListener validationListener;
    private final ErrorHandler errorHandler;
    private final IndentedWriter out;

    public static ValidationContext create(Shapes shapes, Graph data) {
        return create(shapes, data, ShaclSystem.systemShaclErrorHandler, null);
    }

    public static ValidationContext create(Shapes shapes, Graph data, ValidationListener validationListener) {
        return create(shapes, data, null, validationListener);
    }

    public static ValidationContext create(Shapes shapes, Graph data, ErrorHandler errorHandler){
        return create(shapes, data, errorHandler, null);
    }

    public static ValidationContext create(Shapes shapes, Graph data, ErrorHandler errorHandler, ValidationListener validationListener) {
        ValidationContext vCxt = new ValidationContext(shapes, data, errorHandler, validationListener);
        vCxt.setVerbose(VERBOSE);
        return vCxt;
    }

    public static ValidationContext create(ValidationContext vCxt) {
        return new ValidationContext(vCxt);
    }

    private ValidationContext(ValidationContext vCxt) {
        this.shapes = vCxt.shapes;
        this.dataGraph = vCxt.dataGraph;
        this.verbose = vCxt.verbose;
        this.strict = vCxt.strict;
        this.validationListener = vCxt.validationListener;
        this.errorHandler = vCxt.errorHandler;
        this.out = vCxt.out;
    }

    private ValidationContext(Shapes shapes, Graph data, ErrorHandler errorHandler, ValidationListener validationListener) {
        this.shapes = shapes;
        this.dataGraph = data;
        this.validationListener = validationListener;
        if ( errorHandler == null )
            errorHandler = ShaclSystem.systemShaclErrorHandler;
        this.errorHandler = errorHandler;
        this.out = IndentedWriter.stdout.clone();
        validationReportBuilder.addPrefixes(data.getPrefixMapping());
        validationReportBuilder.addPrefixes(shapes.getGraph().getPrefixMapping());
    }

    //public ValidationReport.Builder builder() { return validationReportBuilder; }

    public void reportEntry(ReportItem item, Shape shape, Node focusNode, Path path, Constraint constraint) {
        reportEntry(item.getMessage(), shape, focusNode, path, item.getValue(), constraint);
    }

    public void reportEntry(String message, Shape shape, Node focusNode, Path path, Node valueNode, Constraint constraint) {
        if ( verbose )
            System.out.println("Validation report entry");
        seenValidationReportEntry = true;
        validationReportBuilder.addReportEntry(message, shape, focusNode, path, valueNode, constraint);
    }

    public ValidationReport generateReport() {
        return validationReportBuilder.build();
    }

    public IndentedWriter out() { return out; }

    public boolean hasViolation() { return seenValidationReportEntry; }

    public void setVerbose(boolean value) {
        this.verbose = value;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void setStrict(boolean value) {
        this.strict = value;
    }

    public boolean isStrict() {
        return strict ;
    }
    public Shapes getShapes() {
        return shapes;
    }

    public Graph getShapesGraph() {
        return shapes.getGraph();
    }

    public Graph getDataGraph() {
        return dataGraph;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void notifyValidationListener(Supplier<ValidationEvent> eventSupplier){
        if (validationListener != null){
            ValidationEvent event = eventSupplier.get();
            validationListener.onValidationEvent(event);
        }
    }

}
