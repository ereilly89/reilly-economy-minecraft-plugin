package me.raid42;

public class trade {
	String playerName;
	int diamondsTraded;
	int goldTraded;
	int ironTraded;
	
	trade(String s, String d, String g, String i){
		playerName = s;
		diamondsTraded = Integer.parseInt(d);
		goldTraded = Integer.parseInt(g);
		ironTraded = Integer.parseInt(i);
	}
	
	public void incrementDiamondsTraded(int d){
		diamondsTraded+=d;
	}
	public void incrementGoldTraded(int g){
		goldTraded+=g;
	}
	public void incrementIronTraded(int i){
		ironTraded+=i;
	}
	
	public void setDiamondsTraded(int d){
		diamondsTraded = d;
	}
	
	public void setGoldTraded(int g){
		goldTraded = g;
	}
	
	public void setIronTraded(int i){
		ironTraded = i;
	}
	
	public int getDiamondsTraded(){
		return diamondsTraded;
	}
	
	public int getGoldTraded(){
		return goldTraded;
	}
	
	public int getIronTraded(){
		return ironTraded;
	}
	
	@Override
	public String toString(){
		return playerName+":"+diamondsTraded+":"+goldTraded+":"+ironTraded;
	}
	
}
