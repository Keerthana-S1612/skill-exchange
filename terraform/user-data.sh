#!/bin/bash
# Redirect output to log file for debugging
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1

echo "========================================="
echo "Starting Skill Exchange App Setup"
echo "========================================="

# Update Package Index
apt-get update -y
apt-get upgrade -y

# Install prerequisite packages
apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release git

# Install Docker using official installer script
echo "Installing Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# Start and enable Docker service
systemctl start docker
systemctl enable docker

# Add ubuntu user to docker group to allow running docker commands without sudo
usermod -aG docker ubuntu

# Install Docker Compose CLI plugin (if not already installed by installer script)
apt-get install -y docker-compose-plugin

# Verify Docker installation
docker --version
docker compose version

# Setup Swap Memory (2GB) to prevent Out-Of-Memory crashes
# Especially crucial for t2.micro (1GB RAM) running both MySQL 8.0 and Spring Boot
echo "Configuring Swap Space..."
if [ ! -f /swapfile ]; then
    fallocate -l 2G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
    swapon /swapfile
    echo '/swapfile none swap sw 0 0' | tee -a /etc/fstab
    echo "Swap space configured successfully."
else
    echo "Swap file already exists."
fi

# Verify memory configurations
free -h

# Create Application directories
echo "Creating application workspace..."
mkdir -p /home/ubuntu/app
chown -R ubuntu:ubuntu /home/ubuntu/app

echo "========================================="
echo "Setup Finished Successfully!"
echo "========================================="
