package application;

import main.CSVFile;

public class NumberColumn {

	private String name;
	private ColumnStatstics statstics;
	private double[] data;

	public NumberColumn(CSVFile csvFile, int columnNumber) {
		int n = csvFile.liczbaRekordow();
		
		name = csvFile.getNazwyKolumn()[columnNumber];
		
		data = new double[n];
		for(int i=0; i<n; i++)
			data[i] = Double.parseDouble(csvFile.getValue(i, columnNumber));
	}

	public double value(int index){
		return data[index];
	}

//	public double[] getData(){
//
//	}

	public String getName() {
		return name;
	}
	
	public int size(){
		return data.length;
	}

	public void setStatistics() {
		statstics = new ColumnStatstics(data);
	}

	public ColumnStatstics getStatistics() {
		return statstics;
	}
}
