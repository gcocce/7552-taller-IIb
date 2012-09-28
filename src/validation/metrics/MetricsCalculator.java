package validation.metrics;

import validation.metrics.MetricPair.MetricNames;
import infrastructure.IterableExtensions;
import models.Attribute;
import models.Diagram;
import models.Entity;
import models.Hierarchy;
import models.Relationship;

public class MetricsCalculator implements IMetricsCalculator {
	@Override
	public Metrics calculateMetrics(Iterable<Diagram> diagrams) {
		double diagramCount = IterableExtensions.count(diagrams);
		
		double entities = 0;
		double attributesInEntities = 0;
		double entitiesInRelationships = 0;
		double attributesInRelationships = 0;
		double relationships = 0;
		double hierarchies = 0;
		double entitiesInHierarchies = 0;		
		
		for (Diagram diagram : diagrams) {
			for (Entity entity : diagram.getEntities()) {
				attributesInEntities += MetricsCalculator.getAttributeCount(entity.getAttributes());
				entities++;
			}
			
			for (Relationship relationship : diagram.getRelationships()) {
				entitiesInRelationships += IterableExtensions.count(relationship.getRelationshipEntities());
				attributesInRelationships += MetricsCalculator.getAttributeCount(relationship.getAttributes());
				relationships++;
			}
			
			for (Hierarchy hierarchy : diagram.getHierarchies()) {
				entitiesInHierarchies += IterableExtensions.count(hierarchy.getChildren());
				hierarchies++;
			}
		}
		
		Metrics metrics = new Metrics();
		
		metrics.setAttributesPerEntity(new MetricPair(MetricNames.AttributesPerEntity, attributesInEntities / entities));
		metrics.setAttributesPerRelationship(new MetricPair(MetricNames.AttributesPerRelationship, attributesInRelationships / relationships));
		metrics.setEntitiesPerDiagram(new MetricPair(MetricNames.EntitiesPerDiagram, entities / diagramCount));
		metrics.setEntitiesPerHierarchy(new MetricPair(MetricNames.EntitiesPerHierarchy, entitiesInHierarchies / hierarchies));
		metrics.setEntitiesPerRelationship(new MetricPair(MetricNames.EntitiesPerRelationship, entitiesInRelationships / relationships));
		metrics.setHierarchiesPerDiagram(new MetricPair(MetricNames.HierarchiesPerDiagram, hierarchies / diagramCount));
		metrics.setRelationshipsPerDiagram(new MetricPair(MetricNames.RelationshipsPerDiagram, relationships / diagramCount));
	
		double entitiesDeviation = 0;
		double attributesInEntitiesDeviation = 0;
		double entitiesInRelationshipsDeviation = 0;
		double attributesInRelationshipsDeviation = 0;
		double relationshipsDeviation = 0;
		double hierarchiesDeviation = 0;
		double entitiesInHierarchiesDeviation = 0;
		
		for (Diagram diagram : diagrams) {
			entitiesDeviation += calculateDeviation(metrics.getEntitiesPerDiagram().getMean(), IterableExtensions.count(diagram.getEntities()));
			
			for (Entity entity : diagram.getEntities()) {
				double attributeCount = getAttributeCount(entity.getAttributes());
				attributesInEntitiesDeviation += calculateDeviation(metrics.getAttributesPerEntity().getMean(), attributeCount);
			}
			
			relationshipsDeviation += calculateDeviation(metrics.getRelationshipsPerDiagram().getMean(), IterableExtensions.count(diagram.getRelationships()));
			
			for (Relationship relationship : diagram.getRelationships()) {
				double entitiesCount = IterableExtensions.count(relationship.getRelationshipEntities());
				double attributeCount = getAttributeCount(relationship.getAttributes());
				attributesInRelationshipsDeviation += calculateDeviation(metrics.getAttributesPerRelationship().getMean(), attributeCount);
				entitiesInRelationshipsDeviation += calculateDeviation(metrics.getRelationshipsPerDiagram().getMean(), entitiesCount);
			}
			
			hierarchiesDeviation += calculateDeviation(metrics.getRelationshipsPerDiagram().getMean(), IterableExtensions.count(diagram.getHierarchies()));
			
			for (Hierarchy hierarchy : diagram.getHierarchies()) {
				entitiesInHierarchiesDeviation += calculateDeviation(metrics.getEntitiesPerHierarchy().getMean(), IterableExtensions.count(hierarchy.getChildren()));
			}
		}

		entitiesDeviation = Math.sqrt(entitiesDeviation / entities);
		attributesInEntitiesDeviation = Math.sqrt(attributesInEntitiesDeviation / entities);
		entitiesInRelationshipsDeviation = Math.sqrt(entitiesInRelationshipsDeviation / relationships);
		attributesInRelationshipsDeviation = Math.sqrt(attributesInRelationshipsDeviation / relationships);
		relationshipsDeviation = Math.sqrt(relationshipsDeviation / relationships);
		entitiesInHierarchiesDeviation = Math.sqrt(entitiesInHierarchiesDeviation / hierarchies);
		hierarchiesDeviation = Math.sqrt(hierarchiesDeviation / hierarchies);
		
		metrics.getAttributesPerEntity().setStandardDeviation(attributesInEntitiesDeviation);
		metrics.getAttributesPerRelationship().setStandardDeviation(attributesInRelationshipsDeviation);
		metrics.getEntitiesPerDiagram().setStandardDeviation(entitiesDeviation);
		metrics.getEntitiesPerHierarchy().setStandardDeviation(entitiesInHierarchiesDeviation);
		metrics.getEntitiesPerRelationship().setStandardDeviation(entitiesInRelationshipsDeviation);
		metrics.getHierarchiesPerDiagram().setStandardDeviation(hierarchiesDeviation);
		metrics.getRelationshipsPerDiagram().setStandardDeviation(relationshipsDeviation);
		
		return metrics;
	}
	
	public static int getAttributeCount(Iterable<Attribute> attributes){
		int count = 0;
		
		for (Attribute attribute : attributes) {
			count++;
			
			count += getAttributeCount(attribute.getAttributes());
		}
		
		return count;
	}
	
	private static double calculateDeviation(double mean, double x){
		return Math.pow(mean - x, 2);
	}
}
