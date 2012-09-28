package application.tests.mocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.NameBinding;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;
import org.picocontainer.lifecycle.LifecycleState;

public class MockPicoContainer implements MutablePicoContainer {

	private Map<Object, Object> mappings;
		
	public MockPicoContainer(){
		this.mappings = new HashMap<Object, Object>();
	}
	
	public Map<Object, Object> getMappings(){
		return this.mappings;
	}
	
	@Override
	public MutablePicoContainer addAdapter(ComponentAdapter<?> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutablePicoContainer addChildContainer(PicoContainer arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutablePicoContainer addComponent(Object toType) {
		this.mappings.put(toType, toType);
		return this;
	}

	@Override
	public MutablePicoContainer addComponent(Object fromType, Object toType,
			Parameter... arg2) {
		this.mappings.put(fromType, toType);
		return this;
	}

	@Override
	public MutablePicoContainer addConfig(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutablePicoContainer as(Properties... arg0) {
		return this;
	}

	@Override
	public MutablePicoContainer change(Properties... arg0) {
		
		return null;
	}

	@Override
	public LifecycleState getLifecycleState() {
		
		return null;
	}

	@Override
	public String getName() {
		
		return null;
	}

	@Override
	public MutablePicoContainer makeChildContainer() {
		
		return null;
	}

	@Override
	public boolean removeChildContainer(PicoContainer arg0) {
		
		return false;
	}

	@Override
	public <T> ComponentAdapter<T> removeComponent(Object arg0) {
		return null;
	}

	@Override
	public <T> ComponentAdapter<T> removeComponentByInstance(T arg0) {		
		return null;
	}

	@Override
	public void setLifecycleState(LifecycleState arg0) {
	}

	@Override
	public void setName(String arg0) {
	}

	@Override
	public void accept(PicoVisitor arg0) {
	}

	@Override
	public Object getComponent(Object arg0) {
		return this.mappings.get(arg0);
	}

	@Override
	public <T> T getComponent(Class<T> arg0) {
		return (T)this.mappings.get(arg0);
	}

	@Override
	public Object getComponent(Object arg0, Type arg1) {
		return null;
	}

	@Override
	public <T> T getComponent(Class<T> arg0, Class<? extends Annotation> arg1) {
		return null;
	}

	@Override
	public ComponentAdapter<?> getComponentAdapter(Object arg0) {
		return null;
	}

	@Override
	public <T> ComponentAdapter<T> getComponentAdapter(Class<T> arg0,
			NameBinding arg1) {
		return null;
	}

	@Override
	public <T> ComponentAdapter<T> getComponentAdapter(Class<T> arg0,
			Class<? extends Annotation> arg1) {
		return null;
	}

	@Override
	public Collection<ComponentAdapter<?>> getComponentAdapters() {
		return null;
	}

	@Override
	public <T> List<ComponentAdapter<T>> getComponentAdapters(Class<T> arg0) {
		return null;
	}

	@Override
	public <T> List<ComponentAdapter<T>> getComponentAdapters(Class<T> arg0,
			Class<? extends Annotation> arg1) {
		return null;
	}

	@Override
	public List<Object> getComponents() {
		return null;
	}

	@Override
	public <T> List<T> getComponents(Class<T> arg0) {
		return null;
	}

	@Override
	public PicoContainer getParent() {		
		return null;
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void dispose() {
	}

}
