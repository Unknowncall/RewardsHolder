package com.justmines.rewardsholder;

import org.bukkit.inventory.ItemStack;

public class GUIItem extends ItemStack {
	private ItemStack is;
	private int slot;
	private String itemName;

	public GUIItem(ItemStack is, String itemName, int slot) {
		this.is = is;
		this.itemName = itemName;
		this.slot = slot;
	}

	public ItemStack getIs() {
		return this.is;
	}

	public void setIs(ItemStack is) {
		this.is = is;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
