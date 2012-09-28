package controllers.tests.mocks;

import controllers.IEntityController;
import controllers.factories.IEntityControllerFactory;

public class MockEntityControllerFactory 
	implements IEntityControllerFactory 
{
		private IEntityController controller;
		private int createCalls;
		
		public void setController(IEntityController controller)
		{
			this.controller = controller;
		}
		
		@Override
		public IEntityController create() {
			this.createCalls++;
			return this.controller;
		}

		public int getCreateCallsCount() {
			return this.createCalls;
		}
}
