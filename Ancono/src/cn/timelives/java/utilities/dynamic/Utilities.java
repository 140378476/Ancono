/**
 * 
 */
package cn.timelives.java.utilities.dynamic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.timelives.java.utilities.Printer; 
/**
 * @author liyicheng
 *
 */
@Deprecated//for future fix
public final class Utilities {

	/**
	 * 
	 */
	public Utilities() {
		// TODO Auto-generated constructor stub
	}
	public static void loadAllClasses(String packageName){
		loadAllClasses(packageName, true, Thread.currentThread().getContextClassLoader());
	}
	
	public static void loadAllClasses(String packageName,boolean includeSubPackage,ClassLoader loader){
		getClassName(packageName, includeSubPackage, loader).forEach(x -> {
			try {
				loader.loadClass(x);
			} catch (ClassNotFoundException e) {
				//this should not happen
				e.printStackTrace();
			}
		});
	}
	/**
	 * Load all classes that belongs to the very package from the file system and return them, notice that 
	 * the classes from sub-packages will not be returned.
	 * @param pkgName
	 * @return
	 */
//	public static List<Class<?>> loadAllClassesAndReturn(String pkgName){
//		return loadAllClassesAndReturn(pkgName,false,Utilities.class.getClassLoader());
//	}
//	public static List<Class<?>> loadAllClassesAndReturn(String pkgName,boolean includeSubPackage){
//		String filePath = ClassLoader.getSystemResource("").getPath() + pkgName.replace(".", "\\");  
//        List<Class<?>> fileNames = getClassName(filePath, new ArrayList<>(),classLoader);  
//        return fileNames;  
//	}
//	private static List<Class<?>> getClassName(String filePath, List<Class<?>> clazzes,ClassLoader classLoader) {  
//        File file = new File(filePath);  
//        File[] childFiles = file.listFiles();  
//        for (File childFile : childFiles) {  
//            if (childFile.isDirectory()) {  
//                getClassName(childFile.getPath(), clazzes,classLoader);  
//            } else {  
//                String childFilePath = childFile.getPath();  
//                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
//                childFilePath = childFilePath.replace("\\", ".");  
//                clazzes.add(classLoader.loadClass(name)childFilePath);  
//            }  
//        }  
//  
//        return clazzes;  
//    }  
	
	 /** 
     * 获取某包下（包括该包的所有子包）所有类 
     * @param packageName 包名 
     * @return 类的完整名称 
     */  
    public static List<String> getClassName(String packageName) {  
        return getClassName(packageName, true,Thread.currentThread().getContextClassLoader());  
    }  
  
    /** 
     * 获取某包下所有类 
     * @param packageName 包名 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    public static List<String> getClassName(String packageName, boolean childPackage,ClassLoader loader) {  
        List<String> fileNames = new ArrayList<>();  
        
        String packagePath = packageName.replace(".", "/");  
        URL url = loader.getResource(packagePath);  
        if (url != null) {  
            String type = url.getProtocol();  
            if (type.equals("file")) {  
                fileNames = getClassNameByFile(url.getPath(), fileNames, childPackage,loader);  
            } else if (type.equals("jar")) {  
                fileNames = getClassNameByJar(url.getPath(), childPackage,loader,fileNames);  
            }  
        } else {  
            fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage,loader,fileNames);  
        }  
        return fileNames;  
    }  
  
    /** 
     * 从项目文件获取某包下所有类 
     * @param filePath 文件路径 
     * @param className 类名集合 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage,ClassLoader loader) {  
        File file = new File(filePath);  
        File[] childFiles = file.listFiles();  
        for (File childFile : childFiles) {  
            if (childFile.isDirectory()) {  
                if (childPackage) {  
                    getClassNameByFile(childFile.getPath(), className, childPackage,loader);  
                }  
            } else {  
                String childFilePath = childFile.getPath();  
                if (childFilePath.endsWith(".class")) {  
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));  
                    childFilePath = childFilePath.replace("\\", ".");  
                    className.add(childFilePath);  
                }  
            }  
        }  
  
        return className;  
    }  
  
    /** 
     * 从jar获取某包下所有类 
     * @param jarPath jar文件路径 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage,ClassLoader loader,List<String> className) {  
        String[] jarInfo = jarPath.split("!");  
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));  
        String packagePath = jarInfo[1].substring(1);  
//        try {  
		try(JarFile jarFile = new JarFile(jarFilePath)) {
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							className.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							className.add(entryName);
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        return className;  
    }  
  
    /** 
     * 从所有jar中搜索该包，并获取该包下所有类 
     * @param urls URL集合 
     * @param packagePath 包路径 
     * @param childPackage 是否遍历子包 
     * @return 类的完整名称 
     */  
    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage,ClassLoader loader,List<String> className) {  
        if (urls != null) {  
            for (int i = 0; i < urls.length; i++) {  
                URL url = urls[i];  
                String urlPath = url.getPath();  
                // 不必搜索classes文件夹  
                if (urlPath.endsWith("classes/")) {  
                    continue;  
                }  
                String jarPath = urlPath + "!/" + packagePath;  
                getClassNameByJar(jarPath, childPackage,loader,className);  
            }  
        }  
        return className;  
    }  
}
