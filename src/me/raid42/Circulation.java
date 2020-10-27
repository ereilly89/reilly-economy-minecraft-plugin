package me.raid42;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

//import net.minecraft.server.v1_11_R1.Entity;

public class Circulation {
	
    World world;
	Circulation(World world){
		this.world = world;
	}
	
	double emeralds=0;
	double diamonds=0;
	double gold=0;
	double iron=0;
	

	
	public double getAllEmerald(){
		//update methods
		getEmeraldInventory();
		getEmeraldChest();
		getEmeraldOnGround();
		emeralds = getEmeraldInventory()+getEmeraldChest()+getEmeraldOnGround()+getEmeraldFurnace()+getEmeraldEnderChest();
		return emeralds;
	}
	
	public double getAllDiamond(){
		getDiamondInventory();
		getDiamondChest();
		getDiamondOnGround();
		diamonds = getDiamondInventory()+getDiamondChest()+getDiamondOnGround()+getDiamondFurnace()+getDiamondEnderChest();
		return diamonds;
	}
	
	public double getAllGold(){
		getGoldInventory();
		getGoldChest();
		getGoldOnGround();
		gold = getGoldInventory()+getGoldChest()+getGoldOnGround()+getGoldFurnace()+getGoldEnderChest();
		return gold;
	}

	public double getAllIron(){
		getIronInventory();
		getIronChest();
		getIronOnGround();
		iron = getIronInventory()+getIronChest()+getIronOnGround()+getIronFurnace()+getIronEnderChest();
		return iron;
	}
	
	//**************************************************************************************************************
	
	//Get Emeralds w/in Player's Inventories
		public double getEmeraldInventory(){
			emeralds = 0;
			   for(Player p : Bukkit.getOnlinePlayers()){
				   ItemStack[] inv = p.getInventory().getContents();
				   for(int i=0; i < inv.length; i++){
					   if(inv[i]!=null){
						   for(int x=0; x<1792;x++){
				    	   		if(inv[i].equals(new ItemStack(Material.EMERALD, x))){
				    	   			emeralds += x;
				    	   		}else if(inv[i].equals(new ItemStack(Material.EMERALD_BLOCK, x))){
				    	   			emeralds += x*9;
				    	   		}
						   }
				       }
				   }
			   }
			   return emeralds;
		}
		
		 //Get Emeralds w/in Chests
		public double getEmeraldChest(){
			emeralds = 0;
			 Chunk[] c = world.getLoadedChunks();
				for(int i=0;i<c.length;i++){//loop through loaded chunks
					for(int x=0;x<c[i].getTileEntities().length;x++){//loop through tile entities within loaded chunks
						if(c[i].getTileEntities()[x] instanceof Chest){
							Chest c1 = (Chest) c[i].getTileEntities()[x]; 
							ItemStack[] inv = c1.getInventory().getContents();
							for(int y=0;y<inv.length;y++){
								if(inv[y]!=null){
									for(int z=0;z<3456;z++){
										if(inv[y].equals(new ItemStack(Material.EMERALD, z))){
								    	   	emeralds += z;
								    	}else if(inv[y].equals(new ItemStack(Material.EMERALD_BLOCK, z))){
								    	   	emeralds += z*9;
								    	}
									}
									
								}
							}
						}
					}
				}
				return emeralds;
		}
		//Get Emeralds w/in Furnaces
		public double getEmeraldFurnace(){
			emeralds = 0;
			 Chunk[] c = world.getLoadedChunks();
				for(int i=0;i<c.length;i++){//loop through loaded chunks
					for(int x=0;x<c[i].getTileEntities().length;x++){//loop through tile entities within loaded chunks
						if(c[i].getTileEntities()[x] instanceof Furnace){
							Furnace f1 = (Furnace) c[i].getTileEntities()[x]; 
							ItemStack[] inv = f1.getInventory().getContents();
							for(int y=0;y<inv.length;y++){
								if(inv[y]!=null){
									for(int z=0;z<=64;z++){
										if(inv[y].equals(new ItemStack(Material.EMERALD, z))){
								    	   	emeralds += z;
								    	}else if(inv[y].equals(new ItemStack(Material.EMERALD_BLOCK, z))){
								    	   	emeralds += z*9;
								    	}
									}
									
								}
							}
						}
					}
				}
				return emeralds;
		}
		//Get Emeralds w/in Ender Chests
		public double getEmeraldEnderChest(){
			emeralds = 0;
			Player[] onlinePlayers = new Player[Bukkit.getOnlinePlayers().size()];
			Bukkit.getServer().getOnlinePlayers().toArray(onlinePlayers);
			
			for(int i=0;i<onlinePlayers.length;i++){
				ItemStack[] inv = onlinePlayers[i].getEnderChest().getContents();
				for(int y=0;y<inv.length;y++){
					if(inv[y]!=null){
						for(int z=0;z<=1728;z++){
							if(inv[y].equals(new ItemStack(Material.EMERALD, z))){
								emeralds += z;
							}else if(inv[y].equals(new ItemStack(Material.EMERALD_BLOCK, z))){
								emeralds += z*9;
							}
						}
					}
				}
			}
			
			return emeralds;
		}
		
