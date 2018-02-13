package siec;

public class ANN {

	private ANNDetails details;


	public ANN(int[] architecture) {
		details = new ANN_matrix(architecture);
	}

	public ANN(ANNDetails details) {
		this.details = details;
	}

	public void setDetails(ANNDetails details) {
		this.details = details;
	}
	public ANNDetails getDetails() {
		return details;
	}

	public float licz(float[] wejscie){
		return details.licz(wejscie);
	}

	//tutaj w przyszlosci mogobyby coœ na wzór kreatora
}
