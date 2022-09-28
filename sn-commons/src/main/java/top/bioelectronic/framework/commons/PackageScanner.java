package top.bioelectronic.framework.commons;

import lombok.extern.slf4j.Slf4j;
import top.bioelectronic.sdk.logger.Marker;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
@Marker("包扫描器")
public abstract class PackageScanner {
    public PackageScanner() {
    }

    //抽象方法，等待以后使用工具的人编写
    public abstract boolean dealClass(Class<?> clazz) throws Exception;


    //扫描包
    public void scanPackage(String packageName, ClassLoader classLoader) throws Exception {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        //获得当前线程的上下文加载器
        log.debug("启动扫描任务，目标包：{} ", packageName);
        log.debug("当前类加载器: {}", classLoader);
        ClassLoader parent = classLoader.getParent();

        //将包名转化URL的文件路径的格式
        String path = packageName.replace(".", "/");

        try {
            //用类加载器获得指定路径下的资源的URL对象的枚举
            Enumeration<URL> urls = classLoader.getResources(path);
            //遍历枚举
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                //getProtocol方法是用来获得指定URL的协议名称
                //判断是否是Jar包
                if (url.getProtocol().equals("jar")) {
                    log.debug("档案扫描模式: {}", url);
                    dealJar(url, path, classLoader);
                } else {
                    try {
                        //url转化为等价的uri，用以生成一个文件类型对象
                        File curFile = new File(url.toURI());
                        //处理指定包里的文件
                        log.debug("包扫描模式: {}", url);
                        dealPackage(curFile, packageName, classLoader);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dealJar(URL url, String packageName, ClassLoader loader) throws Exception {
        try {
            //通过URL对象获得一个Jar包的URL连接
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            //通过上述的连接获得jar文件
            JarFile jarFile = connection.getJarFile();

            //获得zip文件项的枚举
            Enumeration<JarEntry> entryList = jarFile.entries();

            //遍历枚举
            while (entryList.hasMoreElements()) {
                JarEntry jarEntry = entryList.nextElement();

                //如果每一个元素不是目录，且不是class文件会跳入下一次循环
                //确保找到每一个.class文件
                String className = jarEntry.getName();
                if (jarEntry.isDirectory() ||  !className.startsWith(packageName) || !className.endsWith(".class")) {
                    continue;
                }
                //通过class文件名得到文件名称以及类名称
                className = className.replace(".class", "");
                className = className.replace("/", ".");
                // 安全性检查
                if (loader != PackageScanner.class.getClassLoader() && (className.startsWith("com.bioelectronic"))){
                    log.debug("跳过：{}", className);
                    continue;
                }



                try {
                    //获得类对象，并且用抽象方法处理
                    Class<?> klass = loader.loadClass(className);
                    log.debug("已加载: {}", className);
                    if (!dealClass(klass)) {
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    log.error("类丢失: ", e);
                }
            }
        } catch (IOException e) {
            log.error("执行失败: ", e);
        }

    }

    private void dealPackage(File curFile, String packageName, ClassLoader loader) throws Exception {
        //获得文件的集合

        File[] files = curFile.listFiles();
        //遍历每个文件
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".class")) {
                    continue;
                }
                //找出.class文件并且得到文件名与类名
                fileName = fileName.replace(".class", "");
                String className = packageName + "." +fileName;

                try {
                    //通过类名生成指定类对象，用抽象方法处理这个类
                    Class<?> klass = loader.loadClass(className);
                    log.debug("已加载: {}", className);
                    if (!dealClass(klass)) {
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    log.error("类丢失: ", e);
                }
            } else {
                //递归调用该方法，知道没有文件为止，即遍历该目录下的所有文件
                dealPackage(file, packageName + "." + file.getName(), loader);
            }
        }
    }
}
