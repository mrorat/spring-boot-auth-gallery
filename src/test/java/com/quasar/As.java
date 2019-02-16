package com.quasar;

import org.junit.Test;

public class As
{

	@Test
	public void test()
	{
		Integer a = -129, b = -129;
		int a1 = -129, b1 = -129;
		System.out.println(a == b); // false
		System.out.println(a1 == b1); // false
		Integer c = 100, d = 100;
		System.out.println(c == d); // true  
	}

}