		//Get Emeralds that are on the ground as entities
		public double getEmeraldOnGround(){
			emeralds = 0;
			Item onTheGround;
			List<org.bukkit.entity.Entity> entities = world.getEntities();
		
			for(int i=0;i<entities.size();i++){
				if(entities.get(i).getName().equals("item.item.emerald")){
					if(entities.get(i) instanceof Item){
						onTheGround = (Item)entities.get(i);
						emeralds+=onTheGround.getItemStack().getAmount();
					}
				}else if(entities.get(i).getName().equals("item.tile.blockEmerald")){
					if(entities.get(i) instanceof Item){
						onTheGround = (Item)entities.get(i);
						emeralds+=onTheGround.getItemStack().getAmount()*9;
					}
				}
				
			}
			return emeralds;
		}
		
	//***********************************************	
		
	//Get Diamonds w/in Player's Inventories
	public double getDiamondInventory(){
		diamonds = 0;
		   for(Player p : Bukkit.getOnlinePlayers()){
			   ItemStack[] inv = p.getInventory().getContents();
			   for(int i=0; i < inv.length; i++){
				   if(inv[i]!=null){
					   for(int x=0; x<1792;x++){
			    	   		if(inv[i].equals(new ItemStack(Material.DIAMOND, x))){
			    	   			diamonds += x;
			    	   		}else if(inv[i].equals(new ItemStack(Material.DIAMOND_BLOCK, x))){
			    	   			diamonds += x*9;
			    	   		}
					   }
			       }
			   }
		   }
		   return diamonds;
	}
	
	//Get Diamonds w/in Chests
	public double getDiamondChest(){
		diamonds = 0;
		 Chunk[] c = world.getLoadedChunks();
			for(int i=0;i<c.length;i++){
				for(int x=0;x<c[i].getTileEntities().length;x++){
					if(c[i].getTileEntities()[x] instanceof Chest){
						Chest c1 = (Chest) c[i].getTileEntities()[x]; 
						ItemStack[] inv = c1.getInventory().getContents();
						for(int y=0;y<inv.length;y++){
							if(inv[y]!=null){
								for(int z=0;z<1792;z++){
									if(inv[y].equals(new ItemStack(Material.DIAMOND, z))){
										diamonds += z;
									}else if(inv[y].equals(new ItemStack(Material.DIAMOND_BLOCK, z))){
										diamonds += z*9;
									}
								}
						
							}
						}
					}
				}
			}
			return diamonds;
	}
	//Get Diamonds w/in Furnaces
	public double getDiamondFurnace(){
		diamonds = 0;
		 Chunk[] c = world.getLoadedChunks();
			for(int i=0;i<c.length;i++){//loop through loaded chunks
				for(int x=0;x<c[i].getTileEntities().length;x++){//loop through tile entities within loaded chunks
					if(c[i].getTileEntities()[x] instanceof Furnace){
						Furnace f1 = (Furnace) c[i].getTileEntities()[x]; 
						ItemStack[] inv = f1.getInventory().getContents();
						for(int y=0;y<inv.length;y++){
							if(inv[y]!=null){
								for(int z=0;z<=64;z++){
									if(inv[y].equals(new ItemStack(Material.DIAMOND, z))){
							    	   	diamonds += z;
							    	}else if(inv[y].equals(new ItemStack(Material.DIAMOND_BLOCK, z))){
							    	   	diamonds += z*9;
							    	}
								}
								
							}
						}
					}
				}
			}
			return diamonds;
	}
	//Get Diamonds w/in Ender Chests
	public double getDiamondEnderChest(){
		diamonds = 0;
		Player[] onlinePlayers = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getServer().getOnlinePlayers().toArray(onlinePlayers);
		
		for(int i=0;i<onlinePlayers.length;i++){
			ItemStack[] inv = onlinePlayers[i].getEnderChest().getContents();
			for(int y=0;y<inv.length;y++){
				if(inv[y]!=null){
					for(int z=0;z<=1728;z++){
						if(inv[y].equals(new ItemStack(Material.DIAMOND, z))){
							diamonds += z;
						}else if(inv[y].equals(new ItemStack(Material.DIAMOND_BLOCK, z))){
							diamonds += z*9;
						}
					}
				}
			}
		}
		
		return diamonds;
	}
	
