agent: FOLBC.class
	
Run.class: FOLBC.java
	JAVA_HOME=/usr/usc/jdk/1.6.0_23 PATH=/usr/usc/jdk/1.6.0_23/bin:${PATH} javac FOLBC.java

run: FOLBC.class
	JAVA_HOME=/usr/usc/jdk/1.6.0_23/bin/ PATH=/usr/usc/jdk/1.6.0_23/bin:${PATH} java FOLBC
