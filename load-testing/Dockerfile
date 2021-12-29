FROM adoptopenjdk/openjdk11
CMD echo "*****"
CMD ls -la /load_test/ > output.txt && echo output.txt
ENTRYPOINT ["/bin/sh", "/load_test/run_load_test.sh"]%
