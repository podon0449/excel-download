package com.podong.utils;

import com.podong.CommonException;
import com.podong.annotation.PivotEnumModel;
import com.podong.gc.resource.ExcelRenderGcResourceFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


public final class SuperClassReflectionUtils {

	private SuperClassReflectionUtils() {

	}

	public static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
			fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
		}
		return fields;
	}

	public static Annotation getAnnotation(Class<?> clazz,
										   Class<? extends Annotation> targetAnnotation) {
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
			if (clazzInClasses.isAnnotationPresent(targetAnnotation)) {
				return clazzInClasses.getAnnotation(targetAnnotation);
			}
		}
		return null;
	}

	public static Field getField(Class<?> clazz, String name) throws Exception {
		for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
			for (Field field : clazzInClasses.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return clazzInClasses.getDeclaredField(name);
				}
			}
		}
		throw new NoSuchFieldException();
	}

	private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
		List<Class<?>> classes = new ArrayList<>();
		while (clazz != null) {
			classes.add(clazz);
			clazz = clazz.getSuperclass();
		}
		if (fromSuper) {
			Collections.reverse(classes);
		}
		return classes;
	}
	/**
	 * PivotEnumModel 형태 구조로 key 와 field 를 매칭시키기 위한 함수
	 * 피벗 테이블에 해당 필터 / 행 레이블 / 열 레이블 / 컬럼 / 값 을 셋팅하는 필드를 넣어줌.
	 * */
	public static List<Map<String, Object>> getPivotEnumClassFieldOrientations(Class<?> clazz) {
		List<Map<String, Object>> pivotFieldNameMapList = new ArrayList<>();

		PivotEnumModel[] pivotEnumModels = (PivotEnumModel[]) clazz.getEnumConstants();
		if (null == pivotEnumModels) {
			throw new CommonException("pivotEnumModels is null !!", new NullPointerException());
		}
		Arrays.stream(pivotEnumModels).forEach(enumModel -> {
			if (enumModel.isActive()) {
				Map<String, Object> pivotMap = new HashMap<>();
				pivotMap.put("key", enumModel.getName());
				pivotMap.put("field", ExcelRenderGcResourceFactory.getPivotFieldOrientation(enumModel.getPivotFieldOrientation()));
				pivotFieldNameMapList.add(pivotMap);
			}
		});
		return pivotFieldNameMapList;
	}
	/**
	 * PivotEnumModel 형태 구조로 header List 를 꺼내오기 위함.
	 * */
	public static List<String> getPivotEnumClassHeaderNames(Class<?> clazz) {
		List<String> pivotHeaderList = new ArrayList<>();

		PivotEnumModel[] pivotEnumModels = (PivotEnumModel[]) clazz.getEnumConstants();
		if (null == pivotEnumModels) {
			throw new CommonException("pivotEnumModels is null !!", new NullPointerException());
		}
		Arrays.stream(pivotEnumModels).forEach(enumModel -> {
			pivotHeaderList.add(enumModel.getName());
		});
		return pivotHeaderList;
	}

}
