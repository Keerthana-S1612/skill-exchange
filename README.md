# Skill Exchange Platform 🎓

A containerized Spring Boot web application designed for students to exchange skills, schedule learning sessions, and request learning matches. Built using Java 21, Thymeleaf templates, Spring Data JPA, and MySQL.

---

## 🛠️ Features & Architecture
- **Multi-Container Architecture**: Separates the Web application logic (Spring Boot) and the persistent database storage (MySQL 8.0).
- **Orchestration**: Managed dynamically via Docker Compose.
- **Boot Ordering (Health Checks)**: The web application container waits for the database container to be fully initialized and `Healthy` before starting up to prevent connection crashes.
- **Port Conflict Resolution**: Configured to run database traffic on port `3307` on the host to avoid collisions with local MySQL installations on port `3306`.

---

## 🚀 Local Execution Guide

### Prerequisites
- Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) and ensure the Docker daemon is running.

### Running the Application
1. Clone the repository and navigate to the project directory:
   ```bash
   cd skill-exchange-main
   ```
2. Start the application services in detached mode:
   ```bash
   docker-compose up -d
   ```
3. Access the web application in your browser:
   👉 **[http://localhost:8080](http://localhost:8080)**

4. To stop the running containers:
   ```bash
   docker-compose down
   ```

---

## 🤖 GitHub Actions CI/CD Pipeline

The project features an automated compilation, build, and delivery pipeline configured in `.github/workflows/ci-cd.yml`.

### How it works:
1. Every `push` to the `main` branch triggers the GitHub runner.
2. The pipeline checks out the repository, sets up JDK 21, and runs a verification build via Maven.
3. It signs in to the Docker Hub registry and builds a production Docker image.
4. The image is uploaded automatically to the Docker Hub image registry.

### Required Repository Secrets:
To allow the pipeline to push build artifacts to Docker Hub, configure the following secrets under **Settings -> Secrets and variables -> Actions**:
- `DOCKER_USERNAME`: Your Docker Hub username.
- `DOCKER_PASSWORD`: Your Docker Hub password or Personal Access Token.

---

## ☁️ AWS EC2 Cloud Deployment Guide

The application is deployed on a free-tier virtual server on AWS EC2 (Ubuntu Linux).

### 1. Inbound Firewall Rules (AWS Security Groups)
Enable access to the following ports in the AWS console:
- **SSH (Port 22)**: For server command access.
- **Custom TCP (Port 8080)**: For public browser access.

### 2. Configure SWAP Memory (Virtual RAM)
Because AWS Free-Tier `t2.micro` instances only provide 1GB of RAM, running both MySQL 8.0 and a Java Spring Boot container can lead to Out of Memory (OOM) crashes. Establish a 2GB SWAP file on the server to prevent system crashes:

```bash
# Allocate 2GB empty file
sudo fallocate -l 2G /swapfile

# Set secure permissions
sudo chmod 600 /swapfile

# Format file as swap area
sudo mkswap /swapfile

# Enable swap memory
sudo swapon /swapfile

# Configure swap to auto-enable on server restarts
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Verify memory allocations
free -h
```

### 3. Deploying using Docker Compose on the Server
1. Install Docker and Docker Compose on the AWS instance:
   ```bash
   sudo apt-get update
   sudo apt-get install -y docker.io docker-compose
   ```
2. Copy the `docker-compose.yml` file to the server and start the stack:
   ```bash
   sudo docker-compose up -d
   ```
3. Access your live application at:
   👉 **`http://<YOUR_EC2_PUBLIC_IP>:8080`**
