output "instance_public_ip" {
  description = "Public IP address of the provisioned EC2 instance"
  value       = aws_instance.app_server.public_ip
}

output "instance_public_dns" {
  description = "Public DNS name of the provisioned EC2 instance"
  value       = aws_instance.app_server.public_dns
}

output "ssh_connection_command" {
  description = "Convenient command to SSH into the newly created EC2 instance"
  value       = "ssh -i /path/to/your-key.pem ubuntu@${aws_instance.app_server.public_ip}"
}

output "application_url" {
  description = "The HTTP URL for the Skill Exchange application"
  value       = "http://${aws_instance.app_server.public_ip}:${var.app_port}"
}
