# 🚀 Skill Exchange – Production-Style CI/CD Pipeline
### Production-grade DevOps implementation using Docker, GitHub Actions, and DockerHub

---

## 📌 Overview
This project demonstrates how a real-world Java Spring Boot application can be transformed into a production-ready system using modern DevOps practices. 

The base application, **Skill Exchange Platform**, is a web app where students can register, schedule learning sessions, and request matching skills.

👉 **The focus of this repository is not just the application, but how it is built, packaged, orchestrated, and delivered using a modern cloud-native DevOps workflow.**

---

## 🎯 Project Objective
The goal of this project is to simulate how real-world enterprise systems:
- Automate build and deployment processes.
- Reduce manual intervention and human configuration errors.
- Ensure consistent, repeatable deployments using containerization.
- Deliver applications reliably using GitHub CI/CD pipelines.

This mirrors how containerized web services are handled in real cloud production environments.

---

## 🏗️ Architecture
```
Developer
   ↓
GitHub Repository
   ↓
GitHub Actions (CI/CD Pipeline)
   ↓
Docker Image Build
   ↓
DockerHub (Image Registry)
   ↓
Deployment Ready (AWS EC2 / Cloud Server)
```

---

## 🔄 CI/CD Workflow
Every push to the `main` branch automatically triggers the automated deployment pipeline.

### Pipeline Steps:
1. **Code Push** → Developer pushes the code updates to GitHub.
2. **CI Trigger** → GitHub Actions runner spins up automatically.
3. **Build & Package** → Compiles Java code and builds the Docker image.
4. **Authentication** → Securely logs into DockerHub using repository secrets.
5. **Push Phase** → Uploads the latest container image to DockerHub.
6. **Deployment Ready** → The new version is ready to be instantly pulled and run on any server.

### 💡 This automation ensures:
- Faster delivery cycles.
- Consistent, tested builds.
- Zero manual local packaging errors.

---

## 📁 Project Structure
```
skill-exchange-main/
│
├── .github/workflows/
│   ├── ci-cd.yml          # Main CI/CD pipeline definition
│   └── deploy.yml         # Java compilation test workflow
│
├── src/                   # Spring Boot Java source code
│
├── Dockerfile             # Multi-stage container config
├── docker-compose.yml     # Multi-container setup (App + MySQL)
├── pom.xml                # Maven Dependencies
└── README.md              # Project documentation
```

---

## 🛠️ Tech Stack
- **Java 21 / Spring Boot 4** – Backend logic and Web Server (Tomcat)
- **Thymeleaf** – Frontend UI templating
- **MySQL 8.0** – Persistent Relational Database
- **Docker** – Containerization & Multi-stage building
- **Docker Compose** – Multi-container orchestration (App + Database)
- **GitHub Actions** – CI/CD automation pipeline
- **DockerHub** – Container image registry
- **AWS EC2** – Cloud hosting environment

---

## 🚀 Getting Started (Run Locally)

### 1️⃣ Clone Repository
```bash
git clone https://github.com/Keerthana-S1612/skill-exchange.git
cd skill-exchange
```

### 2️⃣ Run App & Database via Docker Compose
No local Java or MySQL installation is required. Simply run:
```bash
docker-compose up -d
```
*Note: This automatically spins up the MySQL database, runs health checks to ensure it is fully ready, and then starts the Spring Boot web app on port `8080` (with database traffic mapped to host port `3307` to avoid host conflicts).*

### 3️⃣ Access Web App
Open in your browser:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 🔐 CI/CD Setup (GitHub Secrets)
To enable automated DockerHub integration:

1. Go to your GitHub Repository:
   👉 **Settings → Secrets and variables → Actions**
2. Add the following repository secrets:
   - `DOCKER_USERNAME` (Your DockerHub Username)
   - `DOCKER_PASSWORD` (Your DockerHub Password or Access Token)

GitHub Actions securely uses these credentials during pipeline execution to push images under `keerthana161205/skill-exchange`.

---

## 🌍 AWS Cloud Deployment (Production-Ready)
The application is hosted on an **AWS EC2 Ubuntu (Free-Tier)** virtual instance.

### 1. Inbound Network Security (AWS Firewall)
The EC2 Security Group is configured to allow:
- **SSH (Port 22)**: For terminal configuration.
- **Custom TCP (Port 8080)**: Public web access.

### 2. Configure SWAP Memory (Virtual RAM)
Since Free-Tier EC2 instances (`t2.micro`) only have 1GB of physical RAM, running both MySQL 8.0 and Spring Boot can trigger Out-of-Memory (OOM) crashes (Exit Code 137). We allocate a **2GB swapfile** to prevent system crashes:

```bash
# Allocate 2GB file on SSD
sudo fallocate -l 2G /swapfile

# Set secure permissions
sudo chmod 600 /swapfile

# Format as swap memory
sudo mkswap /swapfile

# Activate swap memory
sudo swapon /swapfile

# Ensure swap remains active after server reboot
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Confirm memory levels
free -h
```

### 3. Deploy via Docker Compose on AWS
Once the server is configured:
```bash
sudo docker compose up -d
```
Access the live deployment here:
👉 **[http://65.2.132.75:8080](http://65.2.132.75:8080)**

---

## 🧠 Final Note
This project reflects a real-world DevOps mindset — where building the application is only one part, and automating its delivery pipeline, configuring health checks, and tuning resource usage for cloud environments is equally critical.
