# Create lib directory if it doesn't exist
if (!(Test-Path "lib")) { New-Item -ItemType Directory -Path "lib" }

# Download dependencies if they don't exist
$pgJar = "lib/postgresql-42.7.2.jar"
$flJar = "lib/flatlaf-3.4.1.jar"
$flMacJar = "lib/flatlaf-extras-3.4.1.jar" # Optional but good

if (!(Test-Path $pgJar)) {
    Write-Host "Downloading PostgreSQL JDBC Driver..."
    Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.2/postgresql-42.7.2.jar" -OutFile $pgJar
}

if (!(Test-Path $flJar)) {
    Write-Host "Downloading FlatLaf Theme..."
    Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4.1/flatlaf-3.4.1.jar" -OutFile $flJar
}

# Compile
Write-Host "Compiling Java files..."
$classpath = ".;$pgJar;$flJar"
$javaFiles = Get-ChildItem -Recurse -Filter *.java | Select-Object -ExpandProperty FullName
javac -cp $classpath -d bin $javaFiles

# Run
if ($LastExitCode -eq 0) {
    Write-Host "Starting Application..."
    java -cp "bin;$classpath" com.foodwaste.MainApp
} else {
    Write-Host "Compilation failed."
}
