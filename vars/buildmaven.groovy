import org.myorg.utils.Helper

def call() {
    def helper = new Helper(this)

    pipeline {
        agent any

        environment {
            APP_NAME = 'MyApp'
        }

        stages {
            stage('Build') {
                steps {
                    script {
                        helper.logInfo('Building the application...')
                        sh 'mvn clean install'
                    }
                }
            }

            stage('Test') {
                steps {
                    script {
                        helper.logInfo('Running tests...')
                        sh 'mvn test'
                        sh 'pwd'
                        sh 'ls -ltR'
                    }
                }
            }

            stage('Archive Artifacts') {
                steps {
                    script {
                        helper.logInfo('Archiving build artifacts...')
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    }
                }
            }

            stage('Deploy') {
                when {
                    branch 'main'
                }
                steps {
                    script {
                        helper.logInfo('Deploying application...')
                        // sh './scripts/deploy.sh'
                    }
                }
            }

            stage('Print Build Notification Template') {
                steps {
                    script {
                        def template = libraryResource('org/myorg/templates/example.txt')
                        def message = template
                            .replace('${APP_NAME}', env.APP_NAME)
                            .replace('${BUILD_STATUS}', currentBuild.currentResult)
                            .replace('${BRANCH_NAME}', env.BRANCH_NAME ?: 'N/A')
                            .replace('${BUILD_NUMBER}', env.BUILD_NUMBER)
                            .replace('${JOB_NAME}', env.JOB_NAME)

                        echo message
                    }
                }
            }
        }

        post {
            success {
                script {
                    helper.logInfo('Pipeline completed successfully!')
                }
            }
            failure {
                script {
                    helper.logError('Pipeline failed.')
                }
            }
        }
    }
}
