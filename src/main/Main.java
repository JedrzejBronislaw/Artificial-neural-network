package main;

import java.awt.EventQueue;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import siec.ANN;
import siec.ANNBledy;
import siec.ANNDetails;
import siec.ANNFile;
import siec.ANNPreview;
import siec.ANN_bledy;
import siec.ANN_matrix;
import siec.ANN_matrix_preview;
import siec.UczenieSieci;
import test.ParametryUczenia;
import test.ParametryUczenia.Tasowanie;
import test.TestUczenia;
import test.TestUczenia.Wynik;
import wykres.OknoWykresu;

public class Main {

//	static OknoWykresu oknoWykresu;

	public static void main(String[] args) {


		naglowek();

		diamenty();


//		new Oko();
//		test1_uczenieJednymSposobem();
//		testWieluMetodUczenia();
//		porownanieCzasuZapisuDoPliku();
//		testZapisuWag();
//		naukaSieciISkorzystanieZNiej();
//		testZakresowWejsc();

	}


	private static void diamenty() {
		ANN siec = new ANN(new int[]{4,4,1});
		UczenieSieci uczenieSieci = new UczenieSieci(siec);
		ANNBledy bledy = new ANN_bledy(siec);
		float[][] dane = diamentyDane();
		float[][] daneT, daneU;
		long time;

		DzielDane dzielDane = new DzielDane(dane, 0.3f);
		daneT = dzielDane.getDaneT();
		daneU = dzielDane.getDaneU();

		uczenieSieci.setPrzykladyTestowe(daneT);
		uczenieSieci.setPrzykladyUczace(daneU);
		uczenieSieci.setWspolczynnykUczenia(0.1f);
		uczenieSieci.setTasowanie(true);
		uczenieSieci.setZapisDoPliku(true);
		uczenieSieci.setZapisDoPlikuWag(true);

		System.out.println("Œredni bl¹d T: " + bledy.bladSredni(daneT));
		System.out.println("Œredni bl¹d U: " + bledy.bladSredni(daneU));

		System.out.println();

		System.out.print("Uczenie... ");
		time = System.nanoTime();
//		uczenieSieci.uczWstepnie(10,0.01f);
//		uczenieSieci.uczWstepnie(25,0.001f);
//		uczenieSieci.uczWstepnie(25,0.0001f);
//		uczenieSieci.uczWstepnie(25,0.00001f);
//		uczenieSieci.uczWstepnie(25,0.000001f);
//		uczenieSieci.uczWstepnie(25,0.0000001f);
//		uczenieSieci.uczWstepnie(25,0.00000001f);
		uczenieSieci.ucz(10);
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");

		System.out.println();

		System.out.println("Œredni bl¹d T: " + bledy.bladSredni(daneT));
		System.out.println("Œredni bl¹d U: " + bledy.bladSredni(daneU));

		System.out.println();


		pokazWykres("bledyTemp.txt");
		pokazWykres("wagiTemp.txt");

		float[] wejscie = new float[4];
//		DataInputStream scanner = new DataInputStream(System.in);
		Scanner s = new Scanner(System.in);

		while(true){
			System.out.println("Podaj dane: ");
			try {
				System.out.print("   karaty: ");
//				scanner.readUTF();
//				s.nextFloat()
				wejscie[0] = s.nextFloat();//Float.parseFloat(scanner.readUTF());
				System.out.print("   x: ");
				wejscie[1] = s.nextFloat();//scanner.readFloat();
				System.out.print("   y: ");
				wejscie[2] = s.nextFloat();//scanner.readFloat();
				System.out.print("   z: ");
				wejscie[3] = s.nextFloat();//scanner.readFloat();
			} catch (InputMismatchException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				s.next();
				continue;
			}

			System.out.println("karaty: " + wejscie[0]);
			System.out.println("     x: " + wejscie[1]);
			System.out.println("     y: " + wejscie[2]);
			System.out.println("     z: " + wejscie[3]);
			pokazWynik(siec.licz(wejscie));
		}

//		s.close();
	}

	public static float[][] diamentyDane(){
		long time;

		System.out.println(" * DIAMENTY * ");

		System.out.print("Wczytywanie danych... ");
		time = System.nanoTime();
		CSVFile diamonds = new CSVFile("data\\diamonds.csv");
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");


		System.out.print("Porzadkowanie kolumn... ");
		time = System.nanoTime();
		diamonds.usunKolumne(1);
		diamonds.usunKolumne(1);
		diamonds.setKolumnaWynikow(1);
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");

//		diamonds.pokazKolumny();
//		System.out.println();
//
		float[][] dane = diamonds.getDane();
//
//		for (int i=0;i<10;i++){
//			for(float f : dane[i])
//				System.out.print(f + " | ");
//			System.out.println();
//		}

		return dane;
	}


