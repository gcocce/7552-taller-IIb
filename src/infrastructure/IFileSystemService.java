package infrastructure;

public interface IFileSystemService {
	boolean exists(String dataDirectory, String defaultDiagramName);
	void save(String fileName, String content);
}
