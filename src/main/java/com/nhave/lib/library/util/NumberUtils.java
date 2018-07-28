package com.nhave.lib.library.util;

public class NumberUtils
{
	public static enum MeasurementUnit
	{
		FEMTO("Femto", "f", 0.000000000000001D),
		PICO("Pico", "p", 0.000000000001D),
		NANO("Nano", "n", 0.000000001D),
		MICRO("Micro", "u", 0.000001D),
		MILLI("Milli", "m", 0.001D),
		BASE("", "", 1),
		KILO("Kilo", "K", 1000D),
		MEGA("Mega", "M", 1000000D),
		GIGA("Giga", "G", 1000000000D),
		TERA("Tera", "T", 1000000000000D),
		PETA("Peta", "P", 1000000000000000D),
		EXA("Exa", "E", 1000000000000000000D),
		ZETTA("Zetta", "Z", 1000000000000000000000D),
		YOTTA("Yotta", "Y", 1000000000000000000000000D);
		
		/** long name for the unit */
		public String name;
		
		/** short unit version of the unit */
		public String symbol;
		
		/** Point by which a number is consider to be of this unit */
		public double value;
		
		private MeasurementUnit(String s, String s1, double v)
		{
			name = s;
			symbol = s1;
			value = v;
		}
		
		public String getName(boolean getShort)
		{
			if(getShort)
			{
				return symbol;
			}
			else {
				return name;
			}
		}
		
		public double process(double d)
		{
			return d / value;
		}
		
		public boolean above(double d)
		{
			return d > value;
		}
		
		public boolean below(double d)
		{
			return d < value;
		}
	}
	
	public static String getDisplay(double value, int decimalPlaces, boolean isShort)
	{
		String prefix = "";
		
		if(value < 0)
		{
			value = Math.abs(value);
			prefix = "-";
		}
		
		if(value == 0)
		{
			return String.format ("%." + decimalPlaces + "f", value) + "";
		}
		else
		{
			for(int i = 0; i < MeasurementUnit.values().length; i++)
			{
				MeasurementUnit lowerMeasure = MeasurementUnit.values()[i];
				
				if(lowerMeasure.below(value) && lowerMeasure.ordinal() == 0)
				{
					return prefix + String.format ("%." + decimalPlaces + "f", roundDecimals(lowerMeasure.process(value), decimalPlaces)) + lowerMeasure.getName(isShort);
				}
				
				if(lowerMeasure.ordinal() + 1 >= MeasurementUnit.values().length)
				{
					return prefix + String.format ("%." + decimalPlaces + "f", roundDecimals(lowerMeasure.process(value), decimalPlaces)) + lowerMeasure.getName(isShort);
				}
				
				MeasurementUnit upperMeasure = MeasurementUnit.values()[i + 1];
				
				if((lowerMeasure.above(value) && upperMeasure.below(value)) || lowerMeasure.value == value)
				{
					return prefix + String.format ("%." + decimalPlaces + "f", roundDecimals(lowerMeasure.process(value), decimalPlaces)) + lowerMeasure.getName(isShort);
				}
			}
		}
		
		return prefix + String.format ("%." + decimalPlaces + "f", roundDecimals(value, decimalPlaces));
	}
	
	public static String getDisplayShort(double value)
	{
		return getDisplay(value, 2, true);
	}
	
	public static String getDisplayShort(double value, int decimalPlaces)
	{
		return getDisplay(value, decimalPlaces, true);
	}
	
	public static double roundDecimals(double d, int decimalPlaces)
	{
		int j = (int)(d*Math.pow(10, decimalPlaces));
		return j/Math.pow(10, decimalPlaces);
	}
	
	public static double roundDecimals(double d)
	{
		return roundDecimals(d, 2);
	}
}