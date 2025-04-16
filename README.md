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
- Performance testing with JMeter

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Maven 3.9.5
- Spring Cache
- Docker
- Kubernetes
- JMeter (for performance testing)

## Prerequisites

- Java 21 or higher
- Maven 3.9.5 or higher
- Docker (optional, for containerized deployment)
- Kubernetes cluster (optional, for Kubernetes deployment)
- kubectl (optional, for Kubernetes deployment)
- JMeter 5.6.2 or higher (for performance testing)

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

- `POST /transactions` - Create a new transaction
  - Request Body: JSON with `description` and `amount` fields
  - Response: Created transaction object with ID

- `DELETE /transactions/{id}` - Delete a transaction
  - Path Parameter: `id` - Transaction ID
  - Response: 200 OK with deleted transaction, or 404 Not Found

- `PUT /transactions/{id}` - Modify a transaction
  - Path Parameter: `id` - Transaction ID
  - Request Body: JSON with updated `description` and `amount` fields
  - Response: 200 OK with modified transaction, or 404 Not Found

- `GET /transactions` - List transactions with pagination
  - Query Parameters:
    - `page` (default: 0) - Page number (0-based)
    - `size` (default: 10) - Number of items per page
  - Response: JSON object containing:
    - `transactions`: Array of transactions
    - `currentPage`: Current page number
    - `totalItems`: Total number of transactions
    - `totalPages`: Total number of pages

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

## Performance Testing

The project includes JMeter test plans for performance testing. The test scenarios include:

- Initial transaction creation
- Concurrent transaction operations (create, list, modify, delete)
- Mixed workload testing with realistic transaction patterns

### Running Performance Tests

1. Install JMeter:
```bash
# On macOS
brew install jmeter
```

2. Run the performance test:
```bash
jmeter -n -t performance-test/TransactionManager.jmx -l performance-test/results/test_results.jtl -e -o performance-test/results/report
```

### Test Configuration

The test plan consists of two thread groups:

1. Initialization Group:
   - Single thread
   - Creates initial transactions
   - Runs once at the start

2. Transaction API Test Group:
   - 50 concurrent users
   - 30 seconds ramp-up time
   - 3 minutes test duration
   - Mixed workload including:
     - Create transactions with random descriptions and amounts
     - List transactions with pagination
     - Modify existing transactions
     - Delete transactions

### Test Results

After running the tests, you can find:
- Detailed test results in `performance-test/results/test_results.jtl`
- HTML report in `performance-test/results/report`
- Summary report in `performance-test/results/summary_report.jtl`

Key metrics to monitor:
- Response time (average, median, 90th percentile)
- Throughput (transactions per second)
- Error rate
- Active requests count
- System resource utilization

### Performance Test Results Analysis

The test results will help identify:
- System throughput under concurrent load
- Response time patterns
- Potential bottlenecks
- System stability under sustained load
- Resource utilization patterns 

## Latest Performance Test Results

The most recent performance test was conducted with 50 concurrent users over a 3-minute period with a 30-second ramp-up time.

### Summary

| Metric                  | Value              |
|-------------------------|-------------------|
| Total Transactions      | 23,000            |
| Throughput              | 498.14 tps        |
| Average Response Time   | 0.98 ms           |
| 90% Response Time (pct1)| 2.0 ms            |
| 99% Response Time (pct3)| 7.0 ms            |
| Error Rate              | 0%                |

### Transaction Type Analysis

| Transaction Type        | Count | Avg Response | 90% Response | Throughput  |
|-------------------------|-------|--------------|--------------|-------------|
| Create Transaction      | 5,000 | 0.67 ms      | 1.0 ms       | 108.67 tps  |
| List Transactions       | 5,000 | 2.06 ms      | 4.0 ms       | 108.78 tps  |
| Modify Transaction      | 5,000 | 0.78 ms      | 2.0 ms       | 108.79 tps  |
| Delete Transaction      | 5,000 | 0.65 ms      | 1.0 ms       | 108.79 tps  |
| Complete Flow           | 5,000 | 4.16 ms      | 8.0 ms       | 108.64 tps  |
| Batch Create            | 1,500 | 0.46 ms      | 1.0 ms       | 50.82 tps   |
| Batch Query             | 1,500 | 0.70 ms      | 1.0 ms       | 50.83 tps   |

### Analysis

- The system demonstrated excellent performance, with zero errors across all 23,000 test transactions.
- The overall throughput of 498.14 transactions per second indicates high capacity for concurrent operations.
- All operations show exceptional response times, with the average under 1 millisecond.
- List operation is relatively more resource-intensive (2.06 ms on average), as expected due to data retrieval and pagination.
- The complete transaction flow (create, list, modify, delete) averaged only 4.16 ms, demonstrating excellent end-to-end performance.
- Even under high concurrency (50 users), the system maintained consistent performance with no degradation.

These results indicate that the transaction management system can easily handle high throughput with minimal latency, making it suitable for production environments with demanding performance requirements. 