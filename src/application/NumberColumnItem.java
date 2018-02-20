package application;

public class NumberColumnItem {

	private NumberColumn column;

	private String name;
	private double min;
	private double max;


	public NumberColumnItem(NumberColumn column) {
		this.column = column;

		name = column.getName();
		min = column.getStatistics().getMin();
		max = column.getStatistics().getMax();
	}
	
	public String getName() {
		return name;
	}
	
	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}
}
