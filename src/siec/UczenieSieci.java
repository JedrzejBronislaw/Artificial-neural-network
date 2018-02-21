package siec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.function.DoubleConsumer;

public class UczenieSieci {

	private ANNDetails ANN;
	private float[][] przykladyUczace;
	private float[][] przykladyTestowe;

	private FileWriter fw = null;
	private FileWriter fwWagi = null;
	private int nrEpoki = 0;

	//parameters
	private boolean tasowanie = false;
	private boolean zapisDoPliku = false;
	private boolean zapisDoPlikuWag = false;
//	private String nazwaPliku;

	public UczenieSieci(ANNDetails ANN) {
		this.ANN = ANN;
	}
	public UczenieSieci(ANN ANN) {
		this.ANN = ANN.getDetails();
	}

	public void setTasowanie(boolean tasowanie) {
		this.tasowanie = tasowanie;
	}
	public boolean isTasowanie(){
		return tasowanie;
	}

	public boolean setZapisDoPliku(boolean zapisDoPliku) {
		this.zapisDoPliku = zapisDoPliku;

		if (zapisDoPliku == true){
			return podlaczPlik();
		} else{
			odlaczPlik();
			return true;
		}
	}

	public boolean isZapisDoPliku() {
		return zapisDoPliku;
	}

	public boolean setZapisDoPlikuWag(boolean zapisDoPlikuWag) {
		this.zapisDoPlikuWag = zapisDoPlikuWag;

		if (zapisDoPlikuWag == true){
			return podlaczPlikWag();
		} else{
			odlaczPlikWag();
			return true;
		}
	}

	public boolean isZapisDoPlikuWag() {
		return zapisDoPlikuWag;
	}

	public void setPrzykladyUczace(float[][] przykladyUczace) {
		this.przykladyUczace = przykladyUczace.clone();
	}
	public void setPrzykladyTestowe(float[][] przykladyTestowe) {
		this.przykladyTestowe = przykladyTestowe.clone();
	}


	public long ucz(long liczbaEpok){
		return ucz(liczbaEpok, null);
	}

	public long ucz(long liczbaEpok, DoubleConsumer progressUpdate){
		long czas = System.nanoTime();
		float perc, oldPerc = 0;
		
		if (tasowanie) tasuj(przykladyUczace);

		if (zapisDoPliku)
			zapiszBledyDoPlku();
		if (zapisDoPlikuWag)
			zapiszWagiDoPlku();

		for(long i=0; i<liczbaEpok; i++){
			epokaUczenia();
			if (progressUpdate != null){
				perc = (float)i/liczbaEpok;
				if((perc-oldPerc) >= 0.0001){
					progressUpdate.accept(perc);
					oldPerc = perc;
				}
			}
		}

		czas = System.nanoTime() - czas;

		return czas;
	}

	public long epokaUczenia(){
		long czas = System.nanoTime();

		if (tasowanie) tasuj(przykladyUczace);

		ANN.ucz(przykladyUczace);

		czas = System.nanoTime() - czas;

		nrEpoki++;
		if (zapisDoPliku)
			zapiszBledyDoPlku();
		if (zapisDoPlikuWag)
			zapiszWagiDoPlku();

		return czas;
	}

	private boolean zapiszBledyDoPlku(){
		double bladU = sredniBladDlaZbioru(przykladyUczace);
		double bladT = sredniBladDlaZbioru(przykladyTestowe);

		try {
			fw.write(nrEpoki + ";" + bladU + ";" + bladT + ";\n");
			fw.flush();
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	public double sredniBladDlaZbioru(float[][] zbior){
		ANNBledy bledy = new ANN_bledy(ANN);
		return bledy.bladSredni(zbior);
	}

	private void tasuj(float[][] zbiorTestowy) {
		Random r = new Random();
		int n = zbiorTestowy.length;
		int x;
		float[] temp;

		for(int i=0; i<n-1; i++){
			x = r.nextInt(n-i-1)+i+1;
			temp = zbiorTestowy[x];
			zbiorTestowy[x] = zbiorTestowy[i];
			zbiorTestowy[i] = temp;
		}
	}

	private void odlaczPlik() {
		fw = null;
	}

	private boolean podlaczPlik() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH_mm_ss SSS");
		File file = new File("bledyTemp.txt");//("fun bledu " + sdf.format(date) + ".txt");
//SimpleDateFormat
		nrEpoki = 0;

		try {
			if (fw != null)	fw.close();
		} catch (IOException e1) {}

		try {

			fw = new FileWriter(file);
			fw.write("time: " + new Date().toString() + "\n");

			fw.write("---\n");
			fw.write("epoka;blad U;blad T\n");
			fw.flush();
		} catch (IOException e) {
			fw = null;
			zapisDoPliku = false;
			return false;
		}


		return true;
	}

	private void odlaczPlikWag() {
		fwWagi = null;
	}

	private boolean podlaczPlikWag() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH_mm_ss SSS");
		File file = new File("wagiTemp.txt");//("wagi " + sdf.format(date) + ".txt");
//SimpleDateFormat
		nrEpoki = 0;

		try {
			if (fwWagi != null)	fwWagi.close();
		} catch (IOException e1) {}

		try {

			fwWagi = new FileWriter(file);
			fwWagi.write("time: " + new Date().toString() + "\n");

			fwWagi.write("---\n");
			fwWagi.write("epoka;");

			int[] arch = ANN.getArchitecture();
			for(int i=0; i<arch.length-1; i++)
			{
				for(int j=0; j<arch[i]; j++){
//					fwWagi.write("b "+j+","+j+";");
					for(int k=0; k<arch[i+1]; k++)
						fwWagi.write("w "+i+","+j+","+k+";");
				}
			}
			for(int i=1; i<arch.length; i++)
			{
				for(int j=0; j<arch[i]; j++){
					fwWagi.write("b "+i+","+j+";");//"0;");
				}
			}
			fwWagi.write("\n");
			fwWagi.flush();
		} catch (IOException e) {
			fwWagi = null;
			zapisDoPlikuWag = false;
			return false;
		}


		return true;
	}

	private boolean zapiszWagiDoPlku(){
		Wagi wagi = ANN.getWeightsWagi();

		try {
			fwWagi.write(nrEpoki + ";");

			int[] arch = ANN.getArchitecture();
			for(int i=0; i<arch.length-1; i++)
			{
				for(int j=0; j<arch[i]; j++){
//					fwWagi.write(wagi.getTheta(i+1, k);//"0;");
					for(int k=0; k<arch[i+1]; k++)
						fwWagi.write(wagi.getWeight(i, j, k)+";");//"w"+j+"|"+k+";");
				}
			}

			for(int i=1; i<arch.length; i++)
			{
				for(int j=0; j<arch[i]; j++){
					fwWagi.write((float)wagi.getTheta(i, j)+";");//"0;");
				}
			}


			fwWagi.write("\n");
			fwWagi.flush();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
	public void setWspolczynnykUczenia(double wspUczenia) {
		ANN.setWspUczenia(wspUczenia);
	}
	public void uczWstepnie(int i, float d) {
		float min = ANN.getMinPochFunAktywacji();
		ANN.setMinPochFunAktywacji(d);
		ucz(i);
		ANN.setMinPochFunAktywacji(min);

	}
}
