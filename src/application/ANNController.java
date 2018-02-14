package application;

import java.awt.EventQueue;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

import main.DzielDane;
import main.Main;
import siec.ANN;
import siec.ANNBledy;
import siec.ANNPreview;
import siec.ANN_bledy;
import siec.ANN_matrix_preview;
import siec.UczenieSieci;

public class ANNController {

	private ANN siec = new ANN(new int[]{4,4,1});
	private String dataFileName;

	public ANNController(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public float ANNResult(float[] wejscie){
		return siec.licz(wejscie);
	}

	public void uczODiamentach(DoubleConsumer progressUpdate, LongConsumer afterEvent){
		EventQueue.invokeLater(()->{
			long time = diamenty(progressUpdate);
			if(afterEvent!=null)
				afterEvent.accept(time);
			});
	}

	private long diamenty(DoubleConsumer progressUpdate) {
		UczenieSieci uczenieSieci = new UczenieSieci(siec);
		ANNPreview preview = new ANN_matrix_preview(siec);
		ANNBledy bledy = new ANN_bledy(siec);
		float[][] dane = main.Main.diamentyDane();
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

		System.out.println("똱edni bl퉐 T: " + bledy.bladSredni(daneT));
		System.out.println("똱edni bl퉐 U: " + bledy.bladSredni(daneU));

		System.out.println();

		System.out.print("Uczenie... ");
		time = System.nanoTime();
		if (progressUpdate != null) progressUpdate.accept(0);
//		uczenieSieci.uczWstepnie(10,0.01f);
//		uczenieSieci.uczWstepnie(25,0.001f);
//		uczenieSieci.uczWstepnie(25,0.0001f);
//		uczenieSieci.uczWstepnie(25,0.00001f);
//		uczenieSieci.uczWstepnie(25,0.000001f);
//		uczenieSieci.uczWstepnie(25,0.0000001f);
//		uczenieSieci.uczWstepnie(25,0.00000001f);
		uczenieSieci.ucz(10);
		if (progressUpdate != null) progressUpdate.accept(1);
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");

		System.out.println();

		System.out.println("똱edni bl퉐 T: " + bledy.bladSredni(daneT));
		System.out.println("똱edni bl퉐 U: " + bledy.bladSredni(daneU));

		System.out.println();

		preview.showWeights();

		Main.pokazWykres("bledyTemp.txt");
		Main.pokazWykres("wagiTemp.txt");

		return time;
	}
}
