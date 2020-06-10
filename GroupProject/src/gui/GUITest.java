package gui;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Jonathan Bentham
 * @Version 12/03/2020
 */


class GUITest {

	public final static double TOLERANCE = 0.000001;

	// tests to check angle to vector method
	
	
	@Test
	public void testAngleToVector1() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		int check = 90;
		double[] expected = { 0, 1 };
		assertEquals(expected[1], tank1.angleToVector(check)[1]);
	}
	@Test
	public void testAngleToVector2() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		int check = 90;
		double[] expected = { 0, 1 };
		assertEquals(expected[0], tank1.angleToVector(check)[0], TOLERANCE);
	}
	
	@Test
	public void testAngleToVector3() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		int check = 0;
		double[] expected = { 1, 0 };
		assertEquals(expected[0], tank1.angleToVector(check)[0]);
	}
	@Test
	public void testAngleToVector4() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		int check = 0;
		double[] expected = { 1, 0 };
		assertEquals(expected[1], tank1.angleToVector(check)[1], TOLERANCE);
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
	// testing rotateXY for different angles
	
	@Test
	public void testRotateXY_1() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		double[] check = { 90.0, 90.0 };
		double[] expected = tank1.rotateXY(90.0, 90.0);
		assertEquals(check[0], expected[0], TOLERANCE);
	}
	
	@Test
	public void testRotateXY_2() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		double[] check = { 0.0, 90.0 };
		double[] expected = tank1.rotateXY(90.0, 0.0);
		assertEquals(check[0], expected[1], TOLERANCE);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// testing findDistance method when the distance is all 0 

	@Test
	public void testFindDistance_1() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		double check = 0.0;
		double expected = tank1.findDistance(0.0, 0.0,0.0,0.0);
		assertEquals(check, expected, TOLERANCE);
	}
	
	@Test
	public void testFindDistance_2() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		double check = Math.sqrt(50);
		double expected = tank1.findDistance(10.0, 10.0,5.0,5.0);
		assertEquals(check, expected, TOLERANCE);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	// tests to check missile collisions 
	@Test
	public void testCheckMissileCollisions_1() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		boolean expected = tank1.checkMissileCollisions(10.0,10.0,5.0);
		assertTrue(expected);
	}
	
	@Test
	public void testCheckMissileCollisions_2() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		boolean expected = tank1.checkMissileCollisions(1000.0,10.0,30.0);
		assertFalse(expected);
	}
	
	@Test
	public void testCheckMissileCollisions_3() {
		Tank tank1 = new Tank(0.0, 0.0, 0.0, 0.0, 0.0);
		boolean expected = tank1.checkMissileCollisions(10.0,20.0,30.0);
		assertTrue(expected);
	}
	

}