	//Get diamonds that are on the ground as entities
	public double getDiamondOnGround(){
		diamonds = 0;
		Item onTheGround;
		List<org.bukkit.entity.Entity> entities = world.getEntities();
	
		for(int i=0;i<entities.size();i++){
			
			if(entities.get(i).getName().equals("item.item.diamond")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					diamonds+=onTheGround.getItemStack().getAmount();
				}
			}else if(entities.get(i).getName().equals("item.tile.blockDiamond")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					diamonds+=onTheGround.getItemStack().getAmount()*9;
				}
			}
			
		}
		return diamonds;
	}
	
	//***********************************************
	
	//Get Gold w/in Player's Inventories
	public double getGoldInventory(){
		gold = 0;
		   for(Player p : Bukkit.getOnlinePlayers()){
			   ItemStack[] inv = p.getInventory().getContents();
			   for(int i=0; i < inv.length; i++){
				   if(inv[i]!=null){
					   for(int x=0; x<1792;x++){
			    	   		if(inv[i].equals(new ItemStack(Material.GOLD_INGOT, x))){
			    	   			gold += x;
			    	   		}else if(inv[i].equals(new ItemStack(Material.GOLD_BLOCK, x))){
			    	   			gold += x*9;
			    	   		}else if(inv[i].equals(new ItemStack(Material.GOLD_NUGGET, x))){
			    	   		    gold+=x*(1.0/9.0);
			    	   		}
					   }
			       }
			   }
		   }
		   return gold;
	}
	
	//Get Gold w/in Chests
	public double getGoldChest(){
		gold = 0;
		Chunk[] c = world.getLoadedChunks();
		for(int i=0;i<c.length;i++){
			for(int x=0;x<c[i].getTileEntities().length;x++){
				if(c[i].getTileEntities()[x] instanceof Chest){
					Chest c1 = (Chest) c[i].getTileEntities()[x]; 
					ItemStack[] inv = c1.getInventory().getContents();
					for(int y=0;y<inv.length;y++){
						if(inv[y]!=null){
							for(int z=0;z<1792;z++){
								if(inv[y].equals(new ItemStack(Material.GOLD_INGOT, z))){
					    	   		gold += z;
					    	   	}else if(inv[y].equals(new ItemStack(Material.GOLD_BLOCK, z))){
					    	   		gold += z*9;
					    	   	}else if(inv[y].equals(new ItemStack(Material.GOLD_NUGGET, z))){
				    	   		    gold+=z*(1.0/9.0);
				    	   		}
							}
						
						}
					}
				}
			}
		}	
		return gold;
	}
	//Get Gold w/in Furnaces
	public double getGoldFurnace(){
		gold = 0;
		 Chunk[] c = world.getLoadedChunks();
			for(int i=0;i<c.length;i++){//loop through loaded chunks
				for(int x=0;x<c[i].getTileEntities().length;x++){//loop through tile entities within loaded chunks
					if(c[i].getTileEntities()[x] instanceof Furnace){
						Furnace f1 = (Furnace) c[i].getTileEntities()[x]; 
						ItemStack[] inv = f1.getInventory().getContents();
						for(int y=0;y<inv.length;y++){
							if(inv[y]!=null){
								for(int z=0;z<=64;z++){
									if(inv[y].equals(new ItemStack(Material.GOLD_INGOT, z))){
							    	   	gold += z;
							    	}else if(inv[y].equals(new ItemStack(Material.GOLD_BLOCK, z))){
							    	   	gold += z*9;
							    	}
								}
								
							}
						}
					}
				}
			}
			return gold;
	}
	//Get Gold w/in Ender Chests
	public double getGoldEnderChest(){
		gold = 0;
		Player[] onlinePlayers = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getServer().getOnlinePlayers().toArray(onlinePlayers);
		
		for(int i=0;i<onlinePlayers.length;i++){
			ItemStack[] inv = onlinePlayers[i].getEnderChest().getContents();
			for(int y=0;y<inv.length;y++){
				if(inv[y]!=null){
					for(int z=0;z<=1728;z++){
						if(inv[y].equals(new ItemStack(Material.GOLD_INGOT, z))){
							gold += z;
						}else if(inv[y].equals(new ItemStack(Material.GOLD_BLOCK, z))){
							gold += z*9;
						}
					}
				}
			}
		}
		
		return gold;
	}
	
	//Get gold that is on the ground as an entity
	public double getGoldOnGround(){
		gold = 0;
		Item onTheGround;
		List<org.bukkit.entity.Entity> entities = world.getEntities();
	
		for(int i=0;i<entities.size();i++){
			
			if(entities.get(i).getName().equals("item.item.ingotGold")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					gold+=onTheGround.getItemStack().getAmount();
				}
			}else if(entities.get(i).getName().equals("item.tile.blockGold")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					gold+=onTheGround.getItemStack().getAmount()*9;
				}
			}
			
		}
		return gold;	
	}
	//***********************************************
	
	//Get Iron w/in Player's Inventories
	public double getIronInventory(){
		iron = 0;
		   for(Player p : Bukkit.getOnlinePlayers()){
			   ItemStack[] inv = p.getInventory().getContents();
			   for(int i=0; i < inv.length; i++){
				   if(inv[i]!=null){
					   for(int x=0; x<1792;x++){
			    	   		if(inv[i].equals(new ItemStack(Material.IRON_INGOT, x))){
			    	   			iron += x;
			    	   		}else if(inv[i].equals(new ItemStack(Material.IRON_BLOCK, x))){
			    	   			iron += x*9;
			    	   		}
					   }
			       }
			   }
		   }
		   return iron;
	}
	
	//Get Iron w/in Chests
	public double getIronChest(){
		iron = 0;
		Chunk[] c = world.getLoadedChunks();
		for(int i=0;i<c.length;i++){
			for(int x=0;x<c[i].getTileEntities().length;x++){
				if(c[i].getTileEntities()[x] instanceof Chest){
					Chest c1 = (Chest) c[i].getTileEntities()[x]; 
					ItemStack[] inv = c1.getInventory().getContents();
					for(int y=0;y<inv.length;y++){
						if(inv[y]!=null){
							for(int z=0;z<1792;z++){
								if(inv[y].equals(new ItemStack(Material.IRON_INGOT, z))){
					    	   		iron += z;
					    	   	}else if(inv[y].equals(new ItemStack(Material.IRON_BLOCK, z))){
					    	   		iron += z*9;
					    	   	}
							}
						
						}
					}
				}
			}
		}
		return iron;
	}
	//Get Iron w/in Furnaces
	public double getIronFurnace(){
		iron = 0;
		 Chunk[] c = world.getLoadedChunks();
			for(int i=0;i<c.length;i++){//loop through loaded chunks
				for(int x=0;x<c[i].getTileEntities().length;x++){//loop through tile entities within loaded chunks
					if(c[i].getTileEntities()[x] instanceof Furnace){
						Furnace f1 = (Furnace) c[i].getTileEntities()[x]; 
						ItemStack[] inv = f1.getInventory().getContents();
						for(int y=0;y<inv.length;y++){
							if(inv[y]!=null){
								for(int z=0;z<=64;z++){
									if(inv[y].equals(new ItemStack(Material.IRON_INGOT, z))){
							    	   	iron += z;
							    	}else if(inv[y].equals(new ItemStack(Material.IRON_BLOCK, z))){
							    	   	iron += z*9;
							    	}
								}
								
							}
						}
					}
				}
			}
			return iron;
	}
	//Get Iron w/in Ender Chests
	public double getIronEnderChest(){
		iron = 0;
		Player[] onlinePlayers = new Player[Bukkit.getOnlinePlayers().size()];
		Bukkit.getServer().getOnlinePlayers().toArray(onlinePlayers);
		
		for(int i=0;i<onlinePlayers.length;i++){
			ItemStack[] inv = onlinePlayers[i].getEnderChest().getContents();
			for(int y=0;y<inv.length;y++){
				if(inv[y]!=null){
					for(int z=0;z<=1728;z++){
						if(inv[y].equals(new ItemStack(Material.IRON_INGOT, z))){
							iron += z;
						}else if(inv[y].equals(new ItemStack(Material.IRON_BLOCK, z))){
							iron += z*9;
						}
					}
				}
			}
		}
		
		return iron;
	}
	
	//Get iron that is on the ground as an entity
	public double getIronOnGround(){
		iron = 0;
		Item onTheGround;
		List<org.bukkit.entity.Entity> entities = world.getEntities();
	
		for(int i=0;i<entities.size();i++){
			
			if(entities.get(i).getName().equals("item.item.ingotIron")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					iron+=onTheGround.getItemStack().getAmount();
				}
			}else if(entities.get(i).getName().equals("item.tile.blockIron")){
				if(entities.get(i) instanceof Item){
					onTheGround = (Item)entities.get(i);
					iron+=onTheGround.getItemStack().getAmount()*9;
				}
			}
			
		}
		return iron;
	}
	//***********************************************
}
