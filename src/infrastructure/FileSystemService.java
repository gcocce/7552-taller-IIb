package infrastructure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSystemService implements IFileSystemService {

	@Override
	public boolean exists(String dataDirectory, String diagramName) {
		File file = new File(dataDirectory + "/" + diagramName + "-comp");
		return file.exists();
	}

	@Override
	public void save(String fileName, String content) {
		BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        finally
        {
            try
            {
                if (writer != null)
                {
                    writer.close();
                }
            }
            catch (IOException e)
            {
            	e.printStackTrace();
            }
        }
	}
}
