package com.justmines.rewardsholder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private RewardsHolder plugin;

	public Commands(RewardsHolder rewardsHolder) {
		this.plugin = rewardsHolder;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 4) {
			if (!args[0].equalsIgnoreCase("give")) {
				this.plugin.config.sms(sender, this.plugin.config.getWrongusage(), true);
				return true;
			}
			if (!sender.hasPermission("rh.give")) {
				sender.sendMessage(ChatColor.RED + "No Permission!");
				return true;
			}
			if (args[1].equalsIgnoreCase("*")) {
				if (this.plugin.getReward(args[2]) == null) {
					sender.sendMessage(ChatColor.RED + "Invalid Reward!");
					return true;
				}
				Reward reward = this.plugin.getReward(args[2]);
				if (!isInt(args[3])) {
					this.plugin.config.sms(sender, this.plugin.config.getMustbeint(), true);
					return true;
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					this.plugin.addReward(player, reward, Integer.parseInt(args[3]), sender);
				}
				return true;
			}
			if (Bukkit.getPlayer(args[1]) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid Player!");
				return true;
			}
			Player player = Bukkit.getPlayer(args[1]);
			if (this.plugin.getReward(args[2]) == null) {
				sender.sendMessage(ChatColor.RED + "Invalid Reward!");
				return true;
			}
			Reward reward = this.plugin.getReward(args[2]);
			if (!isInt(args[3])) {
				this.plugin.config.sms(sender, this.plugin.config.getMustbeint(), true);
				return true;
			}
			this.plugin.addReward(player, reward, Integer.parseInt(args[3]), sender);
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("rh.reload")) {
					sender.sendMessage(ChatColor.RED + "No Permission!");
					return true;
				}
				this.plugin.reload();
				this.plugin.config.sms(sender, this.plugin.config.getReload(), true);
				return true;
			}
			if ((args[0].equalsIgnoreCase("hush")) || (args[0].equalsIgnoreCase("stfu"))
					|| (args[0].equalsIgnoreCase("ignore")) || (args[0].equalsIgnoreCase("shutup"))) {
				if (!sender.hasPermission("rh.hush")) {
					sender.sendMessage(ChatColor.RED + "No Permission!");
					return true;
				}
				if (!(sender instanceof Player)) {
					this.plugin.config.sms(sender, this.plugin.config.getPlayeronly(), false);
					return true;
				}
				Player player = (Player) sender;
				if (RewardsHolder.hushed.contains(player)) {
					RewardsHolder.hushed.remove(player);
					this.plugin.config.sms(sender, this.plugin.config.getUnhushed(), true);
					return true;
				}
				RewardsHolder.hushed.add(player);
				this.plugin.config.sms(sender, this.plugin.config.getHushed(), true);
				return true;
			}
			this.plugin.config.sms(sender, this.plugin.config.getWrongusage(), true);
			return true;
		}
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				this.plugin.config.sms(sender, this.plugin.config.getPlayeronly(), false);
				return true;
			}
			this.plugin.openGUI((Player) sender);
			return true;
		}
		this.plugin.config.sms(sender, this.plugin.config.getWrongusage(), true);
		return true;
	}

	private boolean isInt(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}
}
