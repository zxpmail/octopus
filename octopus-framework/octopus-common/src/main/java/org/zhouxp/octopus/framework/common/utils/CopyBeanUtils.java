package org.zhouxp.octopus.framework.common.utils;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p/>
 * {@code @description}  : 复制一个bean或bean列表 并返回一个目标bean或列表
 * <b>@create:</b> 2025-07-14 19:01:05
 *
 * @author zhouxp
 */
@SuppressWarnings("unused")
public class CopyBeanUtils<S, T> {

    private final S source;
    private final Supplier<T> targetSupplier;
    private final Map<String, String> fieldMapping = new HashMap<>();
    private final Set<String> ignoreFields = new HashSet<>();

    private CopyBeanUtils(S source, Supplier<T> targetSupplier) {
        this.source = source;
        this.targetSupplier = targetSupplier;
    }

    /**
     * 快速拷贝并忽略 null 字段（静态方法）
     */
    public static <S, T> T fastCopy(S source, Supplier<T> target) {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }

        T targetObj = target.get();
        String[] nullProperties = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, targetObj,  nullProperties);
        return targetObj;
    }

    /**
     * 从源bean list集合中复制到目标bean List集合中
     *
     * @param source 源bean List集合
     * @param target 目标类型对象
     * @param <T>    目标类型
     * @param <S>    源类型
     * @return 返回目标bean List集合
     */
    public static <T, S> List<T> fastCopyList(List<S> source, Supplier<T> target) {
        if(CollectionUtils.isEmpty(source)){
            return new ArrayList<>();
        }
        return source.stream()
                .map(u -> CopyBeanUtils.fastCopy(u, target))
                .collect(Collectors.toList());
    }
    /**
     * 创建 CopyBeanUtils 实例
     */
    public static <S, T> CopyBeanUtils<S, T> copy(S source, Supplier<T> targetSupplier) {
        return new CopyBeanUtils<>(source, targetSupplier);
    }

    /**
     * 添加字段映射关系
     */
    public CopyBeanUtils<S, T> map(String sourceField, String targetField) {
        fieldMapping.put(sourceField, targetField);
        return this;
    }

    /**
     * 添加要忽略的字段
     */
    public CopyBeanUtils<S, T> ignore(String fieldName) {
        ignoreFields.add(fieldName);
        return this;
    }

    /**
     * 执行拷贝
     */
    public T execute() {
        if (ObjectUtils.isEmpty(source)) {
            return null;
        }

        T target = targetSupplier.get();


        String[] nullNamesArray = getNullPropertyNames(source);

        Set<String> ignoreNames = new HashSet<>(Arrays.asList(nullNamesArray));

        ignoreNames.addAll(ignoreFields);

        // 处理字段别名映射
        BeanWrapper srcWrapper = new BeanWrapperImpl(source);
        BeanWrapper destWrapper = new BeanWrapperImpl(target);

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

        // 使用 Spring BeanUtils 拷贝剩余字段
        BeanUtils.copyProperties(source,target, ignoreNames.toArray(new String[0]));

        return target;
    }

    /**
     * 批量拷贝 List 对象
     */
    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> targetSupplier, Consumer<CopyBeanUtils<S, T>> mapper) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(s -> {
                    CopyBeanUtils<S, T> copier = new CopyBeanUtils<>(s, targetSupplier);
                    mapper.accept(copier);
                    return copier.execute();
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取对象中为 null 的字段名
     */
    public static String[] getNullPropertyNames(Object source) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        return Arrays.stream(wrapper.getPropertyDescriptors())
                .map(PropertyDescriptor::getName)
                .filter(name -> wrapper.getPropertyValue(name) == null)
                .toArray(String[]::new);
    }

    @Data
    static class User {
        private String userName;
        private Integer age;
        private String emailAddress;
        private String password;
    }

    @Data
    static class UserDTO {
        private String name;
        private Integer age;
        private String email;
        private String password;
    }
    public static void main(String[] args) {
        User user = new User();
        user.setUserName("Tom");
        user.setAge(25);
        user.setEmailAddress("tom@example.com");

        UserDTO dto = CopyBeanUtils.copy(user, UserDTO::new)
                .map("userName", "name")
                .map("emailAddress", "email")
                .ignore("password")
                .execute();

        System.out.println(dto);

        List<User> userList = Arrays.asList(user, user);
        List<UserDTO> dtoList = CopyBeanUtils.copyList(userList, UserDTO::new, copier -> copier.map("userName", "name")
                .map("emailAddress", "email")
                .ignore("password"));
        System.out.println(dtoList);
    }
}