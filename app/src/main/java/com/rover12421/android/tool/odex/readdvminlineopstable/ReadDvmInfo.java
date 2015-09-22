package com.rover12421.android.tool.odex.readdvminlineopstable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by rover12421 on 9/14/15.
 */
public class ReadDvmInfo {

    static {
        System.loadLibrary("readlibdvm");
    }

    public native static String ReadDvmInlineOpsTable();

    public static String GetInlineMethodResolver() {
        String dvmInlineOpsTable = ReadDvmInlineOpsTable();
        StringBuffer sb = new StringBuffer();
        for (String line : dvmInlineOpsTable.split("\n")) {
            sb.append("# ").append(line).append("\n");
            String info[] = line.split(",");
            String classDescriptor = info[0];
            String methodName = info[1];
            String methodSignature = info[2];
            int index = methodSignature.indexOf(")");
            String methodArgSign = methodSignature.substring(1, index);
            String methodRetSign = methodSignature.substring(index+1);

            try {
                String clazzName = classDescriptor.substring(1, classDescriptor.length()-1).replace("/", ".");
                Class cl = Class.forName(clazzName);
                Method methods[] = cl.getMethods();
                boolean find = false;
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        int flag = method.getModifiers();
                        if (Modifier.isStatic(flag)) {
                            sb.append("STATIC,");
                            find = true;
                            break;
                        } else {
                            sb.append("VIRTUAL,");
                            find = true;
                            break;
                        }
                    }
                }
                if (!find) {
                    sb.append("DIRECT,");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            sb.append(classDescriptor).append(",")
                    .append(methodName).append(",")
                    .append(methodArgSign).append(",")
                    .append(methodRetSign).append("\n");
        }

        return sb.toString();
    }
}
