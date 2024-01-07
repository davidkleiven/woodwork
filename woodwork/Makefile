PREFIX ?= ~/.local

build:
		mvn package

install: build
		cp target/woodwork.jar ${PREFIX}/bin
		cp woodwork ${PREFIX}/bin

clean:
		rm -r target