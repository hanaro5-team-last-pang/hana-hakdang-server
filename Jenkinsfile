pipeline {
  agent {
    label 'api-agent'
  }

  stages {
    stage("Checkout") {
      steps {
        git url: 'https://github.com/hanaro5-team-last-pang/hana-hakdang-server',
        branch: 'main'
      }
    }
    stage("Deploy") {
      steps {
        sh 'sudo sh ./run-prod-docker.sh'
      }
    }
  }
}