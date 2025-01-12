ASSEMBLER_DIR = Assembler
TABLE_DIR = Table

$(ASSEMBLER_DIR)/pass2.class: $(ASSEMBLER_DIR)/pass2.java $(TABLE_DIR)/Table.class
	javac -d . $(ASSEMBLER_DIR)/pass2.java

$(TABLE_DIR)/Table.class: $(TABLE_DIR)/Table.java
	javac -d . $(TABLE_DIR)/Table.java

all: $(ASSEMBLER_DIR)/pass1.class $(TABLE_DIR)/Table.class

run: all
	@java -classpath . Assembler.pass2

clean:
	rm -f $(ASSEMBLER_DIR)/*.class $(TABLE_DIR)/*.class *.class