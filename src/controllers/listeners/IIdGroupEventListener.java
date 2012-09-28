package controllers.listeners;

import java.util.HashMap;
import java.util.List;

import models.IKey;

public interface IIdGroupEventListener {
	void handleEvent(HashMap<Integer, List<IKey>> param);
}
