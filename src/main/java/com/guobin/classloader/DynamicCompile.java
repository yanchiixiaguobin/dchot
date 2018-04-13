package com.guobin.classloader;

import java.io.File;

import com.sun.tools.javac.Main;

public class DynamicCompile {
	
	public static boolean compile(File file, String path) {
		if (!file.isFile()) {
			return false;
		}
		String loadPath = Utils.getSourceDirectory(path);
		String[] cpargs = new String[] { "-d", loadPath, path };  
        int status = Main.compile(cpargs);
        return status == 0 ? true : false;
	}
	

//	
//	public static void main(String[] args) {
//		
//		String str = "D:/算法/openjdk/common/Worker.java";
////		Utils.init(str);
////		System.out.println(Utils.getClassFullName());
////		System.out.println(Utils.getClassFullName());
//		
//		compile(new File(str),str);
//	}

}
