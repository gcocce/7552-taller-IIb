package controllers;

import infrastructure.IProjectContext;

public abstract class BaseController {
	
	protected IProjectContext projectContext;
	
	public BaseController(IProjectContext projectContext)
	{
		this.projectContext = projectContext;
	}
}
