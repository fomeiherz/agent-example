package top.fomeiherz;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class PerfMonAgent {

    private static Instrumentation inst = null;

    public static void premain(String agentArgs, Instrumentation _inst) {
        System.out.println("PerfMonAgent.premain() was called.");
        inst = _inst;
        ClassFileTransformer transformer = new PerfMonXformer();
        System.out.println("Adding a PerfMonXformer instance to JVM.");
        inst.addTransformer(transformer);
    }

}
