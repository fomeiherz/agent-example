package top.fomeiherz;

import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PerfMonXformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;

        System.out.println("Transforming " + className);

        ClassPool pool = ClassPool.getDefault();

        CtClass cl = null;

        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (!cl.isInterface()) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    CtBehavior method = methods[i];
                    method.insertBefore("long stime = System.nanoTime();");
                    method.insertAfter("System.out.println(\"leave \"" + method.getName() + "\" and time: \" + (System.nanoTime() - stime));");
                }
                transformed = cl.toBytecode();
            }
        } catch (Exception ex) {

        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return transformed;
    }
}
