package com.guobin.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class SelfClassLoader extends URLClassLoader{

	public SelfClassLoader(URL[] arg0) {
		super(arg0);
	}
	
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class c = findLoadedClass(name);
		if (c == null) {
			try {				
				c = findClass(name);
			} catch (Exception e) {
				
			}
		}
		
		if (c == null) {
			c = super.loadClass(name, resolve);
		}
		return c;
	}
}
