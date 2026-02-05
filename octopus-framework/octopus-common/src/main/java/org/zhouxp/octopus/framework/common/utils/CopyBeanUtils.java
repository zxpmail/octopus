package org.zhouxp.octopus.framework.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Bean 复制工具类
 * - 支持单个/批量复制
 * - 默认忽略源对象中为 null 的字段
 * - 支持字段别名映射和忽略字段（通过链式调用）
 *
 * @author zhouxp
 */
@SuppressWarnings("unused")
@JsonIgnoreType
public class CopyBeanUtils<S, T> {

    private final S source;
    private final Supplier<T> targetSupplier;
    private final Map<String, String> fieldMapping = new HashMap<>();
    private final Set<String> ignoreFields = new HashSet<>();

    private CopyBeanUtils(S source, Supplier<T> targetSupplier) {
        this.source = source;
        this.targetSupplier = targetSupplier;
    }

    // ==================== 静态快捷方法（推荐日常使用）====================

    /**
     * 复制单个对象，忽略源对象中为 null 的字段
     */
    public static <S, T> T copy(S source, Supplier<T> target) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }
        T targetObj = target.get();
        String[] nullProperties = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, targetObj, nullProperties);
        return targetObj;
    }

    /**
     * 批量复制 List，忽略源对象中为 null 的字段
     */
    public static <S, T> List<T> copyList(List<S> source, Supplier<T> target) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        return source.stream()
                .map(item -> copy(item, target))
                .toList();
    }

    // ==================== 链式构建器（用于复杂映射）====================

    /**
     * 创建 CopyBeanUtils 实例，用于自定义字段映射或忽略
     */
    public static <S, T> CopyBeanUtils<S, T> of(S source, Supplier<T> targetSupplier) {
        return new CopyBeanUtils<>(source, targetSupplier);
    }

    public CopyBeanUtils<S, T> map(String sourceField, String targetField) {
        fieldMapping.put(sourceField, targetField);
        return this;
    }

    public CopyBeanUtils<S, T> ignore(String fieldName) {
        ignoreFields.add(fieldName);
        return this;
    }

    public T execute() {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }

        T target = targetSupplier.get();
        String[] nullNamesArray = getNullPropertyNames(source);
        Set<String> ignoreNames = new HashSet<>(Arrays.asList(nullNamesArray));
        ignoreNames.addAll(ignoreFields);

        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper destWrapper = new BeanWrapperImpl(target);

        // 处理字段映射
        for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
            String sourceName = entry.getKey();
            String targetName = entry.getValue();
            if (!ignoreNames.contains(sourceName)) {
                Object value = srcWrapper.getPropertyValue(sourceName);
                if (value != null) {
                    destWrapper.setPropertyValue(targetName, value);
                }
            }
        }

        // 拷贝其余字段
        BeanUtils.copyProperties(source, target, ignoreNames.toArray(new String[0]));
        return target;
    }

    /**
     * 批量复制，支持自定义映射规则
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> targetSupplier, Consumer<CopyBeanUtils<S, T>> mapper) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }
        return sourceList.stream()
                .map(s -> of(s, targetSupplier).apply(mapper).execute())
                .toList();
    }

    // ==================== 工具方法 ====================

    private CopyBeanUtils<S, T> apply(Consumer<CopyBeanUtils<S, T>> mapper) {
        mapper.accept(this);
        return this;
    }

    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        return Arrays.stream(wrapper.getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> wrapper.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }
}