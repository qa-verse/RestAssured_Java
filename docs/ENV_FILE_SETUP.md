# .env File Setup Guide

This guide explains how to set up and use `.env` files for environment configuration.

## What is .env File?

The `.env` file is a simple text file that contains environment variables in `KEY=VALUE` format. It allows you to configure your local development environment without modifying property files or committing sensitive information to version control.

## Creating .env File

### Step 1: Create .env File

In the project root directory, create a file named `.env`:

```bash
# Windows
type nul > .env

# macOS/Linux
touch .env
```

### Step 2: Add Configuration

Copy the following template and update with your values:

```env
# Environment Configuration
# Copy this file to .env and update with your actual values
# DO NOT commit .env file to version control

# Environment name (local, qa, staging, production)
ENV=local

# API Base URL
BASE_URL=https://jsonplaceholder.typicode.com

# Health Check Configuration
HEALTH_ENDPOINT=/posts/1
HEALTH_EXPECTED_ID=1

# Timeout Settings (milliseconds)
TIMEOUT=5000

# Retry Configuration
RETRY_COUNT=1

# Logging Level (BASIC, FULL, NONE)
LOG_LEVEL=BASIC

# API Authentication (if required)
API_KEY=
AUTH_TOKEN=

# Database Configuration (if applicable)
DB_HOST=localhost
DB_PORT=5432
DB_NAME=testdb
DB_USER=testuser
DB_PASSWORD=

# Reporting Configuration
REPORT_DIR=target/reports
ALLURE_RESULTS_DIR=target/allure-results
```

### Step 3: Update Values

Replace the placeholder values with your actual configuration:

```env
ENV=local
BASE_URL=https://api.example.com
TIMEOUT=10000
RETRY_COUNT=3
LOG_LEVEL=FULL
API_KEY=your-api-key-here
```

## .env File Format Rules

1. **Key-Value Pairs**: Use `KEY=VALUE` format
2. **No Spaces**: No spaces around the `=` sign
3. **Quotes**: Values with spaces should be quoted: `KEY="value with spaces"`
4. **Comments**: Lines starting with `#` are comments
5. **Empty Lines**: Empty lines are ignored
6. **Case Sensitive**: Keys are case-sensitive

### Valid Examples

```env
# Simple value
BASE_URL=https://api.example.com

# Value with spaces (quoted)
APP_NAME="My Test Application"

# Empty value
API_KEY=

# Number value
TIMEOUT=5000

# Boolean-like value
ENABLE_LOGGING=true
```

### Invalid Examples

```env
# ❌ Space around =
BASE_URL = https://api.example.com

# ❌ Missing value (will be empty string)
BASE_URL=

# ❌ Special characters without quotes (may cause issues)
APP_NAME=My App v1.0
```

## How It Works

The framework automatically loads `.env` file when `ConfigManager` is initialized:

1. **Load Order**:
   - System properties (`-Dproperty=value`)
   - Environment variables (`ENV=qa`)
   - `.env` file variables
   - Environment-specific property files (`config/<env>.properties`)
   - Default property file (`config/local.properties`)

2. **Priority**: Higher priority sources override lower priority ones.

3. **Automatic Loading**: The `EnvFileLoader` class handles loading `.env` files automatically.

## Security Best Practices

### ✅ DO:

- **Add to .gitignore**: `.env` is already in `.gitignore`
- **Use .env.example**: Create a template file (without sensitive data) for reference
- **Use Different Files**: Create `.env.local`, `.env.qa` for different environments
- **Rotate Secrets**: Regularly update API keys and tokens

### ❌ DON'T:

- **Commit .env**: Never commit `.env` file to version control
- **Share Secrets**: Don't share `.env` files via email or chat
- **Hardcode Values**: Don't hardcode sensitive values in code
- **Use Production Secrets Locally**: Use separate credentials for local development

## Environment-Specific .env Files

You can create multiple `.env` files for different environments:

```bash
.env.local      # Local development
.env.qa         # QA environment
.env.staging    # Staging environment
```

To use a specific file:

```bash
# Copy the appropriate file
cp .env.qa .env

# Or use environment variable
ENV=qa mvn test
```

## Troubleshooting

### Issue: .env file not loading

**Check**:
1. File is named exactly `.env` (not `env` or `.env.txt`)
2. File is in project root directory
3. File has correct format (KEY=VALUE)
4. No syntax errors in file

### Issue: Values not being used

**Check**:
1. System properties override .env values
2. Environment variables override .env values
3. Property files override .env values
4. Check priority order in `ConfigManager`

### Issue: Special characters in values

**Solution**: Use quotes for values with special characters:
```env
# ❌ Problematic
PASSWORD=my@pass#word

# ✅ Correct
PASSWORD="my@pass#word"
```

## Example .env Files

### Minimal .env (Local Development)

```env
ENV=local
BASE_URL=http://localhost:8080
TIMEOUT=5000
LOG_LEVEL=FULL
```

### Complete .env (Full Configuration)

```env
ENV=local
BASE_URL=https://api.example.com
HEALTH_ENDPOINT=/health
HEALTH_EXPECTED_ID=1
TIMEOUT=10000
RETRY_COUNT=3
LOG_LEVEL=FULL
API_KEY=sk_test_1234567890
AUTH_TOKEN=Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
DB_HOST=localhost
DB_PORT=5432
REPORT_DIR=target/reports
```

## Integration with CI/CD

For CI/CD pipelines, use environment variables or secrets management:

```yaml
# GitHub Actions example
env:
  BASE_URL: ${{ secrets.BASE_URL }}
  API_KEY: ${{ secrets.API_KEY }}
```

The `.env` file is primarily for local development. CI/CD should use:
- Environment variables
- Secrets management (GitHub Secrets, AWS Secrets Manager, etc.)
- Configuration files in the repository (without secrets)

## Next Steps

1. Create your `.env` file using the template
2. Update values for your environment
3. Verify it's working: `mvn test -Denv=local`
4. Check logs to confirm configuration is loaded correctly

## Related Documentation

- `README.md` - Main framework documentation
- `ENV_SETUP.md` - Complete environment setup guide
- `naming_conventions.md` - Coding standards

