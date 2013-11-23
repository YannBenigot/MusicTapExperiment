rm *.class
export CLASSPATH=/home/yann/work/JSFML/out/:/home/yann/work/JSFML/out/classes/:/home/yann/work/JSFML/out/bin/:.:./WavFile/:./jtransforms-2.4.jar
javac Viewer.java && \
java Viewer test4.wav

