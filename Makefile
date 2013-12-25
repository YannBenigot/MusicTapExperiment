all:
	export CLASSPATH=/home/yann/work/JSFML/out/:/home/yann/work/JSFML/out/classes/:/home/yann/work/JSFML/out/bin/:.:./jtransforms-2.4.jar && \
	javac org/musictap/viewer/Viewer.java && \
	java org.musictap.viewer.Viewer test9.wav

