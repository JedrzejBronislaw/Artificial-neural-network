package siec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.directory.InvalidAttributeValueException;

public class ANN_matrix implements ANNDetails {

	class Neuron{
		double in=0, out=0, d=0, theta=0;
	}

	private /*static*/ /*final*/ double wspUczenia = 0.01;//0.01;
	private int[] architecture;
	List<float[][]> wagi;
	float[] zakresyWejsc;
	Neuron[][] neurony;
	ANNBledy bledy = new ANN_bledy(this);
	private float minPochFunAktywacji = 0;

	@Override
	public void setMinPochFunAktywacji(float minPochFunAktywacji) {
		this.minPochFunAktywacji = minPochFunAktywacji;
	}
	@Override
	public float getMinPochFunAktywacji() {
		return minPochFunAktywacji;
	}

	@Override
	public void setWspUczenia(double wspUczenia) {
		this.wspUczenia = wspUczenia;
	}
	@Override
	public double getWspUczenia() {
		return wspUczenia;
	}

	public ANN_matrix(int[] architecture) {
		build(architecture);
	}

	@Override
	public void build(int[] architecture) {
		this.architecture = architecture;
		wagi = nowaListaWag();

		neurony = new Neuron[architecture.length][];
		zakresyWejsc = new float[architecture[0]];
		for (int i=0; i<zakresyWejsc.length; i++)
			zakresyWejsc[i] = 1;

		for (int i=0;i<architecture.length; i++){
			neurony[i] = new Neuron[architecture[i]];
			for(int j=0; j<architecture[i]; j++)
				neurony[i][j] = new Neuron();
		}
		losujWagi();
//		generujWagiPoczatkowe();
	}

	private List<float[][]> nowaListaWag() {
		List<float[][]> nListaWag = new ArrayList<>();
		float[][] kraw;

		for (int i=0;i<architecture.length-1; i++){
			kraw = new float[architecture[i]][architecture[i+1]];
			nListaWag.add(kraw);
		}

		return nListaWag;
	}

//	private void generujWagiPoczatkowe() {
//		float[][] w;
////		Random r = new Random();
//
//		for(int i=0; i<wagi.size(); i++){
//			w = wagi.get(i);
//			for (int j=0; j<w.length; j++)
//				for(int k=0; k<w[j].length; k++)
//					w[j][k] = 1f/w[j].length;//r.nextFloat();
//		}
//	}

	private void losujWagi() {
		float[][] w;
		Random r = new Random();

		for(int i=0; i<wagi.size(); i++){
			w = wagi.get(i);
			for (int j=0; j<w.length; j++)
				for(int k=0; k<w[j].length; k++)
					w[j][k] = r.nextFloat()/10;
		}
	}

//	@Override
//	public void setWeights(float[][][] weights) {
//		wagi = new ArrayList<>();
//		float[][] warstwa;
//		int i,j;
//
//		i = j = 0;
//		for(float[][] layer : weights)
//		{
//			i = 0;
//			warstwa = new float[layerSize(i)][layerSize(i+1)];
//			for(float[] neuron : layer)
//			{
//				j = 0;
//				for(float weight : neuron)
//					warstwa[i][j++] = weight;
//				i++;
//			}
//			wagi.add(warstwa);
//		}
//	}

	@Override
	public void setWeightsWagi(Wagi weights) {
		wagi = new ArrayList<>();

		for(int l=0; l<weights.annLayerCount()-1; l++)
			wagi.add(weights.getLayerWeights(l));
	}

	@Override
	public void setZakresyWejsc(float[] zakresy){
		for (int i=0; i<zakresyWejsc.length; i++)
			zakresyWejsc[i] = 1;

		for (int i=0; i<zakresy.length && i<zakresyWejsc.length; i++)
			zakresyWejsc[i] = zakresy[i];
	}

//	@Override
//	public float[][][] getWeights() {
//		int layerCount = layerCount();
//		float[][][] outcome = new float[layerCount][][];
//
//		for(int i=0; i<layerCount-1; i++){
//			outcome[i] = new float[layerSize(i)][layerSize(i+1)];
//			for (int j=0; j<layerSize(i); j++)
//				for (int k=0; k<layerSize(i+1); k++)
//				outcome[i][j][k] = wagi.get(i)[j][k];
//		}
//		return outcome;
//	}

	@Override
	public Wagi getWeightsWagi() {
		Wagi outcome = new Wagi(architecture);
		int layerCount = layerCount();
		float[][] layerWeigths;

		for(int i=0; i<layerCount-1; i++){
			layerWeigths = new float[layerSize(i)][layerSize(i+1)];
			for (int j=0; j<layerSize(i); j++)//{
//				outcome.setTheta(i+1,j,neurony[i+1][j].theta);
				for (int k=0; k<layerSize(i+1); k++)
					layerWeigths[j][k] = wagi.get(i)[j][k];
//			}
			outcome.setLayerWeights(i, layerWeigths);
		}
		for(int i=0; i<layerCount; i++)//{
//			layerWeigths = new float[layerSize(i)][layerSize(i+1)];
			for (int j=0; j<layerSize(i); j++){
				outcome.setTheta(i,j,neurony[i][j].theta);
//				for (int k=0; k<layerSize(i+1); k++)
//					layerWeigths[j][k] = wagi.get(i)[j][k];
//			}
//			outcome.setLayerWeights(i, layerWeigths);
		}
		return outcome;
	}

	@Override
	public int layerSize(int i) {
		if (i<0 || i>architecture.length-1)
			return 0;
		return architecture[i];
	}

