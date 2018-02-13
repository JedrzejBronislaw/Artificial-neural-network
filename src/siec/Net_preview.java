package siec;

import java.util.List;

public class Net_preview implements ANNPreview{

	private ANNDetails ANN;
	private int[] architecture;

	public Net_preview(ANNDetails network) throws IllegalArgumentException {

		if(network.getClass() != Net.class)
			throw new IllegalArgumentException("network must be " + Net.class.getName() + " class");

		setANN(network);
	}

	@Override
	public void setANN(ANNDetails network) {
		this.ANN = network;
		this.architecture = network.getArchitecture();
	}

	@Override
	public ANNDetails getANN() {
		return ANN;
	}

	@Override
	public void showArchitecture()
	{


		System.out.print(" - Architektura ");
		StringBuffer arch = new StringBuffer();
		for (int a : architecture)
			arch.append(a + ",");
		System.out.println("(" + arch.toString().substring(0,arch.length()-1) + ") - ");
		System.out.println("Liczba warstw: " + ((Net)ANN).getWarstwy().size());
		int j = 0;
		for(List<Neuron> warstwa : ((Net)ANN).getWarstwy())
		{
			System.out.println("Warstwa " + j);
			System.out.println("\trozmiar warstwy: " + warstwa.size());
			int i = 0;
			for (Neuron n : warstwa)
			{
				System.out.println("\tNeuron " + i);
				System.out.println("\tpolaczony z: ");
				for (Krawedz krawedz : n.getKrawedzie())
				{
					Neuron przed,po;
					przed = krawedz.getNeuronPrzed();
					po = krawedz.getNeuronPo();
					if (przed == n)
						System.out.println("\t\t"+po.getNazwa() + " (" + krawedz.getWaga() + ")" );
					else
						System.out.println("\t\t"+przed.getNazwa() + " (" + krawedz.getWaga() + ")" );
				}
				i++;
			}
			j++;
		}

	}

	@Override
	public void showValues(){
		System.out.println(" - Wartosci - ");
		System.out.println("Liczba warstw: " + ((Net)ANN).getWarstwy().size());
		int j = 0;
		for(List<Neuron> warstwa : ((Net)ANN).getWarstwy())
		{
			System.out.println("Warstwa " + (j++));
			System.out.println("\trozmiar warstwy: " + warstwa.size());
			int i = 0;
			for (Neuron n : warstwa)
				System.out.println("\tNeuron " + (i++) + " = " + n.getWartosc());
		}

	}

	@Override
	public void showWeights() {
		// TODO Auto-generated method stub

	}
}
