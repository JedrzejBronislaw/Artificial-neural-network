package siec;

import java.util.Random;

public class Krawedz {
	private float waga = 0;
	private Neuron neuronPrzed, neuronPo;
	private boolean policzyla = false;
	private float wartosc = 0;
	
	public Krawedz() {
	}
	
	public Krawedz(Neuron przed, Neuron po) {
		neuronPrzed = przed;
		neuronPo = po;
		
		neuronPrzed.dodajKrawedz(this);
		neuronPo.dodajKrawedz(this);
	}
	public Neuron getNeuronPo() {
		return neuronPo;
	}
	public Neuron getNeuronPrzed() {
		return neuronPrzed;
	}

	public boolean gettPoliczyla() {
		return policzyla;
	}
	
	public void losujWage()
	{
		Random r = new Random();
		waga = r.nextFloat();
	}
	
	public void setWaga(float waga) {
		this.waga = waga;
	}
	public float getWaga() {
		return waga;
	}
	
	public float getWartosc() {
		return wartosc;
	}
	
	public float licz(float we){
		wartosc = we*waga;
		policzyla = true;
//		neuronPo.setWartosc(wartosc);
		neuronPo.warunkowyGo();
		return wartosc;
	}
}
