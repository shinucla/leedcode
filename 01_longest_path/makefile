
BUILDFILE=build.xml
ANT=~/.opt/lib/apache-ant-1.10.4/bin/ant

default :: compile

compile ::
	$(ANT) -emacs -f $(BUILDFILE) compile

jar ::
	$(ANT) -emacs -f $(BUILDFILE) jar

clean ::
	$(exp) $(ANT) -emacs clean

run.Test :: jar
	$(exp) $(ANT) -emacs \
	-Dtarget.main=test.Test \
	run.SpecifiedMain

