package siec;

import java.util.ArrayList;
import java.util.List;

public class Net implements ANNDetails{

	private int[] architektura;
	private List<Neuron> allNeurons = new ArrayList<>();
	private List<List<Neuron>> warstwy = new ArrayList<>();


	public Net(int[] architektura) {
		build(architektura);
	}

	@Override
	public void build(int[] architecture)
	{
		this.architektura = architecture;
		List<Neuron> warstwa;
		int nrWarstwy = 0;
		for (int rozmWarstwy : architecture)
		{
			warstwa = new ArrayList<>();
			for (int i=0;i<rozmWarstwy; i++)
			{
				Neuron neuron = new Neuron("w"+nrWarstwy+"|n"+i);
				warstwa.add(neuron);
				allNeurons.add(neuron);

				if (nrWarstwy != 0){
					List<Neuron> warstwaPoprzednia = warstwy.get(nrWarstwy-1);
					for (Neuron n : warstwaPoprzednia)
					{
						Krawedz krawedz = new Krawedz(n, neuron);
						krawedz.losujWage();
					}
				}

			}

			warstwy.add(warstwa);
			nrWarstwy++;
		}
	}


	@Override
	public void zeruj_neurony(){
		for(List<Neuron> warstwa : warstwy)
			for (Neuron n : warstwa)
				n.setWartosc(0);
	}


	@Override
	public float licz(float[] wejscie){
		zeruj_neurony();
		List<Neuron> warstwaWej = warstwy.get(0);
		List<Neuron> warstwaWyj = warstwy.get(warstwy.size()-1);
		for(int i=0;i<wejscie.length||i<warstwaWej.size();i++)
		{
			warstwaWej.get(i).setWartosc(wejscie[i]);
		}

		for(Neuron n : warstwaWej)
			n.go();

//		for(Neuron n : warstwaWyj)
//			System.out.println("Neuron Wyj: " + n.getWartosc());

		return warstwaWyj.get(0).getWartosc();
	}


	@Override
	public int layerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int layerSize(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Wagi getWeightsWagi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWeightsWagi(Wagi weights) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ucz(float[][] zbiorTestowy) {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getArchitecture() {
		return architektura.clone();
	}

	public List<List<Neuron>> getWarstwy()
	{
		return warstwy;
	}

	@Override
	public void setZakresyWejsc(float[] zakresy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public double getWspUczenia() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setWspUczenia(double wspUczenia) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinPochFunAktywacji(float minPochFunAktywacji) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getMinPochFunAktywacji() {
		// TODO Auto-generated method stub
		return 0;
	}
}
