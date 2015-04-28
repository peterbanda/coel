package edu.banda.coel.server.common;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.hibernate.util.ReflectHelper;

import com.banda.chemistry.domain.AcEvaluationItemState;
import com.banda.core.reflection.ReflectionUtil;
import com.banda.core.util.ConversionUtil;

public abstract class SQLArrayUserType<T> implements UserType {

	protected static final int SQL_TYPE = Types.ARRAY;
	protected static final int VALUE_TYPE_SQL_TYPE = Types.VARCHAR;
	
	private static final int[] SQL_TYPES = { SQL_TYPE };
	private static final int[] GENERIC_SQL_TYPES = { SQL_TYPE, VALUE_TYPE_SQL_TYPE };

	abstract protected Array getDataAsArray(T[] value);

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$BOOLEAN"
	 * hibernate.column name="fieldName" sql-type="bool[]"
	 */
	public static class BOOLEAN extends SQLArrayUserType<Boolean> {

		@Override
		protected Array getDataAsArray(Boolean[] value) {
			return new SqlArray.BOOLEAN(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$INTEGER"
	 * hibernate.column name="fieldName" sql-type="int[]"
	 */
	public static class INTEGER extends SQLArrayUserType<Integer> {

		@Override
		protected Array getDataAsArray(Integer[] value) {
			return new SqlArray.INTEGER(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$LONG"
	 * hibernate.column name="fieldName" sql-type="long[]"
	 */
	public static class LONG extends SQLArrayUserType<Long> {

		@Override
		protected Array getDataAsArray(Long[] value) {
			return new SqlArray.LONG(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$FLOAT"
	 * hibernate.column name="fieldName" sql-type="real[]"
	 */
	public static class FLOAT extends SQLArrayUserType<Float> {

		@Override
		protected Array getDataAsArray(Float[] value) {
			return new SqlArray.FLOAT(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$DOUBLE"
	 * hibernate.column name="fieldName" sql-type="float8[]"
	 */
	public static class NUMBER extends SQLArrayUserType<Number> {

		@Override
		protected Array getDataAsArray(Number[] value) {
			Double[] doubleArray = new Double[value.length];
			for (int i = 0; i < value.length; i++) {
				doubleArray[i] = value[i].doubleValue();
			}
			return new SqlArray.DOUBLE(doubleArray);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$DOUBLE"
	 * hibernate.column name="fieldName" sql-type="float8[]"
	 */
	public static class DOUBLE extends SQLArrayUserType<Double> {

		@Override
		protected Array getDataAsArray(Double[] value) {
			return new SqlArray.DOUBLE(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$STRING"
	 * hibernate.column name="fieldName" sql-type="text[]"
	 */
	public static class STRING extends SQLArrayUserType<String> {

		@Override
		protected Array getDataAsArray(String[] value) {
			return new SqlArray.STRING(value);
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$STRING"
	 * hibernate.column name="fieldName" sql-type="text[]"
	 */
	public static class GENERIC<T> extends SQLArrayUserType<T> {

		public int[] sqlTypes() {
			return GENERIC_SQL_TYPES;
		}

		@Override
		protected Array getDataAsArray(T[] value) {
			String[] strings = new String[value.length];
			for (int i = 0; i < value.length; i++) {
				strings[i] = value[i].toString();
			}
			return new SqlArray.GENERIC(strings);
		}

		@Override
		public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner) throws HibernateException, SQLException {
			final Array sqlArray = resultSet.getArray(names[0]);
			if (resultSet.wasNull()) {
				return null;
			}

			final String[] array = (String[]) sqlArray.getArray();
			Class<T> typeClass;
			try {
				typeClass = ReflectHelper.classForName(resultSet.getString(names[1]), this.getClass());
			} catch (ClassNotFoundException e) {
				throw new HibernateException("Generic array class not found", e);
			}
			return ConversionUtil.convertArray(typeClass, array, "Generic SQL array element");
		}

		@Override
		public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index) throws HibernateException, SQLException {
			if (null == value) {
				preparedStatement.setNull(index, SQL_TYPE);
				preparedStatement.setNull(index + 1, VALUE_TYPE_SQL_TYPE);
			} else {
				final T[] array = (T[]) value;
				preparedStatement.setArray(index, getDataAsArray(array));
				preparedStatement.setString(index + 1, array[0].getClass().getName());
			}
		}
	}

	/**
	 * To use, define : hibernate.property
	 * type="edu.banda.coel.server.common.SQLArrayUserType$DATE"
	 * hibernate.column name="fieldName" sql-type="timestamp[]"
	 */
	public static class DATE extends SQLArrayUserType<Date> {

		@Override
		protected Array getDataAsArray(Date[] value) {
			return new SqlArray.DATE(value);
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
	public static class ENUM<E extends Enum<E>> extends SQLArrayUserType<E> {
		private E[] enumValues;

		/**
		 * @param enumValues
		 *            The values of enum (by invoking .values()).
		 */
		protected ENUM(E[] enumValues) {
			if (enumValues == null || enumValues.length == 0) {
				throw new RuntimeException(
						"For SQL array enum type at least one value is expected.");
			}
			this.enumValues = enumValues;
		}

		@Override
		protected Array getDataAsArray(E[] value) {
			int length = value.length;
			Integer[] integers = new Integer[length];
			for (int i = 0; i < length; i++) {
				integers[i] = value[i].ordinal();
			}
			return new SqlArray.INTEGER(integers);
		}

		@Override
		protected E[] getDataFromArray(Object[] array) {
			Integer[] ints = (Integer[]) array;
			E[] result = ReflectionUtil.createNewArray(
					(Class<E>) enumValues[0].getClass(), ints.length);
			int i = 0;
			for (int val : ints) {
				for (E enumValue : enumValues) {
					if (enumValue.ordinal() == val) {
						result[i] = enumValue;
						break;
					}
				}
				if (result[i] == null) {
					throw new RuntimeException("Error attempting to convert "
							+ array + " into an array of enums (" + enumValues
							+ ").");
				}
				i++;
			}
			return result;
		}
	}

	// TODO: Get rid of this dummy class
	public static class ENUM_EVALUATION_ITEM_STATE extends
			ENUM<AcEvaluationItemState> {
		public ENUM_EVALUATION_ITEM_STATE() {
			super(AcEvaluationItemState.values());
		}
	}

	public Class returnedClass() {
		return Object[].class;
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

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {

		Array sqlArray = resultSet.getArray(names[0]);
		if (resultSet.wasNull()) {
			return null;
		}

		Object[] array = (Object[]) sqlArray.getArray();
		return getDataFromArray(array);
	}

	protected T[] getDataFromArray(Object[] array) {
		return (T[]) array;
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value,
			int index) throws HibernateException, SQLException {
		if (null == value)
			preparedStatement.setNull(index, SQL_TYPE);
		else
			preparedStatement.setArray(index, getDataAsArray((T[]) value));
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