package com.gserver.data.editor.util;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.google.common.collect.Sets;
import com.gserver.data.editor.annotation.Comment;

public class ClassUtils {

	static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	/**
	 * 获得包下所有的类
	 * 
	 * @param packageName
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
		String slashPackageName = packageName.replace('.', '/');
		Set<Class<?>> classes = Sets.newHashSet();
		String resPath = "classpath*:" + "/" + slashPackageName + "/**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(resPath);
		for (Resource resource : resources) {
			String path = resource.getURL().getPath();
			String className = path.substring(path.indexOf(slashPackageName), path.length() - 6).replace('/', '.');
			Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			classes.add(loadClass);
		}
		return classes;
	}

	/**
	 * 获得包下所有实现某接口的类
	 * 
	 * @param <Interface>
	 * @param packageName
	 * @param interfaces
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static <Interface> Set<Class<Interface>> getSubClasses(String packageName, Class<Interface> interfaces) throws ClassNotFoundException, IOException {
		String slashPackageName = packageName.replace('.', '/');
		Set<Class<Interface>> classes = Sets.newHashSet();
		String resPath = "classpath*:" + "/" + slashPackageName + "/**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(resPath);
		for (Resource resource : resources) {
			String path = resource.getURL().getPath();
			String className = path.substring(path.indexOf(slashPackageName), path.length() - 6).replace('/', '.');
			Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
			if (interfaces != null) {
				if (!loadClass.isInterface() && interfaces.isAssignableFrom(loadClass)) {
					@SuppressWarnings("unchecked")
					Class<Interface> casted = (Class<Interface>) loadClass;
					classes.add(casted);
				}
			}
		}
		return classes;
	}

	public static Set<String> getDirectChildrenNames(String packageName) throws IOException {
		String packageNameSlashed = packageName.replace('.', '/');
		Set<String> children = Sets.newTreeSet();
		// Firstly, find all classes, and some packages which not in jars.
		String resPath1 = "classpath*:" + "/" + packageNameSlashed + "/*";
		Resource[] resources1 = resourcePatternResolver.getResources(resPath1);
		for (Resource resource : resources1) {
			String path = resource.getURL().getPath();
			if (path.endsWith(".class")) {
				String className = path.substring(path.indexOf(packageNameSlashed)).replace('/', '.');
				children.add(className);
			} else {
				if (path.endsWith("/")) {
					String folderName = path.substring(path.indexOf(packageNameSlashed), path.length() - 1).replace('/', '.');
					children.add(folderName);
				}
			}
		}
		// As for some packages not in a jar, no sub-package names could be
		// obtained in earlier step. We should add these folderNames manually.
		String resPath2 = "classpath*:" + "/" + packageNameSlashed + "/*/";
		Resource[] resources2 = resourcePatternResolver.getResources(resPath2);
		for (Resource resource : resources2) {
			String protocal = resource.getURL().getProtocol();
			if (protocal.equals("jar")) {
				String path = resource.getURL().getPath();
				String folderName = path.substring(path.indexOf(packageNameSlashed), path.length() - 1).replace('/', '.');
				children.add(folderName);
			}
		}
		return children;
	}

	public static String findPackageInfo(String packageName) {
		String packageDesc = packageName.substring(packageName.lastIndexOf('.') + 1);
		try {
			Class<?> clazz = Class.forName(packageName + ".package-info");
			if (clazz != null && clazz.isAnnotationPresent(Comment.class)) {
				packageDesc = clazz.getAnnotation(Comment.class).desc();
			}
			return packageDesc;
		} catch (ClassNotFoundException e) {
			return packageDesc;
		}
	}
}
