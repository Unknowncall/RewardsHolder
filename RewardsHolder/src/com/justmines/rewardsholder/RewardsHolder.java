package com.justmines.rewardsholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class RewardsHolder extends JavaPlugin {
	public ArrayList<Reward> rewards;
	public Config config;
	public static Map<Player, Reward> openAll = new HashMap<Player, Reward>();
	public static ArrayList<Player> hushed = new ArrayList<Player>();
	public static int openspeed;

	public void onEnable() {
		this.config = new Config(this);

		loadRewards();

		getCommand("rh").setExecutor(new Commands(this));

		getServer().getPluginManager().registerEvents(new InvClickEvent(this), this);

		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, new Task(this), 0L, 10L);
	}

	private void loadRewards() {
		this.rewards = new ArrayList<Reward>();
		for (String s : getConfig().getConfigurationSection("rewards").getKeys(false)) {
			String rewardName = getConfig().getString("rewards." + s + ".name");
			List<String> commands = getConfig().getStringList("rewards." + s + ".commands");
			this.rewards.add(new Reward(s, rewardName, commands));
		}
	}

	public void onDisable() {
	}

	public Reward getReward(String name) {
		for (Reward r : this.rewards) {
			if (r.getName().equalsIgnoreCase(name)) {
				return r;
			}
		}
		return null;
	}

	public void reload() {
		reloadConfig();
		saveConfig();
		loadRewards();
	}

	public void addReward(Player player, Reward reward, int amount, CommandSender giver) {
		if (getRewardAmount(this, player, reward) == -1) {
			int level = amount;
			getConfig().set("data." + player.getUniqueId().toString() + "." + reward.getName(), Integer.valueOf(level));
		} else {
			int level = getRewardAmount(this, player, reward);
			int newLevel = level + amount;
			getConfig().set("data." + player.getUniqueId().toString() + "." + reward.getName(),
					Integer.valueOf(newLevel));
		}
		saveConfig();
		if (!hushed.contains(player)) {
			String s = this.config.getRewardgiven();
			s = s.replace("{amount}", "" + amount);
			s = s.replace("{reward}", ChatColor.translateAlternateColorCodes('&', reward.getDisplayName()));
			s = s.replace("{player}", player.getName());
			this.config.sms(giver, s, true);
			s = this.config.getRewardreceived();
			s = s.replace("{amount}", "" + amount);
			s = s.replace("{reward}", ChatColor.translateAlternateColorCodes('&', reward.getDisplayName()));
			this.config.sms(player, s, true);
		}
	}

	public static int getRewardAmount(RewardsHolder plugin, Player player, Reward reward) {
		return plugin.getConfig().getInt("data." + player.getUniqueId().toString() + "." + reward.getName());
	}

	public void openGUI(Player player) {
		int slots = getConfig().getInt("gui.settings.rows") * 9;
		Inventory inv = Bukkit.createInventory(player, slots, ChatColor.translateAlternateColorCodes('&',
				getConfig().getString("gui.settings.title").replace("{player}", player.getName())));

		ArrayList<GUIItem> isList = new ArrayList<GUIItem>();
		for (String s : getConfig().getConfigurationSection("gui.items").getKeys(false)) {
			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(getConfig().getInt("gui.items." + s + ".item.id"));
			ItemStack is = new ItemStack(material);
			is.setAmount(1);
			is.setDurability((short) getConfig().getInt("gui.items." + s + ".item.dura"));
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("gui.items." + s + ".item.name")));
			List<String> tmp = new ArrayList<String>();
			for (String s2 : getConfig().getStringList("gui.items." + s + ".item.lore")) {
				int amount = 0;
				if (getRewardAmount(this, player, getReward(s)) <= 0) {
					amount = 0;
				} else {
					amount = getRewardAmount(this, player, getReward(s));
				}
				s2 = s2.replace("{amount}", "" + amount);
				tmp.add(ChatColor.translateAlternateColorCodes('&', s2));
			}
			im.setLore(tmp);
			is.setItemMeta(im);

			isList.add(new GUIItem(is, s, getConfig().getInt("gui.items." + s + ".slot") - 1));
		}
		for (GUIItem is : isList) {
			inv.setItem(is.getSlot(), is.getIs());
		}
		player.openInventory(inv);
	}

	public boolean isInv(Inventory inv, Player player) {
		String invName = inv.getName();
		invName = invName.replace(player.getName(), "{player}");
		invName = ChatColor.stripColor(invName);
		String configInvName = ChatColor
				.stripColor(ChatColor.translateAlternateColorCodes('&', getConfig().getString("gui.settings.title")));
		if (invName.equals(configInvName)) {
			return true;
		}
		return false;
	}

	public boolean canRedeem(Player player, Reward reward) {
		if (getRewardAmount(this, player, reward) <= 0) {
			return false;
		}
		return true;
	}

	public Reward getRewardSlot(int slot) {
		for (String s : getConfig().getConfigurationSection("gui.items").getKeys(false)) {
			if (getConfig().getInt("gui.items." + s + ".slot") - 1 == slot) {
				return getReward(s);
			}
		}
		return null;
	}

	public void redeemReward(Player player, Reward reward) {
		getConfig().set("data." + player.getUniqueId().toString() + "." + reward.getName(), Integer
				.valueOf(getConfig().getInt("data." + player.getUniqueId().toString() + "." + reward.getName())));
		saveConfig();
		for (String s : reward.getCommands()) {
			s = s.replace("{player}", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
		}
	}

	public void redeemAllRewards(Player player, Reward reward) {
		openAll.put(player, reward);
	}

	public void addReward(Player player, Reward reward, int amount) {
		if (getRewardAmount(this, player, reward) <= 0) {
			int level = amount;
			getConfig().set("data." + player.getUniqueId().toString() + "." + reward.getName(), Integer.valueOf(level));
		} else {
			int level = getRewardAmount(this, player, reward);
			int newLevel = level + amount;
			getConfig().set("data." + player.getUniqueId().toString() + "." + reward.getName(),
					Integer.valueOf(newLevel));
		}
		saveConfig();
		String s = this.config.getRewardgiven();
		s = s.replace("{amount}", "" + amount);
		s = s.replace("{reward}", ChatColor.translateAlternateColorCodes('&', reward.getDisplayName()));
		s = s.replace("{player}", player.getName());
	}
}
