package siec;

public class SiecNeuronowa {

	ANNDetails siec;

	public SiecNeuronowa(ANNDetails siec) {
		//TODO p�niej ten konstruktor usun�c, zamiast niego powinien byc "kreator"
		this.siec = siec;
	}

	public double oblicz(float[] daneWejscowe){
		return siec.licz(daneWejscowe);
	}
}
