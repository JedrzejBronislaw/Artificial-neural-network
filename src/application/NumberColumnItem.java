package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class NumberColumnItem {

	private NumberColumn column;

	private String name;
	private double min;
	private double max;
	private double avg;

	private BooleanProperty selected = new SimpleBooleanProperty(true);
	private BooleanProperty predict = new SimpleBooleanProperty(false);



	public NumberColumnItem(NumberColumn column) {
		this.column = column;

		name = column.getName();
		min = column.getStatistics().getMin();
		max = column.getStatistics().getMax();
		avg = column.getStatistics().getAvg();
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

	public double getAvg() {
		return avg;
	}

	public double getR() {
		return column.getStatistics().getR();
	}



	public boolean isSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		if (!isPredict() || selected)
			this.selected.set(selected);
	}

	public BooleanProperty selectedProperty() {
		return selected;
	}

	public boolean isPredict() {
		return predict.get();
	}

	public void setPredict(boolean predict) {
		if (isSelected() || !predict)
			this.predict.set(predict);
	}

	public BooleanProperty predictProperty() {
		return predict;
	}

	public NumberColumn getNumberColumn() {
		return column;
	}
}
