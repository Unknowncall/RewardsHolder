package com.justmines.rewardsholder;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.entity.Player;

public class Task implements Runnable {
	private RewardsHolder plugin;

	public Task(RewardsHolder rewardsHolder) {
		this.plugin = rewardsHolder;
	}

	public void run() {
		ArrayList<Map.Entry<Player, Reward>> tmp = new ArrayList<Map.Entry<Player, Reward>>();
		for (Map.Entry<Player, Reward> entry : RewardsHolder.openAll.entrySet()) {
			Player player = (Player) entry.getKey();
			Reward reward = (Reward) entry.getValue();
			if (RewardsHolder.getRewardAmount(this.plugin, player, reward) <= 0) {
				tmp.add(entry);
			} else {
				this.plugin.redeemReward(player, reward);
			}
		}
		for (Map.Entry<Player, Reward> entry : tmp) {
			RewardsHolder.openAll.remove(entry.getKey());
		}
	}
}
