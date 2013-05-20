	.file	"asm_enc_offsets.c"
	.section	.text.startup,"ax",@progbits
	.p2align 4,,15
	.globl	main
	.type	main, @function
main:
.LFB99:
	.cfi_startproc
#APP
# 23 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_coeff EQU $4
# 0 "" 2
# 24 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_zbin EQU $20
# 0 "" 2
# 25 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_round EQU $28
# 0 "" 2
# 26 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_quant EQU $8
# 0 "" 2
# 27 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_quant_fast EQU $12
# 0 "" 2
# 28 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_zbin_extra EQU $32
# 0 "" 2
# 29 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_zrun_zbin_boost EQU $24
# 0 "" 2
# 30 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_quant_shift EQU $16
# 0 "" 2
# 32 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_blockd_qcoeff EQU $0
# 0 "" 2
# 33 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_blockd_dequant EQU $16
# 0 "" 2
# 34 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_blockd_dqcoeff EQU $4
# 0 "" 2
# 35 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_blockd_eob EQU $44
# 0 "" 2
# 38 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_base_src EQU $36
# 0 "" 2
# 39 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_src EQU $40
# 0 "" 2
# 40 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_src_diff EQU $0
# 0 "" 2
# 41 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_block_src_stride EQU $44
# 0 "" 2
# 43 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_blockd_predictor EQU $8
# 0 "" 2
# 46 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_lowvalue EQU $0
# 0 "" 2
# 47 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_range EQU $4
# 0 "" 2
# 48 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_value EQU $8
# 0 "" 2
# 49 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_count EQU $12
# 0 "" 2
# 50 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_pos EQU $16
# 0 "" 2
# 51 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_writer_buffer EQU $20
# 0 "" 2
# 53 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenextra_token EQU $6
# 0 "" 2
# 54 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenextra_extra EQU $4
# 0 "" 2
# 55 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenextra_context_tree EQU $0
# 0 "" 2
# 56 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenextra_skip_eob_node EQU $7
# 0 "" 2
# 57 "vp8/encoder/asm_enc_offsets.c" 1
	
TOKENEXTRA_SZ EQU $8
# 0 "" 2
# 59 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_extra_bit_struct_sz EQU $16
# 0 "" 2
# 61 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_token_value EQU $0
# 0 "" 2
# 62 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_token_len EQU $4
# 0 "" 2
# 64 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_extra_bit_struct_tree EQU $0
# 0 "" 2
# 65 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_extra_bit_struct_prob EQU $4
# 0 "" 2
# 66 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_extra_bit_struct_len EQU $8
# 0 "" 2
# 67 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_extra_bit_struct_base_val EQU $12
# 0 "" 2
# 69 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_comp_tplist EQU $181336
# 0 "" 2
# 70 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_comp_common EQU $105408
# 0 "" 2
# 71 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_comp_bc2 EQU $128480
# 0 "" 2
# 73 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenlist_start EQU $0
# 0 "" 2
# 74 "vp8/encoder/asm_enc_offsets.c" 1
	
tokenlist_stop EQU $4
# 0 "" 2
# 75 "vp8/encoder/asm_enc_offsets.c" 1
	
TOKENLIST_SZ EQU $8
# 0 "" 2
# 77 "vp8/encoder/asm_enc_offsets.c" 1
	
vp8_common_mb_rows EQU $12984
# 0 "" 2
#NO_APP
	xorl	%eax, %eax
	ret
	.cfi_endproc
.LFE99:
	.size	main, .-main
	.ident	"GCC: (Ubuntu/Linaro 4.6.3-1ubuntu5) 4.6.3"
	.section	.note.GNU-stack,"",@progbits
