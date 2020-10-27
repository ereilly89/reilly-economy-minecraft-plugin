package me.raid42;

//import me.raid42.ReillyEconomy;

public class Convert{//Calculates exchange rates for the different commodities
	Circulation circ;
	int diamondTraded;
	int goldTraded;
	int ironTraded;
	
	int initialDiamond;
	int initialGold;
	int initialIron;
	Convert(Circulation c, int d, int g, int i, int iD, int iG, int iI){
		circ = c;
		diamondTraded = d;
		goldTraded = g;
		ironTraded = i;
		initialDiamond = iD;
		initialGold = iG;
		initialIron = iI;
	}
	
	public double getDiamondForEmerald(){
		int diamondPerEmerald = (int)(1/(((circ.getAllGold()-goldTraded+initialGold+circ.getAllIron()-ironTraded+initialIron)/(((circ.getAllDiamond())-diamondTraded+initialDiamond) ))/(9.16))*10.0);
		return diamondPerEmerald;
	}
	
	public double getGoldForEmerald(){
		int goldEquation =(int)(1/(((circ.getAllIron()-ironTraded+initialIron+circ.getAllDiamond()-diamondTraded+initialDiamond)/(((circ.getAllGold())-goldTraded+initialGold) ))/(7.292))*10.0);
		return goldEquation;
	}
	
	public double getIronForEmerald(){
		int ironEquation =(int)(1/(((circ.getAllDiamond()-diamondTraded+initialDiamond+circ.getAllGold()-goldTraded+initialGold)/(((circ.getAllIron())-ironTraded+initialIron) ))/(3.125))*10.0);
		return ironEquation;
	}
	
	
}
