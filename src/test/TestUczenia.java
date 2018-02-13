package test;

import java.util.Arrays;
import java.util.Comparator;

import siec.ANN;
import siec.ANNBledy;
import siec.ANNDetails;
import siec.ANN_bledy;
import siec.UczenieSieci;
import siec.Wagi;
import test.ParametryUczenia.Tasowanie;

public class TestUczenia {


	public class Wynik{
		ParametryUczenia parametry;
		double bladU;
		double bladT;
		float czas;

		boolean sukces = false;
		public String opis = "";
	}

	ANNDetails ANN;
	UczenieSieci uczenieSieci;
	float[][] globPrzykladyUczace;
	float[][] globPrzykladyTestujace;
	Wagi poczatkoweWagi;
	ANNBledy bledy;

	public TestUczenia(ANNDetails siec, float[][] przykladyUczace, float[][] przykladyTestujace){
		constructor(siec, przykladyUczace, przykladyTestujace);
	}
	public TestUczenia(ANN siec, float[][] przykladyUczace, float[][] przykladyTestujace) {
		constructor(siec.getDetails(), przykladyUczace, przykladyTestujace);
	}
	public void constructor(ANNDetails siec, float[][] przykladyUczace, float[][] przykladyTestujace){
		ANN = siec;
		uczenieSieci = new UczenieSieci(siec);
		bledy = new ANN_bledy(siec);
		this.globPrzykladyUczace = przykladyUczace;
		this.globPrzykladyTestujace = przykladyTestujace;
		poczatkoweWagi = ANN.getWeightsWagi();
	}


	public Wynik[] test(ParametryUczenia[] parametry){
		float[][] przykladyUczace;
		Wynik[] wyniki = new Wynik[parametry.length];
		Wynik wynik;
		long czas;

		sortujTesty(parametry);


		ParametryUczenia param;
		int liczbaPowtorzen;
		boolean kontynuacja = false;

		for(int p=0; p<parametry.length; p++){
			param = parametry[p];

			przykladyUczace = new float[param.liczbaPrzykladow][];
			if(globPrzykladyUczace.length < param.liczbaPrzykladow){
				wynik = new Wynik();
				wynik.parametry = param;
				wynik.sukces = false;
				wynik.opis = "brak przykladow uczacych";
				wyniki[p] = wynik;
				continue;
			}

			for (int i=0; i<param.liczbaPrzykladow; i++)
				przykladyUczace[i] = globPrzykladyUczace[i];


			liczbaPowtorzen = param.liczbaPowtorzen;
			kontynuacja = (p != 0 && parametry[p-1].liczbaPrzykladow == param.liczbaPrzykladow);
			if (kontynuacja)
				liczbaPowtorzen -= parametry[p-1].liczbaPowtorzen;
			else
				ANN.setWeightsWagi(poczatkoweWagi);

			uczenieSieci.setTasowanie(param.tasowanie==Tasowanie.TAK || param.tasowanie==Tasowanie.TAKiNIE);
			uczenieSieci.setPrzykladyUczace(przykladyUczace);
			uczenieSieci.setPrzykladyTestowe(globPrzykladyTestujace);
			uczenieSieci.setZapisDoPliku(param.zapisDoPliku);


			czas = System.nanoTime();
			for(int i=0; i<liczbaPowtorzen; i++)
				uczenieSieci.epokaUczenia();
//				ANN.ucz(przykladyUczace);
			czas = System.nanoTime() - czas;


			wynik = new Wynik();
			wynik.sukces = true;
			wynik.parametry = param;
			wynik.bladT = bledy.bladSredni(globPrzykladyTestujace);
			wynik.bladU = bledy.bladSredni(przykladyUczace);
			wynik.czas = (float)(czas/1000000)/1000;
			if (kontynuacja) wynik.czas += wyniki[p-1].czas;
			wyniki[p] = wynik;
		}


		return wyniki;
	}

	private void sortujTesty(ParametryUczenia[] parametry) {

		Comparator<ParametryUczenia> comparator = new Comparator<ParametryUczenia>() {

			@Override
			public int compare(ParametryUczenia p1, ParametryUczenia p2) {
				int x = p1.tasowanie.ordinal() - p2.tasowanie.ordinal();
				if (x == 0)
					x = p1.liczbaPrzykladow - p2.liczbaPrzykladow;
				if (x == 0)
					x = p1.liczbaPowtorzen - p2.liczbaPowtorzen;
				return x;
			}
		};
		Arrays.sort(parametry, comparator);
	}

	public void show(Wynik[] wyniki){
		final String separator = " - ";

		System.out.println(
				"s" + separator +
				"tas" + separator +
				"zapis" + separator +
				"przyk" + separator +
				"powto" + separator +
				"blad U" + separator +
				"blad T" + separator +
				"czas [s]" + separator +
				"opis");

		for (Wynik wynik : wyniki)
			System.out.println(
					(wynik.sukces ? "+" : "-") + separator +
					wynik.parametry.tasowanie + separator +
					wynik.parametry.zapisDoPliku + separator +
					wynik.parametry.liczbaPrzykladow + separator +
					wynik.parametry.liczbaPowtorzen + separator +
					wynik.bladU + separator +
					wynik.bladT + separator +
					wynik.czas + separator +
					wynik.parametry.opis + separator +
					(!wynik.opis.isEmpty() ? "(" + wynik.opis + ")" : ""));
	}
}
