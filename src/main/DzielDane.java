package main;

public class DzielDane {

	private float[][] dane;
	private float procTestowych = 0.3f;
	private float[][] daneT = null;
	private float[][] daneU = null;


	public DzielDane(float[][] dane, float procTestowych) {
		this.dane = dane;
		this.procTestowych = procTestowych;

		dziel();
	}


	public float[][] getDaneT() {
		return daneT;
	}

	public float[][] getDaneU() {
		return daneU;
	}

	private void dziel() {
		int liczRekordowTestowych = (int) (dane.length*procTestowych);
		daneT = new float[liczRekordowTestowych][];
		daneU = new float[dane.length - liczRekordowTestowych][];

		for(int i=0;i<liczRekordowTestowych;i++)
			daneT[i] = dane[i];

		int j=0;
		for(int i=liczRekordowTestowych;i<dane.length;i++)
			daneU[j++] = dane[i];
	}
}
