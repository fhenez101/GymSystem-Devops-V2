pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/fhenez101/GymSystem-DevOps-V2.git'
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw test'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Deploy to EC2') {
            steps {
                sh '''
                ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/gymsystem-key2.pem ec2-user@100.54.212.116 "
                sudo docker pull fhenez/gymsystem:jenkins &&
                sudo docker stop gymsystem-app || true &&
                sudo docker rm gymsystem-app || true &&
                sudo docker run -d --name gymsystem-app \
                  --network gymsystem-network \
                  -p 8080:8080 \
                  -e SPRING_DATASOURCE_URL='jdbc:mysql://gymsystem-mysql:3306/gymsystem?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true' \
                  -e SPRING_DATASOURCE_USERNAME=root \
                  -e SPRING_DATASOURCE_PASSWORD=root \
                  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                  fhenez/gymsystem:jenkins
                "
                '''
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    docker push fhenez/gymsystem:jenkins
                    '''
                }
            }
        }
    }
}