package edu.banda.coel.server.common;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.banda.core.util.ParseUtil;

/**
 * Hibernate type to store Lists of primitives using SQL ARRAY.
 * 
 * @author Sylvain
 * 
 *         References : http://forum.hibernate.org/viewtopic.php?t=946973
 *         http://archives.postgresql.org/pgsql-jdbc/2003-02/msg00141.php
 */
public abstract class ListAsSQLArrayUserType<T> implements UserType {

	private static final int SQL_TYPE = Types.ARRAY;
	private static final int[] SQL_TYPES = { SQL_TYPE };

	abstract protected Array getDataAsArray(Object value);

	abstract protected List<T> getDataFromArray(Object primitivesArray);

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$BOOLEAN"
	 * hibernate.column name="fieldName" sql-type="bool[]"
	 */
	public static class BOOLEAN extends ListAsSQLArrayUserType<Boolean> {

		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.BOOLEAN((List<Boolean>) value);
		}

		@Override
		protected List<Boolean> getDataFromArray(Object array) {
			Boolean[] booleans = (Boolean[]) array;
			List<Boolean> result = new ArrayList<Boolean>(booleans.length);
			for (boolean b : booleans)
				result.add(b);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$INTEGER"
	 * hibernate.column name="fieldName" sql-type="int[]"
	 */
	public static class INTEGER extends ListAsSQLArrayUserType<Integer> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.INTEGER((List<Integer>) value);
		}

		@Override
		protected List<Integer> getDataFromArray(Object array) {
			Integer[] ints = (Integer[]) array;
			List<Integer> result = new ArrayList<Integer>(ints.length);
			for (Integer i : ints)
				result.add(i);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$LONG"
	 * hibernate.column name="fieldName" sql-type="long[]"
	 */
	public static class LONG extends ListAsSQLArrayUserType<Long> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.LONG((List<Long>) value);
		}

		@Override
		protected List<Long> getDataFromArray(Object array) {
			Long[] longs = (Long[]) array;
			ArrayList<Long> result = new ArrayList<Long>(longs.length);
			for (Long i : longs)
				result.add(i);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$FLOAT"
	 * hibernate.column name="fieldName" sql-type="real[]"
	 */
	public static class FLOAT extends ListAsSQLArrayUserType<Float> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.FLOAT((List<Float>) value);
		}

		@Override
		protected List<Float> getDataFromArray(Object array) {
			Float[] floats = (Float[]) array;
			ArrayList<Float> result = new ArrayList<Float>(floats.length);
			for (Float f : floats)
				result.add(f);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$DOUBLE"
	 * hibernate.column name="fieldName" sql-type="float8[]"
	 */
	public static class DOUBLE extends ListAsSQLArrayUserType<Double> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
//			Collection<Double> doubles = (Collection<Double>) value;
//			return new SqlListArray.DOUBLE(new ArrayList<Double>(doubles));
			return new SqlListArray.DOUBLE((List<Double>) value);
		}

		@Override
		protected List<Double> getDataFromArray(Object array) {
			Double[] doubles = (Double[]) array;
			ArrayList<Double> result = new ArrayList<Double>(doubles.length);
			for (Double d : doubles)
				result.add(d);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$STRING"
	 * hibernate.column name="fieldName" sql-type="text[]"
	 */
	public static class STRING extends ListAsSQLArrayUserType<String> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.STRING((List<String>) value);
		}

		@Override
		protected List<String> getDataFromArray(Object array) {
			String[] strings = (String[]) array;
			ArrayList<String> result = new ArrayList<String>(strings.length);
			for (String s : strings)
				result.add(s);

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property type=
	 * "com.seanergie.persistence.ListAsSQLArrayUserType$STRING_LONG_ARRAY"
	 * hibernate.column name="fieldName" sql-type="text[]"
	 */
	public static class STRING_LONG_ARRAY extends
			ListAsSQLArrayUserType<Collection<Long>> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			List<Collection<Long>> list = (List<Collection<Long>>) value;
			List<String> concatenatedLongs = new ArrayList<String>();
			for (Collection<Long> longs : list) {
				concatenatedLongs.add(StringUtils.join(longs, " "));
			}
			return new SqlListArray.STRING((List<String>) concatenatedLongs);
		}

