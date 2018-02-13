package main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {

	private String nazwaPliku;
	private String[] kolumny;
	private List<float[]> wartosci;
	private boolean[] maska;


	public CSVFile(String nazwaPliku) {
		this.nazwaPliku = nazwaPliku;
		zaladujWartosci();
	}

	public String[] getNazwyKolumn(){
		return kolumny.clone();
	}
	
	public float[][] getDane(){
		float[][] dane = new float[wartosci.size()][];
		
		dane = wartosci.toArray(dane);
		return dane;
	}
	
	public void pokazKolumny(){
		System.out.println("Kolumny (" + kolumny.length + "):");
		for(String k : kolumny){
			System.out.println(k);
		}
	}

	private void zaladujWartosci() {
		File file = new File(nazwaPliku);
		BufferedInputStream bis = null;
		DataInputStream dis = null;
		wartosci = new ArrayList<>();
		
		try{
			bis = new BufferedInputStream(new FileInputStream(file));
			dis = new DataInputStream(bis);
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		String linia;

		try{
			linia = czytajDo(dis,'\n');
		}catch(IOException e){return;}
//		System.out.println(linia);
		kolumny = linia.split(",");
//		for(String kolumna : kolumny)
//			System.out.println(" -> " + kolumna);

		//List<float[]> wszystkie = new ArrayList<>();

		String[] tempWart;
		float[] rekord;
		int liczbaPol=0;
		int j;

		
		//while(true){
			try {
				linia = czytajDo(dis,'\n');
			} catch (IOException e) {
				return;
			}

			tempWart = linia.split(",");

			maska = new boolean[kolumny.length];
			for(int i=0;i<maska.length;i++)
				maska[i] = true;
			

			liczbaPol = maska.length;
			for(int i=0;i<tempWart.length;i++)
				try {
					Float.parseFloat(tempWart[i]);
				} catch(NumberFormatException e)
				{
					maska[i] = false;
					liczbaPol--;
				}
			
			//nowe kolumny
			String[] noweKolumny = new String[liczbaPol];
			j=0;
			for(int i=0; i<kolumny.length; i++)
				if (maska[i])
					noweKolumny[j++] = kolumny[i];
			kolumny = noweKolumny;
			
			//wpisywanie piewrszego rekordu
			rekord = new float[liczbaPol];
			j=0;
			for(int i=0; i<tempWart.length;i++)
				if (maska[i])
					rekord[j++] = Float.parseFloat(tempWart[i]);

			wartosci.add(rekord);
		//}

		//nastpne rekordy
		while(true){
			try {
				linia = czytajDo(dis,'\n');
			} catch (IOException e) {
				break;
			}

			tempWart = linia.split(",");
			rekord = new float[tempWart.length];
			
			rekord = new float[liczbaPol];
			j=0;
			for(int i=0; i<tempWart.length;i++)
				if (maska[i])
					rekord[j++] = Float.parseFloat(tempWart[i]);
			
			wartosci.add(rekord);
		}

	}

	private String czytajDo(DataInputStream dis, char koniec) throws IOException {
		StringBuffer sb = new StringBuffer();
		byte c;

		while(true) {
			c = dis.readByte();
			
			if (c == koniec)
				break;
			else
				sb.append((char)c);
		};

		return sb.toString();
	}

	public void usunKolumne(int k) {
		int liczbaKolumn = kolumny.length;
		String[] noweKolumny = new String[liczbaKolumn-1];
		float[] nowyRekord;
		int ii;

		ii=0;
		for(int i=0;i<kolumny.length;i++)
			if(i!=k)
				noweKolumny[ii++] = kolumny[i];
		kolumny = noweKolumny;
		
		for(int i=0;i<wartosci.size();i++){
			ii=0;
			nowyRekord = new float[liczbaKolumn-1];
			for(int j=0;j<liczbaKolumn;j++)
				if(j!=k)
					nowyRekord[ii++] = wartosci.get(i)[j];

			wartosci.set(i, nowyRekord);
		}
			
	}

	public void setKolumnaWynikow(int k) {
		int liczbaKolumn = kolumny.length;
		String[] noweKolumny = new String[liczbaKolumn];
		float[] nowyRekord;
		int ii;

		ii=0;
		for(int i=0;i<kolumny.length;i++)
			if(i!=k)
				noweKolumny[ii++] = kolumny[i];
		noweKolumny[liczbaKolumn-1] = kolumny[k];
		kolumny = noweKolumny;	
		
		for(int i=0;i<wartosci.size();i++){
			ii=0;
			nowyRekord = new float[liczbaKolumn];
			for(int j=0;j<kolumny.length;j++)
				if(j!=k)
					nowyRekord[ii++] = wartosci.get(i)[j];

			nowyRekord[liczbaKolumn-1] = wartosci.get(i)[k];
			wartosci.set(i, nowyRekord);
		}
	}

}
