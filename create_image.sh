mvn package
docker build -t lexisnexis/tms .
docker run -p 9099:9099 lexisnexis/tms