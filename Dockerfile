FROM ubuntu:latest
LABEL authors="maciejfilochowski"

ENTRYPOINT ["top", "-b"]