echo "Build Started"
echo "Compiling Files..."

javac $(find ../src -name "*.java")

echo "Compiled .class files"

echo "Creating dskloggerclient.jar..."
jar cf dskloggerclient.jar ../src/core/*.class ../src/models/*.class ../src/utils/*.class
echo "Created dskloggerclient.jar..."

echo "Cleaning .class files"
rm -rf ../src/core/*.class ../src/models/*.class ../src/utils/*.class

mkdir out

mv dskloggerclient.jar ./out

echo "Build Succeed"
