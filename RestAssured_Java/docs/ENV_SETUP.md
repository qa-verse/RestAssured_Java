# Environment Setup Guide

This guide provides detailed instructions for setting up the testing environment for the RestAssured Java framework.

## Prerequisites Installation

### Java Installation

1. **Download Java 17+**
   - Visit [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
   - Download the appropriate version for your OS

2. **Install Java**
   - **Windows**: Run the installer and follow the wizard
   - **macOS**: Use Homebrew: `brew install openjdk@17`
   - **Linux**: Use package manager: `sudo apt install openjdk-17-jdk` (Ubuntu/Debian)

3. **Verify Installation**
   ```bash
   java -version
   # Should output: openjdk version "17.x.x" or java version "17.x.x"
   ```

4. **Set JAVA_HOME** (if not set automatically)
   - **Windows**: Add to System Environment Variables
     - Variable: `JAVA_HOME`
     - Value: `C:\Program Files\Java\jdk-17` (adjust path)
   - **macOS/Linux**: Add to `~/.bashrc` or `~/.zshrc`
     ```bash
     export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
     export PATH=$JAVA_HOME/bin:$PATH
     ```

### Maven Installation

1. **Download Maven 3.9+**
   - Visit [Maven Download Page](https://maven.apache.org/download.cgi)
   - Download `apache-maven-3.9.x-bin.zip` (or tar.gz for Linux/macOS)

2. **Install Maven**
   - **Windows**:
     1. Extract to `C:\Program Files\Apache\maven`
     2. Add `C:\Program Files\Apache\maven\bin` to PATH
   - **macOS**: `brew install maven`
   - **Linux**: 
     ```bash
     sudo apt install maven  # Ubuntu/Debian
     # OR
     sudo yum install maven  # CentOS/RHEL
     ```

3. **Verify Installation**
   ```bash
   mvn -version
   # Should show Maven version and Java version
   ```

4. **Set MAVEN_HOME** (if needed)
   - Similar to JAVA_HOME setup
   - **Windows**: `MAVEN_HOME=C:\Program Files\Apache\maven`
   - **macOS/Linux**: `export MAVEN_HOME=/usr/local/apache-maven`

## IDE Setup

### IntelliJ IDEA

1. **Open Project**
   - File → Open → Select project directory

2. **Configure JDK**
   - File → Project Structure → Project
   - Set SDK to Java 17

3. **Import Maven Project**
   - Right-click `pom.xml` → Maven → Reload Project

4. **Install Plugins** (Optional)
   - Checkstyle-IDEA
   - SpotBugs
   - Allure

### Eclipse

1. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Select project directory

2. **Configure JDK**
   - Window → Preferences → Java → Installed JREs
   - Add JDK 17

### VS Code

1. **Install Extensions**
   - Extension Pack for Java
   - Maven for Java
   - Test Runner for Java

2. **Open Project**
   - File → Open Folder → Select project directory

## Environment Configuration

### Option 1: Property Files (Recommended for CI/CD)

1. **Navigate to config directory**
   ```bash
   cd config
   ```

2. **Edit environment file** (e.g., `local.properties`)
   ```properties
   baseUrl=https://jsonplaceholder.typicode.com
   healthEndpoint=/posts/1
   health.expectedId=1
   timeout=5000
   retry.count=1
   log.level=BASIC
   ```

3. **Create environment-specific files**
   - Copy `local.properties` to `qa.properties`, `staging.properties`
   - Update values for each environment

### Option 2: .env File (Recommended for Local Development)

1. **Create .env file** in project root
   ```bash
   touch .env
   # OR copy from example
   cp .env.example .env
   ```

2. **Edit .env file**
   ```env
   ENV=local
   BASE_URL=https://jsonplaceholder.typicode.com
   HEALTH_ENDPOINT=/posts/1
   HEALTH_EXPECTED_ID=1
   TIMEOUT=5000
   RETRY_COUNT=1
   LOG_LEVEL=BASIC
   ```

3. **Note**: `.env` file is in `.gitignore` - never commit it!

## Verify Setup

### 1. Build Project

```bash
mvn clean compile
```

Expected output: `BUILD SUCCESS`

### 2. Run Tests

```bash
mvn clean test
```

Expected output: Tests should execute successfully

### 3. Check Logs

```bash
# Logs should be created in logs/ directory
ls logs/
# Should see: api-tests.log, errors.log
```

### 4. Generate Reports

```bash
mvn allure:report
```

Expected: Allure report generated in `target/site/allure-maven-plugin/`

## Common Setup Issues

### Issue: "JAVA_HOME is not set"

**Solution**:
```bash
# Windows (PowerShell)
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'User')

# macOS/Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
```

### Issue: "Maven not found"

**Solution**:
- Verify Maven is in PATH: `echo $PATH` (macOS/Linux) or `echo %PATH%` (Windows)
- Reinstall Maven or add to PATH manually

### Issue: "Dependencies not downloading"

**Solution**:
1. Check internet connection
2. Verify Maven settings: `mvn help:effective-settings`
3. Check proxy settings if behind corporate firewall
4. Clear Maven cache: `rm -rf ~/.m2/repository` (macOS/Linux) or `del /s /q %USERPROFILE%\.m2\repository` (Windows)

### Issue: "Port already in use" (Allure serve)

**Solution**:
```bash
# Use different port
mvn allure:serve -Dallure.port=8081
```

## Next Steps

After setup is complete:

1. ✅ Read `README.md` for framework overview
2. ✅ Review `docs/naming_conventions.md` for coding standards
3. ✅ Explore example tests in `src/test/java`
4. ✅ Run sample tests to verify everything works
5. ✅ Start writing your own tests!

## Additional Resources

- [Java Installation Guide](https://docs.oracle.com/en/java/javase/17/install/)
- [Maven Installation Guide](https://maven.apache.org/install.html)
- [IntelliJ IDEA Setup](https://www.jetbrains.com/help/idea/getting-started.html)
- [Eclipse Setup](https://www.eclipse.org/getting_started/)