		@Override
		protected List<Collection<Long>> getDataFromArray(Object array) {
			String[] concatenatedLongs = (String[]) array;
			List<Collection<Long>> result = new ArrayList<Collection<Long>>(
					concatenatedLongs.length);
			for (String longs : concatenatedLongs) {
				result.add(ParseUtil.parseArray(longs, Long.class, "SQL Long",
						" "));
			}

			return result;
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="com.seanergie.persistence.ListAsSQLArrayUserType$DATE"
	 * hibernate.column name="fieldName" sql-type="timestamp[]"
	 */
	public static class DATE extends ListAsSQLArrayUserType<Date> {
		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			return new SqlListArray.DATE((List<Date>) value);
		}

		@Override
		protected List<Date> getDataFromArray(Object array) {
			Date[] dates = (Date[]) array;
			ArrayList<Date> result = new ArrayList<Date>(dates.length);
			for (Date d : dates)
				result.add(d);

			return result;
		}
	}

	/**
	 * Warning, this one is special. You have to define a class that extends
	 * ENUM_LIST&lt;E&gt; and that has a no arguments constructor. For example :
	 * class MyEnumsList extends ENUM_LIST&&ltMyEnumType&gt; { public
	 * MyEnumList(){ super( MyEnum.values() ); } } Then, define :
	 * hibernate.property type="com.myPackage.MyEnumsList" hibernate.column
	 * name="fieldName" sql-type="int[]"
	 */
	public static class ENUM<E extends Enum<E>> extends  ListAsSQLArrayUserType<E> {
		private E[] theEnumValues;

		/**
		 * @param clazz
		 *            the class of the enum.
		 * @param theEnumValues
		 *            The values of enum (by invoking .values()).
		 */
		protected ENUM(E[] theEnumValues) {
			this.theEnumValues = theEnumValues;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Array getDataAsArray(Object value) {
			List<E> enums = (List<E>) value;
			List<Integer> integers = new ArrayList<Integer>(enums.size());
			for (E theEnum : enums)
				integers.add(theEnum.ordinal());

			return new SqlListArray.INTEGER(integers);
		}

		@Override
		protected List<E> getDataFromArray(Object array) {
			int[] ints = (int[]) array;
			ArrayList<E> result = new ArrayList<E>(ints.length);
			for (int val : ints) {
				for (int i = 0; i < theEnumValues.length; i++) {
					if (theEnumValues[i].ordinal() == val) {
						result.add(theEnumValues[i]);
						break;
					}
				}
			}

			if (result.size() != ints.length)
				throw new RuntimeException("Error attempting to convert "
						+ array + " into an array of enums (" + theEnumValues
						+ ").");

			return result;
		}
	}

	public Class returnedClass() {
		return List.class;
	}

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Object deepCopy(Object value) {
		return value;
	}

	public boolean isMutable() {
		return true;
	}

	@SuppressWarnings("unused")
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {

		Array sqlArray = resultSet.getArray(names[0]);
		if (resultSet.wasNull())
			return null;

		return getDataFromArray(sqlArray.getArray());
	}

	public void nullSafeSet(PreparedStatement preparedStatement, Object value,
			int index) throws HibernateException, SQLException {
		if (null == value)
			preparedStatement.setNull(index, SQL_TYPE);
		else
			preparedStatement.setArray(index, getDataAsArray(value));
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		if (null == x || null == y)
			return false;
		Class javaClass = returnedClass();
		if (!javaClass.equals(x.getClass()) || !javaClass.equals(y.getClass()))
			return false;

		return x.equals(y);
	}

	@SuppressWarnings("unused")
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@SuppressWarnings("unused")
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}
}