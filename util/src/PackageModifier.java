import org.objectweb.asm.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PackageModifier
{
    public static void main(String[] args) throws IOException
    {
        String inputDir = args[0];  // Directory containing the .class files
        String outputDir = args[1]; // Directory to save modified .class files
        String oldPackage = args[2];   // Original package to change
        String newPackage = args[3];       // New package name

        // Create output directory if it doesn't exist
        new File(outputDir).mkdirs();

        // Loop through all .class files in the input directory
        Files.walk(Paths.get(inputDir))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".class"))
                .forEach(path -> {
                    try {
                        modifyClassPackage(path.toFile(), oldPackage, newPackage, outputDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void modifyClassPackage(File classFile, String oldPackage, String newPackage, String outputDir) throws IOException {
        ClassReader cr = new ClassReader(classFile.getPath());
        ClassWriter cw = new ClassWriter(0);

        ClassVisitor visitor = new ClassVisitor(Opcodes, cw) { // Use ASM4 instead of ASM3
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                // Change the package name if it matches the old package
                if (name.startsWith(oldPackage)) {
                    name = newPackage + name.substring(oldPackage.length());
                }
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public void visitSource(String s, String s1) {

            }

            @Override
            public void visitOuterClass(String s, String s1, String s2) {

            }

            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                return null;
            }

            @Override
            public void visitAttribute(Attribute attribute) {

            }

            @Override
            public void visitInnerClass(String s, String s1, String s2, int i) {

            }

            @Override
            public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
                return null;
            }

            @Override
            public void visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                // You must implement this method to avoid the abstract method error
                super.visitMethod(access, name, desc, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
            }
        };

        cr.accept(visitor, 0);

        // Write the modified class to the output directory
        try (FileOutputStream fos = new FileOutputStream(new File(outputDir, classFile.getName()))) {
            fos.write(cw.toByteArray());
        }
    }
}
