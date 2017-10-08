package utils;

import java.lang.reflect.Field;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class ReflectUtil {
	public static void setObjAllValue(JSONObject jsonObject, Object obj, List<String> excludes)
			throws NoSuchFieldException, IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (excludes == null || !excludes.contains(field.getName())) {
				setObjValue(jsonObject, obj, field.getName());
			}
		}
	}

	public static void setObjValue(JSONObject jsonObject, Object obj, String[] fieldNames)
			throws NoSuchFieldException, IllegalAccessException {
		for (String string : fieldNames) {
			setObjValue(jsonObject, obj, string);
		}
	}

	public static void setObjValue(JSONObject jsonObject, Object obj, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, jsonObject.get(fieldName));
	}
}
