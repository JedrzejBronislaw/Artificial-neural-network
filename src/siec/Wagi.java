package siec;

public class Wagi {

	private int[] architecture;
	private float[][][] wagi;
	private double[][] thetas;


	public Wagi(int[] architecture) {
		this.architecture = architecture;
		int layerCount = architecture.length;

		wagi = new float[layerCount-1][][];

		for (int i=0; i<layerCount-1; i++)
			wagi[i] = new float[architecture[i]][architecture[i+1]];

		thetas = new double[layerCount][];
		for (int i=0; i<layerCount; i++)
			thetas[i] = new double[architecture[i]];

	}

	/**
	 *
	 * @param layerOut warstwa, z której krawedŸ wychodzi
	 * @param neuronOut	neuron, z którego krawedŸ wychodzi
	 * @param neuronIn	neuron, do którego krawedŸ wchodzi
	 * @return
	 */
	public float getWeight(int layerOut, int neuronOut, int neuronIn){
		return wagi[layerOut][neuronOut][neuronIn];
	}
	public void setWeight(int layerOut, int neuronOut, int neuronIn, float value){
		wagi[layerOut][neuronOut][neuronIn] = value;
	}

	public void setLayerWeights(int layerOut, float[][] values){
		wagi[layerOut] = values;
	}
	public float[][] getLayerWeights(int layerOut){
		return wagi[layerOut];
	}

	public void show() {
		float[][] wagiDanejWarstwy;
		int layerCount = architecture.length;

		System.out.println("Liczba warstw: " + layerCount);
		for (int i=0; i<layerCount-1; i++){
			System.out.println("\tWarstwa " + i);
			wagiDanejWarstwy = wagi[i];
			for (float[] wagiNeuronu : wagiDanejWarstwy){
				for(float waga : wagiNeuronu)
					System.out.println(waga);
				System.out.println();
			}
		}

	}

	/**
	 * @return liczba warstw sieci, a nie liczba macierzy wag
	 */
	public int annLayerCount() {
		return architecture.length;
	}

	public int annLayerSize(int i) {
		return architecture[i];
	}

	public void setTheta(int i, int j, double theta) {
		thetas[i][j] = theta;
	}
	public double getTheta(int i, int j) {
		return thetas[i][j];
	}
}
