package validation.metrics;

public class MetricPair {
	
	public static class MetricNames{
		public static final String HierarchiesPerDiagram = "HierarchiesPerDiagram";
		public static final String EntitiesPerDiagram = "EntitiesPerDiagram";
		public static final String RelationshipsPerDiagram = "RelationshipsPerDiagram";
		public static final String EntitiesPerHierarchy = "EntitiesPerHierarchy";
		public static final String EntitiesPerRelationship = "EntitiesPerRelationship";
		public static final String AttributesPerEntity = "AttributesPerEntity";
		public static final String AttributesPerRelationship = "AttributesPerRelationship";
	}
	
	private double mean;
	private double standardDeviation;
	private String metricName;
	
	public MetricPair(String metricName, double mean){
		this(metricName, mean, 0);
	}
	
	public MetricPair(String metricName, double mean, double deviation){
		this.metricName = metricName;
		this.mean = Double.isNaN(mean) ? 0 : mean;
		this.standardDeviation = Double.isNaN(deviation) ? 0 : deviation;
	}
	
	public double getStandardDeviation() {
		return standardDeviation;
	}
	
	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = Double.isNaN(standardDeviation) ? 0 : standardDeviation;
	}
	
	public double getMean() {
		return mean;
	}

	public boolean isInRange(double value, int deviationsCount){
		return value >= this.mean - deviationsCount * this.standardDeviation 
				&& value <= this.mean + deviationsCount * this.standardDeviation;
	}

	public String getName() {
		return metricName;
	}
}
