package application;

import siec.ANNBledy;

public class LossSnapshot {

	private double avgLossT;
	private double avgLossL;

	public LossSnapshot(ANNBledy bledy, float[][] dataT, float[][] dataL) {
		avgLossL = bledy.bladSredni(dataL);
		avgLossT = bledy.bladSredni(dataT);
	}

	public double getAvgLossT() {
		return avgLossT;
	}

	public double getAvgLossL() {
		return avgLossL;
	}
}
