package com.justmines.rewardsholder;

import java.util.List;

public class Reward {
	private String name;
	private List<String> commands;
	private String displayName;

	public Reward(String name, String displayName, List<String> commands) {
		this.name = name;
		this.displayName = displayName;
		this.commands = commands;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCommands() {
		return this.commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
