nohup mvn compile sonar:sonar -Dsonar.projectKey=agilework -Dsonar.host.url=http://$1:9000 -Dsonar.login=$2 > scan.log 2>&1 &
