package application;

import java.util.List;

import application.SourceData.ColumnTypes;
import main.CSVFile;

public class ColumnStatstics {

	private SourceData sourceData;
	private ColumnTypes type;
//	private int columnIndex;
//	private int predictionColumnIndex;
	private double min;
	private double max;
	private double avg;

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getAvg() {
		return avg;
	}
//	public ColumnStatstics(SourceData sourceData, int columnIndex, int predictionColumnIndex) {
//		this.sourceData = sourceData;
//		this.columnIndex = columnIndex;
//		this.predictionColumnIndex = predictionColumnIndex;
//		comuputeStatistics();
//	}

	public ColumnStatstics(double[] data) {
		type = ColumnTypes.Number;
		comuputeStatistics(data);
	}

//	private void comuputeStatistics() {
//		if(sourceData.getColumnType(columnIndex) == ColumnTypes.Class){
//
//		} else
//		if(sourceData.getColumnType(columnIndex) == ColumnTypes.Number){
//			computeMin();
//			computeMax();
//		}
//	}

	private void comuputeStatistics(double[] data) {
		computeMin(data);
		computeMax(data);
	}

	private double computeMin(double[] data){
		double min = Double.MAX_VALUE;

//		for(Double d : (List<Double>)sourceData.getColumn(columnIndex))
		for(double d : data)
			if(d<min) min = d;

		this.min = min;
		return min;
	}

	private double computeMax(double[] data){
		double max = Double.MIN_VALUE;

//		for(Double d : (List<Double>)sourceData.getColumn(columnIndex))
		for(double d : data)
			if(d>max) max = d;

		this.max = max;
		return max;
	}

	@Override
	public String toString() {
		return
				"min: " + min + "\n" +
				"max : " + max;
	}
}
