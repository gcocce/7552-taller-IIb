package validation;

import models.Diagram;

public interface IProjectValidationService {
    String generateGlobalReport(String diagramName, Iterable<Diagram> diagrams, int tolerance);

    String generateIndividualReport(Diagram diagram);
}
