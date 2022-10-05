package com.slimenano.framework.plugin;

import sun.misc.CompoundEnumeration;
import sun.misc.PerfCounter;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

/**
 * @author xzy54
 */
public class DynamicJarClassLoader extends URLClassLoader {
    private File file;
    private static boolean canCloseJar = false;
    private JarURLConnection cachedJarFile;

    public File getFile() {
        return file;
    }

    static {
        // JDK1.7以上版本支持直接调用close方法关闭打开的jar
        // 如果不支持close方法，需要手工释放缓存，避免卸载模块后无法删除jar
        try {
            URLClassLoader.class.getMethod("close");
            canCloseJar = true;
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public DynamicJarClassLoader(File libDir, ClassLoader parent) {
        super(new URL[]{}, null == parent ? Thread.currentThread().getContextClassLoader() : parent);
        try {
            URL element = libDir.toURI().normalize().toURL();
            addURL(element);
            file = libDir;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // 不允许加载核心资源
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL>[] tmp = (Enumeration<URL>[]) new Enumeration<?>[1];
        tmp[0] = findResources(name);
        return new CompoundEnumeration<>(tmp);
    }

    // 不允许加载核心类
    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                if (!name.startsWith("org.snc")) {
                    try {
                        if (this.getParent() != null) {
                            c = this.getParent().loadClass(name);
                        }
                    } catch (ClassNotFoundException e) {
                        // ClassNotFoundException thrown if class not found
                        // from the non-null parent class loader
                    }
                }

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);

                    // this is the defining class loader; record the stats
                    PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    @Override
    protected void addURL(URL url) {
        if (!canCloseJar) {
            try {
                // 打开并缓存文件url连接
                URLConnection uc = url.openConnection();
                if (uc instanceof JarURLConnection) {
                    uc.setUseCaches(true);
                    ((JarURLConnection) uc).getManifest();
                    cachedJarFile = ((JarURLConnection) uc);
                }
            } catch (Exception ignored) {

            }
        }
        super.addURL(url);
    }

    @Override
    public void close() throws IOException {
        if (canCloseJar) {
            try {
                super.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cachedJarFile.getJarFile().close();
            cachedJarFile = null;
        }
    }

}
