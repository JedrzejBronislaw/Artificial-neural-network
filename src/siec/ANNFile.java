package siec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ANNFile {

	public boolean writeWeights(String fileName, ANN wagi) {
		return writeWeights(fileName, wagi.getDetails().getWeightsWagi());
	}

	public boolean writeWeights(String fileName, Wagi wagi) {
		File file = new File(fileName);
		DataOutputStream dos;

		//Opening
		try {
			dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e1) {
			return false;
		}

		//Writing
		int n = wagi.annLayerCount();

		try {
			dos.writeInt(n);
			for (int i = 0; i < n; i++)
				dos.writeInt(wagi.annLayerSize(i));

			for (int i = 0; i < n - 1; i++)
				for (int j = 0; j < wagi.annLayerSize(i); j++)
					for (int k = 0; k < wagi.annLayerSize(i + 1); k++)
						dos.writeDouble(wagi.getWeight(i, j, k));
		} catch (IOException e) {
			return false;
		}

		//Closing
		try {
			dos.close();
		} catch (IOException e) {}

		return true;
	}

	public void readWeights(String fileName, ANN siec) {
		siec.getDetails().setWeightsWagi(readWeights(fileName));
	}

	public Wagi readWeights(String fileName) {
		Wagi wagi = null;
		File file = new File(fileName);
		DataInputStream dis;

		//Opening
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		} catch (FileNotFoundException e1) {
			return null;
		}

		int n;
		int[] architektura;

		try {
			//Architecture reading
			n = dis.readInt();
			architektura = new int[n];
			for (int i = 0; i < n; i++)
				architektura[i] = dis.readInt();

			//Architecture creating
			wagi = new Wagi(architektura);

			//Weights reading
			for (int i = 0; i < n - 1; i++)
				for (int j = 0; j < architektura[i]; j++)
					for (int k = 0; k < architektura[i + 1]; k++)
						wagi.setWeight(i, j, k, dis.readFloat());

		} catch (IOException e) {
			return null;
		}

		//Closing
		try {
			dis.close();
		} catch (IOException e) {}

		return wagi;
	}
}
