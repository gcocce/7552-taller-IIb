package application;

import static org.picocontainer.Characteristics.CACHE;
import infrastructure.FileSystemService;
import infrastructure.IFileSystemService;
import infrastructure.IProjectContext;
import infrastructure.ProjectContext;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import persistence.DiagramXmlManager;
import persistence.GraphPersistenceService;
import persistence.IGraphPersistenceService;
import persistence.IXmlFileManager;
import persistence.IXmlManager;
import persistence.XmlFileManager;
import validation.IProjectValidationService;
import validation.ProjectValidationService;
import validation.metrics.AttributesPerEntityValidator;
import validation.metrics.AttributesPerRelationshipValidator;
import validation.metrics.EntitiesPerDiagramValidator;
import validation.metrics.EntitiesPerHierarchyValidator;
import validation.metrics.EntitiesPerRelationshipValidator;
import validation.metrics.HierarchiesPerDiagramValidator;
import validation.metrics.IMetricsCalculator;
import validation.metrics.MetricsCalculator;
import validation.metrics.RelationshipsPerDiagramValidator;
import validation.rules.AttributeCardinalityValidator;
import validation.rules.DiagramMustHaveEntitiesValidator;
import validation.rules.DiagramShouldHaveRelationshipsValidator;
import validation.rules.EntityTypeValidator;
import validation.rules.RelationshipCardinalityValidator;
import views.AttributeView;
import views.DiagramView;
import views.EntityView;
import views.HierarchyView;
import views.IAttributeView;
import views.IDiagramView;
import views.IEntityView;
import views.IHierarchyView;
import views.IKeysView;
import views.IProjectView;
import views.IRelationshipEntityView;
import views.IRelationshipView;
import views.KeyView;
import views.ProjectView;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxIMarker;
import com.mxgraph.shape.mxMarkerRegistry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import views.RelationshipEntityViewImpl;
import views.RelationshipViewImpl;

import controllers.AttributeController;
import controllers.DiagramController;
import controllers.EntityController;
import controllers.HierarchyController;
import controllers.IAttributeController;
import controllers.IDiagramController;
import controllers.IEntityController;
import controllers.IHierarchyController;
import controllers.IKeysController;
import controllers.IProjectController;
import controllers.IRelationshipController;
import controllers.IRelationshipEntityController;
import controllers.KeysController;
import controllers.ProjectController;

import controllers.factories.AttributeControllerFactory;
import controllers.factories.DiagramControllerFactory;
import controllers.factories.EntityControllerFactory;
import controllers.factories.HierarchyControllerFactory;
import controllers.factories.IAttributeControllerFactory;
import controllers.factories.IDiagramControllerFactory;
import controllers.factories.IEntityControllerFactory;
import controllers.factories.IHierarchyControllerFactory;
import controllers.factories.IKeysControllerFactory;
import controllers.factories.IRelationshipControllerFactory;
import controllers.factories.IRelationshipEntityControllerFactory;
import controllers.factories.KeyControllerFactory;
import controllers.factories.RelationshipControllerFactory;
import controllers.factories.RelationshipEntityControllerFactory;
import controllers.RelationshipController;
import controllers.RelationshipEntityController;

public class Bootstrapper {

	private MutablePicoContainer container;

	public MutablePicoContainer getContainer() {
		return this.container;
	}

	public void run() {
		this.container = this.createContainer();
		this.configureContainer();
		this.registerJGraphExtensions();
	}

	private void registerJGraphExtensions() {
		mxMarkerRegistry.registerMarker("emptyCircle", new mxIMarker()
		{
			public mxPoint paintMarker(mxGraphics2DCanvas canvas,
					mxCellState state, String type, mxPoint pe, double nx,
					double ny, double size)
			{
				double cx = pe.getX() - nx / 2;
				double cy = pe.getY() - ny / 2;
				double a = size / 2;
				Shape shape = new Ellipse2D.Double(cx - a, cy - a, size, size);
				
				canvas.getGraphics().draw(shape);

				return new mxPoint(-nx / 2, -ny / 2);
			}
		});
		mxMarkerRegistry.registerMarker("emptyRedCircle", new mxIMarker()
		{
			public mxPoint paintMarker(mxGraphics2DCanvas canvas,
					mxCellState state, String type, mxPoint pe, double nx,
					double ny, double size)
			{
				double cx = pe.getX() - nx / 2;
				double cy = pe.getY() - ny / 2;
				double a = size / 2;
				Shape shape = new Ellipse2D.Double(cx - a, cy - a, size, size);
				
				Color originalColor = canvas.getGraphics().getColor();
				canvas.getGraphics().setColor(Color.RED);
				canvas.getGraphics().draw(shape);
				canvas.getGraphics().setColor(originalColor);

				return new mxPoint(-nx / 2, -ny / 2);
			}
		});
		mxMarkerRegistry.registerMarker("filledRedCircle", new mxIMarker()
		{
			public mxPoint paintMarker(mxGraphics2DCanvas canvas,
					mxCellState state, String type, mxPoint pe, double nx,
					double ny, double size)
			{
				double cx = pe.getX() - nx / 2;
				double cy = pe.getY() - ny / 2;
				double a = size / 2;
				Shape shape = new Ellipse2D.Double(cx - a, cy - a, size, size);
				
				Color originalColor = canvas.getGraphics().getColor();
				canvas.getGraphics().setColor(Color.RED);
				canvas.getGraphics().draw(shape);
				canvas.getGraphics().fill(shape);
				canvas.getGraphics().setColor(originalColor);

				return new mxPoint(-nx / 2, -ny / 2);
			}
		});
	}

