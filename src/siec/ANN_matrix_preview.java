package siec;

public class ANN_matrix_preview implements ANNPreview {

	private ANN_matrix ANN;
	private int[] architecture;

	public ANN_matrix_preview(ANNDetails network) throws IllegalArgumentException {

		if(network.getClass() != ANN_matrix.class)
			throw new IllegalArgumentException("network must be " + ANN_matrix.class.getName() + " class");

		setANN(network);
	}

	public ANN_matrix_preview(ANN network) throws IllegalArgumentException {

		if(network.getDetails().getClass() != ANN_matrix.class)
			throw new IllegalArgumentException("network must be " + ANN_matrix.class.getName() + " class");

		setANN(network.getDetails());
	}

	@Override
	public void setANN(ANNDetails network) {
		this.ANN = (ANN_matrix) network;
		this.architecture = network.getArchitecture();
	}

	@Override
	public ANNDetails getANN() {
		return ANN;
	}


	@Override
	public void showArchitecture() {
		StringBuffer sb = new StringBuffer();
		for(int i =0;i<architecture.length; i++)
			sb.append(architecture[i] + ",");


//		System.out.println("Liczba warstw: " + architecture.length);
		System.out.println("Architektura: " + sb.substring(0, sb.length()-1));
	}

	@Override
	public void showValues() {
		int neuronCount = 0;
		for (int i=0; i<ANN.layerCount(); i++)
			neuronCount += ANN.layerSize(i);

		System.out.println("Liczba neuronów: " + neuronCount);
		for (int i=0; i<ANN.layerCount(); i++){
			System.out.println("\tWarstwa " + i);

			for(int j=0; j<ANN.layerSize(i); j++){
				System.out.print(ANN.neurony[i][j].in);
//				System.out.print("(" + ANN.neurony[i][j].d + ")");
				if (i != ANN.layerCount()-1){
					System.out.print(" -> ");
					System.out.print(ANN.neurony[i][j].out);
				}
				System.out.println();
			}
		}
	}

	@Override
	public void showWeights() {
		Wagi wagi = ANN.getWeightsWagi();
		float[][] wagiDanejWarstwy;
		int weightsCount = 0;

		for (int i=0; i<ANN.layerCount()-1; i++)
			weightsCount += ANN.layerSize(i)*ANN.layerSize(i+1);

		System.out.println("Liczba polaczen: " + weightsCount);
		for (int i=0; i<ANN.layerCount()-1; i++){
			System.out.println("\tPolaczenia " + i + "-" + (i+1));

			for(siec.ANN_matrix.Neuron n : ANN.neurony[i+1])
				System.out.println("[" + n.theta + "]");
			System.out.println();

			wagiDanejWarstwy = wagi.getLayerWeights(i);
			for (float[] wagiNeuronu : wagiDanejWarstwy){	
				for(float waga : wagiNeuronu)
					System.out.println(waga);
				System.out.println();
			}

		}

	}


}
