pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn --version'
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