package com.baselib.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * 
 * @Auhtor: Mustafa   
 */

public final class NumberUtil
{
	public static String CLASS_NAME = NumberUtil.class.getName();

	private NumberUtil()
	{
		super();
	}

	public static String truncateNumber(String number, int power, int round)
	{
		try
		{
			if (number != null)
			{
				BigDecimal decimal = new BigDecimal(number);
				BigDecimal divider = new BigDecimal(Math.pow(10, power));
				return decimal.divide(divider, 0, round).toString();
			}
		}
		catch (NumberFormatException e)
		{
			return number;
		}
		return number;
	}

	public static String formatNumber(String number, String pattern, Locale locale)
	{
		try
		{
			if (number != null)
			{
				BigDecimal decimal = new BigDecimal(number);
				decimal.setScale(4, BigDecimal.ROUND_DOWN);
				DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
				DecimalFormat format = new DecimalFormat(pattern, dfs);
				return format.format(decimal);
			}
		}
		catch (NumberFormatException e)
		{
			return number;
		}
		return number;
	}

	public static String formatNumber(Number number, String pattern, Locale locale)
	{
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		DecimalFormat format = new DecimalFormat(pattern, dfs);
		return format.format(number);
	}

	public static Number add(Number number1, Number number2)
	{
		if (number1 == null && number2 == null)
		{
			return null;
		}

		if (number1 == null)
		{
			return number2;
		}

		if (number2 == null)
		{
			return number1;
		}

		return new Double(number1.doubleValue() + number2.doubleValue());
	}

	public static String addNumbers(String number1, String number2)
	{
		BigDecimal decimal1 = null;
		BigDecimal decimal2 = null;
		if (number1 == null)
		{
			decimal1 = new BigDecimal(0);
		}
		else
		{
			decimal1 = new BigDecimal(number1);
		}

		if (number2 == null)
		{
			decimal2 = new BigDecimal(0);
		}
		else
		{
			decimal2 = new BigDecimal(number2);
		}
		return decimal1.add(decimal2).toString();
	}

	public static String divideNumbers(String number1, String number2)
		throws Exception
	{
		double num1 = Double.parseDouble(number1);
		double num2 = Double.parseDouble(number2);

		if (num2 == 0)
		{
			throw new Exception("division by zero error");
		}

		return "" + num1 / num2;
	}


}
