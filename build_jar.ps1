# Compile the application first
Write-Host "Compiling the application..."
$pgJar = "lib/postgresql-42.7.2.jar"
$flJar = "lib/flatlaf-3.4.1.jar"
$classpath = ".;$pgJar;$flJar"

if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }
$javaFiles = Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -cp $classpath -d bin $javaFiles

if ($LastExitCode -ne 0) {
    Write-Host "Compilation failed. Aborting JAR creation."
    exit
}

Write-Host "Packaging dependencies..."
cd bin
jar xf ..\lib\postgresql-42.7.2.jar
jar xf ..\lib\flatlaf-3.4.1.jar

# Remove digital signatures from extracted dependencies to prevent security exceptions
if (Test-Path "META-INF\*.SF") { Remove-Item "META-INF\*.SF" -Force }
if (Test-Path "META-INF\*.DSA") { Remove-Item "META-INF\*.DSA" -Force }
if (Test-Path "META-INF\*.RSA") { Remove-Item "META-INF\*.RSA" -Force }

Write-Host "Creating Executable JAR (KhaanaApp.jar)..."
jar cfe ..\KhaanaApp.jar com.foodwaste.MainApp .

cd ..
Write-Host "✅ Done! You can now share KhaanaApp.jar with anyone."
