variable "aws_region" {
  description = "The AWS region to deploy resources in"
  type        = string
  default     = "ap-south-1"
}

variable "instance_type" {
  description = "The EC2 instance type"
  type        = string
  default     = "t2.micro"
}

variable "key_name" {
  description = "The name of the EC2 Key Pair to allow SSH access to the instance"
  type        = string
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "subnet_cidr" {
  description = "CIDR block for the public subnet"
  type        = string
  default     = "10.0.1.0/24"
}

variable "app_port" {
  description = "The port on which the application runs"
  type        = number
  default     = 8080
}

variable "environment" {
  description = "Environment name for tagging resources"
  type        = string
  default     = "production"
}

variable "project_name" {
  description = "Project name to prefix resource tags"
  type        = string
  default     = "skill-exchange"
}
