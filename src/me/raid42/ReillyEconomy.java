package me.raid42;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ReillyEconomy extends JavaPlugin{
	
	private static final Logger log = Logger.getLogger("Minecraft");
	World world;
	Player player;
	
	int diamondTraded;
	int goldTraded;
	int ironTraded;
	
	int diamondEquation;
	int goldEquation;
	int ironEquation;
	
	int multiplier = 10;
	int initialDiamond=3*multiplier;
	int initialGold=12*multiplier;
	int initialIron=32*multiplier;
	
	File file = new File("tradefile.txt");

	ArrayList<trade> tradeList = new ArrayList<trade>();
	Circulation circ;
	Convert conv;
	
	Scanner s;
	
	double latestDiamond = diamondEquation;
	double latestGold = goldEquation;
	double latestIron = ironEquation;
	
	String signD = "+";
	String signG = "+";
	String signI = "+";
	
	double dailyDiamond;
	double dailyGold;
    double dailyIron;
	
    DecimalFormat ft = new DecimalFormat("0.00");
    
    int startingMarketPrice = 1;
    
	public void resetTraded(){
		for(int i=0; i<tradeList.size(); i++){
			tradeList.get(i).setDiamondsTraded(0);
			tradeList.get(i).setGoldTraded(0);
			tradeList.get(i).setIronTraded(0);
		}
	}
	
	@Override
	public void onEnable()
	{
		//load the tradelist from the text file, otherwise, create a new one
		
		try {
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.info("ReillyEconomy - Trade data could not be loaded!");
			if(dailyDiamond > -0.001){
		    	signD = "+";
		    }else{
		    	signD = "-";
		    }
		    if(dailyGold > -0.001){
		    	signG = "+";
		    }else{
		    	signG = "-";
		    }
		    if(dailyIron > -0.001){
		    	signI = "+";
		    }else{
		    	signI = "-";
		    }			e.printStackTrace();
		}
		updateMarket();
		log.info("ReillyEconomy - Plugin Enabled.");
		
		world = Bukkit.getServer().getWorld("world");
		world.setTime(0);
		log.info("ReillyEconomy - Time has been automatically set to day.");
		circ = new Circulation(world);
		conv = new Convert(circ, diamondTraded, goldTraded, ironTraded, initialDiamond, initialGold, initialIron); 
		
		if(s.hasNextLine()){
			log.info("ReillyEconomy - Listing Trade data - [Name: Diamonds Traded: Gold Traded: Iron traded]");
			while(s.hasNextLine()){
				String theLine = s.nextLine();
				String[] fileLine = theLine.split(":");
				trade newTrade = new trade(fileLine[0],fileLine[1],fileLine[2],fileLine[3]);
				log.info(newTrade.toString());
				tradeList.add(newTrade);	
			}
			//******Uncomment When Trades Should Be Reset***************************
			//resetTraded();
			//log.info("Trades Reset...");
			//*********************************
			log.info("ReillyEconomy - Trade data has been loaded successfully.");
		}else{
			log.info("ReillyEconomy - Trade data could not be loaded!");
		}
	}
	@Override
	public void onDisable()
	{
		PrintWriter printwriter;
		try {
			printwriter = new PrintWriter(file);
			for(int i=0;i<tradeList.size();i++){
				printwriter.println(tradeList.get(i).toString());
			}
			printwriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.info("ReillyEconomy - Couldn't save trade data!");
			e.printStackTrace();
		}
		//save the tradelist to a text file
		
		log.info("ReillyEconomy - Plugin Disabled.");
	}
	
	ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	public void updateMarket(){
		Runnable drawRunnable = new Runnable() {
			public void run(){
				// TODO Auto-generated method stub
				Player[] tradeArray = new Player[Bukkit.getServer().getOnlinePlayers().size()];
				Bukkit.getServer().getOnlinePlayers().toArray(tradeArray);
				diamondTraded=0;
				goldTraded=0;
				ironTraded=0;
				
				for(int i=0;i<tradeArray.length;i++){
					for(int x=0; x<tradeList.size();x++){
						if(tradeList.get(x).playerName.equals(tradeArray[i].toString())){
							diamondTraded+=tradeList.get(x).getDiamondsTraded();
							goldTraded+=tradeList.get(x).getGoldTraded();
							ironTraded+=tradeList.get(x).getIronTraded();
						}
					}
				}
				conv = new Convert(circ, diamondTraded, goldTraded, ironTraded, initialDiamond, initialGold, initialIron);
				diamondEquation =(int)conv.getDiamondForEmerald();
				
				goldEquation =(int)conv.getGoldForEmerald();
				ironEquation =(int)conv.getIronForEmerald();
				
				dailyDiamond = ((100/(diamondEquation*1.0))-(100/(latestDiamond*1.0)));
				dailyGold = ((100/(goldEquation*1.0))-(100/(latestGold*1.0)));
			    dailyIron = ((100/(ironEquation*1.0))-(100/(latestIron*1.0)));
				
			    if(dailyDiamond > -0.001){
			    	signD = "+";
			    }else{
			    	signD = "-";
			    }
			    if(dailyGold > -0.001){
			    	signG = "+";
			    }else{
			    	signG = "-";
			    }
			    if(dailyIron > -0.001){
			    	signI = "+";
			    }else{
			    	signI = "-";
			    }
			    
				startingMarketPrice--;
				if(startingMarketPrice==0){
					//Show all players the market at close for the day
					
					for(int i=0;i<tradeArray.length;i++){
						Player player1 = tradeArray[i];
						player1.sendMessage(ChatColor.AQUA+"Yesterday's Closing Prices");
						player1.sendMessage(ChatColor.AQUA+"----------------------");
						if(signD.equals("+")){
							player1.sendMessage(ChatColor.AQUA+"10 Diamonds= "+ft.format(100/(diamondEquation*1.0))+" E "+ChatColor.GREEN+signD+ft.format(dailyDiamond));
						}else{
							player1.sendMessage(ChatColor.AQUA+"10 Diamonds= "+ft.format(100/(diamondEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyDiamond));
						}
						
						if(signG.equals("+")){
							player1.sendMessage(ChatColor.AQUA+"10 Gold      = "+ft.format(100/(goldEquation*1.0))+" E "+ChatColor.GREEN+signG+ft.format(dailyGold));
						}else{
							player1.sendMessage(ChatColor.AQUA+"10 Gold      = "+ft.format(100/(goldEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyGold));
						}
						
						if(signI.equals("+")){
							player1.sendMessage(ChatColor.AQUA+"10 Iron      = "+ft.format(100/(ironEquation*1.0))+" E "+ChatColor.GREEN+signI+ft.format(dailyIron));
						}else{
							player1.sendMessage(ChatColor.AQUA+"10 Iron      = "+ft.format(100/(ironEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyIron));
						}
						player1.sendMessage(ChatColor.AQUA+"----------------------");
						player1.sendMessage(ChatColor.AQUA+"Emeralds in Circulation: "+circ.getAllEmerald());
						player1.sendMessage(ChatColor.AQUA+"----------------------");
					}
					
					log.info("The market day has started...");
					latestDiamond = diamondEquation;
					latestGold = goldEquation;
					latestIron = ironEquation;
					startingMarketPrice = 4;					
				}
				
				dailyDiamond = ((100/(diamondEquation*1.0))-(100/(latestDiamond*1.0)));
				dailyGold = ((100/(goldEquation*1.0))-(100/(latestGold*1.0)));
			    dailyIron = ((100/(ironEquation*1.0))-(100/(latestIron*1.0)));
			    
			    if(dailyDiamond > -0.001){
			    	signD = "+";
			    }else{
			    	signD = "-";
			    }
			    if(dailyGold > -0.001){
			    	signG = "+";
			    }else{
			    	signG = "-";
			    }
			    if(dailyIron > -0.001){
			    	signI = "+";
			    }else{
			    	signI = "-";
			    }
			    
				log.info("ReillyEconomy - Market prices updated successfully.");
				log.info("----------------------");
				log.info("Market");
				log.info("----------------------");
				log.info(diamondEquation+" Diamonds  = 10 emeralds");
				log.info(goldEquation+" Gold     = 10 emeralds");
				log.info(ironEquation+" Iron     = 10 emeralds");
				log.info("10 Emeralds = "+diamondEquation+" diamonds");
				log.info("10 Emeralds = "+goldEquation+" gold");
				log.info("10 Emeralds = "+ironEquation+" iron");
				log.info("----------------------");
				log.info("Emeralds in Circulation: "+circ.getAllEmerald());
				log.info("----------------------");
			}
		};
		exec.scheduleAtFixedRate(drawRunnable , 0, 5, TimeUnit.MINUTES);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		ItemStack emerald10 = new ItemStack(Material.EMERALD, 10);
		
		if(!(sender instanceof Player))
    	{
    		sender.sendMessage("You must be a player to issue this command");
    		return false;
    	}else{
    		player = (Player) sender;
    	}
    	
		//**TRADE COMMAND**//
		if(label.equalsIgnoreCase("trade")){
			
			if(args[0].equalsIgnoreCase("emerald")){
				
				if(args[1].equalsIgnoreCase("diamond")){
					
					if(player.getInventory().contains(Material.EMERALD, 10)){
						if(((circ.getAllDiamond())-diamondTraded)>=diamondEquation){
							player.sendMessage(ChatColor.GREEN+"10 emeralds have been traded for "+diamondEquation+" diamonds.");
							//diamondTraded+=diamondEquation;
							
							boolean isInList = false;
							for(int i=0; i<tradeList.size();i++){
								if(tradeList.get(i).playerName.equals(player.toString())){
									tradeList.get(i).incrementDiamondsTraded(diamondEquation);
									isInList = true;
								}
							}
							if(isInList == false){
								trade newTrade = new trade(player.toString(),Integer.toString(diamondEquation),"0","0");
								tradeList.add(newTrade);
							}
							isInList = false;
							
							player.getInventory().addItem(new ItemStack(Material.DIAMOND, diamondEquation));
							player.getInventory().removeItem(emerald10);
						}else{ 
							player.sendMessage(ChatColor.YELLOW+"There are not enough diamonds in circulation!");
						}
					}else{
						player.sendMessage(ChatColor.YELLOW+"You must have 10 emeralds to trade!");
					}
					
				}else if(args[1].equalsIgnoreCase("gold")){
					if(player.getInventory().contains(Material.EMERALD, 10)){
						if(((circ.getAllGold())-goldTraded)>=goldEquation){
							player.sendMessage(ChatColor.GREEN+"10 emeralds have been traded for "+goldEquation+" gold.");
							//goldTraded+=goldEquation;
							
							boolean isInList = false;
							for(int i=0; i<tradeList.size();i++){
								if(tradeList.get(i).playerName.equals(player.toString())){
									tradeList.get(i).incrementGoldTraded(goldEquation);
									isInList = true;
								}
							}
							if(isInList == false){
								trade newTrade = new trade(player.toString(),"0",Integer.toString(goldEquation),"0");
								tradeList.add(newTrade);
							}
							isInList = false;
							
							player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, goldEquation));
							player.getInventory().removeItem(emerald10);
						}else {
							player.sendMessage(ChatColor.YELLOW+"There is not enough gold in circulation!");
						}
					}else{
						player.sendMessage(ChatColor.YELLOW+"You must have 10 emeralds to trade!");
					}
					
				}else if(args[1].equalsIgnoreCase("iron")){
					if(player.getInventory().contains(Material.EMERALD, 10)){
						if(((circ.getAllIron())-ironTraded)>=ironEquation){
							player.sendMessage(ChatColor.GREEN+"10 emeralds have been traded for "+ironEquation+" iron.");
							//ironTraded+=ironEquation;
							
							boolean isInList = false;
							for(int i=0; i<tradeList.size();i++){
								if(tradeList.get(i).playerName.equals(player.toString())){
									tradeList.get(i).incrementIronTraded(ironEquation);
									isInList = true;
								}
							}
							if(isInList == false){
								trade newTrade = new trade(player.toString(),"0","0",Integer.toString(ironEquation));
								tradeList.add(newTrade);
							}
							isInList = false;
							
							player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, ironEquation));
							player.getInventory().removeItem(emerald10);
						}else{
							player.sendMessage(ChatColor.YELLOW+"There is not enough iron in circulation!");
						}
					}else{
						player.sendMessage(ChatColor.YELLOW+"You must have 10 emeralds to trade!"); 
					}
					
				}else{
					player.sendMessage(ChatColor.YELLOW+"Try '/trade emerald [diamond/gold/iron]' ");
				}
				
			}else if(args[0].equalsIgnoreCase("diamond")){
				
				if(player.getInventory().contains(Material.DIAMOND, diamondEquation)){
					player.sendMessage(ChatColor.GREEN+""+diamondEquation+" diamonds have been traded for 10 emeralds.");
					//diamondTraded-=diamondEquation;
					
					boolean isInList = false;
					for(int i=0; i<tradeList.size();i++){
						if(tradeList.get(i).playerName.equals(player.toString())){
							tradeList.get(i).incrementDiamondsTraded(diamondEquation*-1);
							isInList = true;
						}
					}
					if(isInList == false){
						trade newTrade = new trade(player.toString(),Integer.toString(diamondEquation*-1),"0","0");
						tradeList.add(newTrade);
					}
					isInList = false;
					
					player.getInventory().removeItem(new ItemStack(Material.DIAMOND, diamondEquation));
					player.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
					
				}else{
					player.sendMessage(ChatColor.YELLOW+"You must have "+diamondEquation+" diamonds to trade!");
				}

			}else if(args[0].equalsIgnoreCase("gold")){
				
				if(player.getInventory().contains(Material.GOLD_INGOT, goldEquation)){
					player.sendMessage(ChatColor.GREEN+""+goldEquation+" gold ingots have been traded for 10 emeralds.");
					//goldTraded-=goldEquation;
					
					boolean isInList = false;
					for(int i=0; i<tradeList.size();i++){
						if(tradeList.get(i).playerName.equals(player.toString())){
							tradeList.get(i).incrementGoldTraded(goldEquation*-1);
							isInList = true;
						}
					}
					if(isInList == false){
						trade newTrade = new trade(player.toString(),"0",Integer.toString(goldEquation*-1),"0");
						tradeList.add(newTrade);
					}
					isInList = false;
					
					player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, goldEquation));
					player.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
					
				}else{
					player.sendMessage(ChatColor.YELLOW+"You must have "+goldEquation+" gold ingots to trade!");
				}
				
			}else if(args[0].equalsIgnoreCase("iron")){
				
				if(player.getInventory().contains(Material.IRON_INGOT, ironEquation)){
					player.sendMessage(ChatColor.GREEN+""+ironEquation+" iron ingots have been traded for 10 emeralds.");
					//ironTraded-=ironEquation;
					
					boolean isInList = false;
					for(int i=0; i<tradeList.size();i++){
						if(tradeList.get(i).playerName.equals(player.toString())){
							tradeList.get(i).incrementIronTraded(ironEquation*-1);
							isInList = true;
						}
					}
					if(isInList == false){
						trade newTrade = new trade(player.toString(),"0","0",Integer.toString(ironEquation*-1));
						tradeList.add(newTrade);
					}
					isInList = false;
					
					player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, ironEquation));
					player.getInventory().addItem(new ItemStack(Material.EMERALD, 10));
					
				}else{
					player.sendMessage(ChatColor.YELLOW+"You must have "+ironEquation+" iron ingots to trade!");
				}
			}
		}
		
		//**MARKET COMMAND**//
		
		else if(label.equalsIgnoreCase("market")){
			
			player.sendMessage(ChatColor.AQUA+"Market");
			player.sendMessage(ChatColor.AQUA+"----------------------");
			player.sendMessage(ChatColor.AQUA+""+diamondEquation+" Diamonds  = 10 emeralds");
			player.sendMessage(ChatColor.AQUA+""+goldEquation+" Gold       = 10 emeralds");
			player.sendMessage(ChatColor.AQUA+""+ironEquation+" Iron       = 10 emeralds");
			player.sendMessage(ChatColor.AQUA+"10 Emeralds = "+diamondEquation+" diamonds");
			player.sendMessage(ChatColor.AQUA+"10 Emeralds = "+goldEquation+" gold");
			player.sendMessage(ChatColor.AQUA+"10 Emeralds = "+ironEquation+" iron");
			player.sendMessage(ChatColor.AQUA+"----------------------");
			player.sendMessage(ChatColor.AQUA+"Emeralds in Circulation: "+circ.getAllEmerald());
			player.sendMessage(ChatColor.AQUA+"----------------------");
			//player.sendMessage(circ.getAllDiamond()+" "+circ.getAllGold()+" "+circ.getAllIron());
		}else if(label.equalsIgnoreCase("market2")){
			player.sendMessage(ChatColor.AQUA+"Market");
			player.sendMessage(ChatColor.AQUA+"----------------------");
			
			if(signD.equals("+")){
				player.sendMessage(ChatColor.AQUA+"10 Diamonds= "+ft.format(100/(diamondEquation*1.0))+" E "+ChatColor.GREEN+signD+ft.format(dailyDiamond));
			}else{
				player.sendMessage(ChatColor.AQUA+"10 Diamonds= "+ft.format(100/(diamondEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyDiamond));
			}
			
			if(signG.equals("+")){
				player.sendMessage(ChatColor.AQUA+"10 Gold      = "+ft.format(100/(goldEquation*1.0))+" E "+ChatColor.GREEN+signG+ft.format(dailyGold));
			}else{
				player.sendMessage(ChatColor.AQUA+"10 Gold      = "+ft.format(100/(goldEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyGold));
			}
			
			if(signI.equals("+")){
				player.sendMessage(ChatColor.AQUA+"10 Iron      = "+ft.format(100/(ironEquation*1.0))+" E "+ChatColor.GREEN+signI+ft.format(dailyIron));
			}else{
				player.sendMessage(ChatColor.AQUA+"10 Iron      = "+ft.format(100/(ironEquation*1.0))+" E "+ChatColor.RED+ft.format(dailyIron));
			}
			player.sendMessage(ChatColor.AQUA+"----------------------");
			player.sendMessage(ChatColor.AQUA+"Emeralds in Circulation: "+circ.getAllEmerald());
			player.sendMessage(ChatColor.AQUA+"----------------------");
    	}else{
    		return false;
    	}
		return false;
	}
}
