data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }
}

resource "aws_security_group" "gymsystem_sg" {
  name = "gymsystem-sg"

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Spring Boot"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "SonarQube"
    from_port   = 9000
    to_port     = 9000
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "gymsystem-sg"
  }
}

resource "aws_instance" "gymsystem_server" {
  ami           = data.aws_ami.amazon_linux.id
  instance_type = "t3.micro"
  key_name      = "gymsystem-key2"

  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name

  vpc_security_group_ids = [
    aws_security_group.gymsystem_sg.id
  ]

  root_block_device {
    volume_size = 20
    volume_type = "gp3"
  }

  user_data = <<-EOF
#!/bin/bash
yum update -y
yum install docker -y
systemctl start docker
systemctl enable docker

docker network create gymsystem-network

docker run -d \
--name gymsystem-mysql \
--network gymsystem-network \
-e MYSQL_DATABASE=gymsystem \
-e MYSQL_ROOT_PASSWORD=root \
-v gymsystem_mysql_data:/var/lib/mysql \
mysql:8.0

sleep 40

docker pull fhenez/gymsystem:latest

docker run -d \
--name gymsystem-app \
--network gymsystem-network \
-p 8080:8080 \
-e SPRING_DATASOURCE_URL="jdbc:mysql://gymsystem-mysql:3306/gymsystem?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true" \
-e SPRING_DATASOURCE_USERNAME=root \
-e SPRING_DATASOURCE_PASSWORD=root \
-e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
fhenez/gymsystem:latest
EOF

  tags = {
    Name = "GymSystem-Server"
  }
}