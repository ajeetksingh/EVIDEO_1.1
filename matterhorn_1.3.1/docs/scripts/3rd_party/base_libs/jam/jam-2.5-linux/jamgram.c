/* A Bison parser, made by GNU Bison 2.5.  */

/* Bison implementation for Yacc-like parsers in C
   
      Copyright (C) 1984, 1989-1990, 2000-2011 Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.5"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1

/* Using locations.  */
#define YYLSP_NEEDED 0



/* Copy the first part of user declarations.  */

/* Line 268 of yacc.c  */
#line 84 "jamgram.y"

#include "jam.h"

#include "lists.h"
#include "variable.h"
#include "parse.h"
#include "scan.h"
#include "compile.h"
#include "newstr.h"
#include "rules.h"

# define YYMAXDEPTH 10000	/* for OSF and other less endowed yaccs */

# define F0 (LIST *(*)(PARSE *, LOL *, int *))0
# define P0 (PARSE *)0
# define S0 (char *)0

# define pappend( l,r )    	parse_make( compile_append,l,r,P0,S0,S0,0 )
# define pbreak( l,f )     	parse_make( compile_break,l,P0,P0,S0,S0,f )
# define peval( c,l,r )		parse_make( compile_eval,l,r,P0,S0,S0,c )
# define pfor( s,l,r )    	parse_make( compile_foreach,l,r,P0,s,S0,0 )
# define pif( l,r,t )	  	parse_make( compile_if,l,r,t,S0,S0,0 )
# define pincl( l )       	parse_make( compile_include,l,P0,P0,S0,S0,0 )
# define plist( s )	  	parse_make( compile_list,P0,P0,P0,s,S0,0 )
# define plocal( l,r,t )  	parse_make( compile_local,l,r,t,S0,S0,0 )
# define pnull()	  	parse_make( compile_null,P0,P0,P0,S0,S0,0 )
# define pon( l,r )	  	parse_make( compile_on,l,r,P0,S0,S0,0 )
# define prule( a,p )     	parse_make( compile_rule,a,p,P0,S0,S0,0 )
# define prules( l,r )	  	parse_make( compile_rules,l,r,P0,S0,S0,0 )
# define pset( l,r,a ) 	  	parse_make( compile_set,l,r,P0,S0,S0,a )
# define pset1( l,r,t,a )	parse_make( compile_settings,l,r,t,S0,S0,a )
# define psetc( s,l,r )     	parse_make( compile_setcomp,l,r,P0,s,S0,0 )
# define psete( s,l,s1,f ) 	parse_make( compile_setexec,l,P0,P0,s,s1,f )
# define pswitch( l,r )   	parse_make( compile_switch,l,r,P0,S0,S0,0 )
# define pwhile( l,r )   	parse_make( compile_while,l,r,P0,S0,S0,0 )

# define pnode( l,r )    	parse_make( F0,l,r,P0,S0,S0,0 )
# define psnode( s,l )     	parse_make( F0,l,P0,P0,s,S0,0 )



/* Line 268 of yacc.c  */
#line 113 "y.tab.c"

/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     _BANG_t = 258,
     _BANG_EQUALS_t = 259,
     _AMPER_t = 260,
     _AMPERAMPER_t = 261,
     _LPAREN_t = 262,
     _RPAREN_t = 263,
     _PLUS_EQUALS_t = 264,
     _COLON_t = 265,
     _SEMIC_t = 266,
     _LANGLE_t = 267,
     _LANGLE_EQUALS_t = 268,
     _EQUALS_t = 269,
     _RANGLE_t = 270,
     _RANGLE_EQUALS_t = 271,
     _QUESTION_EQUALS_t = 272,
     _LBRACKET_t = 273,
     _RBRACKET_t = 274,
     ACTIONS_t = 275,
     BIND_t = 276,
     BREAK_t = 277,
     CASE_t = 278,
     CONTINUE_t = 279,
     DEFAULT_t = 280,
     ELSE_t = 281,
     EXISTING_t = 282,
     FOR_t = 283,
     IF_t = 284,
     IGNORE_t = 285,
     IN_t = 286,
     INCLUDE_t = 287,
     LOCAL_t = 288,
     MAXLINE_t = 289,
     ON_t = 290,
     PIECEMEAL_t = 291,
     QUIETLY_t = 292,
     RETURN_t = 293,
     RULE_t = 294,
     SWITCH_t = 295,
     TOGETHER_t = 296,
     UPDATED_t = 297,
     WHILE_t = 298,
     _LBRACE_t = 299,
     _BAR_t = 300,
     _BARBAR_t = 301,
     _RBRACE_t = 302,
     ARG = 303,
     STRING = 304
   };
