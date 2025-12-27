# Pokemon Rankings Backend

Spring Boot backend application for Pokemon card rankings.

## Setup Instructions

### 1. Environment Variables

Copy the example environment file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env` with your actual credentials:
- AWS DynamoDB credentials (for production)
- Pokemon TCG API key
- JWT secret (generate a secure random hex string)
- CORS allowed origins

### 2. Load Environment Variables

#### Option A: Export variables manually (Linux/Mac)
```bash
export $(cat .env | xargs)
mvn spring-boot:run
```

#### Option B: Use dotenv-cli (recommended for local development)
```bash
npm install -g dotenv-cli
dotenv -e .env -- mvn spring-boot:run
```

#### Option C: Use IDE environment variable support
Most IDEs (IntelliJ, VS Code) can load `.env` files automatically.

### 3. Local Development with DynamoDB Local

For local development, you'll need DynamoDB Local:

```bash
# Download and run DynamoDB Local
docker run -p 8000:8000 amazon/dynamodb-local

# Or use the JAR file
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

Then set in your `.env`:
```
AMAZON_DYNAMODB_ENDPOINT=http://localhost:8000
AMAZON_AWS_ACCESSKEY=local
AMAZON_AWS_SECRETKEY=local
```

### 4. Production Deployment

For production on EC2:

1. Set environment variables in your systemd service file or `/etc/environment`
2. Leave `AMAZON_DYNAMODB_ENDPOINT` empty to use AWS DynamoDB service
3. Use IAM roles instead of access keys when possible
4. Update `CORS_ALLOWED_ORIGINS` with your production frontend URL

Example systemd service configuration:
```ini
[Service]
Environment="AMAZON_AWS_REGION=us-east-1"
Environment="JWT_SECRET=your-secret-here"
Environment="POKEMON_TCG_API_KEY=your-key-here"
Environment="CORS_ALLOWED_ORIGINS=https://your-frontend.com"
```

## Building and Running

```bash
# Build
mvn clean package

# Run
java -jar target/pokemon-rankings-backend-0.0.1-SNAPSHOT.jar

# Or with Maven
mvn spring-boot:run
```

## Security Notes

- **Never commit `.env` file** - it's already in `.gitignore`
- **Use `.env.example`** as a template for team members
- **For production**: Use AWS Secrets Manager or IAM roles instead of hardcoded credentials
- **Generate a strong JWT secret** (64+ character random hex string)

## Environment Variables Reference

| Variable | Description | Required |
|----------|-------------|----------|
| `AMAZON_DYNAMODB_ENDPOINT` | DynamoDB endpoint (empty for AWS service) | No |
| `AMAZON_AWS_ACCESSKEY` | AWS access key (not needed with IAM roles) | No |
| `AMAZON_AWS_SECRETKEY` | AWS secret key (not needed with IAM roles) | No |
| `AMAZON_AWS_REGION` | AWS region | Yes |
| `POKEMON_TCG_API_KEY` | Pokemon TCG API key | Yes |
| `JWT_SECRET` | JWT signing secret | Yes |
| `JWT_EXPIRATION` | JWT expiration in milliseconds | No (default: 86400000) |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins | Yes |
| `LOG_LEVEL_*` | Logging levels for different packages | No |