	public static void pokazWykres(String nazwaPliku) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				@SuppressWarnings("unused")
				OknoWykresu oknoWykresu = new OknoWykresu(nazwaPliku);
			}
		});
	}


	private static void pokazWynik(float wynik){
		System.out.println(" --------------");
		System.out.println(" -> Wynik: " + wynik);
		System.out.println(" --------------");
		System.out.println();

	}

	private static void naglowek() {
		System.out.println("Sieæ Neuronowa");
		System.out.println("Jêdrzej Kruczek");
		System.out.println("7 grudnia 2017");
		System.out.println();
	}

	private static void testZakresowWejsc(){
		ANN siec = new ANN(new int[] {2,1});
		ANNPreview podglad = new ANN_matrix_preview(siec);
		float[] wejscie = new float[]{15, 104};
		float[] zakresy = new float[]{20, 200};
		float wynik;

		wynik = siec.licz(wejscie);

		pokazWynik(wynik);

		System.out.println("Ustawianie zakresów wejœæ...\n");
		siec.getDetails().setZakresyWejsc(zakresy);

		wynik = siec.licz(wejscie);

		pokazWynik(wynik);

		podglad.showValues();
		System.out.println("\nZerowanie wartoœci neuronów...\n");
		siec.getDetails().zeruj_neurony();
		podglad.showValues();
	}


	private static void naukaSieciISkorzystanieZNiej(){
		ANN siec = new ANN(new int[]{2,5,1});
		UczenieSieci uczenie = new UczenieSieci(siec);
		float[][] przykladyUczace  = zbiorTestowy_2aPlus3b(1000);
		float[][] przykladyTestowe = zbiorTestowy_2aPlus3b(300);
		float wynik;

		uczenie.setPrzykladyUczace(przykladyUczace);
		uczenie.setPrzykladyTestowe(przykladyTestowe);
		uczenie.setTasowanie(true);

		wynik = siec.licz(new float[] {3,4});
		pokazWynik(wynik);

		System.out.println("Uczenie...\n");
		uczenie.ucz(1000);

		wynik = siec.licz(new float[] {3,4});
		pokazWynik(wynik);
	}

	@SuppressWarnings("unused")
	private static void testZapisuWag(){
		ANN siec = new ANN(new int[]{3,2,1});
		ANNFile annFile = new ANNFile();
		ANNPreview preview = new ANN_matrix_preview(siec);
		float[][] przykladyUczace = zbiorTestowy_2aPlus3b(1000);
		UczenieSieci uczenieSieci = new UczenieSieci(siec);

		preview.showWeights();
		annFile.writeWeights("wagi", siec);

		System.out.println("Uczenie...");
		uczenieSieci.setPrzykladyUczace(przykladyUczace );
		uczenieSieci.ucz(100);

		preview.showWeights();

		System.out.println("Wczytanie...");
		annFile.readWeights("wagi",siec);

		preview.showWeights();
	}

	@SuppressWarnings("unused")
	private static void porownanieCzasuZapisuDoPliku(){
		ANN siec = new ANN(new int[] {2,1});
		float[][] przykladyUczace = zbiorTestowy_2aPlus3b(10000);
		float[][] przykladyTestujace = zbiorTestowy_2aPlus3b(10000);
		ParametryUczenia[] parametry = ParametryUczenia.ilorazKartezjanski(new int[] {100,1000,10000}, new int[] {100,1000,10000}, Tasowanie.NIE);
		TestUczenia tu = new TestUczenia(siec, przykladyUczace, przykladyTestujace);
		Wynik[] wyniki;

		long czas = System.nanoTime();

		wyniki = tu.test(parametry);
		tu.show(wyniki);

		for(ParametryUczenia param : parametry)
			param.zapisDoPliku = true;

		wyniki = tu.test(parametry);
		tu.show(wyniki);

		czas = System.nanoTime() - czas;

		System.out.println("czas: " + (float)(czas/1000000)/1000);
	}

	@SuppressWarnings("unused")
	private static void testWieluMetodUczenia(){
		int maksymalnaLiczbaPrzykladow = 1000;
		ANNDetails siec = new ANN_matrix(new int[] {2,1});
		float[][] zbiorUczacy = zbiorTestowy_2aPlus3b(maksymalnaLiczbaPrzykladow);
		float[][] zbiorTestowy = zbiorTestowy_2aPlus3b(maksymalnaLiczbaPrzykladow);
		TestUczenia test = new TestUczenia(siec, zbiorUczacy, zbiorTestowy);
		ParametryUczenia[] parametry = ParametryUczenia.ilorazKartezjanski(new int[]{100,200,300}, new int[]{100,1000/*,10000*/}, Tasowanie.TAKiNIE);

		for(ParametryUczenia pu : parametry){
			if(pu.liczbaPowtorzen == 1000) pu.opis = "du¿o";
			if(pu.liczbaPowtorzen == 100 && pu.liczbaPrzykladow == 300) pu.zapisDoPliku=true;
		}
		long czas1 = System.nanoTime();
		Wynik[] wyniki = test.test(parametry);
		czas1 = System.nanoTime() - czas1;

		test.show(wyniki);
		System.out.println("czas: " + (float)(czas1/1000000)/1000);
	}

	@SuppressWarnings("unused")
	private static void test1_uczenieJednymSposobem() {
		int liczbaPrzykladow = 1000;
		int liczbaPowtorzen  = 100;//100000;

//		ANNDetails siec = new ANN_matrix(new int[] {2,3,3,3,1});
		ANNDetails siec = new ANN_matrix(new int[] {2,3,1});
		UczenieSieci uczenie = new UczenieSieci(siec);
		ANNPreview podglad = new ANN_matrix_preview(siec);
		ANNBledy bledy = new ANN_bledy(siec);
		float[][] zbiorUczacy = zbiorTestowy_2aPlus3b(liczbaPrzykladow);
		float[][] zbiorTestowy = zbiorTestowy_2aPlus3b(liczbaPrzykladow);

		uczenie.setWspolczynnykUczenia(0.01);
		uczenie.setPrzykladyTestowe(zbiorTestowy);
		uczenie.setPrzykladyUczace(zbiorUczacy);
		uczenie.setTasowanie(true);
		uczenie.setZapisDoPliku(true);
		uczenie.setZapisDoPlikuWag(true);


		podglad.showArchitecture();
		podglad.showWeights();
		podglad.showValues();

		System.out.println("sredni blad U: " + bledy.bladSredni(zbiorUczacy));
		System.out.println("sredni blad T: " + bledy.bladSredni(zbiorTestowy));

		float wynik;
		wynik = siec.licz(new float[]{10f, 10f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{6f, 6f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{0f, 3f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{2f, 8f});
		pokazWynik(wynik);

		System.out.println(" * NAUKA (przyk=" + liczbaPrzykladow + "; powto=" + liczbaPowtorzen + ")");

		long czas = System.nanoTime();
		uczenie.ucz(liczbaPowtorzen);
//		for(int i=0;i<liczbaPowtorzen;i++)
//			siec.ucz(zbiorUczacy);
		czas = System.nanoTime()-czas;
		System.out.println(" * czas = " + (float)(czas/1000000)/1000);
		System.out.println();

		System.out.println("sredni blad U: " + bledy.bladSredni(zbiorUczacy));
		System.out.println("sredni blad T: " + bledy.bladSredni(zbiorTestowy));


		wynik = siec.licz(new float[]{10f, 10f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{6f, 6f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{0f, 3f});
		pokazWynik(wynik);
		wynik = siec.licz(new float[]{2f, 8f});
		pokazWynik(wynik);


		podglad.showArchitecture();
		podglad.showWeights();
		podglad.showValues();


		pokazWykres("bledyTemp.txt");
		pokazWykres("wagiTemp.txt");
	}

	@SuppressWarnings("unused")
	private static float[][] zbiorTestowy_nauka_sen_ocena(){
		return new float[][]{
			{10,10,5},
			{0,0,2},
			{0,10,2},
			{10,0,3},
			{10,5,5},
			{10,1,4},
			{5,1,3},
			{5,10,4},
			{1,5,2},
			{3,5,3}
		};
	}

	@SuppressWarnings("unused")
	private static float[][] zbiorTestowy_2aPlus3b(int n){
		Random r = new Random();
//		final int n = 100000;
		float[][] przyklady = new float[n][3];
		float a,b;

		for(int i=0; i<n; i++){
			a = r.nextFloat() * 10;
			b = r.nextFloat() * 10;
//			a = (float)r.nextInt() / Integer.MAX_VALUE;
//			b = (float)r.nextInt() / Integer.MAX_VALUE;
//			a = r.nextInt();
//			b = r.nextInt();
			przyklady[i][0] = a;
			przyklady[i][1] = b;
			przyklady[i][2] = (2*a) + (3*b);
//			System.out.println(a + ", " + b + " = " + przyklady[i][2]);
		}

		return przyklady;

//		return new float[][]{
//			{10,10,50},
//			{0,0,0},
//			{0,10,30},
//			{10,0,20},
//			{10,5,35},
//			{10,1,23},
//			{5,1,13},
//			{5,10,40},
//			{1,5,17},
//			{3,5,21},
//			{6,6,30}
//		};
	}
}
