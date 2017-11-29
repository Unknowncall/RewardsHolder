package com.justmines.rewardsholder;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickEvent implements Listener {
	private RewardsHolder plugin;

	public InvClickEvent(RewardsHolder rewardsHolder) {
		this.plugin = rewardsHolder;
	}

	@EventHandler
	public void invClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) {
			return;
		}
		if (!this.plugin.isInv(e.getInventory(), (Player) e.getWhoClicked())) {
			return;
		}
		if (this.plugin.getRewardSlot(e.getSlot()) == null) {
			e.setCancelled(true);
			return;
		}
		Reward reward = this.plugin.getRewardSlot(e.getSlot());
		if (!this.plugin.canRedeem((Player) e.getWhoClicked(), reward)) {
			e.setCancelled(true);
			return;
		}
		if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
			e.setCancelled(true);
			this.plugin.redeemAllRewards((Player) e.getWhoClicked(), reward);
			this.plugin.openGUI((Player) e.getWhoClicked());
		} else {
			e.setCancelled(true);
			this.plugin.redeemReward((Player) e.getWhoClicked(), reward);
			this.plugin.openGUI((Player) e.getWhoClicked());
		}
	}
}
