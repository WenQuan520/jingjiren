package cn.softbank.purchase.utils;

import android.annotation.SuppressLint;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressLint("DefaultLocale")
public class ObjectIsEmpty {

	public static Object isEmpty(Object obj, Class<?> clazz) {
		if ((obj == null) || (clazz == null)) {
			return obj;
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldName = field.getName();
				if (fieldName != null){
					if (fieldName.length() != 0) {
						String fieldMethodName = fieldName.substring(0, 1)
								.toUpperCase() + fieldName.substring(1);
						String getfieldName = "get" + fieldMethodName;
						Method getMethod = clazz.getMethod(getfieldName, new Class []{});
						if (getMethod != null) {
							String setfieldName;
							Method setMethod;
							Object value = getMethod.invoke(obj,new Object());
							Class type = field.getType();
							if (type == String.class) {
								if ((value == null)
										|| ("null".equals(value.toString()))
										|| (value.toString().trim().length() == 0)) {
									value = "";
									setfieldName = "set" + fieldMethodName;
									setMethod = clazz.getMethod(setfieldName,
											new Class[] { String.class });
									if (setMethod != null) {
										setMethod.invoke(obj,
												new Object[] { value });
									}
								}
							} else if ((((type == Integer.class)
									|| (type == Integer.TYPE)
									|| (type == Float.class)
									|| (type == Float.TYPE)
									|| (type == Double.class) || (type == Double.TYPE)))
									&& (((value == null)
									|| ("null".equals(value.toString())) || (value
									.toString().length() == 0)))) {
								setfieldName = "set" + fieldMethodName;
								setMethod = clazz.getMethod(setfieldName,
										new Class[] { type });
								setMethod.invoke(obj,
										new Object[] { Integer.valueOf(0) });
							}
						}
					}}
			} catch (Exception e) {
//				e.printStackTrace();
			}
		}

		return obj;
	}

}
