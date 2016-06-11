mvn deploy:deploy-file ^
	-DrepositoryId=bytle-m2-repo ^
	-Dfile=target\bytle-niofs-sftp-1.0.0.jar ^
	-Durl=sftp://sftp.bytle.net:22%BYTLE_M2_REPO_HOME% ^
	-DpomFile=pom.xml