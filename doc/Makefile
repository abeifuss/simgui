DIRS=dot
GRAPHS=arch

all: doc

doc: doc.tex doc.aux $(DIRS)
	bibtex doc.aux
	pdflatex doc.tex

%.aux: %.tex
	pdflatex doc.tex
	
.PHONY: subdirs $(DIRS)

subdirs: $(DIRS)

$(DIRS):
	$(MAKE) -C $@