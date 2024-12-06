package me.mchiappinam.pdghlog;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public static boolean logSTAFFOnly = false;
    public static String mysql_url;
    public static String mysql_user;
    public static String mysql_pass;
    public static String serverName;
    public static String tableGeneral;
    public static String tableStaff;
    public static String dateFormat = "MM/dd/yyyy-HH:mm:ss";
    public static List<String> staffPermissions;
	
	public void onEnable() {
	    getServer().getPluginManager().registerEvents(new Listeners(this), this);

		File file = new File(getDataFolder(),"config.yml");
		if(!file.exists()) {
			try {
				saveResource("config_template.yml",false);
				File file2 = new File(getDataFolder(),"config_template.yml");
				file2.renameTo(new File(getDataFolder(),"config.yml"));
			}catch(Exception e) {}
		}
		logSTAFFOnly=getConfig().getBoolean("logSTAFFOnly");
		mysql_url="jdbc:mysql://"+getConfig().getString("mySQL.host")+":"+getConfig().getString("mySQL.port")+"/"+getConfig().getString("mySQL.database");
		mysql_user=getConfig().getString("mySQL.username");
		mysql_pass=getConfig().getString("mySQL.password");
		tableGeneral=getConfig().getString("mySQL.tableGeneral");
		tableStaff=getConfig().getString("mySQL.tableStaff");
		serverName=getConfig().getString("serverName");
		dateFormat=getConfig().getString("dateFormat");
		staffPermissions=getConfig().getStringList("staffPermissions");
		try {
			Connection con = DriverManager.getConnection(mysql_url,mysql_user,mysql_pass);
			if (con == null) {
				getLogger().warning("ERRO: Conexao ao banco de dados MySQL falhou!");
				getServer().getPluginManager().disablePlugin(this);
			}else{
				Statement st = con.createStatement();
				st.execute("CREATE TABLE IF NOT EXISTS `"+tableGeneral+"` ( `id` INT NOT NULL AUTO_INCREMENT, `server` text, `nick` text, `type` text, `log` text, `world` text, `location` text, `dateTime` text, PRIMARY KEY (`id`))");
				st.execute("CREATE TABLE IF NOT EXISTS `"+tableStaff+"` ( `id` INT NOT NULL AUTO_INCREMENT, `server` text, `nick` text, `role` text, `type` text, `log` text, `world` text, `location` text, `dateTime` text, PRIMARY KEY (`id`))");
				st.close();
				getServer().getConsoleSender().sendMessage("§3[PDGHLog] §3Conectado ao banco de dados MySQL!");
			}
			con.close();
		}catch (SQLException e) {
			getLogger().warning("ERRO: Conexao ao banco de dados MySQL falhou!");
			getLogger().warning("ERRO: "+e.toString());
			getServer().getPluginManager().disablePlugin(this);
		}
		getServer().getConsoleSender().sendMessage("§3[PDGHLog] §2ativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHLog] §2Acesse: http://pdgh.com.br/");
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHLog] §2desativado - Plugin by: mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHLog] §2Acesse: http://pdgh.com.br/");
	}
    
    public static String calendario() { //String dateTime=calendario().trim();
		Calendar agora = Calendar.getInstance();
		SimpleDateFormat gdf = new SimpleDateFormat(dateFormat);
        return gdf.format(agora.getTime());
    }
    
    public static boolean staff(Player p) {
    	for (String permission : staffPermissions) {
    		if(p.hasPermission(permission.split(":")[0])) {
    			return true;
    		}
		}
    	return false;
    }
    
    public static String getRole(Player p) {
    	for (String permission : staffPermissions) {
    		if(p.hasPermission(permission.split(":")[0])) {
    			return permission.split(":")[1];
    		}
		}
    	return "";
    }
	
	public static void add(Player p, String type, String log) {
		if(logSTAFFOnly) {
	        if(staff(p)) {
	        	Threads t2 = new Threads("staff",p.getName().trim(),getRole(p).trim(),type.trim(),log.trim(),p.getWorld().getName().trim(),p.getLocation().getBlockX()+","+p.getLocation().getBlockY()+","+p.getLocation().getBlockZ(),calendario().trim());
				t2.start();
	        }
			return;
		}
		if(p==null) {
			Threads t = new Threads("general","",type.trim(),log.trim(),"","",calendario().trim());
			t.start();
			return;
		}
		Threads t = new Threads("general",p.getName().trim(),type.trim(),log.trim(),p.getWorld().getName().trim(),p.getLocation().getBlockX()+","+p.getLocation().getBlockY()+","+p.getLocation().getBlockZ(),calendario().trim());
		t.start();
        if(staff(p)) {
        	Threads t2 = new Threads("staff",p.getName().trim(),getRole(p).trim(),type.trim(),log.trim(),p.getWorld().getName().trim(),p.getLocation().getBlockX()+","+p.getLocation().getBlockY()+","+p.getLocation().getBlockZ(),calendario().trim());
			t2.start();
        }
	}
}