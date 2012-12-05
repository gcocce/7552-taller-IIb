package validation;

import models.der.Diagram;

public interface IProjectValidationService {
    String generateGlobalReport(String diagramName, Iterable<Diagram> diagrams, int tolerance);

    String generateIndividualReport(Diagram diagram);
}