#endif
/* Tokens.  */
#define _BANG_t 258
#define _BANG_EQUALS_t 259
#define _AMPER_t 260
#define _AMPERAMPER_t 261
#define _LPAREN_t 262
#define _RPAREN_t 263
#define _PLUS_EQUALS_t 264
#define _COLON_t 265
#define _SEMIC_t 266
#define _LANGLE_t 267
#define _LANGLE_EQUALS_t 268
#define _EQUALS_t 269
#define _RANGLE_t 270
#define _RANGLE_EQUALS_t 271
#define _QUESTION_EQUALS_t 272
#define _LBRACKET_t 273
#define _RBRACKET_t 274
#define ACTIONS_t 275
#define BIND_t 276
#define BREAK_t 277
#define CASE_t 278
#define CONTINUE_t 279
#define DEFAULT_t 280
#define ELSE_t 281
#define EXISTING_t 282
#define FOR_t 283
#define IF_t 284
#define IGNORE_t 285
#define IN_t 286
#define INCLUDE_t 287
#define LOCAL_t 288
#define MAXLINE_t 289
#define ON_t 290
#define PIECEMEAL_t 291
#define QUIETLY_t 292
#define RETURN_t 293
#define RULE_t 294
#define SWITCH_t 295
#define TOGETHER_t 296
#define UPDATED_t 297
#define WHILE_t 298
#define _LBRACE_t 299
#define _BAR_t 300
#define _BARBAR_t 301
#define _RBRACE_t 302
#define ARG 303
#define STRING 304




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif


/* Copy the second part of user declarations.  */


/* Line 343 of yacc.c  */
#line 253 "y.tab.c"

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int yyi)
#else
static int
YYID (yyi)
    int yyi;
