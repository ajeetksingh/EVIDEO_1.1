pix = lepton.pixRead("images/hello.png")
pix = lepton.pixConvertTo1(pix,128)
pix = lepton.pixInvert(nil,pix)
lepton.pixWritePng("test_input.png",pix,1.0)
comp = lepton.pixErodeCompBrick(eroded,pix,12,1)
lepton.pixWritePng("test_comp.png",comp,1.0)
noncomp = lepton.pixErodeBrick(eroded,pix,12,1)
lepton.pixWritePng("test_noncomp.png",noncomp,1.0)
diff = lepton.pixXor(nil,comp,noncomp)
lepton.pixWritePng("test_diff.png",diff,1.0)
