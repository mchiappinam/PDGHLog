package me.mchiappinam.pdghlog;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class Listeners implements Listener {
	
	//private Main plugin;
	public Listeners(Main main) {
		//plugin=main;
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent e) {
		Main.add(e.getPlayer(), "join", e.getPlayer().getAddress().getAddress().getHostAddress().replaceAll("/", ""));
	}
	  
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Main.add(e.getPlayer(), "quit", "");
	}
		
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent e) {
		Main.add(e.getPlayer(), "quit", "");
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		String s;
		s=e.getItemDrop().getItemStack().getTypeId()+":"+e.getItemDrop().getItemStack().getDurability()+" quantity:"+e.getItemDrop().getItemStack().getAmount();
		if(e.getItemDrop().getItemStack().hasItemMeta()) {
			if(e.getItemDrop().getItemStack().getItemMeta().hasDisplayName())
				s=s+" displayname:"+e.getItemDrop().getItemStack().getItemMeta().getDisplayName();
			if(e.getItemDrop().getItemStack().getItemMeta().hasLore())
				s=s+" lore:"+e.getItemDrop().getItemStack().getItemMeta().getLore().toString();
			if(e.getItemDrop().getItemStack().getItemMeta().hasEnchants())
				s=s+" enchants:"+e.getItemDrop().getItemStack().getItemMeta().getEnchants().toString();
		}
		Main.add(e.getPlayer(), "itemdrop", s);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerPickItem(PlayerPickupItemEvent e) {
		String s;
		s=e.getItem().getItemStack().getTypeId()+":"+e.getItem().getItemStack().getDurability()+" quantity:"+e.getItem().getItemStack().getAmount();
		if(e.getItem().getItemStack().hasItemMeta()) {
			if(e.getItem().getItemStack().getItemMeta().hasDisplayName())
				s=s+" displayname:"+e.getItem().getItemStack().getItemMeta().getDisplayName();
			if(e.getItem().getItemStack().getItemMeta().hasLore())
				s=s+" lore:"+e.getItem().getItemStack().getItemMeta().getLore().toString();
			if(e.getItem().getItemStack().getItemMeta().hasEnchants())
				s=s+" enchants:"+e.getItem().getItemStack().getItemMeta().getEnchants().toString();
		}
		Main.add(e.getPlayer(), "itempick", s);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Main.add(e.getPlayer(), "chat", e.getMessage());
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerCmd(PlayerCommandPreprocessEvent e) {
		Main.add(e.getPlayer(), "command", e.getMessage());
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onEnchant(EnchantItemEvent e) {
		Main.add(e.getEnchanter(), "enchant", e.getItem()+", "+e.getEnchantsToAdd()+". EXP: "+e.getExpLevelCost());
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onSign(SignChangeEvent e) {
	    String[] lines = e.getLines();
		Main.add(e.getPlayer(), "sign", "["+lines[0]+"]["+lines[1]+"]["+lines[2]+"]["+lines[3]+"]");
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerConsoleCommand(ServerCommandEvent e) {
		Main.add(null, "console", e.getCommand());
	}

	@EventHandler
	private void onDeath(PlayerDeathEvent e) {
		if(e.getEntity().getKiller() instanceof Player) {
	    	Main.add(e.getEntity().getKiller(), "killed", e.getEntity().getPlayer().getName());
	    	Main.add(e.getEntity().getPlayer(), "deathby", e.getEntity().getKiller().getName());
	    }else{
	    	Main.add(e.getEntity().getPlayer(), "death", "");
	    }
	}
}