#endif
{
  return yyi;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)				\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack_alloc, Stack, yysize);			\
	Stack = &yyptr->Stack_alloc;					\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  39
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   255

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  50
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  21
/* YYNRULES -- Number of rules.  */
#define YYNRULES  73
/* YYNRULES -- Number of states.  */
#define YYNSTATES  154

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   304

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44,
      45,    46,    47,    48,    49
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint8 yyprhs[] =
{
       0,     0,     3,     4,     6,     7,     9,    11,    14,    19,
      26,    30,    34,    38,    43,    50,    54,    58,    62,    70,
      76,    82,    90,    96,   103,   107,   108,   109,   119,   121,
     123,   125,   128,   130,   134,   138,   142,   146,   150,   154,
     158,   162,   166,   170,   174,   177,   181,   182,   185,   190,
     191,   195,   197,   199,   203,   205,   206,   209,   211,   212,
     217,   220,   225,   230,   231,   234,   236,   238,   240,   242,
     244,   246,   249,   250
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int8 yyrhs[] =
{
      51,     0,    -1,    -1,    53,    -1,    -1,    53,    -1,    54,
      -1,    54,    53,    -1,    33,    63,    11,    52,    -1,    33,
      63,    14,    63,    11,    52,    -1,    44,    52,    47,    -1,
      32,    63,    11,    -1,    65,    62,    11,    -1,    65,    57,
      63,    11,    -1,    65,    35,    63,    57,    63,    11,    -1,
      22,    63,    11,    -1,    24,    63,    11,    -1,    38,    63,
      11,    -1,    28,    48,    31,    63,    44,    52,    47,    -1,
      40,    63,    44,    59,    47,    -1,    29,    58,    44,    52,
      47,    -1,    29,    58,    44,    52,    47,    26,    54,    -1,
      43,    58,    44,    52,    47,    -1,    39,    48,    61,    44,
      52,    47,    -1,    35,    65,    54,    -1,    -1,    -1,    20,
      68,    48,    70,    44,    55,    49,    56,    47,    -1,    14,
      -1,     9,    -1,    17,    -1,    25,    14,    -1,    65,    -1,
      58,    14,    58,    -1,    58,     4,    58,    -1,    58,    12,
      58,    -1,    58,    13,    58,    -1,    58,    15,    58,    -1,
      58,    16,    58,    -1,    58,     5,    58,    -1,    58,     6,
      58,    -1,    58,    45,    58,    -1,    58,    46,    58,    -1,
      65,    31,    63,    -1,     3,    58,    -1,     7,    58,     8,
      -1,    -1,    60,    59,    -1,    23,    48,    10,    52,    -1,
      -1,    48,    10,    61,    -1,    48,    -1,    63,    -1,    63,
      10,    62,    -1,    64,    -1,    -1,    64,    65,    -1,    48,
      -1,    -1,    18,    66,    67,    19,    -1,    65,    62,    -1,
      35,    65,    65,    62,    -1,    35,    65,    38,    63,    -1,
      -1,    68,    69,    -1,    42,    -1,    41,    -1,    30,    -1,
      37,    -1,    36,    -1,    27,    -1,    34,    48,    -1,    -1,
      21,    63,    -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   127,   127,   129,   141,   142,   146,   148,   150,   152,
     156,   158,   160,   162,   164,   166,   168,   170,   172,   174,
     176,   178,   180,   182,   184,   187,   189,   186,   198,   200,
     202,   204,   212,   214,   216,   218,   220,   222,   224,   226,
     228,   230,   232,   234,   236,   238,   249,   250,   254,   264,
     265,   267,   276,   278,   288,   293,   294,   298,   300,   300,
     309,   311,   313,   323,   324,   328,   330,   332,   334,   336,
     338,   340,   350,   351
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "_BANG_t", "_BANG_EQUALS_t", "_AMPER_t",
  "_AMPERAMPER_t", "_LPAREN_t", "_RPAREN_t", "_PLUS_EQUALS_t", "_COLON_t",
  "_SEMIC_t", "_LANGLE_t", "_LANGLE_EQUALS_t", "_EQUALS_t", "_RANGLE_t",
  "_RANGLE_EQUALS_t", "_QUESTION_EQUALS_t", "_LBRACKET_t", "_RBRACKET_t",
  "ACTIONS_t", "BIND_t", "BREAK_t", "CASE_t", "CONTINUE_t", "DEFAULT_t",
  "ELSE_t", "EXISTING_t", "FOR_t", "IF_t", "IGNORE_t", "IN_t", "INCLUDE_t",
  "LOCAL_t", "MAXLINE_t", "ON_t", "PIECEMEAL_t", "QUIETLY_t", "RETURN_t",
  "RULE_t", "SWITCH_t", "TOGETHER_t", "UPDATED_t", "WHILE_t", "_LBRACE_t",
  "_BAR_t", "_BARBAR_t", "_RBRACE_t", "ARG", "STRING", "$accept", "run",
  "block", "rules", "rule", "$@1", "$@2", "assign", "expr", "cases",
  "case", "params", "lol", "list", "listp", "arg", "$@3", "func", "eflags",
  "eflag", "bindlist", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297,   298,   299,   300,   301,   302,   303,   304
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    50,    51,    51,    52,    52,    53,    53,    53,    53,
      54,    54,    54,    54,    54,    54,    54,    54,    54,    54,
      54,    54,    54,    54,    54,    55,    56,    54,    57,    57,
      57,    57,    58,    58,    58,    58,    58,    58,    58,    58,
      58,    58,    58,    58,    58,    58,    59,    59,    60,    61,
      61,    61,    62,    62,    63,    64,    64,    65,    66,    65,
      67,    67,    67,    68,    68,    69,    69,    69,    69,    69,
      69,    69,    70,    70
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     0,     1,     0,     1,     1,     2,     4,     6,
       3,     3,     3,     4,     6,     3,     3,     3,     7,     5,
       5,     7,     5,     6,     3,     0,     0,     9,     1,     1,
       1,     2,     1,     3,     3,     3,     3,     3,     3,     3,
       3,     3,     3,     3,     2,     3,     0,     2,     4,     0,
       3,     1,     1,     3,     1,     0,     2,     1,     0,     4,
       2,     4,     4,     0,     2,     1,     1,     1,     1,     1,
       1,     2,     0,     2
};

/* YYDEFACT[STATE-NAME] -- Default reduction number in state STATE-NUM.
   Performed when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       2,    58,    63,    55,    55,     0,     0,    55,    55,     0,
      55,     0,    55,     0,     4,    57,     0,     3,     6,    55,
       0,     0,     0,    54,     0,     0,     0,     0,     0,    32,
       0,     0,     0,     0,    49,     0,     0,     0,     5,     1,
       7,    29,    28,    30,     0,    55,    55,     0,    52,     0,
      55,     0,    70,    67,     0,    69,    68,    66,    65,    72,
      64,    15,    56,    16,    55,    44,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     4,     0,     0,    55,    11,
       4,    55,    24,    17,    51,     0,    46,     4,    10,    31,
       0,     0,    12,    55,     0,    60,    59,    71,    55,     0,
       0,    45,    34,    39,    40,    35,    36,    33,    37,    38,
       0,    41,    42,    43,     8,     0,    49,     4,     0,     0,
      46,     0,    55,    13,    53,    55,    55,    73,    25,     4,
      20,     4,    50,     0,     0,    19,    47,    22,     0,    62,
      61,     0,     0,     0,     9,    23,     4,    14,    26,    18,
      21,    48,     0,    27
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,    16,    37,    38,    18,   141,   152,    46,    28,   119,
     120,    85,    47,    48,    23,    29,    20,    51,    21,    60,
      99
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -59
static const yytype_int16 yypact[] =
{
     154,   -59,   -59,   -59,   -59,   -32,     8,   -59,   -59,    -6,
     -59,    -7,   -59,     8,   154,   -59,    46,   -59,   154,    49,
     -14,   200,    37,    -6,    40,    21,     8,     8,   120,    22,
      43,    19,   185,    58,     9,    26,   135,    25,   -59,   -59,
     -59,   -59,   -59,   -59,    63,   -59,   -59,    67,    71,    -6,
     -59,    66,   -59,   -59,    38,   -59,   -59,   -59,   -59,    61,
     -59,   -59,   -59,   -59,   -59,   -59,   155,     8,     8,     8,
       8,     8,     8,     8,     8,   154,     8,     8,   -59,   -59,
     154,   -59,   -59,   -59,    79,    47,    69,   154,   -59,   -59,
      10,    82,   -59,   -59,     7,   -59,   -59,   -59,   -59,    51,
      53,   -59,    52,    24,    24,   -59,   -59,    52,   -59,   -59,
      62,   239,   239,   -59,   -59,    87,     9,   154,    42,    65,
      69,    68,   -59,   -59,   -59,   -59,   -59,   -59,   -59,   154,
      73,   154,   -59,    72,    90,   -59,   -59,   -59,   102,   -59,
     -59,    78,    74,   185,   -59,   -59,   154,   -59,   -59,   -59,
     -59,   -59,    75,   -59
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int8 yypgoto[] =
{
     -59,   -59,   -58,    13,   -29,   -59,   -59,    28,    34,    -4,
     -59,    12,   -43,    -2,   -59,     0,   -59,   -59,   -59,   -59,
     -59
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -1
static const yytype_uint8 yytable[] =
{
      19,    22,    24,    82,     1,    30,    31,    95,    33,    32,
      35,    26,     1,    17,    19,    27,    25,   110,    19,    41,
      50,    49,   114,    62,    42,     1,     1,    43,    67,   121,
      80,    40,    19,    81,    15,    44,    70,    71,    72,    73,
      74,    34,    15,    90,    91,   125,    39,    36,    61,    94,
     124,    63,    64,    78,    79,    15,    15,    84,    41,   133,
      65,    66,   100,    42,    70,    71,    43,    73,    74,    83,
      86,   142,    88,   144,    44,    19,   113,    89,    92,   115,
      19,    93,    98,   140,    45,    96,    97,    19,   151,   116,
     134,   117,   118,   123,   126,   128,   127,   129,   131,   143,
     146,   102,   103,   104,   105,   106,   107,   108,   109,   130,
     111,   112,   135,   147,   150,   137,   136,    19,   122,   145,
     138,   149,   153,   139,    67,    68,    69,   148,   132,    19,
       0,    19,    70,    71,    72,    73,    74,     0,     0,    67,
      68,    69,     0,    19,     0,     0,    19,    70,    71,    72,
      73,    74,     0,     0,     0,     0,     0,     0,     0,    67,
      68,    69,     0,   101,    75,    76,    77,    70,    71,    72,
      73,    74,     1,     0,     2,     0,     3,     0,     4,    87,
      76,    77,     5,     6,     0,     0,     7,     8,     0,     9,
       0,     0,    10,    11,    12,     0,     0,    13,    14,     0,
      76,    77,    15,     1,     0,     2,     0,     3,     0,     4,
       0,     0,     0,     5,     6,     0,     0,     7,     0,     0,
       9,     0,     0,    10,    11,    12,     0,    52,    13,    14,
      53,     0,     0,    15,    54,     0,    55,    56,     0,     0,
       0,    57,    58,    67,    68,    69,     0,     0,    59,     0,
       0,    70,    71,    72,    73,    74
};

#define yypact_value_is_default(yystate) \
  ((yystate) == (-59))

#define yytable_value_is_error(yytable_value) \
  YYID (0)

static const yytype_int16 yycheck[] =
{
       0,     3,     4,    32,    18,     7,     8,    50,    10,     9,
      12,     3,    18,     0,    14,     7,    48,    75,    18,     9,
      20,    35,    80,    23,    14,    18,    18,    17,     4,    87,
      11,    18,    32,    14,    48,    25,    12,    13,    14,    15,
      16,    48,    48,    45,    46,    38,     0,    13,    11,    49,
      93,    11,    31,    31,    11,    48,    48,    48,     9,   117,
      26,    27,    64,    14,    12,    13,    17,    15,    16,    11,
      44,   129,    47,   131,    25,    75,    78,    14,    11,    81,
      80,    10,    21,   126,    35,    19,    48,    87,   146,    10,
      48,    44,    23,    11,    94,    44,    98,    44,    11,    26,
      10,    67,    68,    69,    70,    71,    72,    73,    74,    47,
      76,    77,    47,    11,   143,    47,   120,   117,    90,    47,
     122,    47,    47,   125,     4,     5,     6,    49,   116,   129,
      -1,   131,    12,    13,    14,    15,    16,    -1,    -1,     4,
       5,     6,    -1,   143,    -1,    -1,   146,    12,    13,    14,
      15,    16,    -1,    -1,    -1,    -1,    -1,    -1,    -1,     4,
       5,     6,    -1,     8,    44,    45,    46,    12,    13,    14,
      15,    16,    18,    -1,    20,    -1,    22,    -1,    24,    44,
      45,    46,    28,    29,    -1,    -1,    32,    33,    -1,    35,
      -1,    -1,    38,    39,    40,    -1,    -1,    43,    44,    -1,
      45,    46,    48,    18,    -1,    20,    -1,    22,    -1,    24,
      -1,    -1,    -1,    28,    29,    -1,    -1,    32,    -1,    -1,
      35,    -1,    -1,    38,    39,    40,    -1,    27,    43,    44,
      30,    -1,    -1,    48,    34,    -1,    36,    37,    -1,    -1,
      -1,    41,    42,     4,     5,     6,    -1,    -1,    48,    -1,
      -1,    12,    13,    14,    15,    16
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,    18,    20,    22,    24,    28,    29,    32,    33,    35,
      38,    39,    40,    43,    44,    48,    51,    53,    54,    65,
      66,    68,    63,    64,    63,    48,     3,     7,    58,    65,
      63,    63,    65,    63,    48,    63,    58,    52,    53,     0,
      53,     9,    14,    17,    25,    35,    57,    62,    63,    35,
      65,    67,    27,    30,    34,    36,    37,    41,    42,    48,
      69,    11,    65,    11,    31,    58,    58,     4,     5,     6,
      12,    13,    14,    15,    16,    44,    45,    46,    31,    11,
      11,    14,    54,    11,    48,    61,    44,    44,    47,    14,
      63,    63,    11,    10,    65,    62,    19,    48,    21,    70,
      63,     8,    58,    58,    58,    58,    58,    58,    58,    58,
      52,    58,    58,    63,    52,    63,    10,    44,    23,    59,
      60,    52,    57,    11,    62,    38,    65,    63,    44,    44,
      47,    11,    61,    52,    48,    47,    59,    47,    63,    63,
      62,    55,    52,    26,    52,    47,    10,    11,    49,    47,
      54,    52,    56,    47
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  However,
   YYFAIL appears to be in use.  Nevertheless, it is formally deprecated
   in Bison 2.4.2's NEWS entry, where a plan to phase it out is
   discussed.  */

#define YYFAIL		goto yyerrlab
#if defined YYFAIL
  /* This is here to suppress warnings from the GCC cpp's
     -Wunused-macros.  Normally we don't worry about that warning, but
     some users do, and we want to make it easy for users to remove
     YYFAIL uses, which will produce warnings from Bison 2.5.  */
#endif

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* This macro is provided for backward compatibility. */

#ifndef YY_LOCATION_PRINT
# define YY_LOCATION_PRINT(File, Loc) ((void) 0)
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (YYLEX_PARAM)
#else
# define YYLEX yylex ()
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
#else
static void
yy_stack_print (yybottom, yytop)
    yytype_int16 *yybottom;
    yytype_int16 *yytop;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yyrule)
    YYSTYPE *yyvsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       		       );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif


