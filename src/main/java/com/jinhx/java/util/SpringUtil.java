package com.jinhx.java.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 获取spring上下文
 *
 * @author jinhx
 * @since 2022-03-21
 */
@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringUtil() {
        log.info("EasyTool SpringUtil Init");
    }

    /**
     * spring配置文件加载方式
     *
     * @param context context
     */
    @Override
    public void setApplicationContext(ApplicationContext context) {
        SpringUtil.applicationContext = context;
    }

    /**
     * 应用启动时静态加载方式
     *
     * @param context context
     */
    public static void load(ApplicationContext context) {
        SpringUtil.applicationContext = context;
    }

    /**
     * 获取spring上下文 ApplicationContext
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        if (Objects.isNull(applicationContext)) {
            throw new RuntimeException("ApplicationContext is null");
        }
        return applicationContext;
    }

    /**
     * 根据名称注册bean
     *
     * @param clazz    clazz
     * @param beanName beanName
     */
    public static <T> void registerBean(Class<T> clazz, String beanName) {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 根据名称获取bean，如果bean不能创建，抛出BeansException
     *
     * @param name name
     * @return T
     */
    public static <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    /**
     * 根据类型获取bean，如果bean不能创建，抛出BeansException
     *
     * @param clazz clazz
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 根据类型和名称获取bean，如果bean不能创建，抛出BeansException
     *
     * @param name  name
     * @param clazz clazz
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 根据名称判断是否存在bean
     *
     * @param name name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return getApplicationContext().containsBean(name);
    }

    /**
     * 根据名称判断是否是单例，如果没有给定名称的Bean，抛出NoSuchBeanDefinitionException
     *
     * @param name name
     * @return boolean
     */
    public static boolean isSingleton(String name) {
        return getApplicationContext().isSingleton(name);
    }

    /**
     * 根据名称判断是否是指定类型，如果没有给定名称的Bean，抛出NoSuchBeanDefinitionException
     *
     * @param name name
     * @return boolean
     */
    public static boolean isTypeMatch(String name, Class<?> clazz) {
        return getApplicationContext().isTypeMatch(name, clazz);
    }

    /**
     * 获取属性
     *
     * @param key key
     * @return String
     */
    public static String getProperty(String key) {
        return getApplicationContext().getBean(Environment.class).getProperty(key);
    }

}