pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'    // Maven + JDK 17
            args '-v $HOME/.m2:/root/.m2'           // Cache Maven local repo
        }
    }

    environment {
        REGISTRY          = 'docker.io'
        REPO              = 'hunghungdfsasd'       // Tên Docker Hub user của bạn
        IMAGE_NAME        = 'springboot-demo'      // Tên image
        IMAGE_TAG         = "${env.BUILD_NUMBER}"
        DOCKER_CREDENTIALS = 'dockerhub'           // Jenkins Credentials ID cho DockerHub (username/password)
    }

    options {
        skipStagesAfterUnstable()
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw -B clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $REGISTRY/$REPO/$IMAGE_NAME:$IMAGE_TAG ."
                sh "docker tag $REGISTRY/$REPO/$IMAGE_NAME:$IMAGE_TAG $REGISTRY/$REPO/$IMAGE_NAME:latest"
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin $REGISTRY"
                }
                sh "docker push $REGISTRY/$REPO/$IMAGE_NAME:$IMAGE_TAG"
                sh "docker push $REGISTRY/$REPO/$IMAGE_NAME:latest"
            }
        }

        stage('Deploy (Optional)') {
            steps {
                // Nếu bạn muốn tự động deploy trên máy Jenkins hoặc máy deploy có Docker
                sh """
                docker stop $IMAGE_NAME || true
                docker rm $IMAGE_NAME || true
                docker run -d --name $IMAGE_NAME -p 8888:8888 $REGISTRY/$REPO/$IMAGE_NAME:latest
                """
            }
        }
    }
}
