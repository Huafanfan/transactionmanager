# Transaction Manager

A Spring Boot based transaction management system that provides RESTful APIs for managing financial transactions.

## Features

- Create new transactions
- Delete existing transactions
- Modify transaction details
- List all transactions with caching support
- RESTful API endpoints
- Docker containerization support
- Kubernetes deployment support

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Maven 3.9.5
- Spring Cache
- Docker
- Kubernetes

## Prerequisites

- Java 21 or higher
- Maven 3.9.5 or higher
- Docker (optional, for containerized deployment)
- Kubernetes cluster (optional, for Kubernetes deployment)
- kubectl (optional, for Kubernetes deployment)

## Getting Started

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/Huafanfan/transactionmanager.git
cd transactionmanager
```

2. Build the project:
```bash
./mvnw clean package
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Docker Deployment

The project includes a Makefile for simplified Docker operations:

1. Build the Docker image:
```bash
make build
```

2. Run the container:
```bash
make run
```

3. Stop the container:
```bash
make stop
```

4. View running containers:
```bash
make ps
```

5. Clean up:
```bash
make clean-all
```

For more commands, run:
```bash
make help
```

### Kubernetes Deployment

1. Create the Kubernetes namespace:
```bash
kubectl create namespace transaction-manager
```

2. Deploy the application:
```bash
kubectl apply -f k8s/deployment.yaml
```

3. Check the deployment status:
```bash
kubectl get pods -n transaction-manager
```

4. Access the service:
```bash
kubectl port-forward svc/transaction-manager 8080:8080 -n transaction-manager
```

## API Endpoints

- `POST /api/transactions` - Create a new transaction
- `DELETE /api/transactions/{id}` - Delete a transaction
- `PUT /api/transactions/{id}` - Modify a transaction
- `GET /api/transactions` - List all transactions (cached)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/transactionmanager/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── service/
│   │       └── TransactionManagerApplication.java
│   └── resources/
└── test/
    └── java/
        └── com/example/transactionmanager/
k8s/
└── deployment.yaml
```

## Testing

Run the test suite:
```bash
./mvnw test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 