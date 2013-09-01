GRAPHS=arch.pdf

dot:$(GRAPHS)

%.pdf: %.ps
	ps2pdf $<

%.ps: %.dot
	dot -l DatabaseShape.ps $< -Tps2 -o $@