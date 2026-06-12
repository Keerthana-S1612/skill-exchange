# 🛠️ Skill Exchange – AWS Infrastructure Provisioning (Terraform)

This folder contains Terraform configurations to automate provisioning of a fully configured cloud environment on Amazon Web Services (AWS) for hosting the **Skill Exchange Platform**.

## 🏗️ Architecture Provisioned
- **1x Virtual Private Cloud (VPC)** with DNS hostnames and support enabled.
- **1x Public Subnet** in the primary availability zone.
- **1x Internet Gateway** and Route Tables routing public traffic.
- **1x Security Group** allowing incoming traffic on:
  - Port `22` (SSH)
  - Port `8080` (Spring Boot Web Application)
  - Ports `80`/`443` (HTTP/HTTPS for future reverse proxies like Nginx)
- **1x EC2 Instance** (Ubuntu 22.04 LTS) dynamically queried, preloaded with a bootstrapping user-data script that:
  - Installs Docker and Docker Compose.
  - Grants the default `ubuntu` user Docker permissions.
  - Allocates a **2GB swapfile** on SSD storage to prevent Out-Of-Memory (OOM) crashes under database/application workloads.

---

## 📋 Prerequisites

Before starting, ensure you have completed the following:

1. **Install Terraform CLI**: [Download and Install Terraform](https://developer.hashicorp.com/terraform/downloads).
2. **AWS Account & Credentials**: Configure your credentials locally using the AWS CLI:
   ```bash
   aws configure
   ```
   Or set the environment variables `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`.
3. **Existing Key Pair**: Have an EC2 Key Pair created in your AWS account in the region where you intend to deploy. (e.g. `my-key` saved as `my-key.pem`).

---

## 🚀 Step-by-Step Deployment Guide

### Step 1: Configure Variables
1. Copy the example configuration file:
   ```bash
   cp terraform.tfvars.example terraform.tfvars
   ```
2. Edit `terraform.tfvars` and set the name of your AWS Key Pair:
   ```hcl
   key_name = "my-key"
   ```
   *Note: Do not append the `.pem` file extension, use only the name.*

### Step 2: Initialize Terraform
Download the necessary AWS providers:
```bash
terraform init
```

### Step 3: Preview the Plan
Ensure the configurations match your expectations before running resource provisioning:
```bash
terraform plan
```

### Step 4: Deploy Infrastructure
Apply the configurations to launch the infrastructure on AWS:
```bash
terraform apply
```
Type `yes` when prompted to confirm the changes.

---

## 🐳 Running the Application on Your New Server

When deployment completes successfully, Terraform will output variables showing the public IP, public DNS, and access links.

### 1. SSH into the Server
Use the generated SSH command output:
```bash
ssh -i /path/to/my-key.pem ubuntu@<INSTANCE_PUBLIC_IP>
```

### 2. Verify Docker and Memory Allocation
Verify that Docker, Docker Compose, and the SWAP memory have been correctly initialized:
```bash
# Verify Docker status
docker --version
docker compose version

# Verify 2GB Virtual memory configuration
free -h
```

### 3. Deploy App via Docker Compose
To deploy the application to your new server, copy the application code (especially `docker-compose.yml` and the docker resources) to the server. 

An easy way is to run from your local command line:
```bash
# Copy docker-compose.yml to the server
scp -i /path/to/my-key.pem ../docker-compose.yml ubuntu@<INSTANCE_PUBLIC_IP>:/home/ubuntu/app/
```

Then SSH into the server and launch the containers:
```bash
# Go to app directory
cd /home/ubuntu/app

# Pull and run your Docker container
# Replace keerthana161205/skill-exchange:latest with your image if needed in docker-compose.yml
docker compose up -d
```

Your web application will be live and accessible at:
👉 **`http://<INSTANCE_PUBLIC_IP>:8080`**

---

## 🗑️ Clean Up (Tear Down)
To avoid charges, you can terminate all resources created by Terraform by running:
```bash
terraform destroy
```
Type `yes` when prompted to confirm.
