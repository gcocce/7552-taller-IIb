package validation;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.NumberTool;

import validation.metrics.IMetricsCalculator;
import validation.metrics.IMetricsValidator;
import validation.metrics.Metrics;
import validation.rules.IRulesValidator;

import models.Diagram;

public class ProjectValidationService implements IProjectValidationService {

    private IMetricsCalculator metricsCalculator;
    private IMetricsValidator[] metricsValidators;
    private IRulesValidator[] rulesValidators;

    public ProjectValidationService(IMetricsCalculator metricsCalculator, IMetricsValidator[] metricsValidators, IRulesValidator[] rulesValidator) {
        this.metricsCalculator = metricsCalculator;
        this.metricsValidators = metricsValidators;
        this.rulesValidators = rulesValidator;
    }

    @Override
    public String generateGlobalReport(String diagramName, Iterable<Diagram> diagrams, int tolerance) {
        List<IValidationEntry> entries = new ArrayList<IValidationEntry>();
        Metrics metrics = this.metricsCalculator.calculateMetrics(diagrams);

        for (Diagram diagram : diagrams) {
              for (int i = 0; i < this.metricsValidators.length; i++) {
                  IMetricsValidator validator = this.metricsValidators[i];
                  for (IValidationEntry entry : validator.validate(diagram, metrics, tolerance)) {
                      entries.add(entry);
                  }
              }

              for (int i = 0; i < this.rulesValidators.length; i++) {
                  IRulesValidator validator = this.rulesValidators[i];
                  for (IValidationEntry entry : validator.validate(diagram)) {
                      entries.add(entry);
                  }
              }
          }

        VelocityEngine engine = new VelocityEngine();
        engine.init();
        Template template = engine.getTemplate("projectValidation.vm");
        VelocityContext context = new VelocityContext();
        context.put("title", "Project Validation - " + diagramName);
        context.put("metrics", metrics.getMetrics());
        context.put("entries", entries);
        context.put("numberTool", new NumberTool());
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

    @Override
    public String generateIndividualReport(Diagram diagram) {

        List<IValidationEntry> entries = new ArrayList<IValidationEntry>();

        for (IRulesValidator validator : this.rulesValidators) {
            for (IValidationEntry entry : validator.validate(diagram)) {
                entries.add(entry);
            }
        }
        
        if (entries.size() == 0){
        	diagram.isValid();
        }
        else{
        	diagram.isInvalid();
        }

        VelocityEngine engine = new VelocityEngine();
        engine.init();
        Template template = engine.getTemplate("diagramValidation.vm");
        VelocityContext context = new VelocityContext();
        context.put("title", "Diagram Validation - " + diagram.getName());
        context.put("entries", entries);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

}