#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into *YYMSG, which is of size *YYMSG_ALLOC, an error message
   about the unexpected token YYTOKEN for the state stack whose top is
   YYSSP.

   Return 0 if *YYMSG was successfully written.  Return 1 if *YYMSG is
   not large enough to hold the message.  In that case, also set
   *YYMSG_ALLOC to the required number of bytes.  Return 2 if the
   required number of bytes is too large to store.  */
static int
yysyntax_error (YYSIZE_T *yymsg_alloc, char **yymsg,
                yytype_int16 *yyssp, int yytoken)
{
  YYSIZE_T yysize0 = yytnamerr (0, yytname[yytoken]);
  YYSIZE_T yysize = yysize0;
  YYSIZE_T yysize1;
  enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
  /* Internationalized format string. */
  const char *yyformat = 0;
  /* Arguments of yyformat. */
  char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
  /* Number of reported tokens (one for the "unexpected", one per
     "expected"). */
  int yycount = 0;

  /* There are many possibilities here to consider:
     - Assume YYFAIL is not used.  It's too flawed to consider.  See
       <http://lists.gnu.org/archive/html/bison-patches/2009-12/msg00024.html>
       for details.  YYERROR is fine as it does not invoke this
       function.
     - If this state is a consistent state with a default action, then
       the only way this function was invoked is if the default action
       is an error action.  In that case, don't check for expected
       tokens because there are none.
     - The only way there can be no lookahead present (in yychar) is if
       this state is a consistent state with a default action.  Thus,
       detecting the absence of a lookahead is sufficient to determine
       that there is no unexpected or expected token to report.  In that
       case, just report a simple "syntax error".
     - Don't assume there isn't a lookahead just because this state is a
       consistent state with a default action.  There might have been a
       previous inconsistent state, consistent state with a non-default
       action, or user semantic action that manipulated yychar.
     - Of course, the expected token list depends on states to have
       correct lookahead information, and it depends on the parser not
       to perform extra reductions after fetching a lookahead from the
       scanner and before detecting a syntax error.  Thus, state merging
       (from LALR or IELR) and default reductions corrupt the expected
       token list.  However, the list is correct for canonical LR with
       one exception: it will still contain any token that will not be
       accepted due to an error action in a later state.
  */
  if (yytoken != YYEMPTY)
    {
      int yyn = yypact[*yyssp];
      yyarg[yycount++] = yytname[yytoken];
      if (!yypact_value_is_default (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative indexes in
             YYCHECK.  In other words, skip the first -YYN actions for
             this state because they are default actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST - yyn + 1;
          int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
          int yyx;

          for (yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR
                && !yytable_value_is_error (yytable[yyx + yyn]))
              {
                if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
                  {
                    yycount = 1;
                    yysize = yysize0;
                    break;
                  }
                yyarg[yycount++] = yytname[yyx];
                yysize1 = yysize + yytnamerr (0, yytname[yyx]);
                if (! (yysize <= yysize1
                       && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
                  return 2;
                yysize = yysize1;
              }
        }
    }

  switch (yycount)
    {
# define YYCASE_(N, S)                      \
      case N:                               \
        yyformat = S;                       \
      break
      YYCASE_(0, YY_("syntax error"));
      YYCASE_(1, YY_("syntax error, unexpected %s"));
      YYCASE_(2, YY_("syntax error, unexpected %s, expecting %s"));
      YYCASE_(3, YY_("syntax error, unexpected %s, expecting %s or %s"));
      YYCASE_(4, YY_("syntax error, unexpected %s, expecting %s or %s or %s"));
      YYCASE_(5, YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s"));
# undef YYCASE_
    }

  yysize1 = yysize + yystrlen (yyformat);
  if (! (yysize <= yysize1 && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
    return 2;
  yysize = yysize1;

  if (*yymsg_alloc < yysize)
    {
      *yymsg_alloc = 2 * yysize;
      if (! (yysize <= *yymsg_alloc
             && *yymsg_alloc <= YYSTACK_ALLOC_MAXIMUM))
        *yymsg_alloc = YYSTACK_ALLOC_MAXIMUM;
      return 1;
    }

  /* Avoid sprintf, as that infringes on the user's name space.
     Don't have undefined behavior even if the translation
     produced a string with the wrong number of "%s"s.  */
  {
    char *yyp = *yymsg;
    int yyi = 0;
    while ((*yyp = *yyformat) != '\0')
      if (*yyp == '%' && yyformat[1] == 's' && yyi < yycount)
        {
          yyp += yytnamerr (yyp, yyarg[yyi++]);
          yyformat += 2;
        }
      else
        {
          yyp++;
          yyformat++;
        }
  }
  return 0;
}
#endif /* YYERROR_VERBOSE */

/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
#else
static void
yydestruct (yymsg, yytype, yyvaluep)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
#endif
{
  YYUSE (yyvaluep);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}


/* Prevent warnings from -Wmissing-prototypes.  */
#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */


/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;

/* Number of syntax errors so far.  */
int yynerrs;


/*----------.
| yyparse.  |
`----------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{
    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       `yyss': related to states.
       `yyvs': related to semantic values.

       Refer to the stacks thru separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yytoken = 0;
  yyss = yyssa;
  yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */
  yyssp = yyss;
  yyvsp = yyvs;

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;

	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),
		    &yystacksize);

	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss_alloc, yyss);
	YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 3:

/* Line 1806 of yacc.c  */
#line 130 "jamgram.y"
    { parse_save( (yyvsp[(1) - (1)]).parse ); }
    break;

  case 4:

/* Line 1806 of yacc.c  */
#line 141 "jamgram.y"
    { (yyval).parse = pnull(); }
    break;

  case 5:

/* Line 1806 of yacc.c  */
#line 143 "jamgram.y"
    { (yyval).parse = (yyvsp[(1) - (1)]).parse; }
    break;

  case 6:

/* Line 1806 of yacc.c  */
#line 147 "jamgram.y"
    { (yyval).parse = (yyvsp[(1) - (1)]).parse; }
    break;

  case 7:

/* Line 1806 of yacc.c  */
#line 149 "jamgram.y"
    { (yyval).parse = prules( (yyvsp[(1) - (2)]).parse, (yyvsp[(2) - (2)]).parse ); }
    break;

  case 8:

/* Line 1806 of yacc.c  */
#line 151 "jamgram.y"
    { (yyval).parse = plocal( (yyvsp[(2) - (4)]).parse, pnull(), (yyvsp[(4) - (4)]).parse ); }
    break;

  case 9:

/* Line 1806 of yacc.c  */
#line 153 "jamgram.y"
    { (yyval).parse = plocal( (yyvsp[(2) - (6)]).parse, (yyvsp[(4) - (6)]).parse, (yyvsp[(6) - (6)]).parse ); }
    break;

  case 10:

/* Line 1806 of yacc.c  */
#line 157 "jamgram.y"
    { (yyval).parse = (yyvsp[(2) - (3)]).parse; }
    break;

  case 11:

/* Line 1806 of yacc.c  */
#line 159 "jamgram.y"
    { (yyval).parse = pincl( (yyvsp[(2) - (3)]).parse ); }
    break;

  case 12:

/* Line 1806 of yacc.c  */
#line 161 "jamgram.y"
    { (yyval).parse = prule( (yyvsp[(1) - (3)]).parse, (yyvsp[(2) - (3)]).parse ); }
    break;

  case 13:

/* Line 1806 of yacc.c  */
#line 163 "jamgram.y"
    { (yyval).parse = pset( (yyvsp[(1) - (4)]).parse, (yyvsp[(3) - (4)]).parse, (yyvsp[(2) - (4)]).number ); }
    break;

  case 14:

/* Line 1806 of yacc.c  */
#line 165 "jamgram.y"
    { (yyval).parse = pset1( (yyvsp[(1) - (6)]).parse, (yyvsp[(3) - (6)]).parse, (yyvsp[(5) - (6)]).parse, (yyvsp[(4) - (6)]).number ); }
    break;

  case 15:

/* Line 1806 of yacc.c  */
#line 167 "jamgram.y"
    { (yyval).parse = pbreak( (yyvsp[(2) - (3)]).parse, JMP_BREAK ); }
    break;

  case 16:

/* Line 1806 of yacc.c  */
#line 169 "jamgram.y"
    { (yyval).parse = pbreak( (yyvsp[(2) - (3)]).parse, JMP_CONTINUE ); }
    break;

  case 17:

/* Line 1806 of yacc.c  */
#line 171 "jamgram.y"
    { (yyval).parse = pbreak( (yyvsp[(2) - (3)]).parse, JMP_RETURN ); }
    break;

  case 18:

/* Line 1806 of yacc.c  */
#line 173 "jamgram.y"
    { (yyval).parse = pfor( (yyvsp[(2) - (7)]).string, (yyvsp[(4) - (7)]).parse, (yyvsp[(6) - (7)]).parse ); }
    break;

  case 19:

/* Line 1806 of yacc.c  */
#line 175 "jamgram.y"
    { (yyval).parse = pswitch( (yyvsp[(2) - (5)]).parse, (yyvsp[(4) - (5)]).parse ); }
    break;

  case 20:

/* Line 1806 of yacc.c  */
#line 177 "jamgram.y"
    { (yyval).parse = pif( (yyvsp[(2) - (5)]).parse, (yyvsp[(4) - (5)]).parse, pnull() ); }
    break;

  case 21:

/* Line 1806 of yacc.c  */
#line 179 "jamgram.y"
    { (yyval).parse = pif( (yyvsp[(2) - (7)]).parse, (yyvsp[(4) - (7)]).parse, (yyvsp[(7) - (7)]).parse ); }
    break;

  case 22:

/* Line 1806 of yacc.c  */
#line 181 "jamgram.y"
    { (yyval).parse = pwhile( (yyvsp[(2) - (5)]).parse, (yyvsp[(4) - (5)]).parse ); }
    break;

  case 23:

/* Line 1806 of yacc.c  */
#line 183 "jamgram.y"
    { (yyval).parse = psetc( (yyvsp[(2) - (6)]).string, (yyvsp[(3) - (6)]).parse, (yyvsp[(5) - (6)]).parse ); }
    break;

  case 24:

/* Line 1806 of yacc.c  */
#line 185 "jamgram.y"
    { (yyval).parse = pon( (yyvsp[(2) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 25:

/* Line 1806 of yacc.c  */
#line 187 "jamgram.y"
    { yymode( SCAN_STRING ); }
    break;

  case 26:

/* Line 1806 of yacc.c  */
#line 189 "jamgram.y"
    { yymode( SCAN_NORMAL ); }
    break;

  case 27:

/* Line 1806 of yacc.c  */
#line 191 "jamgram.y"
    { (yyval).parse = psete( (yyvsp[(3) - (9)]).string,(yyvsp[(4) - (9)]).parse,(yyvsp[(7) - (9)]).string,(yyvsp[(2) - (9)]).number ); }
    break;

  case 28:

/* Line 1806 of yacc.c  */
#line 199 "jamgram.y"
    { (yyval).number = VAR_SET; }
    break;

  case 29:

/* Line 1806 of yacc.c  */
#line 201 "jamgram.y"
    { (yyval).number = VAR_APPEND; }
    break;

  case 30:

/* Line 1806 of yacc.c  */
#line 203 "jamgram.y"
    { (yyval).number = VAR_DEFAULT; }
    break;

  case 31:

/* Line 1806 of yacc.c  */
#line 205 "jamgram.y"
    { (yyval).number = VAR_DEFAULT; }
    break;

  case 32:

/* Line 1806 of yacc.c  */
#line 213 "jamgram.y"
    { (yyval).parse = peval( EXPR_EXISTS, (yyvsp[(1) - (1)]).parse, pnull() ); }
    break;

  case 33:

/* Line 1806 of yacc.c  */
#line 215 "jamgram.y"
    { (yyval).parse = peval( EXPR_EQUALS, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 34:

/* Line 1806 of yacc.c  */
#line 217 "jamgram.y"
    { (yyval).parse = peval( EXPR_NOTEQ, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 35:

/* Line 1806 of yacc.c  */
#line 219 "jamgram.y"
    { (yyval).parse = peval( EXPR_LESS, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 36:

/* Line 1806 of yacc.c  */
#line 221 "jamgram.y"
    { (yyval).parse = peval( EXPR_LESSEQ, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 37:

/* Line 1806 of yacc.c  */
#line 223 "jamgram.y"
    { (yyval).parse = peval( EXPR_MORE, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 38:

/* Line 1806 of yacc.c  */
#line 225 "jamgram.y"
    { (yyval).parse = peval( EXPR_MOREEQ, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 39:

/* Line 1806 of yacc.c  */
#line 227 "jamgram.y"
    { (yyval).parse = peval( EXPR_AND, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 40:

/* Line 1806 of yacc.c  */
#line 229 "jamgram.y"
    { (yyval).parse = peval( EXPR_AND, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 41:

/* Line 1806 of yacc.c  */
#line 231 "jamgram.y"
    { (yyval).parse = peval( EXPR_OR, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 42:

/* Line 1806 of yacc.c  */
#line 233 "jamgram.y"
    { (yyval).parse = peval( EXPR_OR, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 43:

/* Line 1806 of yacc.c  */
#line 235 "jamgram.y"
    { (yyval).parse = peval( EXPR_IN, (yyvsp[(1) - (3)]).parse, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 44:

/* Line 1806 of yacc.c  */
#line 237 "jamgram.y"
    { (yyval).parse = peval( EXPR_NOT, (yyvsp[(2) - (2)]).parse, pnull() ); }
    break;

  case 45:

/* Line 1806 of yacc.c  */
#line 239 "jamgram.y"
    { (yyval).parse = (yyvsp[(2) - (3)]).parse; }
    break;

  case 46:

/* Line 1806 of yacc.c  */
#line 249 "jamgram.y"
    { (yyval).parse = P0; }
    break;

  case 47:

/* Line 1806 of yacc.c  */
#line 251 "jamgram.y"
    { (yyval).parse = pnode( (yyvsp[(1) - (2)]).parse, (yyvsp[(2) - (2)]).parse ); }
    break;

  case 48:

/* Line 1806 of yacc.c  */
#line 255 "jamgram.y"
    { (yyval).parse = psnode( (yyvsp[(2) - (4)]).string, (yyvsp[(4) - (4)]).parse ); }
    break;

  case 49:

/* Line 1806 of yacc.c  */
#line 264 "jamgram.y"
    { (yyval).parse = P0; }
    break;

  case 50:

/* Line 1806 of yacc.c  */
#line 266 "jamgram.y"
    { (yyval).parse = psnode( (yyvsp[(1) - (3)]).string, (yyvsp[(3) - (3)]).parse ); }
    break;

  case 51:

/* Line 1806 of yacc.c  */
#line 268 "jamgram.y"
    { (yyval).parse = psnode( (yyvsp[(1) - (1)]).string, P0 ); }
    break;

  case 52:

/* Line 1806 of yacc.c  */
#line 277 "jamgram.y"
    { (yyval).parse = pnode( P0, (yyvsp[(1) - (1)]).parse ); }
    break;

  case 53:

/* Line 1806 of yacc.c  */
#line 279 "jamgram.y"
    { (yyval).parse = pnode( (yyvsp[(3) - (3)]).parse, (yyvsp[(1) - (3)]).parse ); }
    break;

  case 54:

/* Line 1806 of yacc.c  */
#line 289 "jamgram.y"
    { (yyval).parse = (yyvsp[(1) - (1)]).parse; yymode( SCAN_NORMAL ); }
    break;

  case 55:

/* Line 1806 of yacc.c  */
#line 293 "jamgram.y"
    { (yyval).parse = pnull(); yymode( SCAN_PUNCT ); }
    break;

  case 56:

/* Line 1806 of yacc.c  */
#line 295 "jamgram.y"
    { (yyval).parse = pappend( (yyvsp[(1) - (2)]).parse, (yyvsp[(2) - (2)]).parse ); }
    break;

  case 57:

/* Line 1806 of yacc.c  */
#line 299 "jamgram.y"
    { (yyval).parse = plist( (yyvsp[(1) - (1)]).string ); }
    break;

  case 58:

/* Line 1806 of yacc.c  */
#line 300 "jamgram.y"
    { yymode( SCAN_NORMAL ); }
    break;

  case 59:

/* Line 1806 of yacc.c  */
#line 301 "jamgram.y"
    { (yyval).parse = (yyvsp[(3) - (4)]).parse; }
    break;

  case 60:

/* Line 1806 of yacc.c  */
#line 310 "jamgram.y"
    { (yyval).parse = prule( (yyvsp[(1) - (2)]).parse, (yyvsp[(2) - (2)]).parse ); }
    break;

  case 61:

/* Line 1806 of yacc.c  */
#line 312 "jamgram.y"
    { (yyval).parse = pon( (yyvsp[(2) - (4)]).parse, prule( (yyvsp[(3) - (4)]).parse, (yyvsp[(4) - (4)]).parse ) ); }
    break;

  case 62:

/* Line 1806 of yacc.c  */
#line 314 "jamgram.y"
    { (yyval).parse = pon( (yyvsp[(2) - (4)]).parse, (yyvsp[(4) - (4)]).parse ); }
    break;

  case 63:

/* Line 1806 of yacc.c  */
#line 323 "jamgram.y"
    { (yyval).number = 0; }
    break;

  case 64:

/* Line 1806 of yacc.c  */
#line 325 "jamgram.y"
    { (yyval).number = (yyvsp[(1) - (2)]).number | (yyvsp[(2) - (2)]).number; }
    break;

  case 65:

/* Line 1806 of yacc.c  */
#line 329 "jamgram.y"
    { (yyval).number = RULE_UPDATED; }
    break;

  case 66:

/* Line 1806 of yacc.c  */
#line 331 "jamgram.y"
    { (yyval).number = RULE_TOGETHER; }
    break;

  case 67:

/* Line 1806 of yacc.c  */
#line 333 "jamgram.y"
    { (yyval).number = RULE_IGNORE; }
    break;

  case 68:

/* Line 1806 of yacc.c  */
#line 335 "jamgram.y"
    { (yyval).number = RULE_QUIETLY; }
    break;

  case 69:

/* Line 1806 of yacc.c  */
#line 337 "jamgram.y"
    { (yyval).number = RULE_PIECEMEAL; }
    break;

  case 70:

/* Line 1806 of yacc.c  */
#line 339 "jamgram.y"
    { (yyval).number = RULE_EXISTING; }
    break;

  case 71:

/* Line 1806 of yacc.c  */
#line 341 "jamgram.y"
    { (yyval).number = atoi( (yyvsp[(2) - (2)]).string ) * RULE_MAXLINE; }
    break;

  case 72:

/* Line 1806 of yacc.c  */
#line 350 "jamgram.y"
    { (yyval).parse = pnull(); }
    break;

  case 73:

/* Line 1806 of yacc.c  */
#line 352 "jamgram.y"
    { (yyval).parse = (yyvsp[(2) - (2)]).parse; }
    break;



/* Line 1806 of yacc.c  */
#line 2130 "y.tab.c"
      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYEMPTY : YYTRANSLATE (yychar);

  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
# define YYSYNTAX_ERROR yysyntax_error (&yymsg_alloc, &yymsg, \
                                        yyssp, yytoken)
      {
        char const *yymsgp = YY_("syntax error");
        int yysyntax_error_status;
        yysyntax_error_status = YYSYNTAX_ERROR;
        if (yysyntax_error_status == 0)
          yymsgp = yymsg;
        else if (yysyntax_error_status == 1)
          {
            if (yymsg != yymsgbuf)
              YYSTACK_FREE (yymsg);
            yymsg = (char *) YYSTACK_ALLOC (yymsg_alloc);
            if (!yymsg)
              {
                yymsg = yymsgbuf;
                yymsg_alloc = sizeof yymsgbuf;
                yysyntax_error_status = 2;
              }
            else
              {
                yysyntax_error_status = YYSYNTAX_ERROR;
                yymsgp = yymsg;
              }
          }
        yyerror (yymsgp);
        if (yysyntax_error_status == 2)
          goto yyexhaustedlab;
      }
# undef YYSYNTAX_ERROR
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;


      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  *++yyvsp = yylval;


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined(yyoverflow) || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}