	private void configureContainer() {
		this.container
					.as(CACHE).addComponent(MutablePicoContainer.class, this.container)
					.addComponent(IDiagramController.class, DiagramController.class)
					.addComponent(IDiagramView.class, DiagramView.class)
					.addComponent(IXmlManager.class, DiagramXmlManager.class)
					.as(CACHE).addComponent(IXmlFileManager.class, XmlFileManager.class)
					.as(CACHE).addComponent(IProjectContext.class, ProjectContext.class)
					.as(CACHE).addComponent(IEntityControllerFactory.class, EntityControllerFactory.class)
					.addComponent(IEntityController.class, EntityController.class)
					.addComponent(IEntityView.class, EntityView.class)
					.as(CACHE).addComponent(IRelationshipControllerFactory.class, RelationshipControllerFactory.class)
					.addComponent(IRelationshipController.class, RelationshipController.class)
					.addComponent(IRelationshipView.class, RelationshipViewImpl.class)
					.as(CACHE).addComponent(IRelationshipEntityControllerFactory.class, RelationshipEntityControllerFactory.class)
					.addComponent(IRelationshipEntityController.class, RelationshipEntityController.class)
					.addComponent(IRelationshipEntityView.class, RelationshipEntityViewImpl.class)
					.as(CACHE).addComponent(IHierarchyControllerFactory.class, HierarchyControllerFactory.class)
					.addComponent(IHierarchyController.class, HierarchyController.class)
					.addComponent(IHierarchyView.class, HierarchyView.class)
					.as(CACHE).addComponent(IKeysControllerFactory.class, KeyControllerFactory.class)
					.addComponent(IKeysController.class, KeysController.class)
					.addComponent(IKeysView.class, KeyView.class)
					.as(CACHE).addComponent(IGraphPersistenceService.class, GraphPersistenceService.class)
					.as(CACHE).addComponent(IDiagramControllerFactory.class, DiagramControllerFactory.class)
					.addComponent(IProjectController.class, ProjectController.class)
					.addComponent(IProjectView.class, ProjectView.class)
					.addComponent(IAttributeController.class, AttributeController.class)
					.as(CACHE).addComponent(IProjectValidationService.class, ProjectValidationService.class)
					.as(CACHE).addComponent(IMetricsCalculator.class, MetricsCalculator.class)
					.as(CACHE).addComponent(IFileSystemService.class, FileSystemService.class)
					.as(CACHE).addComponent(IAttributeControllerFactory.class, AttributeControllerFactory.class)
					.as(CACHE).addComponent(RelationshipsPerDiagramValidator.class)
					.as(CACHE).addComponent(EntitiesPerDiagramValidator.class)
					.as(CACHE).addComponent(EntitiesPerHierarchyValidator.class)
					.as(CACHE).addComponent(EntitiesPerRelationshipValidator.class)
					.as(CACHE).addComponent(HierarchiesPerDiagramValidator.class)
					.as(CACHE).addComponent(AttributesPerRelationshipValidator.class)
					.as(CACHE).addComponent(AttributesPerEntityValidator.class)
					.as(CACHE).addComponent(RelationshipCardinalityValidator.class)
					.as(CACHE).addComponent(EntityTypeValidator.class)
					.as(CACHE).addComponent(AttributeCardinalityValidator.class)
					.as(CACHE).addComponent(DiagramShouldHaveRelationshipsValidator.class)
					.as(CACHE).addComponent(DiagramMustHaveEntitiesValidator.class)
					.addComponent(IAttributeView.class, AttributeView.class);
	}

	public MutablePicoContainer createContainer() {
		return new DefaultPicoContainer();
	}

}
