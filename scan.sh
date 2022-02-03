nohup sonar-scanner -Dsonar.login=$1 -Dsonar.projectKey=$2 -Dsonar.projectName=$2 > scan.log 2>&1 &
