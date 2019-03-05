pipeline {
    agent { docker 'maven:3.3.3' }
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