	/**
	 * Number of layer (input layer, hidden layers, output layer)
	 */
	@Override
	public int layerCount() {
		return architecture.length;
	}



	@Override
	public void zeruj_neurony() {
		for(Neuron[] warstwa : neurony)
			for(Neuron neuron : warstwa)
				neuron.in = neuron.out = neuron.d = 0;
	}

	@Override
	public float licz(float[] wejscie) {

		float[] input = new float[architecture[0]];
//		for (int i=0; i<wejscie.length && i<architecture[0]; i++){
//			neurony[0][i].in = input[i] = wejscie[i] / zakresyWejsc[i];
//		}
//
//		input = funAktywacji(input);
//
//		for (int i=0;i<input.length;i++)
//			neurony[0][i].out = input[i];

		for (int i=0; i<wejscie.length && i<architecture[0]; i++){
			neurony[0][i].in = neurony[0][i].out = input[i] = wejscie[i];
		}

		float[] warstwa1 = input;
		float[] warstwa2 = null;
//		double theta;
		for(int i=0; i<architecture.length-1; i++){
			try{
				warstwa2 = mnozenieMacierzy(wagi.get(i), warstwa1);

				for (int j=0;j<warstwa2.length;j++)
					warstwa2[j] += /*(-1) **/ neurony[i+1][j].theta;

				for (int j=0;j<warstwa2.length;j++)
					neurony[i+1][j].in = warstwa2[j];

				warstwa2 = funAktywacji(warstwa2);

				for (int j=0;j<warstwa2.length;j++)
					neurony[i+1][j].out = warstwa2[j];
			}catch(InvalidAttributeValueException e){
				System.out.println("Error: ");
				e.printStackTrace();
				return 0;
			}

			warstwa1 = warstwa2;
		}

//		return warstwa2[0]*5;
		return (float) neurony[neurony.length-1][0].in;
	}

	private float[] funAktywacji(float[] warstwa){
		//sigmoid
		//tangens hiperboliczny
		for(int i=0;i<warstwa.length; i++)
			warstwa[i] = (float) Math.tanh(warstwa[i]);
		return warstwa;
	}

	private double pochFunAktywacji(double x){
		//dla sigmoida -> sigmoid(1-sigmoid)
		//dla tgh      -> 1-tgh^2(x)
		double v = 1-(Math.tanh(x)*Math.tanh(x));
//		return Math.max(v, 0.00001);
		return v;
//		return 1;
	}

	/**
	 * Mno¿enie macierzy wag przez wektor neuronów (z jednej warstwy). Wynikiem jest wektor (kolejna warstwa) neuronów;
	 * @param wagi
	 * @param warstwaA
	 * @return warstwaB
	 * @throws InvalidAttributeValueException gdy rozmiar macierzy wag nie zgadza sie z rozmiarem warstwy A.
	 */
	private float[] mnozenieMacierzy(float[][] wagi, float[] warstwaA) throws InvalidAttributeValueException{
		int rozm_wagA = wagi.length;
		int rozm_wagB = wagi[0].length;
		int rozm_warstwyA = warstwaA.length;
//		int by = B_warstwa[0].length;


		if (!(rozm_wagA == rozm_warstwyA))
			throw new InvalidAttributeValueException();

		float[] warstwaB = new float[rozm_wagB];

		for(int b=0; b<rozm_wagB; b++)
//			for(int j=0; j<bx; j++)
				for (int a=0; a<rozm_wagA; a++)
					warstwaB[b] += wagi[a][b] * warstwaA[a];

		return warstwaB;
	}

	@Override
	public void ucz(float[][] zbiorTestowy) {
		for(float[] test : zbiorTestowy)
			ucz(test);
	}


	/**
	 * Uczenie na podstawie jednego przykadu
	 * @param przykladTestowy
	 */
	public void ucz(float[] przykladTestowy) {
//		uczWstepnie(przykladTestowy, 0);
//	}
//	public void uczWstepnie(float[] przykladTestowy, float minPochFunAktywacji) {

		double blad = bledy.pochBladu(przykladTestowy);
		Wagi stareWagi = getWeightsWagi();



		//------------------------
		for (int warstwa=layerCount()-1; warstwa>0; warstwa--){
			for (int neuronDo=0; neuronDo<layerSize(warstwa); neuronDo++){
				double bladNeurona = 1;//pochFunAktywacji(neurony[warstwa][neuronDo].in);
				double pochFun = Math.max(pochFunAktywacji(neurony[warstwa][neuronDo].in), minPochFunAktywacji);
				if(warstwa == layerCount()-1)
					bladNeurona *= blad;
				else{
					double propadowaneBledy = 0;
					for (int i=0; i<layerSize(warstwa+1); i++)
						propadowaneBledy += neurony[warstwa+1][i].d*stareWagi.getWeight(warstwa, neuronDo, i);
					bladNeurona *= propadowaneBledy;
				}
				neurony[warstwa][neuronDo].d = bladNeurona * pochFun;
				for (int neuronOd=0; neuronOd<layerSize(warstwa-1); neuronOd++){
					wagi.get(warstwa-1)[neuronOd][neuronDo] -= wspUczenia * bladNeurona * pochFun * neurony[warstwa-1][neuronOd].out;

				}
				neurony[warstwa][neuronDo].theta -= wspUczenia * bladNeurona * pochFun;
			}
		}
	}




	@Override
	public int[] getArchitecture() {
		return architecture.clone();
	}
}
