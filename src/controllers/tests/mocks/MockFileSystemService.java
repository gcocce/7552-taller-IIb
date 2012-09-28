package controllers.tests.mocks;

import java.io.File;

import infrastructure.IFileSystemService;

public class MockFileSystemService implements IFileSystemService{

	private boolean exists;

	public MockFileSystemService() {
		this.exists = false;
	}
	@Override
	public boolean exists(String dataDirectory, String diagramName) {
		File file = new File(dataDirectory + "/" + diagramName + "-rep");
		if (this.exists == false)
			this.exists = file.exists();
		return this.exists;
	}
	
	public void setExistsReturnValue(boolean value) {
		this.exists = value;
	}
	@Override
	public void save(String fileName, String content) {
		// TODO Auto-generated method stub
		
	}

}
