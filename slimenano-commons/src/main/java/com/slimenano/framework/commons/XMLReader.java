package com.slimenano.framework.commons;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import com.slimenano.sdk.logger.Marker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

@Slf4j
@Marker("XML文档阅读器")
public class XMLReader {

    public static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";

    public static final String prefix = "http://www.bioelectronic.top/schema/";

    public static DocumentBuilder documentBuilder;


    static {
        log.warn("请将扩展应用提供的schema文件放入validation文件夹中");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //设置解析器在解析文档的时候校验文档
        factory.setValidating(true);
        //通过指定factory的属性，确定使用Schema进行校验
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA_NS_URI);
        //让解析器支持命名空间
        factory.setNamespaceAware(true);
        try {
            documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setErrorHandler(new DefaultHandler() {
                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw e;
                }
            });
            documentBuilder.setEntityResolver((publicId, systemId) -> {
                if (systemId != null) {
                    if (systemId.startsWith(prefix)) {
                        InputStream is = XMLReader.class.getClassLoader().getResourceAsStream("validation/" + systemId.substring(prefix.length()));
                        if (is != null)
                            return new InputSource(is);
                        try {
                            return new InputSource(new FileInputStream("validation" + File.separator + systemId.substring(prefix.length())));
                        } catch (IOException e) {
                            log.error("打开约束文档失败", e);
                        }
                    }
                }
                return null;
            });

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document parse(InputStream is) throws IOException, SAXException {
        return documentBuilder.parse(is);
    }

    public static Object deepE2M(Node root) {
        String tgName = "";
        if (root.getNodeType() == Node.TEXT_NODE || root.getNodeType() == Node.CDATA_SECTION_NODE) {
            String s = root.getTextContent().trim();
            if (s.isEmpty()) return null;
            return s;
        } else if (root.getNodeType() == Node.ELEMENT_NODE) {
            HashMap<String, Object> map = new HashMap<>();
            NamedNodeMap attrs = root.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                put(map, attr.getNodeName(), attr.getNodeValue());
            }
            StringBuilder content = new StringBuilder();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                Object o = deepE2M(child);
                if (o != null) {
                    if (o instanceof String) {
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            put(map, child.getNodeName(), o);
                        } else {
                            content.append(o);
                        }
                    } else {
                        put(map, child.getNodeName(), o);
                    }
                }
            }
            if ((content.length() == 0) && map.isEmpty()) return null;
            if ((content.length() > 0) && !map.isEmpty()) {
                map.put("*", content.toString());
            }
            if (map.isEmpty()) {
                return content.toString();
            }
            if (map.size() == 1) {
                return map.values().stream().findFirst().get();
            }
            return map;


        }

        return null;
    }

    private static String put(HashMap<String, Object> map, String key, Object value) {
        if (key.contains(":")){
            key = key.substring(key.lastIndexOf(":")+1);
        }
        if (map.containsKey(key)) {
            Object o = map.get(key);
            if (o instanceof LinkedList) {
                ((LinkedList) o).add(value);
            } else {
                LinkedList list = new LinkedList();
                list.add(o);
                list.add(value);
                map.put(key, list);
            }
        } else {
            map.put(key, value);
        }

        return key;
    }

    public static <T> T EMO2Bean(Object emo, Class<T> clazz) throws SAXException {
        try {
            if (emo == null) return null;
            if (clazz == HashMap.class && emo instanceof HashMap) {
                return (T) emo;
            }
            String emoStr = String.valueOf(emo);
            if (clazz == String.class) {
                return (T) emoStr;
            }
            if (clazz == boolean.class || clazz == Boolean.class){
                return (T) (Object) Boolean.parseBoolean(emoStr);
            }
            if (clazz == int.class || clazz == Integer.class){
                return (T) (Object) Integer.parseInt(emoStr);
            }
            if (clazz == float.class || clazz == Float.class){
                return (T) (Object) Float.parseFloat(emoStr);
            }
            if (clazz == double.class || clazz == Double.class){
                return (T) (Object) Double.parseDouble(emoStr);
            }
            if (clazz == short.class || clazz == Short.class){
                return (T) (Object) Short.parseShort(emoStr);
            }
            if (clazz == byte.class || clazz == Byte.class){
                return (T) (Object) Byte.parseByte(emoStr);
            }
            if (clazz == char.class || clazz == Character.class){
                return (T) (Object) emoStr.charAt(0);
            }
            if (clazz.isEnum()){
                for (T enumConstant : clazz.getEnumConstants()) {
                    if (emoStr.equalsIgnoreCase(enumConstant.toString())){
                        return enumConstant;
                    }
                }
                throw new SAXException("无法找到对应的枚举！值：" + emoStr);
            }


            if (emo instanceof LinkedList && clazz.isArray()) {
                T array = (T) Array.newInstance(clazz.getComponentType(), ((LinkedList) emo).size());
                for (int i = 0; i < ((LinkedList) emo).size(); i++) {
                    Array.set(array, i, EMO2Bean(((LinkedList<?>) emo).get(i), clazz.getComponentType()));
                }
                return array;
            }

            if (emo instanceof String && clazz.isArray()){
                return (T) new String[]{(String) emo};
            }

            if (emo instanceof HashMap && clazz.isArray()){
                T array = (T) Array.newInstance(clazz.getComponentType(), 1);
                Array.set(array, 0, EMO2Bean(emo, clazz.getComponentType()));
                return array;
            }

            if (emo instanceof HashMap && Map.class.isAssignableFrom(clazz)){

                if (Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface()){
                    return (T) emo;
                }else{
                    return clazz.getConstructor(Map.class).newInstance(emo);
                }

            }

            if (emo instanceof HashMap) {
                T t = clazz.newInstance();
                List<Field> allField = ClassUtils.getAllField(clazz);
                for (Field field : allField) {
                    field.setAccessible(true);
                    if (((HashMap<?, ?>) emo).containsKey(field.getName())) {
                        field.set(t, EMO2Bean(((HashMap<?, ?>) emo).get(field.getName()), field.getType()));
                    }
                }
                return t;
            }
        }catch (Exception e){
            throw new SAXException("类型转换时出现错误", e);
        }

        throw new SAXException("无法转换到类型：" + clazz);
    }


}
