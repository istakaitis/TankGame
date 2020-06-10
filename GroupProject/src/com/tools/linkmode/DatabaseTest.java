package com.tools.linkmode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gui.Tank;

class DatabaseTest {

	private StatisticsStub p1;
	private StatisticsStub p2;
	private StatisticsStub p3;

	@BeforeEach
	public void beforeEach() {
            //    public StatisticsStub(String u, int gp, int w, int l, String wr, int tk, int td, String kdr)
            p1 = new StatisticsStub("a", 4, 4, 4, "0.7", 4, 4, "0.7");
            p2 = new StatisticsStub("", 0, 0, 0, "", 0, 0, "");
            p3 = new StatisticsStub("797979797", 100000, 100000, 100000, "asdfasfjlkj", 900000, 900000, "");
	}	
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// testing the statistics to string method 
	@Test
	public void testStatistics_1() {
		String check = "Statistics{" + "username='" + "a" + '\'' + ", games_played=" + 4 + ", wins=" + 4
				+ ", losses=" + 4 + ", win_rate='" + "0.7" + '\'' + ", total_kills=" + 4
				+ ", total_deaths=" + 4 + ", kill_death_ratio='" + "0.7" + '\'' + '}';
		String expected = p1.toString();
		assertEquals(check, expected);
	}

	@Test
	public void testStatistics_2() {
		String check = "Statistics{" + "username='" + "" + '\'' + ", games_played=" + 0 + ", wins=" + 0
				+ ", losses=" + 0 + ", win_rate='" + "" + '\'' + ", total_kills=" + 0
				+ ", total_deaths=" + 0 + ", kill_death_ratio='" + "" + '\'' + '}';
		String expected = p2.toString();
		assertEquals(check, expected);
	}

	@Test
	public void testStatistics_3() {
		String check = "Statistics{" + "username='" + "797979797" + '\'' + ", games_played=" + 100000 + ", wins=" + 100000
				+ ", losses=" + 100000 + ", win_rate='" + "asdfasfjlkj" + '\'' + ", total_kills=" + 900000
				+ ", total_deaths=" + 900000 + ", kill_death_ratio='" + "" + '\'' + '}';
		String expected = p3.toString();
		assertEquals(check, expected);
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// test kill_death ratio method using statisticsStub
	@Test
	public void testKDR_1() {
		String check = "5.00";
		String expected = StatisticsStub.kill_death_ratio(10, 2);
		assertEquals(check, expected);
	}
	
	@Test
	public void testKDR_2() {
		String check = "100.00";
		String expected = StatisticsStub.kill_death_ratio(100, 0);
		assertEquals(check, expected);
	}
	
	@Test
	public void testKDR_3() {
		String check = "7.00";
		String expected = StatisticsStub.kill_death_ratio(14, 2);
		assertEquals(check, expected);
	}
	
	@Test
	public void testKDR_4() {
		String check = "0.05";
		String expected = StatisticsStub.kill_death_ratio(3, 56);
		assertEquals(check, expected);
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
	
	// test the winrate method using a statistics stub
	@Test
	public void testWR_1() {
		String check = "0.83";
		String expected = StatisticsStub.win_rate(10, 2);
		assertEquals(check, expected);
	}
	
	@Test
	public void testWR_2() {
		String check = "1.00";
		String expected = StatisticsStub.win_rate(10, 0);
		assertEquals(check, expected);
	}
	
	@Test
	public void testWR_3() {
		String check = "0.33";
		String expected = StatisticsStub.win_rate(17, 35);
		assertEquals(check, expected);
	}
	

	
}
