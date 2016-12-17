FROM tomcat:8.5.9

RUN rm -r webapps
RUN mkdir webapps
RUN mkdir webapps/ROOT
COPY out/artifacts/checkers_war_exploded/. webapps/ROOT/

EXPOSE 8080/tcp
CMD bin/catalina.sh run


