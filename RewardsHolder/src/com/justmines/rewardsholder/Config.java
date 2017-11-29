package com.justmines.rewardsholder;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Config {
	private RewardsHolder plugin;
	private String prefix;
	private String reload;
	private String wrongusage;
	private String playeronly;
	private String mustbeint;
	private String rewardreceived;
	private String rewardgiven;
	private String hushed;
	private String unhushed;

	public Config(RewardsHolder rh) {
		this.plugin = rh;
		load();
	}

	private void load() {
		createConfig();

		RewardsHolder.openspeed = this.plugin.getConfig().getInt("settings.openspeed");
		this.prefix = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("messages.prefix"));
		this.reload = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("messages.reload"));
		this.wrongusage = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.wrongusage"));
		this.playeronly = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.playeronly"));
		this.mustbeint = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.mustbeint"));
		this.rewardreceived = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.rewardreceived"));
		this.rewardgiven = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.rewardgiven"));
		this.hushed = ChatColor.translateAlternateColorCodes('&', this.plugin.getConfig().getString("messages.hushed"));
		this.unhushed = ChatColor.translateAlternateColorCodes('&',
				this.plugin.getConfig().getString("messages.unhushed"));
	}

	public void sms(Player player, String msg, boolean prefix) {
		if (prefix) {
			player.sendMessage(this.prefix + msg);
		} else {
			player.sendMessage(msg);
		}
	}

	public void sms(CommandSender player, String msg, boolean prefix) {
		if (prefix) {
			player.sendMessage(this.prefix + msg);
		} else {
			player.sendMessage(msg);
		}
	}

	public void createConfig() {
		try {
			if (!this.plugin.getDataFolder().exists()) {
				this.plugin.getDataFolder().mkdirs();
			}
			File file = new File(this.plugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				this.plugin.getLogger().info("Config.yml not found, creating!");
				this.plugin.saveDefaultConfig();
			} else {
				this.plugin.getLogger().info("Config.yml found, loading!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getReload() {
		return this.reload;
	}

	public void setReload(String reload) {
		this.reload = reload;
	}

	public String getWrongusage() {
		return this.wrongusage;
	}

	public void setWrongusage(String wrongusage) {
		this.wrongusage = wrongusage;
	}

	public String getPlayeronly() {
		return this.playeronly;
	}

	public void setPlayeronly(String playeronly) {
		this.playeronly = playeronly;
	}

	public String getMustbeint() {
		return this.mustbeint;
	}

	public void setMustbeint(String mustbeint) {
		this.mustbeint = mustbeint;
	}

	public String getRewardreceived() {
		return this.rewardreceived;
	}

	public void setRewardreceived(String rewardreceived) {
		this.rewardreceived = rewardreceived;
	}

	public String getRewardgiven() {
		return this.rewardgiven;
	}

	public void setRewardgiven(String rewardgiven) {
		this.rewardgiven = rewardgiven;
	}

	public String getHushed() {
		return this.hushed;
	}

	public void setHushed(String hushed) {
		this.hushed = hushed;
	}

	public String getUnhushed() {
		return this.unhushed;
	}

	public void setUnhushed(String unhushed) {
		this.unhushed = unhushed;
	}
}
