package siec;

public class ANN_bledy implements ANNBledy{

	private ANNDetails ANN;

	public ANN_bledy(ANN network) {
		this.ANN = network.getDetails();
	}
	public ANN_bledy(ANNDetails network) {
		this.ANN = network;
	}

	@Override
	public double bladSredni(float[][] zbiorTestowy){
		double L = 0;

		for(float[] przyklad : zbiorTestowy)
			L += blad(przyklad);

		L /= zbiorTestowy.length;

//		System.out.println("L=" + L);
		return L;
	}

	@Override
	public double blad(float[] przykladTestowy){
		float wynikUzyskany;
		float wynikOczekiwany;
		double roznica;//, L = 0;

		wynikOczekiwany = przykladTestowy[przykladTestowy.length-1];
		wynikUzyskany = ANN.licz(przykladTestowy);

		roznica = Math.pow((wynikOczekiwany-wynikUzyskany),2)/2;
//		roznica = wynikOczekiwany-wynikUzyskany;

		return roznica;
	}
	
	@Override
	public double pochBladu(float[] przykladTestowy) {
		float wynikUzyskany;
		float wynikOczekiwany;
		double roznica;//, L = 0;

		wynikOczekiwany = przykladTestowy[przykladTestowy.length-1];
		wynikUzyskany = ANN.licz(przykladTestowy);

		roznica = wynikUzyskany-wynikOczekiwany;
//		roznica = wynikOczekiwany-wynikUzyskany;

		return roznica;
	}
}
