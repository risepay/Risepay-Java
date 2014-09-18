/*
Copyright 2009-2014 Igor Polevoy

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/


package com.risepay.net.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.*;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.risepay.net.common.Util.bytes;

/**
 * Convenience class for type conversions. 
 *
 * @author Igor Polevoy
 */
public class Convert {

    /**
     * Returns string representation of an object, including {@link java.sql.Clob}.
     * For large CLOBs, be careful because this will load an entire CLOB in memory as <code>java.lang.String</code>. 
     *
     * @param value value to convert.
     * @return string representation of an object, including {@link java.sql.Clob}.
     */
    public static String toString(Object value) {
        if(value == null) {
            return null;
        } else if(value instanceof Clob) {
            return clobToString((Clob) value);
        } else {
            return value.toString();
        }
    }

    /*
     * Converts clob to string
     */
    private static String clobToString(Clob clob) {
        try {
            Reader r = clob.getCharacterStream();
            StringWriter sw = new StringWriter();
            copyStream(r, sw);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Copying stream
     */
    private static void copyStream(Reader r, Writer w) throws IOException {
        char buffer[] = new char[4096];
        for (int n = 0; -1 != (n = r.read(buffer));) {
            w.write(buffer, 0, n);
        }
    }

    /**
     * Returns true if the value is any numeric type and has a value of 1, or
     * if string type has a value of 'y', 't', 'true' or 'yes'. Otherwise, return false.
     *
     * @param value value to convert
     * @return true if the value is any numeric type and has a value of 1, or
     * if string type has a value of 'y', 't', 'true' or 'yes'. Otherwise, return false.
     */
    public static Boolean toBoolean(Object value){
        if (value == null) {
            return false;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof BigDecimal) {
            return value.equals(BigDecimal.ONE);
        } else if (value instanceof Number) {
            return ((Number)value).intValue() == 1;
        } else if (value instanceof Character) {
            return value.equals('y') || value.equals('Y')
                    || value.equals('t') || value.equals('T');

        }else return value.toString().equalsIgnoreCase("yes")
                || value.toString().equalsIgnoreCase("true") 
                || value.toString().equalsIgnoreCase("y")
                || value.toString().equalsIgnoreCase("t")
                || Boolean.parseBoolean(value.toString());
    }


    /**
     * Expects a <code>java.sql.Date</code>, <code>java.sql.Timestamp</code>, <code>java.sql.Time</code>, <code>java.util.Date</code> or
     * any object whose toString method has this format: <code>yyyy-mm-dd</code>.
     *
     * @param value argument that is possible to convert to <code>java.sql.Date</code>.  
     * @return <code>java.sql.Date</code> instance representing input value.
     */
    public static java.sql.Date toSqlDate(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        } else if (value instanceof Timestamp) {
            return new java.sql.Date(((Timestamp) value).getTime());
        } else if (value instanceof java.util.Date) {
            return new java.sql.Date(((Date) value).getTime());
        } else if (value instanceof Time) {
            return new java.sql.Date(((Time) value).getTime());
        } else {
            try {
                return java.sql.Date.valueOf(value.toString());
            } catch (IllegalArgumentException e) {
                throw new ConversionException("failed to convert: '" + value + "' to java.sql.Date", e);
            }
        }
    }

    /**
     * Expects a <code>java.sql.Date</code>, <code>java.sql.Timestamp</code>, <code>java.sql.Time</code>, <code>java.util.Date</code> or
     * string with format: <code>yyyy-mm-dd</code>. This method will also truncate hours, minutes, seconds and
     * milliseconds to zeros, to conform with JDBC spec:
     * <q href="http://download.oracle.com/javase/6/docs/api/java/sql/Date.html">http://download.oracle.com/javase/6/docs/api/java/sql/Date.html</a>.
     *
     * @param value argument that is possible to convert to <code>java.sql.Date</code>: <code>java.sql.Date</code>,
     * <code>java.sql.Timestamp</code>, <code>java.sql.Time</code>, <code>java.util.Date</code> or any object with toString() == <code>yyyy-mm-dd</code>.
     * @return <code>java.sql.Date</code> instance representing input value.
     */
    public static java.sql.Date truncateToSqlDate(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        } else if (value instanceof Timestamp) {
            return utilDate2sqlDate(((Timestamp) value).getTime());
        } else if (value instanceof java.util.Date) {
            return utilDate2sqlDate(((Date) value).getTime());
        } else if (value instanceof Time) {
            return utilDate2sqlDate(((Time) value).getTime());
        } else {
            try {
                return java.sql.Date.valueOf(value.toString());
            } catch (IllegalArgumentException e) {
                throw new ConversionException("failed to convert: '" + value + "' to java.sql.Date", e);
            }
        }
    }

