package com.guobin.classloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Utils {
	
	private static BufferedInputStream bf;
	
	private static FileInputStream fin;
	
	private static Scanner scanner;
	
	private static File file;
	
	private static String filePath;
	
	private static String text;
	
	private static String classFullName;
	
	public static boolean init(String path) {
		try {
			file = new File(path);
			filePath = path;
			fin = new FileInputStream(path);
			bf = new BufferedInputStream(fin);
			scanner = new Scanner(bf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static String getCurrentPath() {
		Class<?> caller = getCallerClass();
		if (caller == null) {
			caller = Utils.class;
		}
		String path = caller.getProtectionDomain().getCodeSource().getLocation().getPath();
		return path;
	}
	
	private static Class<?> getCallerClass() {
		StackTraceElement[] stack = new Throwable().getStackTrace();
	    if (stack.length < 3) {
	        return Utils.class;
	    }
	    String className = stack[2].getClassName();
	    try {
		     return Class.forName(className);
		    } catch (ClassNotFoundException e) {
		        e.printStackTrace();
		    }
	    return null;
	}
	
	public static String readFileToString() {
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNextLine()) {
			sb.append(scanner.nextLine()).append('\n');
		}
		
 		return sb.toString();
	}
	
	public static String getClassCode() {  
	    StringBuffer text = new StringBuffer();   
	    text.append("package com.guobin.classloader;\n");
	    text.append("public class Worker {\n"); 
	    text.append(Utils.readFileToString());
	    text.append("}");   
	    return text.toString();  
	}  
	 
	public static String getSourceDirectory(String path) {
		int index = path.lastIndexOf("/");
		String dir = path.substring(0, index) + "/";
	    return dir;
	}
	 
	public static String getPackageFullName() {
		if (text == null) {			
			text = readFileToString();
		}
		if (text == null) {
			return "";
		}
		String name = text.split("\n")[0];
		if (name == null || name.isEmpty()) {
			return "";
		}
		int index = name.indexOf("package");
		
		if (index == -1) {
			return "";
		}
		
		name = name.substring(index + 7, name.length() - 1).trim();
		return name;
	}
	 
	public static String getBaseName() {
		String javaName = file.getName();
		return javaName.substring(0,javaName.indexOf('.'));
	}
	
	public static String getClassFullName() {
		if (classFullName == null) {
			String packageName = getPackageFullName();
			String baseName = getBaseName();
			if (!packageName.isEmpty()) {				
				classFullName = packageName + "." + baseName;
			}
			classFullName = baseName;
		}
		return classFullName;
	}
	
	public static String getText() {
		if (text == null) {
			text = readFileToString();
		}
		return text;
	}
	
	public static void exit() {
		if (fin != null) {
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (bf != null) {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (scanner != null) {
			scanner.close();
		}
	}
	
}
