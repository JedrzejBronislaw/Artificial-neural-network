package siec;

public interface ANNDetails {

	public void build(int[] architecture);

	int[] getArchitecture();

//	void setWeights(float[][][] weights);
//	float[][][] getWeights();


	void zeruj_neurony();
	float licz(float[] wejscie);

	int layerCount();

	int layerSize(int i);

	Wagi getWeightsWagi();

	void setWeightsWagi(Wagi weights);

	/**
	 * Jedno wywolanie tej funkcji to jedna epoka nauk sieci.
	 * @param zbiorTestowy
	 */
	public void ucz(float[][] zbiorTestowy);

	void setZakresyWejsc(float[] zakresy);

	void setWspUczenia(double wspUczenia);

	double getWspUczenia();

	void setMinPochFunAktywacji(float minPochFunAktywacji);

	float getMinPochFunAktywacji();

}
