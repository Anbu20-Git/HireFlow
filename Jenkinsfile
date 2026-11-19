pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java') {
            steps {
                bat 'java -version'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvnw.cmd -B -Dtest=HireFlowApplicationTests clean package'
            }
        }
    }

    post {
        success {
            echo 'HireFlow CI Pipeline completed successfully!'
        }
        failure {
            echo 'HireFlow CI Pipeline failed. Check the console output.'
        }
    }
}