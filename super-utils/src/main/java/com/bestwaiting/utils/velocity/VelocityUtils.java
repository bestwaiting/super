package com.bestwaiting.utils.velocity;

import com.google.common.base.Charsets;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by bestwaiting on 17/5/27.
 */
public class VelocityUtils {

    public static void main(String[] args) {
        Map<String, Object> context = new HashMap<>();
        context.put("name", "sss");
        context.put("pwd", "ssssss");
        System.out.println(getTemplateContext4Class("test.vm", context));
    }

    /**
     * 设置模板加载路径配置为class
     *
     * @return
     */
    static Properties getPropertiesClass() {
        //初始化参数
        Properties properties = new Properties();
        //设置velocity资源加载方式为class
        properties.setProperty("resource.loader", "class");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        return properties;
    }

    /**
     * 设置模板加载路径配置为file
     *
     * @return
     */
    static Properties getPropertiesFile() {
        //初始化参数
        Properties properties = new Properties();
        //设置velocity资源加载方式为file
        properties.setProperty("resource.loader", "file");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        return properties;
    }

    /**
     * 设置模板加载路径配置为jar
     *
     * @param jarPath jar包路径
     * @return
     */
    static Properties getPropertiesJar(String jarPath) {
        //初始化参数
        Properties properties = new Properties();
        //设置velocity资源加载方式为jar
        properties.setProperty("resource.loader", "jar");
        //设置velocity资源加载方式为file时的处理类
        properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        //设置jar包所在的位置
        // jar:file:WebRoot/WEB-INF/lib/vm.jar
        properties.setProperty("jar.resource.loader.path", "jar:file:" + jarPath);
        return properties;
    }

    /**
     * class得到模板加载内容
     *
     * @param templateName 模板名称
     * @param context      模板内容
     * @return
     */
    public static String getTemplateContext4Class(String templateName, Map<String, Object> context) {
        //实例化一个VelocityEngine对象
        VelocityEngine velocityEngine = new VelocityEngine(getPropertiesClass());

        VelocityContext velocityContext = new VelocityContext();
        for (String key : context.keySet()) {
            velocityContext.put(key, context.get(key));
        }
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, Charsets.UTF_8.name(), velocityContext, writer);
        return writer.toString();
    }

    /**
     * file得到模板加载内容
     *
     * @param templateName 模板名称
     * @param context      模板内容
     * @return
     */
    public static String getTemplateContext4File(String templateName, Map<String, Object> context) {
        //实例化一个VelocityEngine对象
        VelocityEngine velocityEngine = new VelocityEngine(getPropertiesFile());

        VelocityContext velocityContext = new VelocityContext();
        for (String key : context.keySet()) {
            velocityContext.put(key, context.get(key));
        }
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, Charsets.UTF_8.name(), velocityContext, writer);
        return writer.toString();
    }

    /**
     * jar得到模板加载内容
     *
     * @param templateName 模板名称
     * @param context      模板内容
     * @param jarPath      jar路径
     * @return
     */
    public static String getTemplateContext4Jar(String templateName, Map<String, Object> context, String jarPath) {
        //实例化一个VelocityEngine对象
        VelocityEngine velocityEngine = new VelocityEngine(getPropertiesJar(jarPath));

        VelocityContext velocityContext = new VelocityContext();
        for (String key : context.keySet()) {
            velocityContext.put(key, context.get(key));
        }
        StringWriter writer = new StringWriter();
        velocityEngine.mergeTemplate(templateName, Charsets.UTF_8.name(), velocityContext, writer);
        return writer.toString();
    }
}
