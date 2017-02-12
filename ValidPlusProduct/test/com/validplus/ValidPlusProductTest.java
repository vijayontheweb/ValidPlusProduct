package com.validplus;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ValidPlusProductTest {
	protected ValidPlusProduct validPlusProduct;
	
	@Before
	public void setUp() throws Exception{
		validPlusProduct = ValidPlusProduct.getInstance();
	}
	
	@Test
	public void testGivenSampleInput0() {		
		System.out.println("\nTEST CASE : RUNNING SAMPLE 0");		
		assertEquals(5,validPlusProduct.getProduct(".\\resource\\Sample0.txt"));							
	}
	
	@Test
	public void testGivenSampleInput1() {		
		System.out.println("\nTEST CASE : RUNNING SAMPLE 1");
		assertEquals(25,validPlusProduct.getProduct(".\\resource\\Sample1.txt"));							
	}
	
	@Test
	public void testMyOwnInputHugeGrid() {		
		System.out.println("\nTEST CASE : RUNNING MY OWN SAMPLE");
		assertEquals(377,validPlusProduct.getProduct(".\\resource\\Sample4.txt"));							
	}
	
	@Test(expected = RowSizeOutOfBoundsException.class)
	public void testRowOutOfBounds() {		
		System.out.println("\nTEST CASE : RUNNING ROW OUT OF BOUNDS");
		validPlusProduct.getProduct(".\\resource\\Sample2.txt");							
	}
	
	@Test(expected = ColSizeOutOfBoundsException.class)
	public void testColOutOfBounds() {		
		System.out.println("\nTEST CASE : RUNNING COLUMN OUT OF BOUNDS");		
		validPlusProduct.getProduct(".\\resource\\Sample3.txt");							
	}
}
