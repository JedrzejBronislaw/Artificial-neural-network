package application;

import application.SourceData.ColumnTypes;

public class ColumnStatstics {

	private SourceData sourceData;
	private ColumnTypes type;
//	private int columnIndex;
//	private int predictionColumnIndex;
	private double min;
	private double max;
	private double avg;
	private double r;

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
		return r;
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


	private void comuputeStatistics(double[] data) {
		computeMin(data);
		computeMax(data);
		avg = computeAvg(data);
	}

	public void compureR(double[] data1, double[] data2) {
		if (data1.length != data2.length)
			return;

		int n =  data1.length;
		double avg1 = getAvg();
		double avg2 = computeAvg(data2);

		double vectorProduct = 0;
		double length1 = 0;
		double length2 = 0;

		for(int i=0;i<n; i++)
			vectorProduct += (data1[i]-avg1) * (data2[i]-avg2);

		for(int i=0;i<n; i++)
			length1 += Math.pow((data1[i]-avg1),2);
		length1 = Math.sqrt(length1);

		for(int i=0;i<n; i++)
			length2 += Math.pow((data2[i]-avg2),2);
		length2 = Math.sqrt(length2);

		r = vectorProduct/(length1*length2);
	}

	private double computeAvg(double[] data) {
		double sum = 0;

		for(double d : data)
			sum += d;

		return sum/data.length;
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
