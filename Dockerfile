# The first line is always FROM that defines a base image
FROM tomcat:8.5-jre8

# Adding info about who created this image
LABEL maintainer="Damion Shirkey"

# We need to move the WAR file to the webapps directory
ADD target/employee-servlet-app.war /usr/local/tomcat/webapps
# The webapps directory contains the app that tomcat serves

# Expose 8080 from the container
EXPOSE 8080

# CMD instruction specifies what to run when the container is run
# In our case the tomcat server is started by running a shell script
CMD ["catalina.sh", "run"]
