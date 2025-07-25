def call() {
    pipeline {
        agent any

        environment {
            APP_NAME = 'MyApp'
        }

        stages {

            stage('Build') {
                steps {
                    echo 'Building the application...'
                    sh 'mvn clean install'
                }
            }

            stage('Test') {
                steps {
                    echo 'Running tests...'
                    sh 'mvn test'
                    sh 'pwd'
                    sh 'ls -ltR'
                }
            }

            stage('Archive Artifacts') {
                steps {
                    echo 'Archiving build artifacts...'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }

            stage('Deploy') {
                when {
                    branch 'main'
                }
                steps {
                    echo 'Deploying application...'
                    // sh './scripts/deploy.sh'
                }
            }
        }

        post {
            success {
                echo 'Pipeline completed successfully!'
            }
            failure {
                echo 'Pipeline failed.'
            }
        }
    }
}
