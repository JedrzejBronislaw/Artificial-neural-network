package siec;

import java.util.ArrayList;
import java.util.List;



public class Neuron {
	private List<Krawedz> krawedzie = new ArrayList<>();
	private String nazwa;
	private float wartosc = 0;
	
//	public void polaczZNeuronami(List<Neuron> neurony)
//	{
//		for (Neuron n : neurony)
//			krawedzie.add(new Krawedz());
//	}
	
	public Neuron(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getNazwa() {
		return nazwa;
	}
	
	public float getWartosc() {
		return wartosc;
	}
	public void setWartosc(float wartosc) {
		this.wartosc = wartosc;
	}
	
	public void dodajKrawedz(Krawedz krawedz)
	{
		krawedzie.add(krawedz);
	}
	
	public List<Krawedz> getKrawedzie() {
		return krawedzie;
	}
	public void go() {
//		float wart = 0;
//		wartosc = 0;
		for(Krawedz k : krawedzie)
			if(k.getNeuronPo() == this){
				wartosc += k.getWartosc();
			}
		
		for(Krawedz k : krawedzie)
			if(k.getNeuronPrzed() == this){
				k.licz(wartosc);
			}
	}
	public void warunkowyGo() {
		for(Krawedz k : krawedzie)
			if(k.getNeuronPo() == this){
				if (!k.gettPoliczyla()) return;
			}
		
		go();
	}
}
