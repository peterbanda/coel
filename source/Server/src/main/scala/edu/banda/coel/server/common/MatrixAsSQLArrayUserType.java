package edu.banda.coel.server.common;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public abstract class MatrixAsSQLArrayUserType<T> implements UserType {

   private static final int SQL_TYPE = Types.ARRAY;
   private static final int[] SQL_TYPES = { SQL_TYPE };

   abstract protected Array getDataAsArray(Object value);
   
   abstract protected List<List<T>> getDataFromArray(Object primitivesArray);

   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$BOOLEAN"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="bool[][]"
    */
   public static class BOOLEAN extends MatrixAsSQLArrayUserType<Boolean>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.BOOLEAN( (List<List<Boolean>>)value );
       }
      
      @Override
      protected List<List<Boolean>> getDataFromArray(Object array){
         boolean[][] booleans = (boolean[][]) array;
         List<List<Boolean>> result = new ArrayList<List<Boolean>>( booleans.length );
         for (boolean[] bArray : booleans) {
        	 List<Boolean> theBList = new ArrayList<Boolean>();
        	 result.add(theBList);
        	 for (boolean b : bArray) {
        		 theBList.add(b);        		 
        	 }
         }
         return result;
      }
   }

   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$INTEGER"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="int[][]"
    */
   public static class INTEGER extends MatrixAsSQLArrayUserType<Integer>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.INTEGER( (List<List<Integer>>) value );
       }
      
      @Override
      protected List<List<Integer>> getDataFromArray(Object array){
         Integer[][] ints = (Integer[][]) array;
         List<List<Integer>> result = new ArrayList<List<Integer>>( ints.length );
         for (Integer[] iArray : ints) {
        	 List<Integer> theIList = new ArrayList<Integer>();
        	 result.add(theIList);
        	 for (Integer i : iArray) {
        		 theIList.add(i);        		 
        	 }
         }
         return result;
      }
   }

   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$LONG"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="long[][]"
    */
   public static class LONG extends MatrixAsSQLArrayUserType<Long>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.LONG( (List<List<Long>>) value );
       }
      
      @Override
      protected List<List<Long>> getDataFromArray(Object array){
         Long[][] longs = (Long[][]) array;
         List<List<Long>> result = new ArrayList<List<Long>>( longs.length );
         for (Long[] lArray : longs) {
        	 List<Long> theLList = new ArrayList<Long>();
        	 result.add(theLList);
        	 for (Long i : lArray) {
        		 theLList.add(i);        		 
        	 }
         }
         return result;
      }
   }
  
   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$FLOAT"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="real[][]"
    */
   public static class FLOAT extends MatrixAsSQLArrayUserType<Float>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.FLOAT( (List<List<Float>>) value );
       }
      
      @Override
      protected List<List<Float>> getDataFromArray(Object array){
         Float[][] floats = (Float[][]) array;
         List<List<Float>> result = new ArrayList<List<Float>>( floats.length );
         for (Float[] fArray : floats) {
        	 List<Float> theFList = new ArrayList<Float>();
        	 result.add(theFList);
        	 for (Float i : fArray) {
        		 theFList.add(i);        		 
        	 }
         }
         return result;
      }
   }

   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$DOUBLE"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="float8[][]"
    */
   public static class DOUBLE extends MatrixAsSQLArrayUserType<Double>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.DOUBLE( (List<List<Double>>)value );
       }
      
      @Override
      protected List<List<Double>> getDataFromArray(Object array){
         Double[][] doubles = (Double[][]) array;
         List<List<Double>> result = new ArrayList<List<Double>>( doubles.length );
         for (Double[] dArray : doubles) {
        	 List<Double> theDList = new ArrayList<Double>();
        	 result.add(theDList);
        	 for (Double i : dArray) {
        		 theDList.add(i);        		 
        	 }
         }
         
         return result;
      }
   }
   
   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$STRING"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="text[]"
    */
   public static class STRING extends MatrixAsSQLArrayUserType<String>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.STRING( (List<List<String>>) value );
       }
      
      @Override
      protected List<List<String>> getDataFromArray(Object array){
         String[][] strings = (String[][]) array;
         List<List<String>> result = new ArrayList<List<String>>( strings.length );
         for (String[] sArray : strings) {
        	 List<String> theSList = new ArrayList<String>();
        	 result.add(theSList);
        	 for (String s : sArray) {
        		 theSList.add(s);        		 
        	 }
         }
         
         return result;
      }
   }
   
   /**
    * To use, define :
     * hibernate.property
     *       type="edu.banda.coel.server.common.MatrixAsSQLArrayUserType$DATE"
     * hibernate.column
     *       name="fieldName"
     *       sql-type="timestamp[][]"
    */
   public static class DATE extends MatrixAsSQLArrayUserType<Date>{
      @Override
      @SuppressWarnings("unchecked")
       protected Array getDataAsArray(Object value){
           return new SqlMatrix.DATE( (List<List<Date>>)value );
       }
      
      @Override
      protected List<List<Date>> getDataFromArray(Object array){
         Date[][] dates = (Date[][]) array;
         List<List<Date>> result = new ArrayList<List<Date>>( dates.length );
         for (Date[] dArray : dates) {
        	 List<Date> theDList = new ArrayList<Date>();
        	 result.add(theDList);
        	 for (Date d : dArray) {
        		 theDList.add(d);        		 
        	 }
         }         
         return result;
      }
   }
   
   public Class returnedClass(){
      return List.class;
   }
   
   public int[] sqlTypes(){
      return SQL_TYPES;
   }
   
    public Object deepCopy(Object value){
          return value;
    }
   
    public boolean isMutable(){
          return true;
    }
     
    @SuppressWarnings("unused")
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
        throws HibernateException, SQLException {
       
      Array SqlMatrix = resultSet.getArray(names[0]);
        if( resultSet.wasNull() )
              return null;

        return getDataFromArray( SqlMatrix.getArray() );
    }
 
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
        if( null == value )
            preparedStatement.setNull(index, SQL_TYPE);
        else
              preparedStatement.setArray(index, getDataAsArray(value));
    }
 
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
   
    public boolean equals(Object x, Object y) throws HibernateException {
        if( x == y)
            return true;
        if( null == x || null == y )
            return false;
        Class javaClass = returnedClass();
        if( ! javaClass.equals( x.getClass() ) || ! javaClass.equals( y.getClass() ) )
              return false;
       
        return x.equals( y );
    }
   
    @SuppressWarnings("unused")
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
         return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }
 
    @SuppressWarnings("unused")
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
} 