    private static java.sql.Date utilDate2sqlDate(long time){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    /**
     * Converts any value to <code>Double</code>.
     * @param value value to convert.
     * 
     * @return converted double. 
     */
    public static Double toDouble(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            NumberFormat nf = new DecimalFormat();
            try {
                return nf.parse(value.toString()).doubleValue();
            } catch (ParseException e) {
                throw new ConversionException("failed to convert: '" + value + "' to Double", e);
            }
        }
    }


    /**
     * If the value is instance of java.sql.Timestamp, returns it, else tries to convert the
     * value to Timestamp using {@link Timestamp#valueOf(String)}.
     * This method might trow <code>IllegalArgumentException</code> if fails at conversion.
     *
     *
     * @see {@link Timestamp#valueOf(String)}
     * @param value value to convert.
     * @return instance of Timestamp.
     */
    public static Timestamp toTimestamp(Object value){

       if (value == null) {
            return null;
        } else if (value instanceof Timestamp) {
            return (Timestamp) value;
        } else if (value instanceof java.sql.Date) {
            return new Timestamp(((java.sql.Date)value).getTime());
        } else if (value instanceof java.util.Date) {
           return new Timestamp(((java.util.Date)value).getTime());
        } else {
           return Timestamp.valueOf(value.toString());            
        }
    }

    /**
     * Converts value to Float if it can. If value is a Float, it is returned, if it is a Number, it is
     * promoted to Float and then returned, in all other cases, it converts the value to String,
     * then tries to parse Float from it.
     *
     * @param value value to be converted to Float.
     * @return value converted to Float.
     */
    public static Float toFloat(Object value){
        if (value == null) {
            return null;        
        }else if (value instanceof Number) {
            return  ((Number)value).floatValue();
        } else {
            NumberFormat nf = new DecimalFormat();
            try {
                return nf.parse(value.toString()).floatValue();
            } catch (ParseException e) {
                throw new ConversionException("failed to convert: '" + value + "' to Float", e);
            }
        }
    }


    /**
     * Converts value to Long if it can. If value is a Long, it is returned, if it is a Number, it is
     * promoted to Long and then returned, in all other cases, it converts the value to String,
     * then tries to parse Long from it.
     *
     * @param value value to be converted to Long.
     * @return value converted to Long.
     */
    public static Long toLong(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return  ((Number)value).longValue();
        } else {
            NumberFormat nf = new DecimalFormat();
            try {
                return nf.parse(value.toString()).longValue();
            } catch (ParseException e) {
                throw new ConversionException("failed to convert: '" + value + "' to Long", e);
            }
        }
    }


   /**
     * Converts value to Integer if it can. If value is a Integer, it is returned, if it is a Number, it is
     * promoted to Integer and then returned, in all other cases, it converts the value to String,
     * then tries to parse Integer from it.
     *
     * @param value value to be converted to Integer.
     * @return value converted to Integer.
     */
    public static Integer toInteger(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return  ((Number)value).intValue();
        } else {
            NumberFormat nf = new DecimalFormat();
            try {
                return nf.parse(value.toString()).intValue();
            } catch (ParseException e) {
                throw new ConversionException("failed to convert: '" + value + "' to Integer", e);
            }
        }
    }

    /**
     * Converts value to BigDecimal if it can. If value is a BigDecimal, it is returned, if it is a BigDecimal, it is
     * promoted to BigDecimal and then returned, in all other cases, it converts the value to String,
     * then tries to parse BigDecimal from it.
     *
     * @param value value to be converted to Integer.
     * @return value converted to Integer.
     */
    public static BigDecimal toBigDecimal(Object value){
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }else {
            return new BigDecimal(value.toString());
        }
    }

    /**
     * Converts value to bytes array if it can. If the value is byte array, it is simply returned,
     * if it is a <code>java.sql.Blob</code>, then data is read from it as bytes.
     * In all other cases the object is converted to <code>String</code>, then bytes are read from it.
     *
     * @param value value to be converted.
     * @return value converted to byte array.
     */
    public static byte[] toBytes(Object value) {
        try {
            if (value instanceof Blob) {
                Blob b = (Blob) value;
                return bytes(b.getBinaryStream());
            } else {
                return value instanceof byte[] ? (byte[]) value : toString(value).getBytes();
            }
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    /**
     * Converts value to Short if it can. If value is a Short, it is returned, if it is a Number, it is
     * promotedn to Short and then returned, in all other cases, it converts the value to String,
     * then tries to parse Short from it.
     *
     * @param value value to be converted to Integer.
     * @return value converted to Integer.
     */
    public static Short toShort(Object  value) {
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return  ((Number)value).shortValue();
        } else {
            NumberFormat nf = new DecimalFormat();
            try {
                return nf.parse(value.toString()).shortValue();
            } catch (ParseException e) {
                throw new ConversionException("failed to convert: '" + value + "' to Short", e);
            }
        }
    }
}


