package test;

public class ParametryUczenia {

	public enum Tasowanie{NIE, TAK, TAKiNIE}

	public int liczbaPrzykladow = 0;
	public int liczbaPowtorzen  = 0;
	public Tasowanie tasowanie = Tasowanie.NIE;
	public boolean zapisDoPliku = false;
	public String opis = "";

	public ParametryUczenia() {}

	public ParametryUczenia(int liczbaPrzykladow, int liczbaPowtorzen, Tasowanie tasowanie) {
		this.liczbaPrzykladow = liczbaPrzykladow;
		this.liczbaPowtorzen = liczbaPowtorzen;
		this.tasowanie = tasowanie;
		if (this.tasowanie == Tasowanie.TAKiNIE)
			this.tasowanie = Tasowanie.TAK;
	}


	public static ParametryUczenia[] ilorazKartezjanski(int[] liczbaPrzykladow, int[] liczbaPowtorzen, Tasowanie tasowanie){
		int nPrzyk = liczbaPrzykladow.length;
		int nPowto = liczbaPowtorzen.length;
		int nTasow = (tasowanie==Tasowanie.TAKiNIE) ? 2 : 1;
		ParametryUczenia[] parametry = new ParametryUczenia[nPrzyk*nPowto*nTasow];
		Tasowanie[] tasVal;
		if (nTasow == 2)
			tasVal = Tasowanie.values();
		else
			tasVal = new Tasowanie[]{tasowanie};

		for(int iTasow=0; iTasow<nTasow; iTasow++)
			for(int iPrzyk=0; iPrzyk<nPrzyk; iPrzyk++)
				for(int iPowto=0; iPowto<nPowto; iPowto++)
					parametry[iTasow*nPrzyk*nPowto + iPrzyk*nPowto + iPowto] = new ParametryUczenia(liczbaPrzykladow[iPrzyk], liczbaPowtorzen[iPowto], tasVal[iTasow]);

		return parametry;
	}
}
