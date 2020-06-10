package com.tools.linkmode;

public class Statistics {
	private String username;
	private int games_played;
	private int wins;
	private int losses;
	private String win_rate;
	private int total_kills;
	private int total_deaths;
	private String kill_death_ratio;

	@Override
	public String toString() {
		return "Statistics{" + "username='" + username + '\'' + ", games_played=" + games_played + ", wins=" + wins
				+ ", losses=" + losses + ", win_rate='" + win_rate + '\'' + ", total_kills=" + total_kills
				+ ", total_deaths=" + total_deaths + ", kill_death_ratio='" + kill_death_ratio + '\'' + '}';
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGames_played() {
		return games_played;
	}

	public void setGames_played(int games_played) {
		this.games_played = games_played;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public String getWin_rate() {
		return win_rate;
	}

	public void setWin_rate() {
		if ((getWins() + getLosses()) == 0) {
			this.win_rate = "0.00";
		} else {
			this.win_rate = String.format("%.2f", (double) getWins() / (getWins() + getLosses()));
		}
		
	}

	public int getTotal_kills() {
		return total_kills;
	}

	public void setTotal_kills(int total_kills) {
		this.total_kills = total_kills;
	}

	public int getTotal_deaths() {
		return total_deaths;
	}

	public void setTotal_deaths(int total_deaths) {
		this.total_deaths = total_deaths;
	}

	public String getKill_death_ratio() {
		return kill_death_ratio;
	}

	public void setKill_death_ratio() {
		if (getTotal_deaths() == 0) {
			this.kill_death_ratio = String.format("%.2f", (double) getTotal_kills());
		} else {
			this.kill_death_ratio = String.format("%.2f", (double) getTotal_kills() / getTotal_deaths());
		}
		
	}

}
