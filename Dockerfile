FROM tomcat:latest

RUN rm -r webapps
RUN mkdir webapps
RUN mkdir webapps/ROOT
COPY out/artifacts/checkers_war_exploded/. webapps/ROOT/

CMD bin/catalina.sh run


