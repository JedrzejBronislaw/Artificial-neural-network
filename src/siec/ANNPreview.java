package siec;

public interface ANNPreview {

	void setANN(ANNDetails network);
	ANNDetails getANN();
	void showArchitecture();
	void showValues();
	void showWeights();
}
