package persistence;

import com.mxgraph.view.mxGraph;

public interface IGraphPersistenceService {
	void save(String name, mxGraph graph);
	void load(String name, mxGraph graph);
}
