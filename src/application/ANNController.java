package application;

import java.awt.EventQueue;
import java.util.function.DoubleConsumer;

import main.DzielDane;
import main.Main;
import siec.ANN;
import siec.ANNBledy;
import siec.ANNPreview;
import siec.ANN_bledy;
import siec.ANN_matrix_preview;
import siec.UczenieSieci;

public class ANNController {

	public interface TeachingReportConsumer{
		void accept(TeachingReport report);
	}


	private ANN siec = new ANN(new int[]{4,4,1});
	private float[][] data;
	private NetworkParametersController networkParameters;
//	LossSnapshot lossSnapshotBefore, lossSnapshotAfter;


//	public LossSnapshot getLossSnapshotAfter() {
//		return lossSnapshotAfter;
//	}
//	public LossSnapshot getLossSnapshotBefore() {
//		return lossSnapshotBefore;
//	}


	public ANNController(NetworkParametersController networkParameters, float[][] data) {
		this.networkParameters = networkParameters;
		this.data = data;
	}

	public float ANNResult(float[] wejscie){
		return siec.licz(wejscie);
	}

	public void teach(DoubleConsumer progressUpdate, TeachingReportConsumer afterEvent){
//		TeachingReport report;
		EventQueue.invokeLater(()->{
			TeachingReport report = teaching(progressUpdate);
			if(afterEvent!=null)
				afterEvent.accept(report);
			});
	}

	private TeachingReport teaching(DoubleConsumer progressUpdate) {
		UczenieSieci uczenieSieci = new UczenieSieci(siec);
		ANNPreview preview = new ANN_matrix_preview(siec);
		ANNBledy bledy = new ANN_bledy(siec);
		float[][] dane = data;//main.Main.diamentyDane();
		float[][] daneT, daneU;
		long time;
		TeachingReport report = new TeachingReport();

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
//		lossSnapshotBefore = new LossSnapshot(bledy, daneT, daneU);
		report.lossSnapshotBefore = new LossSnapshot(bledy, daneT, daneU);
		report.weightsBefore = siec.getDetails().getWeightsWagi();

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
		uczenieSieci.ucz(networkParameters.getNumberOfEpoches(), progressUpdate);
		if (progressUpdate != null) progressUpdate.accept(1);
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");

		System.out.println();

		System.out.println("똱edni bl퉐 T: " + bledy.bladSredni(daneT));
		System.out.println("똱edni bl퉐 U: " + bledy.bladSredni(daneU));
//		lossSnapshotAfter = new LossSnapshot(bledy, daneT, daneU);
		report.lossSnapshotAfter = new LossSnapshot(bledy, daneT, daneU);
		report.weightsAfter = siec.getDetails().getWeightsWagi();

		System.out.println();

		preview.showWeights();

		Main.pokazWykres("bledyTemp.txt");
		Main.pokazWykres("wagiTemp.txt");

		report.teachingTime = time;
		return report;
	}
}
