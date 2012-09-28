package controllers.tests;

import models.Diagram;
import validation.IProjectValidationService;

public class MockProjectValidationService implements IProjectValidationService {

	@Override
	public String generateGlobalReport(String projectName, Iterable<Diagram> diagrams,
                                       int tolerance) {
		return null;
	}

    @Override
    public String generateIndividualReport(Diagram diagram) {
        return null;
    }

}
