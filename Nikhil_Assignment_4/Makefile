JAVAC=javac
SRC_DIR=src

# Find all java files in the src directory
SOURCES := $(wildcard $(SRC_DIR)/*.java)

# Define the corresponding class files
CLASSES := $(SOURCES:.java=.class)

all: $(CLASSES)

$(CLASSES): $(SOURCES)
	$(JAVAC) $(SOURCES)

clean:
	rm -f $(SRC_DIR)/*.class

.PHONY: all clean
