pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'cd user-service'
                sh 'mvn clean'
                sh 'mvn install'
            }
        }
    }
    post {
        success {
            script {
                sh 'ls'
            }
        }

        failure {
            script {
                sh 'java -version'
            }
        }
    }
}