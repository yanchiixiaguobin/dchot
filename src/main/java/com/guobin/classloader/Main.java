package com.guobin.classloader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

public class Main {
	
	private URLClassLoader classLoader;
	
	private Object worker;
	
	private long lastTime;
	
	private static String path;
	
	private static final String FILEPRO = "file:";
	
	private static final String METHOD_NAME = "run";
	
//	private static PrintWriter out;
//	
	private static File file;
	
//	static {
//		File file;
//		try {
//			file = File.createTempFile("JavaRuntime", ".java", new File(Utils.getCurrentPath()));
//			file.deleteOnExit();
//			out = new PrintWriter(new FileWriter(file));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) throws Exception {
		if(!valid(args)) {
			return;
		}
		path = args[0];
		System.out.println("path = " + path);
		file = new File(path);
		Utils.init(path);
		Main main = new Main();
		main.execute();
		Utils.exit();
	}
	
	private static boolean valid(String[] args) {
		if (args == null || args[0] == null || !(new File(args[0]).isFile())) {
			return false;
		}
		return true;
	}
	
	private void execute() throws Exception {
		while (true) {
			if (checkNeedLoad()) {
				recompileAndLoad();
			}
			invokeMethod();
			TimeUnit.MILLISECONDS.sleep(1000);
		}
	}
	
	private boolean checkNeedLoad() throws Exception {
		long newTime = file.lastModified();
		if(lastTime < newTime) {
			lastTime = newTime;
			return true;
		}
		return false;
	}
	
	private void recompileAndLoad() throws Exception {
		if (!DynamicCompile.compile(file, path)) {
			System.out.println("编译失败！");
		}
		String currentPath = Utils.getSourceDirectory(path);		
		classLoader = new SelfClassLoader(new URL[] {new URL(FILEPRO + currentPath)});
		String fullName = Utils.getClassFullName();
		System.out.println("fullName = " + fullName);
		worker = classLoader.loadClass(fullName).newInstance();
		System.out.println(worker);
	}
	
	private void invokeMethod()  {
		try {
			Method method = worker.getClass().getDeclaredMethod(METHOD_NAME, null);
			method.invoke(worker, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
