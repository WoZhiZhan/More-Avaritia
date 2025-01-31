package net.wzz.more_avaritia.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.*;

public class MoreAvaritiaTransformer implements IClassTransformer {
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        final String notchName = name;
        if ("net.minecraft.entity.EntityLivingBase".equals(transformedName)) {
            ClassReader reader = new ClassReader(basicClass);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5, writer) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    String srgName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(notchName, name, desc);
                    if ("getHealth".equals(srgName) || "func_110143_aJ".equals(srgName) && "()F".equals(desc)) {
                        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                        mv.visitCode();
                        Label start = new Label();
                        mv.visitLabel(start);
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "net/wzz/more_avaritia/util/EventUtil", "getHealth", "(Lnet/minecraft/entity/EntityLivingBase;)F", false);
                        mv.visitInsn(Opcodes.FRETURN);
                        Label end = new Label();
                        mv.visitLabel(end);
                        mv.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, start, end, 0);
                        mv.visitMaxs(1,1);
                        mv.visitEnd();
                        return null;
                    }
                    return cv.visitMethod(access, name, desc, signature, exceptions);
                }
            };
            reader.accept(visitor, Opcodes.ASM5);
            return writer.toByteArray();
        }
      return basicClass;
    }
}
