/**
 * @author Jonathan Bentham
 * @Version 12/03/2020
 * 
 * StatisticsStub class to be used alongside the DatabaseTest class
 * 
 */

package com.tools.linkmode;

public class StatisticsStub extends Statistics {

	private String username;
	private int games_played;
	private int wins;
	private int losses;
	private String win_rate;
	private int total_kills;
	private int total_deaths;
	private String kill_death_ratio;

	/**
	 * added a constructor for Statistics to test the toString method
	 * 
	 * @param u
	 * @param gp
	 * @param w
	 * @param l
	 * @param wr
	 * @param tk
	 * @param td
	 * @param kdr
	 */
	public StatisticsStub(String u, int gp, int w, int l, String wr, int tk, int td, String kdr) {
		username = u;
		games_played = gp;
		this.wins = w;
		this.losses = l;
		this.win_rate = wr;
		this.total_kills = tk;
		this.total_deaths = td;
		this.kill_death_ratio = kdr;
	}

	public String toString() {
		return "Statistics{" + "username='" + username + '\'' + ", games_played=" + games_played + ", wins=" + wins
				+ ", losses=" + losses + ", win_rate='" + win_rate + '\'' + ", total_kills=" + total_kills
				+ ", total_deaths=" + total_deaths + ", kill_death_ratio='" + kill_death_ratio + '\'' + '}';
	}

	public static String kill_death_ratio(int tk, int td) {
		if (td == 0) {
			return String.format("%.2f", (double) tk);
		} else {
			return String.format("%.2f", (double) tk / td);
		}
	}

	public static String win_rate(int w, int l) {
		if ((w + l) == 0) {
			return "0.00";
		} else {
			return String.format("%.2f", (double) w / (w + l));
		}
	}

}
