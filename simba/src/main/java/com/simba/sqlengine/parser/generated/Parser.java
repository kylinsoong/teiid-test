package com.simba.sqlengine.parser.generated;

import com.simba.sqlengine.exceptions.SQLEngineException;
import com.simba.sqlengine.parser.IPTLimitChecker;
import com.simba.sqlengine.parser.PTNodeFactory;
import com.simba.sqlengine.parser.PTParseErrorInfo;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.type.PTCountLimit;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.sqlengine.parser.type.PTStringConstraint;
import com.simba.sqlengine.utilities.SQLEngineMessageKey;
import com.simba.support.exceptions.DiagState;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

public class Parser
{
  boolean yydebug;
  int yynerrs;
  int yyerrflag;
  int yychar;
  static final int YYSTACKSIZE = 500;
  int[] statestk = new int['Ǵ'];
  int stateptr;
  int stateptrmax;
  int statemax;
  String yytext;
  ParserVal yyval;
  ParserVal yylval;
  ParserVal[] valstk;
  int valptr;
  public static final short T_INVALID_CHAR = 257;
  public static final short T_ADD = 258;
  public static final short T_ALL = 259;
  public static final short T_ALTER = 260;
  public static final short T_AND = 261;
  public static final short T_ANY = 262;
  public static final short T_APPROX_NUM_LIT = 263;
  public static final short T_AS = 264;
  public static final short T_ASC = 265;
  public static final short T_AVG = 266;
  public static final short T_BETWEEN = 267;
  public static final short T_BY = 268;
  public static final short T_CALL = 269;
  public static final short T_CASE = 270;
  public static final short T_CASCADE = 271;
  public static final short T_CAST = 272;
  public static final short T_CATALOG = 273;
  public static final short T_CHAR_STR_LIT = 274;
  public static final short T_CHECK = 275;
  public static final short T_COALESCE = 276;
  public static final short T_COLUMN = 277;
  public static final short T_CONCAT = 278;
  public static final short T_CONSTRAINT = 279;
  public static final short T_CONVERT = 280;
  public static final short T_CORRESPONDING = 281;
  public static final short T_COUNT = 282;
  public static final short T_CREATE = 283;
  public static final short T_CROSS = 284;
  public static final short T_DATATYPE = 285;
  public static final short T_DATE = 286;
  public static final short T_DAY = 287;
  public static final short T_DECNUM_LIT = 288;
  public static final short T_DEFAULT = 289;
  public static final short T_DELETE = 290;
  public static final short T_DESC = 291;
  public static final short T_DISTINCT = 292;
  public static final short T_DROP = 293;
  public static final short T_ELSE = 294;
  public static final short T_END = 295;
  public static final short T_EQ = 296;
  public static final short T_ESCAPE = 297;
  public static final short T_EXCEPT = 298;
  public static final short T_EXISTS = 299;
  public static final short T_FN = 300;
  public static final short T_FOR = 301;
  public static final short T_FOREIGN = 302;
  public static final short T_FROM = 303;
  public static final short T_FULL = 304;
  public static final short T_GE = 305;
  public static final short T_GRANT = 306;
  public static final short T_GROUP = 307;
  public static final short T_GT = 308;
  public static final short T_HAVING = 309;
  public static final short T_HOUR = 310;
  public static final short T_IDENTIFIER = 311;
  public static final short T_IF = 312;
  public static final short T_IN = 313;
  public static final short T_INDEX = 314;
  public static final short T_INNER = 315;
  public static final short T_INSERT = 316;
  public static final short T_INTERVAL = 317;
  public static final short T_INTO = 318;
  public static final short T_IS = 319;
  public static final short T_JOIN = 320;
  public static final short T_KEY = 321;
  public static final short T_LE = 322;
  public static final short T_LEFT = 323;
  public static final short T_LIKE = 324;
  public static final short T_LIMIT = 325;
  public static final short T_LT = 326;
  public static final short T_MAX = 327;
  public static final short T_MIN = 328;
  public static final short T_MINUTE = 329;
  public static final short T_MONTH = 330;
  public static final short T_NE = 331;
  public static final short T_NOT = 332;
  public static final short T_NOTLIKE = 333;
  public static final short T_NULL = 334;
  public static final short T_NULLIF = 335;
  public static final short T_ON = 336;
  public static final short T_OPTION = 337;
  public static final short T_OR = 338;
  public static final short T_ORDER = 339;
  public static final short T_OUTER = 340;
  public static final short T_PERCENT = 341;
  public static final short T_PRIMARY = 342;
  public static final short T_PRIVILEGES = 343;
  public static final short T_PROCEDURE = 344;
  public static final short T_PUBLIC = 345;
  public static final short T_REFERENCES = 346;
  public static final short T_RESTRICT = 347;
  public static final short T_REVOKE = 348;
  public static final short T_RIGHT = 349;
  public static final short T_SCHEMA = 350;
  public static final short T_SECOND = 351;
  public static final short T_SELECT = 352;
  public static final short T_SET = 353;
  public static final short T_SOME = 354;
  public static final short T_STDDEV = 355;
  public static final short T_STDDEV_POP = 356;
  public static final short T_SUM = 357;
  public static final short T_TABLE = 358;
  public static final short T_THEN = 359;
  public static final short T_TIME = 360;
  public static final short T_TIMESTAMP = 361;
  public static final short T_TIMESTAMPADD = 362;
  public static final short T_TIMESTAMPDIFF = 363;
  public static final short T_TO = 364;
  public static final short T_TOP = 365;
  public static final short T_TSIDATATYPE = 366;
  public static final short T_UNION = 367;
  public static final short T_UNIQUE = 368;
  public static final short T_UPDATE = 369;
  public static final short T_UPSERT = 370;
  public static final short T_USAGE = 371;
  public static final short T_USER = 372;
  public static final short T_USINT_LIT = 373;
  public static final short T_VALUES = 374;
  public static final short T_VAR = 375;
  public static final short T_VAR_POP = 376;
  public static final short T_VIEW = 377;
  public static final short T_WHEN = 378;
  public static final short T_WHERE = 379;
  public static final short T_WITH = 380;
  public static final short T_YEAR = 381;
  public static final short LOW = 382;
  public static final short HIGH = 383;
  public static final short VERY_HIGH = 384;
  public static final short YYERRCODE = 256;
  static final short[] yylhs;
  static final short[] yylen;
  static final short[] yydefred;
  static final short[] yydgoto;
  static final short[] yysindex;
  static final short[] yyrindex;
  static final short[] yygindex;
  static final int YYTABLESIZE = 7423;
  static short[] yytable;
  static short[] yycheck;
  static final short YYFINAL = 15;
  static final short YYMAXTOKEN = 384;
  static final String[] yyname = { "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", null, null, null, null, null, null, null, null, null, null, "':'", null, null, null, null, "'?'", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "'{'", null, "'}'", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "T_INVALID_CHAR", "T_ADD", "T_ALL", "T_ALTER", "T_AND", "T_ANY", "T_APPROX_NUM_LIT", "T_AS", "T_ASC", "T_AVG", "T_BETWEEN", "T_BY", "T_CALL", "T_CASE", "T_CASCADE", "T_CAST", "T_CATALOG", "T_CHAR_STR_LIT", "T_CHECK", "T_COALESCE", "T_COLUMN", "T_CONCAT", "T_CONSTRAINT", "T_CONVERT", "T_CORRESPONDING", "T_COUNT", "T_CREATE", "T_CROSS", "T_DATATYPE", "T_DATE", "T_DAY", "T_DECNUM_LIT", "T_DEFAULT", "T_DELETE", "T_DESC", "T_DISTINCT", "T_DROP", "T_ELSE", "T_END", "T_EQ", "T_ESCAPE", "T_EXCEPT", "T_EXISTS", "T_FN", "T_FOR", "T_FOREIGN", "T_FROM", "T_FULL", "T_GE", "T_GRANT", "T_GROUP", "T_GT", "T_HAVING", "T_HOUR", "T_IDENTIFIER", "T_IF", "T_IN", "T_INDEX", "T_INNER", "T_INSERT", "T_INTERVAL", "T_INTO", "T_IS", "T_JOIN", "T_KEY", "T_LE", "T_LEFT", "T_LIKE", "T_LIMIT", "T_LT", "T_MAX", "T_MIN", "T_MINUTE", "T_MONTH", "T_NE", "T_NOT", "T_NOTLIKE", "T_NULL", "T_NULLIF", "T_ON", "T_OPTION", "T_OR", "T_ORDER", "T_OUTER", "T_PERCENT", "T_PRIMARY", "T_PRIVILEGES", "T_PROCEDURE", "T_PUBLIC", "T_REFERENCES", "T_RESTRICT", "T_REVOKE", "T_RIGHT", "T_SCHEMA", "T_SECOND", "T_SELECT", "T_SET", "T_SOME", "T_STDDEV", "T_STDDEV_POP", "T_SUM", "T_TABLE", "T_THEN", "T_TIME", "T_TIMESTAMP", "T_TIMESTAMPADD", "T_TIMESTAMPDIFF", "T_TO", "T_TOP", "T_TSIDATATYPE", "T_UNION", "T_UNIQUE", "T_UPDATE", "T_UPSERT", "T_USAGE", "T_USER", "T_USINT_LIT", "T_VALUES", "T_VAR", "T_VAR_POP", "T_VIEW", "T_WHEN", "T_WHERE", "T_WITH", "T_YEAR", "LOW", "HIGH", "VERY_HIGH" };
  static final String[] yyrule = { "$accept : TopLevel", "TopLevel : ExecStmt", "ExecStmt : TopLevelSelectStmt", "ExecStmt : DirectSelectStmt", "ExecStmt : AlterTableStmt", "ExecStmt : CreateIndexStmt", "ExecStmt : CreateTableStmt", "ExecStmt : CreateViewStmt", "ExecStmt : DeleteStmtSearched", "ExecStmt : DropIndexStmt", "ExecStmt : DropTableStmt", "ExecStmt : DropViewStmt", "ExecStmt : InsertStmt", "ExecStmt : GrantStmt", "ExecStmt : ProcedureStmt", "ExecStmt : RevokeStmt", "ExecStmt : SetStmt", "ExecStmt : UpdateStmtSearched", "Empty :", "Identifier : T_IDENTIFIER", "Identifier : RelaxedIdent", "Identifier : RestrictedRelaxedIdent", "RelaxedIdent : T_AVG", "RelaxedIdent : T_COUNT", "RelaxedIdent : T_DATE", "RelaxedIdent : T_DAY", "RelaxedIdent : T_HOUR", "RelaxedIdent : T_MAX", "RelaxedIdent : T_MIN", "RelaxedIdent : T_MINUTE", "RelaxedIdent : T_MONTH", "RelaxedIdent : T_SECOND", "RelaxedIdent : T_SUM", "RelaxedIdent : T_TIME", "RelaxedIdent : T_TIMESTAMP", "RelaxedIdent : T_YEAR", "RestrictedRelaxedIdent : T_USER", "TopLevelSelectStmt : DirectSelectStmt OrderBy", "OrderBy : T_ORDER T_BY SortSpecificationList", "SortSpecificationList : SortSpecification", "SortSpecificationList : SortSpecificationList ',' SortSpecification", "SortSpecification : SortKey OrderingSpecification_Opt", "SortKey : Expression", "OrderingSpecification_Opt : T_ASC", "OrderingSpecification_Opt : T_DESC", "OrderingSpecification_Opt : Empty", "DirectSelectStmt : QueryExpression", "DirectSelectStmt : '(' QueryExpression ')'", "QueryExpression : NonJoinQueryExpression", "NonJoinQueryExpression : NonJoinQueryTerm", "NonJoinQueryExpression : DirectSelectStmt T_UNION NonJoinQueryTerm", "NonJoinQueryExpression : DirectSelectStmt T_UNION T_ALL NonJoinQueryTerm", "NonJoinQueryExpression : DirectSelectStmt T_EXCEPT NonJoinQueryTerm", "NonJoinQueryExpression : DirectSelectStmt T_EXCEPT T_ALL NonJoinQueryTerm", "NonJoinQueryExpression : DirectSelectStmt T_UNION '(' NonJoinQueryTerm ')'", "NonJoinQueryExpression : DirectSelectStmt T_UNION T_ALL '(' NonJoinQueryTerm ')'", "NonJoinQueryExpression : DirectSelectStmt T_EXCEPT '(' NonJoinQueryTerm ')'", "NonJoinQueryExpression : DirectSelectStmt T_EXCEPT T_ALL '(' NonJoinQueryTerm ')'", "NonJoinQueryTerm : NonJoinQueryPrimary", "NonJoinQueryPrimary : SimpleTable", "SimpleTable : QuerySpecification", "SimpleTable : TableValueConstructor", "TableValueConstructor : T_VALUES TableValueConstructorList", "TableValueConstructorList : RowValueConstructor", "TableValueConstructorList : TableValueConstructorList ',' RowValueConstructor", "RowValueConstructor : RowValueConstructorElement", "RowValueConstructor : '(' RowValueConstructorList ')'", "RowValueConstructor : '(' T_DEFAULT ')'", "RowValueConstructorList : RowValueConstructorElement ',' RowValueConstructorElement", "RowValueConstructorList : RowValueConstructorList ',' RowValueConstructorElement", "RowValueConstructorElement : Expression", "RowValueConstructorElement : T_DEFAULT", "SubQuery : '(' QueryExpression ')'", "SubQuery : '(' QuerySpecification OrderBy ')'", "QuerySpecification : T_SELECT SetQuantifier_Opt SelectLimit_Opt SelectList T_FROM TableReferenceList WhereSearchCond_Opt GroupBy_Opt Having_Opt", "QuerySpecification : T_SELECT SetQuantifier_Opt SelectLimit_Opt SelectList", "SelectLimit_Opt : T_TOP TopValueSpecification", "SelectLimit_Opt : Empty", "TopValueSpecification : UnsignedIntegerLiteral", "TopValueSpecification : ParameterSpecification", "TopValueSpecification : DynamicParameter", "SetQuantifier_Opt : T_ALL", "SetQuantifier_Opt : T_DISTINCT", "SetQuantifier_Opt : Empty", "SelectList : SelectSubList", "SelectSubList : SelectSubListItem", "SelectSubList : SelectSubList ',' SelectSubListItem", "SelectSubListItem : DerivedColumn", "SelectSubListItem : Identifier '.' Identifier '.' Identifier '.' '*'", "SelectSubListItem : Identifier '.' Identifier '.' '*'", "SelectSubListItem : Identifier '.' '*'", "SelectSubListItem : '*'", "DerivedColumn : Expression AS_Clause_Opt", "AS_Clause_Opt : AS_Opt ColumnName", "AS_Clause_Opt : Empty", "ColumnReference : Identifier '.' Identifier '.' Identifier '.' ColumnName", "ColumnReference : Identifier '.' '.' Identifier '.' ColumnName", "ColumnReference : Identifier '.' Identifier '.' ColumnName", "ColumnReference : Identifier '.' ColumnName", "ColumnReference : RestrictedColumnName", "ColumnNameList : ColumnName", "ColumnNameList : ColumnNameList ',' ColumnName", "ColumnName : Identifier", "RestrictedColumnName : T_IDENTIFIER", "RestrictedColumnName : RelaxedIdent", "TableReferenceList : TableReference", "TableReferenceList : TableReferenceList ',' TableReference", "TableReference : TableName CorrelationSpec DerivedColumnList_Opt", "TableReference : TableName", "TableReference : SubQuery CorrelationSpec DerivedColumnList_Opt", "TableReference : JoinedTable", "TableReference : OuterJoinEscapeSeq", "CorrelationSpec : AS_Opt CorrelationName", "CorrelationName : Identifier", "DerivedColumnList_Opt : '(' ColumnNameList ')'", "DerivedColumnList_Opt : Empty", "AS_Opt : T_AS", "AS_Opt : Empty", "WhereSearchCond_Opt : T_WHERE SearchCondition", "WhereSearchCond_Opt : Empty", "SearchCondition : BooleanTerm", "SearchCondition : SearchCondition T_OR BooleanTerm", "BooleanTerm : BooleanFactor", "BooleanTerm : BooleanTerm T_AND BooleanFactor", "BooleanFactor : BooleanPrimary", "BooleanFactor : T_NOT BooleanPrimary", "BooleanPrimary : Predicate", "BooleanPrimary : '(' SearchCondition ')'", "Predicate : ComparisonPredicate", "Predicate : BetweenPredicate", "Predicate : InPredicate", "Predicate : LikePredicate", "Predicate : NullPredicate", "Predicate : QuantifiedComparisonPredicate", "Predicate : ExistsPredicate", "ComparisonPredicate : RowValueConstructor ComparisonOp RowValueConstructor", "ComparisonOp : T_EQ", "ComparisonOp : T_NE", "ComparisonOp : T_LT", "ComparisonOp : T_GT", "ComparisonOp : T_LE", "ComparisonOp : T_GE", "BetweenPredicate : RowValueConstructor T_NOT T_BETWEEN RowValueConstructor T_AND RowValueConstructor", "BetweenPredicate : RowValueConstructor T_BETWEEN RowValueConstructor T_AND RowValueConstructor", "InPredicate : RowValueConstructor T_NOT T_IN InPredicateValue", "InPredicate : RowValueConstructor T_IN InPredicateValue", "InPredicateValue : SubQuery", "InPredicateValue : '(' InValueList ')'", "InValueList : Expression", "InValueList : InValueList ',' Expression", "LikePredicate : Expression T_NOTLIKE Expression EscapeCharacter_Opt", "LikePredicate : Expression T_LIKE Expression EscapeCharacter_Opt", "EscapeCharacter_Opt : T_ESCAPE Expression", "EscapeCharacter_Opt : Empty", "DynamicParameter : '?'", "NullPredicate : RowValueConstructor T_IS T_NOT T_NULL", "NullPredicate : RowValueConstructor T_IS T_NULL", "QuantifiedComparisonPredicate : RowValueConstructor ComparisonOp Quantifier SubQuery", "Quantifier : T_ALL", "Quantifier : T_ANY", "Quantifier : T_SOME", "ExistsPredicate : T_EXISTS SubQuery", "ValueEscapeSeq : '{' ValueEscape '}'", "ValueEscape : DateTimeTSValueEscape", "ValueEscape : T_FN ScalarFn", "DateTimeTSValueEscape : Identifier CharLiteral", "ScalarFn : Identifier '(' Params_Opt ')'", "ScalarFn : T_CONVERT '(' ConvertParams ')'", "ScalarFn : T_CAST '(' CastParams ')'", "ScalarFn : T_USER '(' Params_Opt ')'", "ScalarFn : T_LEFT '(' Params_Opt ')'", "ScalarFn : T_RIGHT '(' Params_Opt ')'", "ScalarFn : T_INSERT '(' Params_Opt ')'", "ScalarFn : T_NULL '(' Params_Opt ')'", "ScalarFn : T_TIMESTAMPADD '(' T_TSIDATATYPE ',' Expression ',' Expression ')'", "ScalarFn : T_TIMESTAMPDIFF '(' T_TSIDATATYPE ',' Expression ',' Expression ')'", "Params_Opt : ParamList", "Params_Opt : Empty", "ParamList : ParamList ',' Expression", "ParamList : Expression", "ConvertParams : Expression ',' SQLDataType", "CastParams : Expression T_AS T_DATATYPE", "CastParams : Expression T_AS Identifier", "SQLDataType : T_DATATYPE", "SQLDataType : Identifier", "GroupBy_Opt : T_GROUP T_BY GroupByExpressionList", "GroupBy_Opt : Empty", "GroupByExpressionList : GroupByExpressionList ',' Expression", "GroupByExpressionList : Expression", "Having_Opt : T_HAVING SearchCondition", "Having_Opt : Empty", "Expression : Term", "Expression : Expression '+' Term", "Expression : Expression '-' Term", "Expression : Expression T_CONCAT Term", "Term : Factor", "Term : Term '*' Factor", "Term : Term '/' Factor", "Factor : ValueExpressionPrimary", "Factor : '+' ValueExpressionPrimary", "Factor : '-' ValueExpressionPrimary", "Factor : '-' '-' Factor", "ValueExpressionPrimary : UnsignedValueSpecification", "ValueExpressionPrimary : ColumnReference", "ValueExpressionPrimary : SetFunctionSpecification", "ValueExpressionPrimary : SubQuery", "ValueExpressionPrimary : CaseExpression", "ValueExpressionPrimary : ScalarFn", "ValueExpressionPrimary : ValueEscapeSeq", "ValueExpressionPrimary : '(' Expression ')'", "UnsignedValueSpecification : UnsignedLiteral", "UnsignedValueSpecification : GeneralValueSpecification", "ValueSpecification : Literal", "ValueSpecification : GeneralValueSpecification", "UnsignedLiteral : UnsignedNumericLiteral", "UnsignedLiteral : GeneralLiteral", "Literal : SignedNumericLiteral", "Literal : GeneralLiteral", "UnsignedNumericLiteral : ExactNumericLiteral", "UnsignedNumericLiteral : T_APPROX_NUM_LIT", "SignedNumericLiteral : UnsignedNumericLiteral", "SignedNumericLiteral : '+' UnsignedNumericLiteral", "SignedNumericLiteral : '-' UnsignedNumericLiteral", "ExactNumericLiteral : UnsignedIntegerLiteral", "ExactNumericLiteral : T_DECNUM_LIT", "UnsignedIntegerLiteral : T_USINT_LIT", "GeneralValueSpecification : ParameterSpecification", "GeneralValueSpecification : DynamicParameter", "GeneralValueSpecification : T_USER", "GeneralLiteral : CharLiteral", "GeneralLiteral : IntervalLiteral", "GeneralLiteral : '{' IntervalLiteral '}'", "GeneralLiteral : DateTimeLiteral", "GeneralLiteral : T_NULL", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_YEAR IntervalLeadingPrecision_Opt ToYear_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_YEAR IntervalLeadingPrecision_Opt T_TO T_MONTH", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_MONTH IntervalLeadingPrecision_Opt ToMonth_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_DAY IntervalLeadingPrecision_Opt ToDay_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_DAY IntervalLeadingPrecision_Opt T_TO T_HOUR", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_DAY IntervalLeadingPrecision_Opt T_TO T_MINUTE", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_DAY IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_HOUR IntervalLeadingPrecision_Opt ToHour_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_HOUR IntervalLeadingPrecision_Opt T_TO T_MINUTE", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_HOUR IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_MINUTE IntervalLeadingPrecision_Opt ToMinute_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_MINUTE IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_SECOND IntervalLeadingPrecision_Opt ToSecond_Opt", "IntervalLiteral : T_INTERVAL IntervalSign_Opt IntervalString T_SECOND IntervalSecondsPrecision_Opt ToSecond_Opt", "IntervalString : CharLiteral", "IntervalLeadingPrecision_Opt : '(' T_USINT_LIT ')'", "IntervalLeadingPrecision_Opt : Empty", "IntervalFractionalSecondsPrecision_Opt : '(' T_USINT_LIT ')'", "IntervalFractionalSecondsPrecision_Opt : Empty", "IntervalSecondsPrecision_Opt : '(' T_USINT_LIT ',' T_USINT_LIT ')'", "IntervalSign_Opt : '+'", "IntervalSign_Opt : '-'", "IntervalSign_Opt : Empty", "ToYear_Opt : T_TO T_YEAR", "ToYear_Opt : Empty", "ToMonth_Opt : T_TO T_MONTH", "ToMonth_Opt : Empty", "ToDay_Opt : T_TO T_DAY", "ToDay_Opt : Empty", "ToHour_Opt : T_TO T_HOUR", "ToHour_Opt : Empty", "ToMinute_Opt : T_TO T_MINUTE", "ToMinute_Opt : Empty", "ToSecond_Opt : T_TO T_SECOND", "ToSecond_Opt : Empty", "DateTimeLiteral : T_DATE CharLiteral", "DateTimeLiteral : T_TIME CharLiteral", "DateTimeLiteral : T_TIMESTAMP CharLiteral", "CharLiteral : T_CHAR_STR_LIT", "ParameterSpecification : ParameterName", "ParameterName : ':' Identifier", "CaseExpression : CaseSpecification", "CaseExpression : CaseAbbreviation", "CaseSpecification : SimpleCase", "CaseSpecification : SearchedCase", "CaseAbbreviation : T_NULLIF '(' Expression ',' Expression ')'", "CaseAbbreviation : T_COALESCE '(' CoalesceList ')'", "CoalesceList : CoalesceListExpression ',' CoalesceListExpression", "CoalesceList : CoalesceList ',' CoalesceListExpression", "CoalesceListExpression : Expression", "SimpleCase : T_CASE Expression SimpleWhenClauseList ElseClause_Opt T_END", "SearchedCase : T_CASE SearchedWhenClauseList ElseClause_Opt T_END", "SimpleWhenClauseList : SimpleWhenClause", "SimpleWhenClauseList : SimpleWhenClauseList SimpleWhenClause", "SearchedWhenClauseList : SearchedWhenClause", "SearchedWhenClauseList : SearchedWhenClauseList SearchedWhenClause", "SimpleWhenClause : T_WHEN Expression T_THEN Result", "SearchedWhenClause : T_WHEN SearchCondition T_THEN Result", "ElseClause_Opt : T_ELSE Result", "ElseClause_Opt : Empty", "Result : Expression", "SetFunctionSpecification : T_COUNT '(' '*' ')'", "SetFunctionSpecification : SetFunction", "SetFunction : T_AVG '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_MAX '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_MIN '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_STDDEV '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_STDDEV_POP '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_SUM '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_VAR '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_VAR_POP '(' SetQuantifier_Opt Expression ')'", "SetFunction : T_COUNT '(' SetQuantifier_Opt Expression ')'", "JoinedTable : TableReference T_CROSS T_JOIN TableReference", "JoinedTable : QualifiedJoin", "JoinedTable : '(' JoinedTable ')'", "QualifiedJoin : TableReference JoinType T_JOIN TableReference T_ON SearchCondition", "QualifiedJoin : TableReference T_JOIN TableReference T_ON SearchCondition", "OuterJoinEscape : Identifier JoinedTable", "OuterJoinEscapeSeq : '{' OuterJoinEscape '}'", "JoinType : T_FULL T_OUTER", "JoinType : T_LEFT T_OUTER", "JoinType : T_RIGHT T_OUTER", "JoinType : T_LEFT", "JoinType : T_RIGHT", "JoinType : T_FULL", "JoinType : T_INNER", "CreateIndexStmt : T_CREATE Unique_Opt Index_Type_Opt T_INDEX Identifier T_ON TableName '(' OrderColumnList ')'", "Unique_Opt : T_UNIQUE", "Unique_Opt : Empty", "Index_Type_Opt : Identifier", "Index_Type_Opt : Empty", "OrderColumnList : OrderColumn", "OrderColumnList : OrderColumnList ',' OrderColumn", "OrderColumn : Identifier OrderingSpecification_Opt", "CreateViewStmt : T_CREATE T_VIEW TableName Empty T_AS QuerySpecification", "CreateViewStmt : T_CREATE T_VIEW TableName '(' ColumnNameList ')' T_AS QuerySpecification", "CreateTableStmt : T_CREATE T_TABLE TableName '(' ColumnDefinitionList ',' TableConstraintDefinitionList ')'", "CreateTableStmt : T_CREATE T_TABLE TableName '(' ColumnDefinitionList Empty ')'", "ColumnDefinitionList : ColumnDefinition", "ColumnDefinitionList : ColumnDefinitionList ',' ColumnDefinition", "ColumnDefinition : ColumnName DataType ColumnConstraintDefinition_Opt", "DataType : IntervalType Empty", "DataType : Identifier Empty", "DataType : Identifier '(' DataTypeAttributeList ')'", "DataTypeAttributeList : DataTypeAttributeList ',' DataTypeAttributeValue", "DataTypeAttributeList : DataTypeAttributeValue", "DataTypeAttributeValue : Identifier", "DataTypeAttributeValue : Literal", "IntervalType : T_INTERVAL T_YEAR IntervalLeadingPrecision_Opt ToYear_Opt", "IntervalType : T_INTERVAL T_YEAR IntervalLeadingPrecision_Opt T_TO T_MONTH", "IntervalType : T_INTERVAL T_MONTH IntervalLeadingPrecision_Opt ToMonth_Opt", "IntervalType : T_INTERVAL T_DAY IntervalLeadingPrecision_Opt ToDay_Opt", "IntervalType : T_INTERVAL T_DAY IntervalLeadingPrecision_Opt T_TO T_HOUR", "IntervalType : T_INTERVAL T_DAY IntervalLeadingPrecision_Opt T_TO T_MINUTE", "IntervalType : T_INTERVAL T_DAY IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalType : T_INTERVAL T_HOUR IntervalLeadingPrecision_Opt ToHour_Opt", "IntervalType : T_INTERVAL T_HOUR IntervalLeadingPrecision_Opt T_TO T_MINUTE", "IntervalType : T_INTERVAL T_HOUR IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalType : T_INTERVAL T_MINUTE IntervalLeadingPrecision_Opt ToMinute_Opt", "IntervalType : T_INTERVAL T_MINUTE IntervalLeadingPrecision_Opt T_TO T_SECOND IntervalFractionalSecondsPrecision_Opt", "IntervalType : T_INTERVAL T_SECOND IntervalLeadingPrecision_Opt ToSecond_Opt", "IntervalType : T_INTERVAL T_SECOND IntervalSecondsPrecision_Opt ToSecond_Opt", "ColumnConstraintDefinition_Opt : T_NOT T_NULL", "ColumnConstraintDefinition_Opt : T_NULL", "ColumnConstraintDefinition_Opt : Empty", "UniqueSpecification : T_UNIQUE", "UniqueSpecification : T_PRIMARY T_KEY", "TableConstraintDefinitionList : TableConstraintDefinition", "TableConstraintDefinitionList : TableConstraintDefinitionList ',' TableConstraintDefinition", "TableConstraintDefinition : UniqueSpecification '(' ColumnNameList ')'", "DropTableStmt : T_DROP T_TABLE TableName", "DropBehavior : T_CASCADE", "DropBehavior : T_RESTRICT", "DropViewStmt : T_DROP T_VIEW TableName DropBehavior", "DropIndexStmt : T_DROP T_INDEX Identifier T_ON TableName", "AlterTableStmt : T_ALTER T_TABLE TableName AddColumnDefinition", "AddColumnDefinition : T_ADD ColumnDefinition", "AddColumnDefinition : T_ADD T_COLUMN ColumnDefinition", "DeleteStmtSearched : T_DELETE T_FROM TableName WhereSearchCond_Opt", "TableName : QualifiedName", "QualifiedName : Identifier '.' Identifier '.' QualifiedIdentifier", "QualifiedName : Identifier '.' QualifiedIdentifier", "QualifiedName : Identifier '.' '.' QualifiedIdentifier", "QualifiedName : QualifiedIdentifier", "QualifiedIdentifier : Identifier", "InsertStmt : T_INSERT T_INTO TableName InsertList", "InsertList : T_DEFAULT T_VALUES", "InsertList : QueryExpression", "InsertList : '(' QueryExpression ')'", "InsertList : '(' ColumnNameList ')' QueryExpression", "SetStmt : T_SET T_CATALOG ValueSpecification", "SetStmt : T_SET T_SCHEMA ValueSpecification", "ProcedureStmt : '{' ProcedureCall '}'", "ProcedureCall : ProcedureRetVal T_CALL ProcedureName", "ProcedureCall : T_CALL ProcedureName", "ProcedureName : QualifiedProcedureName ProcedureParams_Opt", "QualifiedProcedureName : Identifier '.' Identifier '.' Identifier", "QualifiedProcedureName : Identifier '.' Identifier", "QualifiedProcedureName : Identifier '.' '.' Identifier", "QualifiedProcedureName : Identifier", "ProcedureParams_Opt : ProcedureParamList ')'", "ProcedureParams_Opt : '(' ')'", "ProcedureParams_Opt : Empty", "ProcedureParamList : ProcedureParamList ',' Expression", "ProcedureParamList : ProcedureParamList ','", "ProcedureParamList : '(' Expression", "ProcedureParamList : '(' ','", "ProcedureParamList : '(' ',' Expression", "ProcedureRetVal : DynamicParameter T_EQ", "GrantStmt : T_GRANT Privileges T_ON ObjectName T_TO GranteeList WithGrantOption_Opt", "Privileges : T_ALL T_PRIVILEGES", "Privileges : ActionList", "ActionList : Action", "ActionList : ActionList ',' Action", "Action : T_SELECT", "Action : T_DELETE", "Action : T_INSERT PrivilegeColumnList_Opt", "Action : T_UPDATE PrivilegeColumnList_Opt", "Action : T_REFERENCES PrivilegeColumnList_Opt", "Action : T_USAGE", "PrivilegeColumnList_Opt : '(' ColumnNameList ')'", "PrivilegeColumnList_Opt : Empty", "ObjectName : TableName", "ObjectName : T_TABLE TableName", "GranteeList : Grantee", "GranteeList : GranteeList ',' Grantee", "Grantee : T_PUBLIC", "Grantee : AuthorizationIdentifier", "AuthorizationIdentifier : Identifier", "WithGrantOption_Opt : T_WITH T_GRANT T_OPTION", "WithGrantOption_Opt : Empty", "RevokeStmt : T_REVOKE GrantOptionFor_Opt Privileges T_ON ObjectName T_FROM GranteeList DropBehavior", "GrantOptionFor_Opt : T_GRANT T_OPTION T_FOR", "GrantOptionFor_Opt : Empty", "UpdateStmtSearched : T_UPDATE TableName T_SET SetClauseList WhereSearchCond_Opt", "UpdateStmtSearched : T_UPSERT TableName T_SET SetClauseList WhereSearchCond_Opt", "SetClauseList : SetClause", "SetClauseList : SetClauseList ',' SetClause", "SetClause : ColumnName T_EQ UpdateSource", "UpdateSource : Expression", "UpdateSource : T_DEFAULT" };
  private final String m_queryString;
  private final IPTLimitChecker m_limitChecker;
  private final PTNodeFactory m_nodeFactory;
  final Lexer lexer;
  private IPTNode m_rootNode;
  int yyn;
  int yym;
  int yystate;
  String yys;
  
  void debug(String paramString)
  {
    if (this.yydebug) {
      System.out.println(paramString);
    }
  }
  
  final void state_push(int paramInt)
  {
    try
    {
      this.stateptr += 1;
      this.statestk[this.stateptr] = paramInt;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      int i = this.statestk.length;
      int j = i * 2;
      int[] arrayOfInt = new int[j];
      System.arraycopy(this.statestk, 0, arrayOfInt, 0, i);
      this.statestk = arrayOfInt;
      this.statestk[this.stateptr] = paramInt;
    }
  }
  
  final int state_pop()
  {
    return this.statestk[(this.stateptr--)];
  }
  
  final void state_drop(int paramInt)
  {
    this.stateptr -= paramInt;
  }
  
  final int state_peek(int paramInt)
  {
    return this.statestk[(this.stateptr - paramInt)];
  }
  
  final boolean init_stacks()
  {
    this.stateptr = -1;
    val_init();
    return true;
  }
  
  void dump_stacks(int paramInt)
  {
    System.out.println("=index==state====value=     s:" + this.stateptr + "  v:" + this.valptr);
    for (int i = 0; i < paramInt; i++) {
      System.out.println(" " + i + "    " + this.statestk[i] + "      " + this.valstk[i]);
    }
    System.out.println("======================");
  }
  
  void val_init()
  {
    this.valstk = new ParserVal['Ǵ'];
    this.yyval = new ParserVal();
    this.yylval = new ParserVal();
    this.valptr = -1;
  }
  
  void val_push(ParserVal paramParserVal)
  {
    if (this.valptr >= 500) {
      return;
    }
    this.valstk[(++this.valptr)] = paramParserVal;
  }
  
  ParserVal val_pop()
  {
    if (this.valptr < 0) {
      return new ParserVal();
    }
    return this.valstk[(this.valptr--)];
  }
  
  void val_drop(int paramInt)
  {
    int i = this.valptr - paramInt;
    if (i < 0) {
      return;
    }
    this.valptr = i;
  }
  
  ParserVal val_peek(int paramInt)
  {
    int i = this.valptr - paramInt;
    if (i < 0) {
      return new ParserVal();
    }
    return this.valstk[i];
  }
  
  final ParserVal dup_yyval(ParserVal paramParserVal)
  {
    ParserVal localParserVal = new ParserVal();
    localParserVal.ival = paramParserVal.ival;
    localParserVal.dval = paramParserVal.dval;
    localParserVal.sval = paramParserVal.sval;
    localParserVal.obj = paramParserVal.obj;
    return localParserVal;
  }
  
  static void yytable()
  {
    yytable = new short[] { 44, 210, 210, 433, 26, 62, 66, 146, 206, 206, 434, 118, 666, 463, 18, 583, 740, 165, 266, 60, 265, 381, 349, 382, 383, 91, 92, 458, 444, 13, 681, 167, 432, 188, 666, 84, 266, 105, 265, 129, 256, 365, 354, 177, 127, 328, 328, 433, 459, 460, 186, 186, 118, 186, 719, 174, 175, 127, 176, 62, 255, 180, 601, 182, 183, 196, 187, 294, 294, 266, 327, 265, 393, 789, 188, 192, 84, 188, 105, 391, 38, 105, 838, 280, 68, 354, 390, 582, 266, 266, 265, 265, 86, 86, 262, 47, 784, 437, 407, 120, 62, 296, 268, 18, 230, 268, 170, 187, 267, 18, 187, 451, 266, 75, 265, 284, 93, 431, 785, 431, 40, 38, 86, 86, 790, 86, 217, 178, 86, 181, 86, 86, 572, 790, 673, 170, 323, 324, 46, 48, 120, 102, 86, 120, 18, 266, 497, 265, 18, 18, 18, 294, 255, 18, 75, 46, 590, 393, 49, 419, 307, 69, 593, 60, 391, 42, 801, 802, 740, 351, 8, 262, 426, 545, 290, 172, 171, 297, 293, 61, 701, 18, 455, 611, 64, 266, 255, 265, 185, 18, 18, 184, 12, 18, 575, 623, 309, 266, 777, 265, 189, 266, 512, 265, 172, 303, 42, 46, 594, 42, 260, 263, 312, 18, 66, 190, 390, 65, 266, 356, 265, 778, 18, 66, 120, 367, 121, 781, 191, 185, 367, 66, 66, 367, 483, 367, 66, 66, 66, 673, 779, 189, 753, 367, 66, 66, 782, 572, 273, 467, 266, 724, 265, 264, 18, 484, 389, 18, 64, 106, 431, 296, 780, 166, 410, 784, 367, 301, 783, 255, 555, 264, 86, 415, 590, 266, 60, 265, 700, 319, 193, 564, 407, 266, 185, 265, 781, 837, 656, 328, 690, 65, 682, 270, 658, 326, 301, 301, 271, 538, 106, 213, 279, 106, 264, 835, 18, 524, 525, 118, 438, 445, 483, 577, 657, 195, 449, 441, 118, 46, 118, 47, 46, 264, 264, 539, 303, 836, 293, 293, 229, 188, 228, 84, 283, 105, 395, 297, 84, 394, 398, 8, 188, 216, 105, 60, 105, 264, 665, 408, 118, 356, 572, 351, 121, 572, 440, 62, 634, 439, 273, 572, 292, 12, 187, 292, 572, 214, 301, 553, 301, 301, 188, 436, 84, 187, 105, 86, 118, 467, 264, 431, 433, 120, 86, 602, 207, 207, 46, 301, 47, 46, 591, 86, 592, 8, 255, 120, 62, 255, 188, 18, 84, 120, 105, 187, 120, 454, 120, 306, 18, 75, 18, 266, 120, 265, 105, 12, 222, 120, 264, 540, 120, 761, 89, 89, 62, 8, 767, 215, 42, 551, 264, 187, 223, 120, 264, 120, 120, 224, 225, 18, 18, 18, 97, 572, 18, 272, 120, 12, 572, 227, 75, 264, 89, 89, 42, 89, 120, 89, 89, 89, 89, 89, 726, 808, 120, 587, 232, 389, 18, 8, 8, 18, 89, 18, 814, 599, 120, 18, 75, 18, 310, 18, 8, 264, 185, 18, 526, 494, 18, 233, 493, 12, 12, 530, 447, 185, 189, 470, 535, 255, 634, 534, 249, 250, 12, 819, 471, 234, 264, 301, 821, 548, 387, 18, 18, 235, 264, 18, 18, 301, 255, 310, 557, 236, 310, 185, 311, 266, 725, 265, 301, 566, 86, 319, 411, 412, 454, 189, 301, 41, 89, 89, 237, 652, 654, 655, 18, 269, 624, 42, 266, 238, 265, 185, 667, 106, 793, 626, 43, 266, 239, 265, 470, 669, 106, 189, 106, 664, 678, 678, 293, 471, 605, 552, 240, 243, 439, 609, 89, 244, 245, 691, 691, 627, 210, 266, 698, 265, 246, 277, 628, 206, 266, 247, 265, 745, 106, 89, 18, 631, 276, 266, 89, 265, 255, 310, 649, 635, 650, 636, 278, 704, 707, 710, 713, 51, 717, 717, 721, 848, 282, 286, 309, 849, 106, 89, 89, 89, 288, 289, 301, 281, 492, 285, 86, 632, 106, 266, 502, 265, 291, 52, 793, 557, 777, 292, 264, 86, 675, 403, 404, 405, 723, 858, 266, 830, 265, 266, 295, 265, 831, 503, 266, 309, 265, 672, 309, 832, 310, 561, 89, 53, 439, 757, 389, 311, 757, 54, 763, 89, 504, 505, 606, 106, 607, 110, 833, 772, 743, 382, 89, 742, 643, 188, 55, 189, 56, 383, 89, 388, 89, 89, 506, 89, 391, 110, 774, 89, 834, 773, 171, 393, 401, 301, 89, 644, 110, 728, 402, 89, 413, 110, 414, 89, 110, 704, 707, 710, 713, 717, 717, 721, 507, 739, 645, 646, 210, 89, 435, 776, 420, 421, 787, 206, 427, 428, 557, 309, 568, 443, 110, 751, 86, 442, 452, 86, 647, 18, 453, 759, 817, 86, 264, 456, 310, 457, 86, 806, 823, 840, 805, 18, 439, 219, 462, 469, 825, 18, 310, 825, 18, 825, 18, 490, 310, 264, 648, 310, 18, 310, 491, 495, 496, 18, 264, 310, 18, 500, 843, 845, 310, 842, 439, 310, 253, 540, 517, 301, 373, 18, 498, 376, 18, 378, 501, 508, 310, 683, 89, 310, 264, 384, 18, 511, 301, 89, 809, 264, 283, 310, 825, 283, 825, 825, 513, 301, 264, 684, 518, 179, 18, 89, 179, 89, 409, 86, 400, 310, 685, 400, 86, 89, 18, 686, 89, 399, 687, 398, 399, 310, 398, 519, 89, 89, 89, 522, 523, 268, 89, 820, 89, 178, 264, 401, 178, 318, 401, 71, 397, 542, 536, 397, 688, 148, 527, 739, 148, 543, 528, 264, 537, 70, 264, 301, 544, 342, 350, 264, 550, 8, 359, 362, 364, 226, 89, 368, 309, 71, 554, 89, 368, 72, 73, 368, 377, 368, 483, 562, 241, 242, 309, 18, 809, 368, 18, 588, 309, 563, 683, 309, 350, 309, 603, 613, 619, 74, 75, 309, 50, 89, 149, 89, 309, 149, 633, 309, 368, 637, 684, 70, 640, 642, 76, 77, 78, 79, 653, 663, 309, 685, 422, 309, 89, 662, 686, 671, 89, 687, 447, 51, 342, 309, 694, 697, 24, 89, 80, 8, 702, 89, 89, 703, 81, 715, 706, 82, 83, 709, 727, 309, 712, 683, 741, 688, 716, 52, 720, 84, 736, 12, 226, 309, 241, 242, 744, 746, 85, 747, 752, 754, 461, 684, 342, 466, 24, 24, 24, 24, 24, 24, 24, 24, 685, 755, 18, 53, 762, 686, 765, 485, 687, 54, 488, 371, 766, 827, 768, 828, 770, 769, 110, 775, 776, 850, 499, 89, 786, 794, 55, 89, 56, 788, 509, 510, 796, 18, 688, 514, 515, 516, 110, 798, 400, 520, 521, 89, 803, 812, 472, 813, 816, 110, 815, 824, 857, 852, 110, 18, 89, 110, 859, 18, 829, 89, 89, 851, 853, 89, 854, 855, 3, 89, 533, 89, 18, 18, 405, 473, 89, 24, 71, 18, 18, 18, 18, 110, 474, 18, 70, 475, 176, 18, 318, 464, 476, 316, 317, 18, 18, 584, 477, 18, 839, 478, 600, 567, 18, 479, 70, 71, 680, 318, 480, 481, 18, 579, 18, 486, 71, 89, 760, 71, 693, 466, 699, 94, 71, 342, 585, 586, 392, 841, 71, 313, 399, 71, 89, 70, 89, 71, 418, 342, 488, 576, 71, 71, 70, 89, 357, 70, 331, 362, 70, 362, 70, 487, 641, 89, 612, 581, 70, 807, 89, 70, 315, 450, 94, 70, 212, 94, 625, 804, 70, 70, 800, 629, 630, 795, 734, 749, 797, 70, 799, 856, 194, 670, 305, 748, 568, 638, 70, 446, 639, 70, 0, 0, 0, 89, 70, 0, 422, 0, 0, 0, 70, 89, 0, 70, 0, 0, 0, 70, 0, 0, 0, 24, 70, 70, 24, 24, 24, 24, 0, 0, 0, 0, 0, 204, 0, 203, 0, 0, 24, 0, 89, 0, 24, 0, 24, 0, 24, 24, 129, 0, 0, 24, 695, 127, 24, 24, 24, 24, 24, 0, 0, 103, 0, 24, 24, 24, 488, 24, 24, 24, 24, 24, 0, 24, 0, 24, 0, 0, 0, 24, 24, 0, 24, 24, 24, 0, 24, 24, 24, 24, 24, 24, 24, 24, 0, 0, 24, 0, 24, 24, 0, 19, 103, 103, 103, 103, 103, 19, 103, 24, 0, 24, 0, 205, 0, 0, 0, 24, 0, 24, 24, 24, 0, 0, 0, 0, 0, 24, 0, 18, 0, 0, 24, 0, 0, 0, 0, 70, 24, 24, 0, 24, 0, 0, 0, 18, 0, 0, 0, 18, 18, 0, 0, 71, 771, 0, 0, 72, 73, 0, 0, 18, 0, 0, 0, 33, 18, 0, 0, 0, 0, 0, 0, 18, 18, 616, 617, 618, 620, 622, 0, 74, 75, 0, 0, 0, 103, 0, 791, 792, 18, 18, 18, 18, 0, 0, 0, 117, 76, 77, 78, 79, 18, 0, 0, 33, 33, 33, 33, 33, 33, 33, 33, 117, 18, 0, 0, 117, 117, 0, 18, 0, 80, 18, 18, 0, 0, 0, 81, 94, 18, 82, 83, 0, 94, 18, 0, 822, 0, 0, 0, 117, 117, 84, 18, 0, 0, 0, 0, 0, 0, 0, 85, 93, 0, 0, 0, 0, 117, 117, 117, 117, 0, 0, 97, 0, 0, 0, 0, 0, 94, 0, 0, 0, 0, 0, 198, 0, 102, 846, 342, 0, 117, 0, 342, 0, 0, 0, 117, 34, 33, 117, 117, 0, 0, 0, 0, 0, 94, 0, 0, 0, 0, 117, 0, 0, 0, 106, 0, 0, 0, 0, 117, 0, 342, 729, 730, 731, 732, 733, 735, 860, 103, 0, 199, 103, 103, 103, 103, 34, 34, 34, 34, 34, 34, 34, 34, 0, 0, 103, 0, 0, 0, 103, 0, 103, 0, 103, 103, 0, 200, 201, 103, 0, 0, 103, 103, 103, 103, 103, 0, 0, 202, 121, 103, 103, 103, 0, 103, 103, 103, 103, 103, 0, 103, 0, 103, 0, 0, 0, 103, 103, 0, 103, 103, 103, 0, 103, 103, 103, 103, 103, 103, 103, 103, 0, 0, 103, 0, 103, 103, 0, 0, 0, 0, 0, 0, 0, 0, 0, 103, 0, 103, 0, 34, 0, 0, 0, 103, 0, 103, 103, 103, 0, 0, 0, 33, 0, 103, 33, 33, 33, 33, 103, 0, 0, 0, 0, 0, 103, 103, 0, 103, 33, 0, 0, 0, 33, 0, 33, 0, 33, 33, 0, 0, 0, 33, 0, 0, 33, 33, 33, 33, 33, 0, 104, 0, 0, 33, 33, 33, 0, 33, 33, 33, 33, 33, 0, 33, 0, 33, 0, 0, 0, 33, 33, 0, 33, 33, 33, 0, 33, 33, 33, 33, 33, 33, 33, 33, 0, 0, 33, 0, 33, 33, 20, 104, 104, 104, 104, 104, 20, 104, 0, 33, 0, 33, 0, 332, 0, 0, 0, 33, 0, 33, 33, 33, 366, 0, 0, 0, 0, 33, 0, 0, 374, 375, 33, 0, 0, 379, 380, 381, 33, 33, 0, 33, 0, 385, 386, 34, 0, 0, 34, 34, 34, 34, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 34, 0, 0, 0, 34, 0, 34, 0, 34, 34, 0, 0, 0, 34, 0, 0, 34, 34, 34, 34, 34, 22, 0, 104, 0, 34, 34, 34, 0, 34, 34, 34, 34, 34, 0, 34, 0, 34, 0, 0, 0, 34, 34, 0, 34, 34, 34, 65, 34, 34, 34, 34, 34, 34, 34, 34, 0, 0, 34, 0, 34, 34, 22, 22, 22, 22, 22, 22, 22, 0, 0, 34, 0, 34, 0, 0, 65, 0, 0, 34, 0, 34, 34, 34, 0, 65, 0, 0, 65, 34, 0, 0, 0, 65, 34, 0, 0, 0, 0, 65, 34, 34, 65, 34, 0, 0, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 104, 0, 0, 104, 104, 104, 104, 23, 23, 23, 23, 23, 23, 23, 0, 0, 0, 104, 0, 0, 0, 104, 0, 104, 0, 104, 104, 0, 0, 0, 104, 0, 0, 104, 104, 104, 104, 104, 0, 0, 0, 0, 104, 104, 104, 0, 104, 104, 104, 104, 104, 0, 104, 0, 104, 0, 0, 0, 104, 104, 0, 104, 104, 104, 0, 104, 104, 104, 104, 104, 104, 104, 104, 0, 0, 104, 0, 104, 104, 0, 0, 0, 0, 0, 0, 0, 0, 0, 104, 27, 104, 23, 0, 0, 0, 0, 104, 0, 104, 104, 104, 0, 0, 0, 0, 0, 104, 0, 0, 0, 0, 104, 0, 0, 0, 0, 0, 104, 104, 0, 104, 0, 0, 22, 0, 0, 22, 22, 22, 22, 27, 27, 27, 27, 27, 27, 27, 0, 0, 0, 22, 0, 0, 0, 22, 0, 22, 0, 22, 22, 0, 0, 0, 22, 0, 0, 22, 22, 22, 22, 22, 0, 0, 0, 0, 22, 22, 22, 0, 22, 22, 22, 22, 22, 0, 22, 0, 22, 0, 0, 0, 22, 22, 0, 22, 22, 22, 0, 22, 22, 22, 22, 22, 22, 22, 22, 0, 0, 22, 0, 22, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 22, 0, 22, 27, 0, 0, 0, 0, 22, 0, 22, 22, 22, 0, 0, 0, 23, 0, 22, 23, 23, 23, 23, 22, 0, 0, 0, 0, 0, 22, 22, 0, 22, 23, 0, 0, 0, 23, 0, 23, 0, 23, 23, 0, 0, 0, 23, 0, 0, 23, 23, 23, 23, 23, 28, 0, 0, 0, 23, 23, 23, 0, 23, 23, 23, 23, 23, 0, 23, 0, 23, 0, 0, 0, 23, 23, 0, 23, 23, 23, 0, 23, 23, 23, 23, 23, 23, 23, 23, 0, 0, 23, 0, 23, 23, 28, 28, 28, 28, 28, 28, 28, 0, 0, 23, 0, 23, 0, 0, 0, 0, 0, 23, 0, 23, 23, 23, 0, 0, 0, 0, 0, 23, 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 23, 23, 0, 23, 0, 0, 27, 0, 0, 27, 27, 27, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 27, 0, 27, 0, 27, 27, 0, 0, 0, 27, 0, 0, 27, 27, 27, 27, 27, 32, 0, 28, 0, 27, 27, 27, 0, 27, 27, 27, 27, 27, 0, 27, 0, 27, 0, 0, 0, 27, 27, 0, 27, 27, 27, 0, 27, 27, 27, 27, 27, 27, 27, 27, 0, 0, 27, 0, 27, 27, 32, 32, 32, 32, 32, 32, 32, 0, 0, 27, 0, 27, 0, 0, 0, 0, 0, 27, 0, 27, 27, 27, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0, 27, 0, 0, 0, 0, 0, 27, 27, 0, 27, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 228, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 0, 0, 28, 28, 28, 28, 228, 228, 228, 228, 228, 36, 228, 0, 0, 0, 28, 0, 0, 0, 28, 0, 28, 0, 28, 28, 0, 0, 0, 28, 0, 0, 28, 28, 28, 28, 28, 0, 0, 0, 0, 28, 28, 28, 0, 28, 28, 28, 28, 28, 0, 28, 0, 28, 0, 0, 0, 28, 28, 0, 28, 28, 28, 0, 28, 28, 28, 28, 28, 28, 28, 28, 0, 0, 28, 0, 28, 28, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 18, 28, 228, 0, 0, 0, 0, 28, 0, 28, 28, 28, 0, 0, 0, 0, 0, 28, 0, 0, 0, 0, 28, 0, 0, 0, 0, 0, 28, 28, 0, 28, 0, 0, 32, 0, 0, 32, 32, 32, 32, 18, 18, 18, 18, 18, 0, 18, 0, 0, 0, 32, 0, 0, 0, 32, 0, 32, 0, 32, 32, 0, 0, 0, 32, 0, 0, 32, 32, 32, 32, 32, 0, 0, 0, 0, 32, 32, 32, 0, 32, 32, 32, 32, 32, 0, 32, 0, 32, 0, 0, 0, 32, 32, 0, 32, 32, 32, 0, 32, 32, 32, 32, 32, 32, 32, 32, 0, 0, 32, 0, 32, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 32, 18, 0, 0, 0, 0, 32, 0, 32, 32, 32, 0, 0, 0, 228, 0, 32, 228, 228, 228, 228, 32, 0, 0, 0, 0, 0, 32, 32, 0, 32, 228, 0, 0, 0, 228, 0, 228, 0, 228, 228, 0, 0, 0, 228, 0, 0, 228, 228, 228, 228, 228, 233, 0, 0, 0, 228, 228, 228, 0, 228, 228, 228, 228, 228, 0, 228, 0, 228, 0, 0, 0, 228, 228, 0, 228, 228, 228, 0, 228, 228, 228, 228, 228, 228, 228, 228, 0, 0, 228, 0, 228, 228, 233, 233, 233, 233, 233, 0, 233, 0, 0, 228, 0, 228, 0, 0, 0, 0, 0, 228, 0, 228, 228, 228, 0, 0, 0, 0, 0, 228, 0, 0, 0, 0, 228, 0, 0, 0, 0, 0, 228, 228, 0, 228, 0, 0, 18, 0, 0, 18, 18, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 18, 0, 18, 0, 18, 18, 0, 0, 0, 18, 0, 0, 18, 18, 18, 18, 18, 102, 0, 233, 0, 18, 18, 18, 0, 18, 18, 18, 18, 18, 0, 18, 0, 18, 0, 0, 0, 18, 18, 0, 18, 18, 18, 0, 18, 18, 18, 18, 18, 18, 18, 18, 0, 0, 18, 0, 18, 18, 102, 102, 102, 102, 102, 0, 102, 0, 0, 18, 0, 18, 0, 0, 0, 0, 0, 18, 0, 18, 18, 18, 0, 0, 18, 0, 0, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 18, 18, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 233, 0, 0, 233, 233, 233, 233, 18, 18, 18, 18, 18, 0, 18, 0, 0, 0, 233, 0, 0, 0, 233, 0, 233, 0, 233, 233, 0, 0, 0, 233, 0, 0, 233, 233, 233, 233, 233, 0, 0, 0, 0, 233, 233, 233, 0, 233, 233, 233, 233, 233, 0, 233, 0, 233, 0, 0, 0, 233, 233, 0, 233, 233, 233, 0, 233, 233, 233, 233, 233, 233, 233, 233, 0, 0, 233, 0, 233, 233, 0, 0, 0, 0, 0, 0, 0, 0, 0, 233, 191, 233, 18, 0, 0, 0, 0, 233, 0, 233, 233, 233, 0, 0, 0, 0, 0, 233, 0, 0, 0, 0, 233, 0, 0, 0, 0, 0, 233, 233, 0, 233, 0, 0, 102, 0, 0, 102, 102, 102, 102, 191, 0, 191, 191, 191, 0, 0, 0, 0, 0, 102, 0, 0, 0, 102, 0, 102, 0, 102, 102, 0, 0, 0, 102, 0, 0, 102, 102, 102, 102, 102, 0, 0, 0, 0, 102, 102, 102, 0, 102, 102, 102, 102, 102, 0, 102, 0, 102, 0, 0, 0, 102, 102, 0, 102, 102, 102, 0, 102, 102, 102, 102, 102, 102, 102, 102, 0, 0, 102, 0, 102, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 102, 0, 102, 191, 0, 0, 0, 0, 102, 0, 102, 102, 102, 0, 0, 0, 18, 0, 102, 18, 18, 18, 18, 102, 0, 0, 0, 0, 194, 102, 102, 0, 102, 18, 0, 0, 0, 18, 0, 18, 0, 18, 18, 0, 0, 0, 18, 0, 0, 18, 18, 18, 18, 18, 0, 0, 0, 0, 18, 18, 18, 0, 18, 18, 18, 18, 18, 0, 18, 194, 18, 194, 194, 194, 18, 18, 0, 18, 18, 18, 0, 18, 18, 18, 18, 18, 18, 18, 18, 0, 0, 18, 0, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 18, 0, 0, 0, 0, 0, 18, 0, 18, 18, 18, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 18, 18, 0, 18, 0, 0, 191, 0, 0, 191, 191, 191, 191, 0, 0, 0, 0, 0, 193, 0, 194, 0, 0, 191, 0, 0, 0, 191, 0, 191, 0, 191, 191, 0, 0, 0, 191, 0, 0, 191, 191, 191, 191, 191, 0, 0, 0, 0, 191, 191, 191, 0, 191, 191, 191, 191, 191, 0, 191, 193, 191, 193, 193, 193, 191, 191, 0, 191, 191, 191, 0, 191, 191, 191, 191, 191, 191, 191, 191, 0, 0, 191, 0, 191, 191, 0, 0, 0, 0, 0, 0, 0, 0, 0, 191, 0, 191, 0, 0, 0, 0, 0, 191, 0, 191, 191, 191, 0, 0, 0, 0, 0, 191, 0, 0, 0, 0, 191, 0, 0, 0, 0, 192, 191, 191, 0, 191, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 193, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 194, 0, 0, 194, 194, 194, 194, 192, 0, 192, 192, 192, 0, 0, 0, 0, 0, 194, 0, 0, 0, 194, 0, 194, 0, 194, 194, 0, 0, 0, 194, 0, 0, 194, 194, 194, 194, 194, 0, 0, 0, 0, 194, 194, 194, 0, 194, 194, 194, 194, 194, 0, 194, 0, 194, 0, 0, 0, 194, 194, 0, 194, 194, 194, 0, 194, 194, 194, 194, 194, 194, 194, 194, 0, 0, 194, 0, 194, 194, 0, 0, 0, 0, 0, 0, 0, 0, 0, 194, 0, 194, 192, 378, 0, 0, 0, 194, 0, 194, 194, 194, 0, 0, 0, 0, 0, 194, 0, 0, 0, 0, 194, 0, 0, 0, 0, 0, 194, 194, 0, 194, 0, 0, 193, 0, 0, 193, 193, 193, 193, 0, 0, 378, 378, 0, 0, 378, 0, 0, 0, 193, 0, 0, 0, 193, 0, 193, 0, 193, 193, 0, 0, 0, 193, 0, 0, 193, 193, 193, 193, 193, 0, 0, 0, 0, 193, 193, 193, 0, 193, 193, 193, 193, 193, 0, 193, 0, 193, 0, 0, 0, 193, 193, 0, 193, 193, 193, 0, 193, 193, 193, 193, 193, 193, 193, 193, 0, 0, 193, 0, 193, 193, 0, 0, 0, 0, 0, 0, 0, 0, 108, 193, 0, 193, 0, 0, 0, 378, 0, 193, 0, 193, 193, 193, 0, 0, 0, 192, 0, 193, 192, 192, 192, 192, 193, 0, 0, 0, 0, 0, 193, 193, 0, 193, 192, 0, 0, 0, 192, 0, 192, 108, 192, 192, 108, 0, 0, 192, 0, 0, 192, 192, 192, 192, 192, 102, 0, 0, 0, 192, 192, 192, 0, 192, 192, 192, 192, 192, 0, 192, 0, 192, 0, 0, 0, 192, 192, 0, 192, 192, 192, 0, 192, 192, 192, 192, 192, 192, 192, 192, 0, 0, 192, 0, 192, 192, 102, 102, 102, 102, 102, 0, 102, 0, 0, 192, 0, 192, 0, 0, 0, 0, 0, 192, 0, 192, 192, 192, 0, 70, 0, 0, 0, 192, 0, 108, 18, 0, 192, 0, 0, 0, 0, 0, 192, 192, 0, 192, 0, 0, 378, 0, 0, 0, 0, 0, 378, 0, 378, 0, 0, 0, 0, 378, 0, 0, 0, 0, 0, 0, 70, 0, 0, 70, 378, 0, 378, 18, 378, 378, 18, 378, 0, 0, 0, 0, 0, 0, 0, 0, 378, 0, 0, 0, 0, 378, 378, 0, 0, 378, 0, 378, 378, 378, 13, 0, 0, 378, 0, 0, 0, 0, 378, 0, 0, 378, 152, 0, 0, 378, 378, 378, 378, 0, 0, 0, 0, 0, 378, 0, 0, 378, 0, 0, 0, 0, 0, 0, 0, 378, 0, 378, 0, 378, 378, 378, 0, 0, 0, 378, 0, 0, 378, 378, 70, 0, 378, 152, 0, 378, 152, 18, 121, 0, 378, 0, 378, 0, 0, 0, 0, 378, 0, 378, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 18, 0, 108, 0, 18, 18, 0, 0, 0, 0, 0, 121, 0, 0, 121, 0, 108, 0, 0, 0, 0, 0, 108, 0, 0, 108, 0, 108, 18, 18, 0, 0, 0, 108, 0, 0, 0, 102, 108, 102, 0, 108, 0, 0, 0, 18, 18, 18, 18, 152, 0, 102, 0, 0, 108, 102, 0, 108, 0, 102, 102, 0, 0, 0, 0, 0, 0, 108, 0, 18, 0, 102, 0, 0, 0, 18, 102, 0, 18, 18, 0, 0, 0, 102, 102, 108, 0, 0, 0, 0, 18, 0, 0, 0, 0, 121, 0, 108, 70, 18, 102, 102, 102, 102, 465, 18, 0, 125, 0, 124, 0, 0, 102, 0, 0, 0, 0, 0, 0, 0, 0, 70, 129, 0, 102, 0, 0, 127, 18, 0, 102, 0, 1, 102, 102, 70, 0, 0, 0, 0, 102, 70, 18, 0, 70, 102, 70, 0, 18, 0, 0, 18, 70, 18, 102, 2, 0, 70, 0, 18, 70, 0, 3, 0, 18, 4, 0, 18, 0, 0, 0, 0, 0, 70, 0, 70, 70, 0, 5, 0, 18, 0, 18, 18, 0, 152, 70, 128, 6, 0, 0, 0, 0, 18, 0, 0, 70, 0, 0, 0, 0, 0, 0, 18, 70, 0, 0, 0, 152, 0, 0, 18, 0, 0, 0, 0, 70, 0, 0, 0, 7, 0, 152, 18, 8, 9, 0, 126, 152, 0, 125, 152, 124, 152, 0, 0, 0, 0, 0, 152, 0, 10, 11, 0, 152, 129, 12, 152, 0, 0, 127, 0, 0, 0, 121, 0, 0, 0, 0, 0, 152, 0, 152, 152, 0, 0, 0, 0, 121, 0, 0, 0, 0, 152, 121, 0, 0, 121, 0, 121, 0, 0, 0, 152, 0, 121, 0, 0, 0, 325, 121, 152, 0, 121, 0, 0, 0, 0, 0, 0, 0, 0, 0, 152, 0, 0, 121, 0, 121, 121, 128, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0, 93, 0, 121, 94, 0, 0, 0, 95, 0, 96, 121, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 121, 101, 73, 102, 251, 0, 0, 0, 0, 0, 252, 0, 0, 125, 333, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 104, 0, 129, 0, 0, 105, 106, 127, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 334, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 8, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 12, 122, 123, 0, 128, 0, 0, 85, 595, 0, 0, 596, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 103, 0, 335, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 70, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 71, 0, 0, 0, 72, 73, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 597, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 74, 75, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 0, 0, 0, 85, 0, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 80, 98, 0, 0, 0, 99, 81, 100, 0, 82, 83, 101, 73, 102, 251, 0, 0, 0, 0, 0, 252, 84, 0, 125, 0, 124, 0, 0, 0, 0, 85, 0, 0, 0, 0, 74, 104, 0, 129, 0, 0, 105, 106, 127, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 8, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 12, 122, 123, 0, 128, 0, 0, 85, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 103, 0, 0, 0, 0, 0, 335, 0, 0, 125, 333, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 104, 0, 129, 0, 0, 105, 106, 127, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 334, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 0, 122, 123, 0, 128, 0, 0, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 0, 0, 0, 0, 0, 0, 218, 532, 0, 125, 531, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 104, 0, 129, 0, 0, 105, 106, 127, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 8, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 12, 122, 123, 0, 128, 0, 0, 85, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 103, 0, 0, 0, 0, 0, 18, 0, 18, 18, 333, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 104, 0, 18, 0, 0, 105, 106, 18, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 0, 122, 123, 0, 18, 0, 0, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 0, 0, 126, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 0, 0, 0, 85, 18, 0, 0, 18, 0, 0, 0, 18, 0, 18, 0, 18, 0, 18, 0, 0, 0, 18, 0, 18, 0, 0, 0, 18, 18, 18, 0, 0, 218, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 18, 18, 0, 0, 127, 0, 18, 18, 0, 0, 0, 0, 0, 18, 0, 0, 0, 18, 18, 18, 18, 0, 0, 0, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 18, 0, 0, 0, 18, 18, 18, 0, 0, 18, 18, 18, 18, 0, 18, 0, 0, 0, 0, 0, 0, 18, 18, 128, 18, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 103, 0, 218, 0, 316, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 0, 0, 0, 85, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 0, 0, 218, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 217, 0, 0, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 0, 0, 218, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 0, 0, 0, 85, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 103, 0, 18, 0, 18, 18, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 74, 104, 0, 0, 18, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 18, 122, 123, 0, 0, 0, 0, 85, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 578, 0, 218, 0, 0, 125, 0, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 74, 104, 0, 0, 127, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 0, 0, 0, 0, 0, 120, 121, 128, 122, 123, 0, 0, 0, 0, 85, 18, 0, 0, 18, 0, 0, 0, 18, 0, 18, 0, 18, 0, 18, 0, 0, 0, 18, 0, 18, 0, 0, 0, 18, 18, 18, 0, 0, 18, 0, 0, 18, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 18, 18, 0, 0, 18, 0, 18, 18, 0, 0, 0, 0, 0, 18, 0, 0, 0, 18, 18, 18, 18, 0, 0, 0, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 18, 0, 0, 0, 18, 18, 18, 0, 0, 18, 18, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 18, 18, 18, 18, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 218, 0, 101, 73, 102, 248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 0, 0, 0, 0, 127, 0, 0, 74, 104, 0, 0, 0, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 0, 0, 128, 0, 0, 0, 0, 120, 121, 0, 122, 123, 0, 0, 0, 0, 85, 18, 0, 0, 18, 0, 0, 0, 18, 0, 18, 0, 18, 0, 18, 0, 0, 0, 18, 218, 18, 0, 0, 0, 18, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 129, 0, 0, 0, 0, 127, 0, 0, 0, 0, 0, 18, 18, 0, 0, 0, 0, 18, 18, 0, 0, 0, 0, 0, 18, 0, 0, 0, 18, 18, 18, 18, 0, 0, 0, 18, 18, 13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 18, 0, 0, 0, 18, 18, 18, 0, 0, 18, 18, 18, 18, 128, 0, 0, 0, 0, 0, 0, 0, 18, 18, 0, 18, 18, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 93, 0, 0, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 72, 72, 72, 72, 72, 0, 72, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 74, 104, 0, 0, 0, 0, 105, 106, 0, 0, 0, 0, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 750, 0, 116, 117, 118, 119, 0, 0, 565, 0, 0, 0, 406, 0, 120, 121, 0, 122, 123, 0, 0, 93, 0, 85, 94, 0, 0, 0, 95, 0, 96, 0, 97, 0, 98, 0, 0, 0, 99, 0, 100, 0, 0, 0, 101, 73, 102, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 844, 0, 74, 104, 0, 0, 0, 0, 105, 106, 0, 0, 0, 70, 0, 107, 0, 0, 0, 108, 109, 78, 79, 0, 0, 0, 110, 111, 0, 71, 0, 0, 0, 72, 73, 0, 0, 0, 0, 0, 0, 112, 0, 80, 0, 0, 0, 113, 114, 115, 0, 0, 116, 117, 118, 119, 0, 74, 75, 204, 0, 203, 0, 0, 120, 121, 0, 122, 123, 0, 0, 0, 0, 85, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 72, 72, 0, 0, 0, 0, 0, 0, 80, 8, 406, 0, 72, 0, 81, 0, 72, 82, 83, 0, 72, 72, 0, 0, 0, 0, 0, 0, 0, 84, 72, 12, 47, 0, 0, 0, 0, 0, 85, 72, 0, 0, 72, 0, 72, 72, 0, 72, 529, 205, 0, 0, 0, 72, 0, 0, 72, 0, 72, 0, 72, 72, 72, 72, 72, 72, 72, 72, 70, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 0, 0, 71, 72, 0, 0, 72, 73, 0, 72, 71, 0, 72, 72, 72, 73, 0, 0, 0, 47, 0, 0, 0, 0, 72, 0, 0, 0, 0, 0, 74, 75, 0, 72, 0, 0, 0, 0, 74, 75, 0, 0, 0, 0, 0, 0, 0, 76, 77, 78, 79, 0, 70, 0, 0, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 0, 0, 80, 72, 73, 0, 0, 0, 81, 0, 80, 82, 83, 0, 0, 0, 81, 0, 0, 82, 83, 0, 0, 84, 0, 0, 0, 74, 75, 0, 0, 84, 85, 0, 0, 0, 0, 0, 93, 0, 85, 70, 0, 0, 76, 77, 78, 79, 0, 97, 0, 0, 0, 0, 0, 0, 0, 71, 0, 0, 0, 101, 73, 102, 0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 81, 0, 0, 82, 83, 70, 0, 0, 0, 0, 0, 74, 75, 0, 0, 84, 0, 0, 106, 0, 0, 71, 0, 0, 85, 72, 73, 0, 76, 77, 78, 79, 0, 0, 0, 199, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 0, 74, 75, 0, 80, 0, 0, 0, 0, 0, 81, 71, 0, 116, 117, 72, 73, 0, 76, 77, 78, 79, 0, 0, 0, 84, 121, 0, 0, 0, 0, 0, 0, 0, 85, 0, 0, 0, 0, 74, 75, 0, 80, 0, 0, 0, 0, 0, 81, 0, 0, 82, 83, 0, 70, 0, 76, 77, 78, 79, 96, 0, 70, 84, 0, 0, 0, 0, 99, 0, 71, 0, 85, 0, 72, 73, 0, 0, 71, 0, 80, 0, 72, 73, 0, 0, 81, 0, 0, 82, 83, 0, 0, 0, 0, 0, 258, 0, 74, 75, 0, 84, 0, 0, 105, 0, 74, 75, 0, 0, 85, 107, 0, 106, 0, 76, 77, 78, 79, 0, 70, 0, 396, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 112, 0, 80, 72, 73, 0, 0, 0, 81, 70, 80, 82, 83, 118, 119, 0, 81, 0, 0, 82, 83, 0, 0, 397, 0, 71, 0, 74, 75, 72, 73, 84, 85, 0, 0, 0, 0, 0, 0, 0, 85, 0, 0, 0, 76, 77, 78, 79, 0, 0, 0, 0, 0, 74, 75, 0, 0, 0, 0, 656, 0, 0, 0, 0, 0, 0, 0, 70, 80, 0, 76, 77, 78, 79, 81, 70, 0, 82, 83, 0, 0, 0, 0, 71, 0, 657, 429, 72, 73, 84, 0, 71, 0, 0, 80, 72, 73, 0, 85, 0, 81, 302, 0, 82, 83, 0, 0, 0, 0, 0, 0, 74, 75, 0, 0, 84, 0, 0, 546, 74, 75, 0, 0, 0, 85, 0, 0, 0, 76, 77, 78, 79, 0, 70, 0, 0, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 71, 0, 0, 80, 72, 73, 0, 0, 0, 81, 0, 80, 82, 83, 0, 0, 0, 81, 0, 0, 82, 83, 0, 70, 84, 0, 0, 0, 74, 75, 0, 0, 84, 85, 0, 0, 0, 0, 0, 71, 0, 85, 604, 72, 73, 76, 77, 78, 79, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 556, 0, 0, 0, 74, 75, 80, 0, 0, 0, 0, 0, 81, 0, 71, 82, 83, 608, 72, 73, 0, 76, 77, 78, 79, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, 85, 18, 0, 0, 0, 0, 74, 75, 0, 80, 0, 0, 0, 0, 0, 81, 0, 18, 82, 83, 0, 18, 18, 76, 77, 78, 79, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 70, 85, 0, 0, 0, 0, 0, 18, 18, 80, 0, 18, 0, 0, 0, 81, 71, 0, 82, 83, 72, 73, 0, 0, 18, 18, 18, 18, 0, 0, 84, 0, 0, 0, 0, 18, 0, 0, 0, 85, 0, 0, 0, 0, 74, 75, 0, 0, 18, 0, 0, 18, 0, 0, 18, 18, 18, 18, 18, 0, 0, 76, 77, 78, 79, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 18, 18, 18, 0, 0, 0, 0, 80, 0, 0, 0, 0, 0, 81, 0, 0, 82, 83, 18, 18, 18, 18, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, 85, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 18, 0, 0, 18, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 18 };
  }
  
  static void yycheck()
  {
    yycheck = new short[] { 2, 68, 69, 0, 0, 7, 8, 12, 68, 69, 289, 0, 44, 333, 44, 41, 653, 13, 43, 41, 45, 0, 217, 0, 0, 10, 11, 44, 307, 40, 44, 14, 288, 0, 44, 0, 43, 0, 45, 58, 126, 42, 294, 45, 63, 214, 215, 44, 330, 331, 52, 53, 41, 55, 621, 40, 41, 63, 43, 0, 126, 46, 484, 48, 49, 67, 0, 294, 295, 43, 213, 45, 40, 330, 41, 60, 41, 44, 41, 40, 0, 44, 330, 40, 273, 294, 41, 41, 43, 43, 45, 45, 10, 11, 128, 314, 329, 292, 267, 0, 41, 271, 40, 0, 106, 40, 298, 41, 46, 0, 44, 46, 43, 0, 45, 40, 263, 286, 351, 288, 358, 41, 40, 41, 381, 43, 378, 45, 46, 47, 48, 49, 452, 381, 567, 298, 203, 204, 298, 358, 41, 288, 60, 44, 41, 43, 44, 45, 0, 0, 41, 378, 218, 44, 41, 303, 476, 125, 377, 40, 40, 350, 267, 318, 125, 0, 733, 734, 805, 378, 352, 205, 40, 429, 176, 367, 339, 347, 180, 306, 602, 0, 325, 41, 259, 43, 252, 45, 0, 41, 41, 343, 374, 44, 452, 41, 192, 43, 287, 45, 0, 43, 44, 45, 367, 190, 41, 367, 313, 44, 128, 129, 195, 0, 216, 336, 41, 292, 43, 221, 45, 310, 41, 225, 125, 227, 373, 310, 44, 41, 232, 233, 234, 235, 338, 237, 238, 239, 240, 672, 329, 41, 675, 245, 246, 247, 329, 567, 166, 335, 43, 44, 45, 278, 41, 359, 252, 44, 259, 0, 429, 271, 351, 269, 269, 329, 268, 185, 351, 335, 439, 278, 190, 275, 594, 43, 298, 45, 598, 197, 337, 450, 451, 43, 40, 45, 310, 351, 342, 458, 297, 292, 574, 42, 550, 213, 214, 215, 47, 265, 41, 46, 259, 44, 278, 329, 336, 393, 394, 298, 295, 307, 338, 456, 368, 365, 318, 302, 307, 298, 309, 298, 298, 278, 278, 291, 311, 351, 330, 331, 43, 298, 45, 298, 259, 298, 41, 347, 303, 44, 258, 352, 309, 40, 307, 367, 309, 278, 380, 267, 339, 353, 672, 378, 373, 675, 41, 298, 527, 44, 278, 681, 379, 374, 298, 379, 686, 353, 286, 435, 288, 289, 339, 291, 339, 309, 339, 295, 367, 465, 278, 550, 379, 284, 302, 359, 68, 69, 367, 307, 367, 367, 332, 311, 334, 352, 462, 298, 339, 465, 367, 298, 367, 304, 367, 339, 307, 325, 309, 289, 307, 298, 309, 43, 315, 45, 379, 374, 40, 320, 278, 423, 323, 681, 10, 11, 367, 352, 686, 353, 265, 433, 278, 367, 40, 336, 278, 338, 339, 40, 40, 332, 339, 334, 274, 765, 298, 41, 349, 374, 770, 40, 339, 278, 40, 41, 291, 43, 359, 45, 46, 47, 48, 49, 633, 744, 367, 472, 40, 465, 367, 352, 352, 364, 60, 0, 755, 482, 379, 298, 367, 332, 0, 334, 352, 278, 298, 339, 406, 41, 309, 40, 44, 374, 374, 413, 264, 309, 298, 324, 41, 567, 671, 44, 124, 125, 374, 765, 333, 40, 278, 429, 770, 431, 248, 367, 41, 40, 278, 44, 339, 439, 588, 41, 442, 40, 44, 339, 125, 43, 44, 45, 450, 451, 452, 453, 270, 271, 456, 339, 458, 358, 128, 129, 40, 547, 548, 549, 367, 44, 41, 368, 43, 40, 45, 367, 558, 298, 727, 41, 377, 43, 40, 45, 324, 561, 307, 367, 309, 554, 572, 573, 574, 333, 492, 41, 40, 40, 44, 497, 166, 40, 40, 585, 586, 41, 653, 43, 593, 45, 40, 125, 41, 653, 43, 40, 45, 663, 339, 185, 125, 41, 296, 43, 190, 45, 672, 125, 332, 527, 334, 529, 269, 615, 616, 617, 618, 290, 620, 621, 622, 816, 268, 258, 0, 820, 367, 213, 214, 215, 40, 40, 550, 170, 264, 172, 554, 41, 379, 43, 287, 45, 314, 316, 813, 563, 287, 379, 278, 567, 568, 264, 265, 266, 41, 850, 43, 41, 45, 43, 336, 45, 41, 310, 43, 41, 45, 40, 44, 310, 301, 41, 258, 346, 44, 677, 672, 336, 680, 352, 682, 267, 329, 330, 493, 317, 495, 284, 329, 694, 41, 366, 278, 44, 287, 53, 369, 55, 371, 366, 286, 41, 288, 289, 351, 291, 41, 304, 41, 295, 351, 44, 339, 44, 125, 633, 302, 310, 315, 637, 125, 307, 46, 320, 40, 311, 323, 729, 730, 731, 732, 733, 734, 735, 381, 653, 329, 330, 805, 325, 264, 41, 279, 280, 44, 805, 283, 284, 666, 125, 123, 374, 349, 671, 672, 364, 303, 675, 351, 284, 44, 679, 764, 681, 278, 46, 284, 296, 686, 41, 775, 41, 44, 298, 44, 95, 40, 261, 780, 304, 298, 783, 307, 785, 309, 295, 304, 278, 381, 307, 315, 309, 41, 44, 41, 320, 278, 315, 323, 44, 41, 41, 320, 44, 44, 323, 126, 809, 44, 727, 232, 336, 41, 235, 339, 237, 41, 41, 336, 284, 406, 339, 278, 245, 349, 41, 744, 413, 746, 278, 41, 349, 834, 44, 836, 837, 41, 755, 278, 304, 44, 41, 367, 429, 44, 431, 268, 765, 41, 367, 315, 44, 770, 439, 379, 320, 442, 41, 323, 41, 44, 379, 44, 41, 450, 451, 452, 41, 41, 40, 456, 336, 458, 41, 278, 41, 44, 197, 44, 44, 41, 44, 419, 44, 349, 41, 46, 805, 44, 426, 41, 278, 41, 266, 278, 813, 41, 217, 218, 278, 44, 352, 222, 223, 224, 101, 492, 227, 284, 282, 336, 497, 232, 286, 287, 235, 236, 237, 338, 41, 116, 117, 298, 41, 842, 245, 44, 40, 304, 303, 284, 307, 252, 309, 295, 40, 40, 310, 311, 315, 259, 527, 41, 529, 320, 44, 46, 323, 268, 46, 304, 44, 41, 41, 327, 328, 329, 330, 40, 264, 336, 315, 282, 339, 550, 41, 320, 46, 554, 323, 264, 290, 292, 349, 261, 334, 0, 563, 351, 352, 373, 567, 568, 364, 357, 373, 364, 360, 361, 364, 46, 367, 364, 284, 321, 349, 364, 316, 364, 372, 334, 374, 198, 379, 200, 201, 40, 40, 381, 306, 41, 125, 332, 304, 334, 335, 40, 41, 42, 43, 44, 45, 46, 47, 315, 40, 259, 346, 307, 320, 320, 351, 323, 352, 354, 231, 340, 783, 340, 785, 320, 340, 284, 261, 41, 336, 366, 633, 330, 364, 369, 637, 371, 351, 374, 375, 364, 290, 349, 379, 380, 381, 304, 364, 260, 385, 386, 653, 364, 337, 267, 46, 309, 315, 268, 40, 44, 41, 320, 0, 666, 323, 41, 316, 373, 671, 672, 373, 834, 675, 836, 837, 0, 679, 414, 681, 0, 314, 336, 296, 686, 125, 267, 295, 274, 264, 41, 349, 305, 125, 44, 308, 41, 346, 320, 334, 313, 320, 320, 352, 41, 469, 319, 44, 805, 322, 483, 40, 0, 326, 267, 296, 573, 453, 331, 332, 369, 457, 371, 353, 305, 727, 680, 308, 586, 465, 594, 0, 313, 469, 470, 471, 255, 809, 319, 195, 258, 322, 744, 296, 746, 326, 278, 483, 484, 453, 331, 332, 305, 755, 221, 308, 215, 493, 267, 495, 313, 353, 542, 765, 500, 458, 319, 742, 770, 322, 195, 318, 41, 326, 69, 44, 512, 735, 331, 332, 732, 517, 518, 729, 647, 670, 730, 296, 731, 842, 63, 563, 191, 666, 123, 531, 305, 311, 534, 308, -1, -1, -1, 805, 313, -1, 542, -1, -1, -1, 319, 813, -1, 322, -1, -1, -1, 326, -1, -1, -1, 261, 331, 332, 264, 265, 266, 267, -1, -1, -1, -1, -1, 43, -1, 45, -1, -1, 278, -1, 842, -1, 282, -1, 284, -1, 286, 287, 58, -1, -1, 291, 588, 63, 294, 295, 296, 297, 298, -1, -1, 0, -1, 303, 304, 305, 602, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, 40, 41, 42, 43, 44, 45, 46, 47, 349, -1, 351, -1, 123, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, 266, -1, -1, 372, -1, -1, -1, -1, 266, 378, 379, -1, 381, -1, -1, -1, 282, -1, -1, -1, 286, 287, -1, -1, 282, 690, -1, -1, 286, 287, -1, -1, 298, -1, -1, -1, 0, 303, -1, -1, -1, -1, -1, -1, 310, 311, 503, 504, 505, 506, 507, -1, 310, 311, -1, -1, -1, 125, -1, 724, 725, 327, 328, 329, 330, -1, -1, -1, 266, 327, 328, 329, 330, 339, -1, -1, 40, 41, 42, 43, 44, 45, 46, 47, 282, 351, -1, -1, 286, 287, -1, 357, -1, 351, 360, 361, -1, -1, -1, 357, 298, 367, 360, 361, -1, 303, 372, -1, 773, -1, -1, -1, 310, 311, 372, 381, -1, -1, -1, -1, -1, -1, -1, 381, 263, -1, -1, -1, -1, 327, 328, 329, 330, -1, -1, 274, -1, -1, -1, -1, -1, 339, -1, -1, -1, -1, -1, 286, -1, 288, 815, 816, -1, 351, -1, 820, -1, -1, -1, 357, 0, 125, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, 317, -1, -1, -1, -1, 381, -1, 850, 643, 644, 645, 646, 647, 648, 857, 261, -1, 334, 264, 265, 266, 267, 40, 41, 42, 43, 44, 45, 46, 47, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, 360, 361, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, 372, 373, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, 125, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, 261, -1, 367, 264, 265, 266, 267, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, 0, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 40, 41, 42, 43, 44, 45, 46, 47, -1, 349, -1, 351, -1, 216, -1, -1, -1, 357, -1, 359, 360, 361, 225, -1, -1, -1, -1, 367, -1, -1, 233, 234, 372, -1, -1, 238, 239, 240, 378, 379, -1, 381, -1, 246, 247, 261, -1, -1, 264, 265, 266, 267, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, 125, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, 267, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, 46, 47, -1, -1, 349, -1, 351, -1, -1, 296, -1, -1, 357, -1, 359, 360, 361, -1, 305, -1, -1, 308, 367, -1, -1, -1, 313, 372, -1, -1, -1, -1, 319, 378, 379, 322, 381, -1, -1, 326, -1, -1, -1, -1, 331, 332, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, 42, 43, 44, 45, 46, 47, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, 0, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, 42, 43, 44, 45, 46, 47, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, 261, -1, 367, 264, 265, 266, 267, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, 46, 47, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, 125, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, 46, 47, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, 42, 43, 44, 45, 46, 47, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, 0, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, 42, 43, 44, 45, -1, 47, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, 261, -1, 367, 264, 265, 266, 267, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, -1, 47, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, 125, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, -1, 47, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, 364, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, 42, 43, 44, 45, -1, 47, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, 0, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, -1, 43, 44, 45, -1, -1, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 125, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, 261, -1, 367, 264, 265, 266, 267, 372, -1, -1, -1, -1, 0, 378, 379, -1, 381, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, 41, 315, 43, 44, 45, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, -1, -1, -1, -1, -1, 0, -1, 125, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, 41, 315, 43, 44, 45, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, 0, 378, 379, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 125, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 261, -1, -1, 264, 265, 266, 267, 41, -1, 43, 44, 45, -1, -1, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 125, 0, -1, -1, -1, 357, -1, 359, 360, 361, -1, -1, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 261, -1, -1, 264, 265, 266, 267, -1, -1, 40, 41, -1, -1, 44, -1, -1, -1, 278, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, -1, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, -1, -1, -1, -1, 0, 349, -1, 351, -1, -1, -1, 125, -1, 357, -1, 359, 360, 361, -1, -1, -1, 261, -1, 367, 264, 265, 266, 267, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, 278, -1, -1, -1, 282, -1, 284, 41, 286, 287, 44, -1, -1, 291, -1, -1, 294, 295, 296, 297, 298, 0, -1, -1, -1, 303, 304, 305, -1, 307, 308, 309, 310, 311, -1, 313, -1, 315, -1, -1, -1, 319, 320, -1, 322, 323, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, -1, -1, 336, -1, 338, 339, 41, 42, 43, 44, 45, -1, 47, -1, -1, 349, -1, 351, -1, -1, -1, -1, -1, 357, -1, 359, 360, 361, -1, 0, -1, -1, -1, 367, -1, 125, 0, -1, 372, -1, -1, -1, -1, -1, 378, 379, -1, 381, -1, -1, 258, -1, -1, -1, -1, -1, 264, -1, 266, -1, -1, -1, -1, 271, -1, -1, -1, -1, -1, -1, 41, -1, -1, 44, 282, -1, 284, 41, 286, 287, 44, 289, -1, -1, -1, -1, -1, -1, -1, -1, 298, -1, -1, -1, -1, 303, 304, -1, -1, 307, -1, 309, 310, 311, 40, -1, -1, 315, -1, -1, -1, -1, 320, -1, -1, 323, 0, -1, -1, 327, 328, 329, 330, -1, -1, -1, -1, -1, 336, -1, -1, 339, -1, -1, -1, -1, -1, -1, -1, 347, -1, 349, -1, 351, 352, 353, -1, -1, -1, 357, -1, -1, 360, 361, 125, -1, 364, 41, -1, 367, 44, 125, 0, -1, 372, -1, 374, -1, -1, -1, -1, 379, -1, 381, -1, -1, 266, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 123, -1, -1, -1, -1, 282, -1, 284, -1, 286, 287, -1, -1, -1, -1, -1, 41, -1, -1, 44, -1, 298, -1, -1, -1, -1, -1, 304, -1, -1, 307, -1, 309, 310, 311, -1, -1, -1, 315, -1, -1, -1, 264, 320, 266, -1, 323, -1, -1, -1, 327, 328, 329, 330, 125, -1, 278, -1, -1, 336, 282, -1, 339, -1, 286, 287, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, 298, -1, -1, -1, 357, 303, -1, 360, 361, -1, -1, -1, 310, 311, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, 125, -1, 379, 261, 381, 327, 328, 329, 330, 40, 261, -1, 43, -1, 45, -1, -1, 339, -1, -1, -1, -1, -1, -1, -1, -1, 284, 58, -1, 351, -1, -1, 63, 284, -1, 357, -1, 260, 360, 361, 298, -1, -1, -1, -1, 367, 304, 298, -1, 307, 372, 309, -1, 304, -1, -1, 307, 315, 309, 381, 283, -1, 320, -1, 315, 323, -1, 290, -1, 320, 293, -1, 323, -1, -1, -1, -1, -1, 336, -1, 338, 339, -1, 306, -1, 336, -1, 338, 339, -1, 261, 349, 123, 316, -1, -1, -1, -1, 349, -1, -1, 359, -1, -1, -1, -1, -1, -1, 359, 367, -1, -1, -1, 284, -1, -1, 367, -1, -1, -1, -1, 379, -1, -1, -1, 348, -1, 298, 379, 352, 353, -1, 40, 304, -1, 43, 307, 45, 309, -1, -1, -1, -1, -1, 315, -1, 369, 370, -1, 320, 58, 374, 323, -1, -1, 63, -1, -1, -1, 284, -1, -1, -1, -1, -1, 336, -1, 338, 339, -1, -1, -1, -1, 298, -1, -1, -1, -1, 349, 304, -1, -1, 307, -1, 309, -1, -1, -1, 359, -1, 315, -1, -1, -1, 46, 320, 367, -1, 323, -1, -1, -1, -1, -1, -1, -1, -1, -1, 379, -1, -1, 336, -1, 338, 339, 123, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, -1, -1, -1, -1, -1, -1, 263, -1, 359, 266, -1, -1, -1, 270, -1, 272, 367, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, 379, 286, 287, 288, 289, -1, -1, -1, -1, -1, 40, -1, -1, 43, 299, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, 310, 311, -1, 58, -1, -1, 316, 317, 63, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, 332, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 352, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 374, 375, 376, -1, 123, -1, -1, 381, 259, -1, -1, 262, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, 266, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, 282, -1, -1, -1, 286, 287, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, 354, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, 310, 311, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, -1, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, 351, 276, -1, -1, -1, 280, 357, 282, -1, 360, 361, 286, 287, 288, 289, -1, -1, -1, -1, -1, 40, 372, -1, 43, -1, 45, -1, -1, -1, -1, 381, -1, -1, -1, -1, 310, 311, -1, 58, -1, -1, 316, 317, 63, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 352, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 374, 375, 376, -1, 123, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, -1, -1, -1, -1, 40, -1, -1, 43, 299, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, 310, 311, -1, 58, -1, -1, 316, 317, 63, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, 332, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, -1, 375, 376, -1, 123, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, -1, -1, -1, -1, 40, 41, -1, 43, 44, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, 310, 311, -1, 58, -1, -1, 316, 317, 63, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, 352, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 374, 375, 376, -1, 123, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, -1, -1, -1, -1, 40, -1, 42, 43, 299, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, 310, 311, -1, 58, -1, -1, 316, 317, 63, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, -1, 375, 376, -1, 123, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, 365, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, 40, -1, 42, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, 378, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, 40, -1, 42, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 289, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, 40, -1, -1, 43, -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 310, 311, -1, -1, 63, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, -1, -1, -1, -1, -1, 372, 373, 123, 375, 376, -1, -1, -1, -1, 381, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, 40, -1, 286, 287, 288, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, -1, -1, -1, -1, 63, -1, -1, 310, 311, -1, -1, -1, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, -1, -1, 123, -1, -1, -1, -1, 372, 373, -1, 375, 376, -1, -1, -1, -1, 381, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, 40, 282, -1, -1, -1, 286, 287, 288, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, -1, -1, -1, -1, 63, -1, -1, -1, -1, -1, 310, 311, -1, -1, -1, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, 123, -1, -1, -1, -1, -1, -1, -1, 372, 373, -1, 375, 376, -1, -1, -1, -1, 381, -1, -1, -1, -1, -1, -1, 263, -1, -1, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, 41, 42, 43, 44, 45, -1, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 310, 311, -1, -1, -1, -1, 316, 317, -1, -1, -1, -1, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, 42, -1, 360, 361, 362, 363, -1, -1, 42, -1, -1, -1, 46, -1, 372, 373, -1, 375, 376, -1, -1, 263, -1, 381, 266, -1, -1, -1, 270, -1, 272, -1, 274, -1, 276, -1, -1, -1, 280, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 310, 311, -1, -1, -1, -1, 316, 317, -1, -1, -1, 266, -1, 323, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, 334, 335, -1, 282, -1, -1, -1, 286, 287, -1, -1, -1, -1, -1, -1, 349, -1, 351, -1, -1, -1, 355, 356, 357, -1, -1, 360, 361, 362, 363, -1, 310, 311, 43, -1, 45, -1, -1, 372, 373, -1, 375, 376, -1, -1, -1, -1, 381, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 264, -1, 266, 267, -1, -1, -1, -1, -1, -1, 351, 352, 46, -1, 278, -1, 357, -1, 282, 360, 361, -1, 286, 287, -1, -1, -1, -1, -1, -1, -1, 372, 296, 374, 298, -1, -1, -1, -1, -1, 381, 305, -1, -1, 308, -1, 310, 311, -1, 313, 46, 123, -1, -1, -1, 319, -1, -1, 322, -1, 324, -1, 326, 327, 328, 329, 330, 331, 332, 333, 266, -1, -1, -1, -1, -1, -1, -1, 266, -1, -1, -1, -1, -1, -1, -1, 282, 351, -1, -1, 286, 287, -1, 357, 282, -1, 360, 361, 286, 287, -1, -1, -1, 367, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, 310, 311, -1, 381, -1, -1, -1, -1, 310, 311, -1, -1, -1, -1, -1, -1, -1, 327, 328, 329, 330, -1, 266, -1, -1, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, -1, 282, -1, -1, 351, 286, 287, -1, -1, -1, 357, -1, 351, 360, 361, -1, -1, -1, 357, -1, -1, 360, 361, -1, -1, 372, -1, -1, -1, 310, 311, -1, -1, 372, 381, -1, -1, -1, -1, -1, 263, -1, 381, 266, -1, -1, 327, 328, 329, 330, -1, 274, -1, -1, -1, -1, -1, -1, -1, 282, -1, -1, -1, 286, 287, 288, -1, -1, -1, -1, 351, -1, -1, -1, -1, -1, 357, -1, -1, 360, 361, 266, -1, -1, -1, -1, -1, 310, 311, -1, -1, 372, -1, -1, 317, -1, -1, 282, -1, -1, 381, 286, 287, -1, 327, 328, 329, 330, -1, -1, -1, 334, -1, -1, -1, -1, -1, -1, -1, 266, -1, -1, -1, -1, -1, 310, 311, -1, 351, -1, -1, -1, -1, -1, 357, 282, -1, 360, 361, 286, 287, -1, 327, 328, 329, 330, -1, -1, -1, 372, 373, -1, -1, -1, -1, -1, -1, -1, 381, -1, -1, -1, -1, 310, 311, -1, 351, -1, -1, -1, -1, -1, 357, -1, -1, 360, 361, -1, 266, -1, 327, 328, 329, 330, 272, -1, 266, 372, -1, -1, -1, -1, 280, -1, 282, -1, 381, -1, 286, 287, -1, -1, 282, -1, 351, -1, 286, 287, -1, -1, 357, -1, -1, 360, 361, -1, -1, -1, -1, -1, 300, -1, 310, 311, -1, 372, -1, -1, 316, -1, 310, 311, -1, -1, 381, 323, -1, 317, -1, 327, 328, 329, 330, -1, 266, -1, 334, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, -1, 282, 349, -1, 351, 286, 287, -1, -1, -1, 357, 266, 351, 360, 361, 362, 363, -1, 357, -1, -1, 360, 361, -1, -1, 372, -1, 282, -1, 310, 311, 286, 287, 372, 381, -1, -1, -1, -1, -1, -1, -1, 381, -1, -1, -1, 327, 328, 329, 330, -1, -1, -1, -1, -1, 310, 311, -1, -1, -1, -1, 342, -1, -1, -1, -1, -1, -1, -1, 266, 351, -1, 327, 328, 329, 330, 357, 266, -1, 360, 361, -1, -1, -1, -1, 282, -1, 368, 277, 286, 287, 372, -1, 282, -1, -1, 351, 286, 287, -1, 381, -1, 357, 358, -1, 360, 361, -1, -1, -1, -1, -1, -1, 310, 311, -1, -1, 372, -1, -1, 317, 310, 311, -1, -1, -1, 381, -1, -1, -1, 327, 328, 329, 330, -1, 266, -1, -1, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, -1, 282, -1, -1, 351, 286, 287, -1, -1, -1, 357, -1, 351, 360, 361, -1, -1, -1, 357, -1, -1, 360, 361, -1, 266, 372, -1, -1, -1, 310, 311, -1, -1, 372, 381, -1, -1, -1, -1, -1, 282, -1, 381, 285, 286, 287, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 266, -1, 345, -1, -1, -1, 310, 311, 351, -1, -1, -1, -1, -1, 357, -1, 282, 360, 361, 285, 286, 287, -1, 327, 328, 329, 330, -1, -1, 372, -1, -1, -1, -1, -1, -1, -1, -1, 381, 266, -1, -1, -1, -1, 310, 311, -1, 351, -1, -1, -1, -1, -1, 357, -1, 282, 360, 361, -1, 286, 287, 327, 328, 329, 330, -1, -1, -1, 372, -1, -1, -1, -1, -1, -1, -1, 266, 381, -1, -1, -1, -1, -1, 310, 311, 351, -1, 314, -1, -1, -1, 357, 282, -1, 360, 361, 286, 287, -1, -1, 327, 328, 329, 330, -1, -1, 372, -1, -1, -1, -1, 266, -1, -1, -1, 381, -1, -1, -1, -1, 310, 311, -1, -1, 351, -1, -1, 282, -1, -1, 357, 286, 287, 360, 361, -1, -1, 327, 328, 329, 330, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, -1, -1, -1, 381, 310, 311, -1, -1, -1, -1, 351, -1, -1, -1, -1, -1, 357, -1, -1, 360, 361, 327, 328, 329, 330, -1, -1, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, -1, -1, -1, 381, -1, -1, -1, -1, 351, -1, -1, -1, -1, -1, 357, -1, -1, 360, 361, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 372, -1, -1, -1, -1, -1, -1, -1, -1, 381 };
  }
  
  public Parser(String paramString, IPTLimitChecker paramIPTLimitChecker)
  {
    this.m_queryString = paramString;
    this.m_limitChecker = paramIPTLimitChecker;
    this.m_nodeFactory = new PTNodeFactory();
    this.lexer = new Lexer(new StringReader(paramString), this);
    this.m_rootNode = null;
  }
  
  private int yylex()
  {
    int i = -1;
    try
    {
      this.yylval = new ParserVal();
      i = this.lexer.yylex();
    }
    catch (IOException localIOException)
    {
      i = -1;
    }
    catch (Error localError)
    {
      i = -1;
    }
    return i;
  }
  
  public void yyerror(String paramString)
    throws SQLEngineException
  {
    Lexer.TokenInfo localTokenInfo = this.lexer.getCurrentToken();
    throw PTParseErrorInfo.makeSyntaxError(this.m_queryString, localTokenInfo.getLastColumn());
  }
  
  public IPTNode parse()
    throws SQLEngineException
  {
    yyparse();
    return this.m_rootNode;
  }
  
  private void checkIdentifier(PTIdentifierNode paramPTIdentifierNode, PTStringConstraint paramPTStringConstraint)
    throws SQLEngineException
  {
    this.m_limitChecker.checkString(paramPTStringConstraint, paramPTIdentifierNode.getIdentifier());
  }
  
  private void checkIdentifier(String paramString, PTStringConstraint paramPTStringConstraint)
    throws SQLEngineException
  {
    this.m_limitChecker.checkString(paramPTStringConstraint, paramString);
  }
  
  private void checkLength(PTListNode paramPTListNode, PTCountLimit paramPTCountLimit)
    throws SQLEngineException
  {
    this.m_limitChecker.checkCount(paramPTCountLimit, paramPTListNode.numChildren());
  }
  
  private static void invalidSqlType(String paramString1, String paramString2, int paramInt)
    throws SQLEngineException
  {
    throw PTParseErrorInfo.makeInvalidSqlTypeError(paramString1, paramString2, paramInt);
  }
  
  private static void notImplemented()
    throws SQLEngineException
  {
    throw new SQLEngineException(DiagState.DIAG_SYNTAX_ERR_OR_ACCESS_VIOLATION, SQLEngineMessageKey.SYNTAX_NOT_SUPPORTED.name());
  }
  
  void yylexdebug(int paramInt1, int paramInt2)
  {
    String str = null;
    if (paramInt2 < 0) {
      paramInt2 = 0;
    }
    if (paramInt2 <= 384) {
      str = yyname[paramInt2];
    }
    if (str == null) {
      str = "illegal-symbol";
    }
    debug("state " + paramInt1 + ", reading " + paramInt2 + " (" + str + ")");
  }
  
  int yyparse()
    throws SQLEngineException
  {
    init_stacks();
    this.yynerrs = 0;
    this.yyerrflag = 0;
    this.yychar = -1;
    this.yystate = 0;
    state_push(this.yystate);
    val_push(this.yylval);
    for (;;)
    {
      int i = 1;
      if (this.yydebug) {
        debug("loop");
      }
      for (this.yyn = yydefred[this.yystate]; this.yyn == 0; this.yyn = yydefred[this.yystate])
      {
        if (this.yydebug) {
          debug("yyn:" + this.yyn + "  state:" + this.yystate + "  yychar:" + this.yychar);
        }
        if (this.yychar < 0)
        {
          this.yychar = yylex();
          if (this.yydebug) {
            debug(" next yychar:" + this.yychar);
          }
          if (this.yychar < 0)
          {
            this.yychar = 0;
            if (this.yydebug) {
              yylexdebug(this.yystate, this.yychar);
            }
          }
        }
        this.yyn = yysindex[this.yystate];
        if ((this.yyn != 0) && (this.yyn += this.yychar >= 0) && (this.yyn <= 7423) && (yycheck[this.yyn] == this.yychar))
        {
          if (this.yydebug) {
            debug("state " + this.yystate + ", shifting to state " + yytable[this.yyn]);
          }
          this.yystate = yytable[this.yyn];
          state_push(this.yystate);
          val_push(this.yylval);
          this.yychar = -1;
          if (this.yyerrflag > 0) {
            this.yyerrflag -= 1;
          }
          i = 0;
          break;
        }
        this.yyn = yyrindex[this.yystate];
        if ((this.yyn != 0) && (this.yyn += this.yychar >= 0) && (this.yyn <= 7423) && (yycheck[this.yyn] == this.yychar))
        {
          if (this.yydebug) {
            debug("reduce");
          }
          this.yyn = yytable[this.yyn];
          i = 1;
          break;
        }
        if (this.yyerrflag == 0)
        {
          yyerror("syntax error");
          this.yynerrs += 1;
        }
        if (this.yyerrflag < 3)
        {
          this.yyerrflag = 3;
          for (;;)
          {
            if (this.stateptr < 0)
            {
              yyerror("stack underflow. aborting...");
              return 1;
            }
            this.yyn = yysindex[state_peek(0)];
            if ((this.yyn != 0) && (this.yyn += 256 >= 0) && (this.yyn <= 7423) && (yycheck[this.yyn] == 256))
            {
              if (this.yydebug) {
                debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[this.yyn] + " ");
              }
              this.yystate = yytable[this.yyn];
              state_push(this.yystate);
              val_push(this.yylval);
              i = 0;
              break;
            }
            if (this.yydebug) {
              debug("error recovery discarding state " + state_peek(0) + " ");
            }
            if (this.stateptr < 0)
            {
              yyerror("Stack underflow. aborting...");
              return 1;
            }
            state_pop();
            val_pop();
          }
        }
        if (this.yychar == 0) {
          return 1;
        }
        if (this.yydebug)
        {
          this.yys = null;
          if (this.yychar <= 384) {
            this.yys = yyname[this.yychar];
          }
          if (this.yys == null) {
            this.yys = "illegal-symbol";
          }
          debug("state " + this.yystate + ", error recovery discards token " + this.yychar + " (" + this.yys + ")");
        }
        this.yychar = -1;
      }
      if (i != 0)
      {
        this.yym = yylen[this.yyn];
        if (this.yydebug) {
          debug("state " + this.yystate + ", reducing " + this.yym + " by rule " + this.yyn + " (" + yyrule[this.yyn] + ")");
        }
        if (this.yym > 0) {
          this.yyval = val_peek(this.yym - 1);
        }
        this.yyval = dup_yyval(this.yyval);
        Object localObject1;
        Object localObject2;
        switch (this.yyn)
        {
        case 1: 
          this.yyval.obj = (this.m_rootNode = (IPTNode)val_peek(0).obj);
          break;
        case 2: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 3: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 4: 
          notImplemented();
          break;
        case 5: 
          notImplemented();
          break;
        case 6: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 7: 
          notImplemented();
          break;
        case 8: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 9: 
          notImplemented();
          break;
        case 10: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 11: 
          notImplemented();
          break;
        case 12: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 13: 
          notImplemented();
          break;
        case 14: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 15: 
          notImplemented();
          break;
        case 16: 
          notImplemented();
          break;
        case 17: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 18: 
          this.yyval.obj = this.m_nodeFactory.buildEmptyNode();
          break;
        case 19: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.m_limitChecker.checkString(PTStringConstraint.RESERVED_KEYWORD, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 20: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 21: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 22: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 23: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 24: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 25: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 26: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 27: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 28: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 29: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 30: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 31: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 32: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 33: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 34: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 35: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 36: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 37: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TOP_LEVEL_SELECT_STATEMENT, new Object[] { PTPositionalType.SELECT, (IPTNode)val_peek(1).obj, PTPositionalType.ORDER_BY, val_peek(0).obj });
          break;
        case 38: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.ORDER_BY, new Object[] { PTPositionalType.SORT_SPEC_LIST, (IPTNode)val_peek(0).obj });
          break;
        case 39: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.SORT_SPECIFICATION_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 40: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          checkLength((PTListNode)val_peek(2).obj, PTCountLimit.COLUMNS_IN_ORDER_BY);
          break;
        case 41: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SORT_SPECIFICATION, new Object[] { PTPositionalType.SORT_KEY, (IPTNode)val_peek(1).obj, PTPositionalType.ORDER_SPEC, val_peek(0).obj });
          break;
        case 42: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 43: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.ASC);
          break;
        case 44: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.DESC);
          break;
        case 46: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 47: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 48: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 49: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 50: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UNION, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 51: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UNION_ALL, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(3).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 52: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.EXCEPT, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 53: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.EXCEPT_ALL, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(3).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 54: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UNION, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(4).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj });
          break;
        case 55: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UNION_ALL, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(5).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj });
          break;
        case 56: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.EXCEPT, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(4).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj });
          break;
        case 57: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.EXCEPT_ALL, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(5).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj });
          break;
        case 58: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 59: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 60: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 61: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 62: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 63: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.TABLE_VALUE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 64: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 65: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.ROW_VALUE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 66: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 67: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.ROW_VALUE_LIST, new IPTNode[] { this.m_nodeFactory.buildFlagNode(PTFlagType.DEFAULT) });
          break;
        case 68: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.ROW_VALUE_LIST, new IPTNode[] { (IPTNode)val_peek(2).obj, (IPTNode)val_peek(0).obj });
          break;
        case 69: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 70: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 71: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.DEFAULT);
          break;
        case 72: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SUBQUERY, new Object[] { PTPositionalType.SINGLE_CHILD, val_peek(1).obj });
          break;
        case 73: 
          localObject1 = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SORTED_SELECT_STATEMENT, new Object[] { PTPositionalType.SELECT, val_peek(2).obj, PTPositionalType.ORDER_BY, val_peek(1).obj });
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SUBQUERY, new Object[] { PTPositionalType.SINGLE_CHILD, localObject1 });
          break;
        case 74: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SELECT_STATEMENT, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(7).obj, PTPositionalType.SELECT_LIMIT, val_peek(6).obj, PTPositionalType.SELECT_LIST, val_peek(5).obj, PTPositionalType.TABLE_REF_LIST, val_peek(3).obj, PTPositionalType.WHERE, val_peek(2).obj, PTPositionalType.GROUP_BY, val_peek(1).obj, PTPositionalType.HAVING, val_peek(0).obj });
          break;
        case 75: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SELECT_STATEMENT, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.SELECT_LIMIT, val_peek(1).obj, PTPositionalType.SELECT_LIST, val_peek(0).obj, PTPositionalType.TABLE_REF_LIST, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.WHERE, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.GROUP_BY, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.HAVING, this.m_nodeFactory.buildEmptyNode() });
          break;
        case 76: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SELECT_LIMIT, new Object[] { PTPositionalType.TOP_VALUE_SPEC, val_peek(0).obj });
          break;
        case 78: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 79: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 80: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 81: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.ALL);
          break;
        case 82: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.DISTINCT);
          break;
        case 84: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 85: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.SELECT_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 86: 
          localObject1 = (PTListNode)val_peek(2).obj;
          this.yyval.obj = ((PTListNode)localObject1).addChild((IPTNode)val_peek(0).obj);
          checkLength((PTListNode)localObject1, PTCountLimit.COLUMNS_IN_SELECT);
          break;
        case 87: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 88: 
          checkIdentifier((PTIdentifierNode)val_peek(6).obj, PTStringConstraint.CATALOG_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(4).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.TABLE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, (IPTNode)val_peek(6).obj, PTPositionalType.SCHEMA_IDENT, val_peek(4).obj, PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, this.m_nodeFactory.buildFlagNode(PTFlagType.STAR) });
          break;
        case 89: 
          checkIdentifier((PTIdentifierNode)val_peek(4).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.TABLE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, val_peek(4).obj, PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, this.m_nodeFactory.buildFlagNode(PTFlagType.STAR) });
          break;
        case 90: 
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.TABLE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, this.m_nodeFactory.buildFlagNode(PTFlagType.STAR) });
          break;
        case 91: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.COLUMN_NAME, this.m_nodeFactory.buildFlagNode(PTFlagType.STAR) });
          break;
        case 92: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DERIVED_COLUMN, new Object[] { PTPositionalType.VALUE_EXPRESSION, (IPTNode)val_peek(1).obj, PTPositionalType.AS, val_peek(0).obj });
          break;
        case 93: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 95: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, (IPTNode)val_peek(6).obj, PTPositionalType.SCHEMA_IDENT, val_peek(4).obj, PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, val_peek(0).obj });
          break;
        case 96: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, (IPTNode)val_peek(5).obj, PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, val_peek(0).obj });
          break;
        case 97: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, val_peek(4).obj, PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, val_peek(0).obj });
          break;
        case 98: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, val_peek(2).obj, PTPositionalType.COLUMN_NAME, val_peek(0).obj });
          break;
        case 99: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_REFERENCE, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.COLUMN_NAME, val_peek(0).obj });
          break;
        case 100: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.COLUMN_NAME_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 101: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 102: 
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.COLUMN_NAME_LEN);
          this.yyval.obj = val_peek(0).obj;
          break;
        case 103: 
          this.m_limitChecker.checkString(PTStringConstraint.IDENTIFIER_LEN, val_peek(0).sval);
          this.m_limitChecker.checkString(PTStringConstraint.RESERVED_KEYWORD, val_peek(0).sval);
          checkIdentifier(val_peek(0).sval, PTStringConstraint.COLUMN_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildIdentifierNode(val_peek(0).sval);
          break;
        case 104: 
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.COLUMN_NAME_LEN);
          this.yyval.obj = val_peek(0).obj;
          break;
        case 105: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.TABLE_REFERENCE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 106: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          checkLength((PTListNode)val_peek(2).obj, PTCountLimit.TABLES_IN_SELECT);
          break;
        case 107: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_REFERENCE, new Object[] { PTPositionalType.TABLE_NAME, (IPTNode)val_peek(2).obj, PTPositionalType.CORRELATION_IDENT, val_peek(1).obj, PTPositionalType.DERIVED_COLUMN_LIST, val_peek(0).obj });
          break;
        case 108: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_REFERENCE, new Object[] { PTPositionalType.TABLE_NAME, (IPTNode)val_peek(0).obj, PTPositionalType.CORRELATION_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.DERIVED_COLUMN_LIST, this.m_nodeFactory.buildEmptyNode() });
          break;
        case 109: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_REFERENCE, new Object[] { PTPositionalType.SUBQUERY, (IPTNode)val_peek(2).obj, PTPositionalType.CORRELATION_IDENT, val_peek(1).obj, PTPositionalType.DERIVED_COLUMN_LIST, val_peek(0).obj });
          break;
        case 110: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 111: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 112: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 113: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 114: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 116: 
          break;
        case 118: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.WHERE_CLAUSE, new Object[] { PTPositionalType.SEARCH_COND, (IPTNode)val_peek(0).obj });
          break;
        case 120: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 121: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.OR, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 122: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 123: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.AND, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 124: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 125: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.NOT, new Object[] { PTPositionalType.PREDICATE, (IPTNode)val_peek(0).obj });
          break;
        case 126: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 127: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 128: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 129: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 130: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 131: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 132: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 133: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 134: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 135: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode((PTNonterminalType)val_peek(1).obj, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 136: 
          this.yyval.obj = PTNonterminalType.EQUALS_OP;
          break;
        case 137: 
          this.yyval.obj = PTNonterminalType.NOT_EQUALS_OP;
          break;
        case 138: 
          this.yyval.obj = PTNonterminalType.LESS_THAN_OP;
          break;
        case 139: 
          this.yyval.obj = PTNonterminalType.GREATER_THAN_OP;
          break;
        case 140: 
          this.yyval.obj = PTNonterminalType.LESS_THAN_OR_EQUALS_OP;
          break;
        case 141: 
          this.yyval.obj = PTNonterminalType.GREATER_THAN_OR_EQUALS_OP;
          break;
        case 142: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.BETWEEN, new Object[] { PTPositionalType.ROW_VALUE_CONSTRUCTOR, (IPTNode)val_peek(5).obj, PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.NOT), PTPositionalType.LOWER_LIMIT, val_peek(2).obj, PTPositionalType.UPPER_LIMIT, val_peek(0).obj });
          break;
        case 143: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.BETWEEN, new Object[] { PTPositionalType.ROW_VALUE_CONSTRUCTOR, (IPTNode)val_peek(4).obj, PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.IS), PTPositionalType.LOWER_LIMIT, val_peek(2).obj, PTPositionalType.UPPER_LIMIT, val_peek(0).obj });
          break;
        case 144: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.IN, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.NOT), PTPositionalType.ROW_VALUE_CONSTRUCTOR, val_peek(3).obj, PTPositionalType.PREDICATE_VALUE, val_peek(0).obj });
          break;
        case 145: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.IN, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.IS), PTPositionalType.ROW_VALUE_CONSTRUCTOR, val_peek(2).obj, PTPositionalType.PREDICATE_VALUE, val_peek(0).obj });
          break;
        case 146: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 147: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 148: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.VALUE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 149: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 150: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.LIKE, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.NOT), PTPositionalType.BINARY_LEFT, val_peek(3).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj, PTPositionalType.ESCAPE_CHAR, val_peek(0).obj });
          break;
        case 151: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.LIKE, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.IS), PTPositionalType.BINARY_LEFT, val_peek(3).obj, PTPositionalType.BINARY_RIGHT, val_peek(1).obj, PTPositionalType.ESCAPE_CHAR, val_peek(0).obj });
          break;
        case 152: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 154: 
          this.yyval.obj = this.m_nodeFactory.buildDynamicParameterNode();
          break;
        case 155: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.NULL, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.NOT), PTPositionalType.ROW_VALUE_CONSTRUCTOR, val_peek(3).obj });
          break;
        case 156: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.NULL, new Object[] { PTPositionalType.IS_OR_IS_NOT, this.m_nodeFactory.buildFlagNode(PTFlagType.IS), PTPositionalType.ROW_VALUE_CONSTRUCTOR, val_peek(2).obj });
          break;
        case 157: 
          localObject1 = this.m_nodeFactory.buildNonterminalNode((PTNonterminalType)val_peek(2).obj);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.QUANTIFIED_COMPARISON_PREDICATE, new Object[] { PTPositionalType.ROW_VALUE_CONSTRUCTOR, (IPTNode)val_peek(3).obj, PTPositionalType.COMPARISON_OP, localObject1, PTPositionalType.QUANTIFIER, val_peek(1).obj, PTPositionalType.SUBQUERY, val_peek(0).obj });
          break;
        case 158: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.ALL);
          break;
        case 159: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.ANY);
          break;
        case 160: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.ANY);
          break;
        case 161: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.EXISTS, new Object[] { PTPositionalType.SUBQUERY, (IPTNode)val_peek(0).obj });
          break;
        case 162: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 163: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 164: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 165: 
          localObject1 = ((PTIdentifierNode)val_peek(1).obj).getIdentifier();
          localObject2 = null;
          if ("d".equalsIgnoreCase((String)localObject1))
          {
            localObject2 = PTLiteralType.DATE;
          }
          else if ("t".equalsIgnoreCase((String)localObject1))
          {
            localObject2 = PTLiteralType.TIME;
          }
          else if ("ts".equalsIgnoreCase((String)localObject1))
          {
            localObject2 = PTLiteralType.TIMESTAMP;
          }
          else
          {
            Lexer.TokenInfo localTokenInfo = this.lexer.getCurrentToken();
            throw PTParseErrorInfo.makeSyntaxError(this.m_queryString, localTokenInfo.getFirstColumn() - 2);
          }
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode((PTLiteralType)localObject2, val_peek(0).sval);
          break;
        case 166: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, (IPTNode)val_peek(3).obj, PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 167: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("CONVERT"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 168: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("CAST"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 169: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("USER"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 170: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("LEFT"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 171: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("RIGHT"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 172: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("INSERT"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 173: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("NULL"), PTPositionalType.PARAM_LIST, val_peek(1).obj });
          break;
        case 174: 
          notImplemented();
          break;
        case 175: 
          notImplemented();
          break;
        case 176: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 178: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 179: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 180: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { (IPTNode)val_peek(2).obj, (IPTNode)val_peek(0).obj });
          break;
        case 181: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { (IPTNode)val_peek(2).obj, this.m_nodeFactory.buildLiteralNode(PTLiteralType.DATATYPE, val_peek(0).sval) });
          break;
        case 182: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { (IPTNode)val_peek(2).obj, (IPTNode)val_peek(0).obj });
          break;
        case 183: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.DATATYPE, val_peek(0).sval);
          break;
        case 184: 
          localObject1 = this.lexer.getCurrentToken();
          invalidSqlType(this.m_queryString, ((PTIdentifierNode)val_peek(0).obj).getIdentifier(), ((Lexer.TokenInfo)localObject1).getLastColumn());
          break;
        case 185: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.GROUP_BY, new Object[] { PTPositionalType.GROUP_BY_EXPRESSION_LIST, (IPTNode)val_peek(0).obj });
          break;
        case 187: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          checkLength((PTListNode)val_peek(2).obj, PTCountLimit.COLUMNS_IN_GROUP_BY);
          break;
        case 188: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.GROUPBY_EXPRESSION_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 189: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.HAVING, new Object[] { PTPositionalType.SEARCH_COND, (IPTNode)val_peek(0).obj });
          break;
        case 191: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 192: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.BINARY_PLUS_SIGN, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 193: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.BINARY_MINUS_SIGN, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 194: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.CONCAT_SIGN, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 195: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 196: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.MULTIPLICATION_SIGN, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 197: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DIVISION_SIGN, new Object[] { PTPositionalType.BINARY_LEFT, (IPTNode)val_peek(2).obj, PTPositionalType.BINARY_RIGHT, val_peek(0).obj });
          break;
        case 198: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 199: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 200: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UNARY_MINUS_SIGN, new Object[] { PTPositionalType.SINGLE_CHILD, (IPTNode)val_peek(0).obj });
          break;
        case 201: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 202: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 203: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 204: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 205: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 206: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 207: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 208: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 209: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 210: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 211: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 212: 
          notImplemented();
          break;
        case 213: 
          notImplemented();
          break;
        case 214: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 215: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 216: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 217: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 218: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 219: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.APPROXNUM, val_peek(0).sval);
          break;
        case 220: 
          localObject1 = (PTLiteralNode)val_peek(0).obj;
          localObject2 = ((PTLiteralNode)localObject1).getLiteralType();
          assert ((localObject2 == PTLiteralType.USINT) || (localObject2 == PTLiteralType.APPROXNUM) || (localObject2 == PTLiteralType.DECIMAL));
          if (localObject2 == PTLiteralType.USINT) {
            localObject2 = PTLiteralType.SINT;
          }
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode((PTLiteralType)localObject2, ((PTLiteralNode)localObject1).getStringValue());
          break;
        case 221: 
          localObject1 = (PTLiteralNode)val_peek(0).obj;
          localObject2 = ((PTLiteralNode)localObject1).getLiteralType();
          assert ((localObject2 == PTLiteralType.USINT) || (localObject2 == PTLiteralType.APPROXNUM) || (localObject2 == PTLiteralType.DECIMAL));
          if (localObject2 == PTLiteralType.USINT) {
            localObject2 = PTLiteralType.SINT;
          }
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode((PTLiteralType)localObject2, "+" + ((PTLiteralNode)localObject1).getStringValue());
          break;
        case 222: 
          localObject1 = (PTLiteralNode)val_peek(0).obj;
          localObject2 = ((PTLiteralNode)localObject1).getLiteralType();
          assert ((localObject2 == PTLiteralType.USINT) || (localObject2 == PTLiteralType.APPROXNUM) || (localObject2 == PTLiteralType.DECIMAL));
          if (localObject2 == PTLiteralType.USINT) {
            localObject2 = PTLiteralType.SINT;
          }
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode((PTLiteralType)localObject2, "-" + ((PTLiteralNode)localObject1).getStringValue());
          break;
        case 223: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 224: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.DECIMAL, val_peek(0).sval);
          break;
        case 225: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.USINT, val_peek(0).sval);
          break;
        case 226: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 227: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 228: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.FUNC, new Object[] { PTPositionalType.NAME, this.m_nodeFactory.buildIdentifierNode("USER"), PTPositionalType.PARAM_LIST, this.m_nodeFactory.buildEmptyNode() });
          break;
        case 229: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.CHARSTR, val_peek(0).sval);
          break;
        case 230: 
          notImplemented();
          break;
        case 231: 
          notImplemented();
          break;
        case 232: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 233: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.NULL, "NULL");
          break;
        case 234: 
          notImplemented();
          break;
        case 235: 
          notImplemented();
          break;
        case 236: 
          notImplemented();
          break;
        case 237: 
          notImplemented();
          break;
        case 238: 
          notImplemented();
          break;
        case 239: 
          notImplemented();
          break;
        case 240: 
          notImplemented();
          break;
        case 241: 
          notImplemented();
          break;
        case 242: 
          notImplemented();
          break;
        case 243: 
          notImplemented();
          break;
        case 244: 
          notImplemented();
          break;
        case 245: 
          notImplemented();
          break;
        case 246: 
          notImplemented();
          break;
        case 247: 
          notImplemented();
          break;
        case 248: 
          notImplemented();
          break;
        case 249: 
          notImplemented();
          break;
        case 251: 
          notImplemented();
          break;
        case 253: 
          notImplemented();
          break;
        case 254: 
          notImplemented();
          break;
        case 255: 
          notImplemented();
          break;
        case 257: 
          notImplemented();
          break;
        case 259: 
          notImplemented();
          break;
        case 261: 
          notImplemented();
          break;
        case 263: 
          notImplemented();
          break;
        case 265: 
          notImplemented();
          break;
        case 267: 
          notImplemented();
          break;
        case 269: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.DATE, val_peek(0).sval);
          break;
        case 270: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.TIME, val_peek(0).sval);
          break;
        case 271: 
          this.yyval.obj = this.m_nodeFactory.buildLiteralNode(PTLiteralType.TIMESTAMP, val_peek(0).sval);
          break;
        case 272: 
          this.m_limitChecker.checkString(PTStringConstraint.CHAR_LITERAL_LEN, val_peek(0).sval);
          this.yyval.sval = val_peek(0).sval;
          break;
        case 273: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PARAMETER, new Object[] { PTPositionalType.NAME, (IPTNode)val_peek(0).obj });
          break;
        case 274: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 275: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 276: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 277: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 278: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 279: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.NULLIF, new Object[] { PTPositionalType.EXPRESSION_FST, (IPTNode)val_peek(3).obj, PTPositionalType.EXPRESSION_SND, val_peek(1).obj });
          break;
        case 280: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COALESCE, new Object[] { PTPositionalType.COALESCE_LIST, (IPTNode)val_peek(1).obj });
          break;
        case 281: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.COALESCE_LIST, new IPTNode[] { (IPTNode)val_peek(2).obj, (IPTNode)val_peek(0).obj });
          break;
        case 282: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 283: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 284: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SIMPLE_CASE, new Object[] { PTPositionalType.VALUE_EXPRESSION, (IPTNode)val_peek(3).obj, PTPositionalType.WHEN_CLAUSE_LIST, val_peek(2).obj, PTPositionalType.ELSE, val_peek(1).obj });
          break;
        case 285: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SEARCHED_CASE, new Object[] { PTPositionalType.WHEN_CLAUSE_LIST, (IPTNode)val_peek(2).obj, PTPositionalType.ELSE, val_peek(1).obj });
          break;
        case 286: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.SIMPLE_WHEN_CLAUSE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 287: 
          this.yyval.obj = ((PTListNode)val_peek(1).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 288: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.SEARCHED_WHEN_CLAUSE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 289: 
          this.yyval.obj = ((PTListNode)val_peek(1).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 290: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SIMPLE_WHEN_CLAUSE, new Object[] { PTPositionalType.VALUE_EXPRESSION, (IPTNode)val_peek(2).obj, PTPositionalType.RESULT, val_peek(0).obj });
          break;
        case 291: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SEARCHED_WHEN_CLAUSE, new Object[] { PTPositionalType.SEARCH_COND, (IPTNode)val_peek(2).obj, PTPositionalType.RESULT, val_peek(0).obj });
          break;
        case 292: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.ELSE_CLAUSE, new Object[] { PTPositionalType.RESULT, (IPTNode)val_peek(0).obj });
          break;
        case 294: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 295: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COUNT, new Object[] { PTPositionalType.STAR, this.m_nodeFactory.buildFlagNode(PTFlagType.STAR) });
          break;
        case 296: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 297: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.AVG, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 298: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.MAX, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 299: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.MIN, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 300: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.STDDEV, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 301: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.STDDEV_POP, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 302: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SUM, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 303: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.VAR, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 304: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.VAR_POP, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 305: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COUNT, new Object[] { PTPositionalType.SET_QUANTIFIER, (IPTNode)val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(1).obj });
          break;
        case 306: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.CROSS_JOIN, new Object[] { PTPositionalType.TABLE_REF_LEFT, (IPTNode)val_peek(3).obj, PTPositionalType.TABLE_REF_RIGHT, val_peek(0).obj });
          break;
        case 307: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 308: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 309: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode((PTNonterminalType)val_peek(4).obj, new Object[] { PTPositionalType.TABLE_REF_LEFT, (IPTNode)val_peek(5).obj, PTPositionalType.TABLE_REF_RIGHT, val_peek(2).obj, PTPositionalType.SEARCH_COND, val_peek(0).obj });
          break;
        case 310: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.INNER_JOIN, new Object[] { PTPositionalType.TABLE_REF_LEFT, (IPTNode)val_peek(4).obj, PTPositionalType.TABLE_REF_RIGHT, val_peek(2).obj, PTPositionalType.SEARCH_COND, val_peek(0).obj });
          break;
        case 311: 
          localObject1 = ((PTIdentifierNode)val_peek(1).obj).getIdentifier();
          if (!"oj".equalsIgnoreCase((String)localObject1))
          {
            localObject2 = this.lexer.getCurrentToken();
            throw PTParseErrorInfo.makeSyntaxError(this.m_queryString, ((Lexer.TokenInfo)localObject2).getFirstColumn() - 3);
          }
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.JOIN_ESCAPE, new Object[] { PTPositionalType.JOIN, val_peek(0).obj });
          break;
        case 312: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 313: 
          this.yyval.obj = PTNonterminalType.FULL_OUTER_JOIN;
          break;
        case 314: 
          this.yyval.obj = PTNonterminalType.LEFT_OUTER_JOIN;
          break;
        case 315: 
          this.yyval.obj = PTNonterminalType.RIGHT_OUTER_JOIN;
          break;
        case 316: 
          this.yyval.obj = PTNonterminalType.LEFT_OUTER_JOIN;
          break;
        case 317: 
          this.yyval.obj = PTNonterminalType.RIGHT_OUTER_JOIN;
          break;
        case 318: 
          this.yyval.obj = PTNonterminalType.FULL_OUTER_JOIN;
          break;
        case 319: 
          this.yyval.obj = PTNonterminalType.INNER_JOIN;
          break;
        case 320: 
          notImplemented();
          break;
        case 321: 
          notImplemented();
          break;
        case 323: 
          notImplemented();
          break;
        case 325: 
          notImplemented();
          break;
        case 326: 
          notImplemented();
          break;
        case 327: 
          notImplemented();
          break;
        case 328: 
          notImplemented();
          break;
        case 329: 
          notImplemented();
          break;
        case 330: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.CREATE_TABLE_STATEMENT, new Object[] { PTPositionalType.TABLE_NAME, val_peek(5).obj, PTPositionalType.COLUMN_DEFINITION_LIST, val_peek(3).obj, PTPositionalType.TABLE_CONSTRAINT_DEFINITION_LIST, val_peek(1).obj });
          break;
        case 331: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.CREATE_TABLE_STATEMENT, new Object[] { PTPositionalType.TABLE_NAME, val_peek(4).obj, PTPositionalType.COLUMN_DEFINITION_LIST, val_peek(2).obj, PTPositionalType.TABLE_CONSTRAINT_DEFINITION_LIST, this.m_nodeFactory.buildEmptyNode() });
          break;
        case 332: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.COLUMN_DEFINITION_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 333: 
          ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          this.yyval.obj = val_peek(2).obj;
          break;
        case 334: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_DEFINITION, new Object[] { PTPositionalType.COLUMN_NAME, val_peek(2).obj, PTPositionalType.COLUMN_TYPE, val_peek(1).obj, PTPositionalType.COLUMN_CONSTRAINT, val_peek(0).obj });
          break;
        case 335: 
          notImplemented();
          break;
        case 336: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DATA_TYPE, new Object[] { PTPositionalType.DATA_TYPE_IDENTIFIER, val_peek(1).obj, PTPositionalType.DATA_TYPE_ATTRIBUTE_LIST, val_peek(0).obj });
          break;
        case 337: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DATA_TYPE, new Object[] { PTPositionalType.DATA_TYPE_IDENTIFIER, val_peek(3).obj, PTPositionalType.DATA_TYPE_ATTRIBUTE_LIST, val_peek(1).obj });
          break;
        case 338: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 339: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.DATA_TYPE_ATTRIBUTE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 340: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 341: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 342: 
          notImplemented();
          break;
        case 343: 
          notImplemented();
          break;
        case 344: 
          notImplemented();
          break;
        case 345: 
          notImplemented();
          break;
        case 346: 
          notImplemented();
          break;
        case 347: 
          notImplemented();
          break;
        case 348: 
          notImplemented();
          break;
        case 349: 
          notImplemented();
          break;
        case 350: 
          notImplemented();
          break;
        case 351: 
          notImplemented();
          break;
        case 352: 
          notImplemented();
          break;
        case 353: 
          notImplemented();
          break;
        case 354: 
          notImplemented();
          break;
        case 355: 
          notImplemented();
          break;
        case 356: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_CONSTRAINT_DEFINITION, new Object[] { PTPositionalType.COLUMN_CONSTRAINT_FLAG, this.m_nodeFactory.buildFlagNode(PTFlagType.NOT_NULL) });
          break;
        case 357: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.COLUMN_CONSTRAINT_DEFINITION, new Object[] { PTPositionalType.COLUMN_CONSTRAINT_FLAG, this.m_nodeFactory.buildFlagNode(PTFlagType.NULL) });
          break;
        case 359: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.UNIQUE);
          break;
        case 360: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.PRIMARY_KEY);
          break;
        case 361: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.TABLE_CONSTRAINT_DEFINITION_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 362: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 363: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_CONSTRAINT_DEFINITION, new Object[] { PTPositionalType.UNIQUE_SPECIFICATION, val_peek(3).obj, PTPositionalType.COLUMN_LIST, val_peek(1).obj });
          break;
        case 364: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DROP_TABLE_STATEMENT, new Object[] { PTPositionalType.TABLE_NAME, val_peek(0).obj });
          break;
        case 365: 
          notImplemented();
          break;
        case 366: 
          notImplemented();
          break;
        case 367: 
          notImplemented();
          break;
        case 368: 
          notImplemented();
          break;
        case 369: 
          notImplemented();
          break;
        case 370: 
          notImplemented();
          break;
        case 371: 
          notImplemented();
          break;
        case 372: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.DELETE_STATEMENT_SEARCHED, new Object[] { PTPositionalType.TABLE_NAME, val_peek(1).obj, PTPositionalType.SEARCH_COND, val_peek(0).obj });
          break;
        case 373: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 374: 
          checkIdentifier((PTIdentifierNode)val_peek(4).obj, PTStringConstraint.CATALOG_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, (IPTNode)val_peek(4).obj, PTPositionalType.SCHEMA_IDENT, val_peek(2).obj, PTPositionalType.TABLE_IDENT, val_peek(0).obj });
          break;
        case 375: 
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, val_peek(2).obj, PTPositionalType.TABLE_IDENT, val_peek(0).obj });
          break;
        case 376: 
          checkIdentifier((PTIdentifierNode)val_peek(3).obj, PTStringConstraint.CATALOG_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, (IPTNode)val_peek(3).obj, PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, val_peek(0).obj });
          break;
        case 377: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.TABLE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.TABLE_IDENT, val_peek(0).obj });
          break;
        case 378: 
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.TABLE_NAME_LEN);
          this.yyval.obj = val_peek(0).obj;
          break;
        case 379: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.INSERT_STATEMENT, new Object[] { PTPositionalType.TABLE_NAME, val_peek(1).obj, PTPositionalType.INSERT_LIST, val_peek(0).obj });
          break;
        case 380: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.DEFAULT);
          break;
        case 381: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.INSERT_LIST, new Object[] { PTPositionalType.COLUMN_LIST, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.QUERY_EXPRESSION, val_peek(0).obj });
          break;
        case 382: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.INSERT_LIST, new Object[] { PTPositionalType.COLUMN_LIST, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.QUERY_EXPRESSION, val_peek(1).obj });
          break;
        case 383: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.INSERT_LIST, new Object[] { PTPositionalType.COLUMN_LIST, val_peek(2).obj, PTPositionalType.QUERY_EXPRESSION, val_peek(0).obj });
          break;
        case 384: 
          notImplemented();
          break;
        case 385: 
          notImplemented();
          break;
        case 386: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 387: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_CALL, new Object[] { PTPositionalType.RETURN_VALUE, val_peek(2).obj, PTPositionalType.PROCEDURE, val_peek(0).obj });
          break;
        case 388: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_CALL, new Object[] { PTPositionalType.RETURN_VALUE, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.PROCEDURE, val_peek(0).obj });
          break;
        case 389: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE, new Object[] { PTPositionalType.PROCEDURE_NAME, val_peek(1).obj, PTPositionalType.PARAM_LIST, val_peek(0).obj });
          break;
        case 390: 
          checkIdentifier((PTIdentifierNode)val_peek(4).obj, PTStringConstraint.CATALOG_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.PROCEDURE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, val_peek(4).obj, PTPositionalType.SCHEMA_IDENT, val_peek(2).obj, PTPositionalType.PROCEDURE_IDENT, val_peek(0).obj });
          break;
        case 391: 
          checkIdentifier((PTIdentifierNode)val_peek(2).obj, PTStringConstraint.SCHEMA_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.PROCEDURE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, val_peek(2).obj, PTPositionalType.PROCEDURE_IDENT, val_peek(0).obj });
          break;
        case 392: 
          checkIdentifier((PTIdentifierNode)val_peek(3).obj, PTStringConstraint.CATALOG_NAME_LEN);
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.PROCEDURE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, val_peek(3).obj, PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.PROCEDURE_IDENT, val_peek(0).obj });
          break;
        case 393: 
          checkIdentifier((PTIdentifierNode)val_peek(0).obj, PTStringConstraint.PROCEDURE_NAME_LEN);
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.PROCEDURE_NAME, new Object[] { PTPositionalType.CATALOG_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.SCHEMA_IDENT, this.m_nodeFactory.buildEmptyNode(), PTPositionalType.PROCEDURE_IDENT, val_peek(0).obj });
          break;
        case 394: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 395: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[0]);
          break;
        case 397: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 398: 
          this.yyval.obj = ((PTListNode)val_peek(1).obj).addChild(this.m_nodeFactory.buildDefaultParameterNode());
          break;
        case 399: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 400: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { this.m_nodeFactory.buildDefaultParameterNode(), this.m_nodeFactory.buildDefaultParameterNode() });
          break;
        case 401: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.PARAMETER_LIST, new IPTNode[] { this.m_nodeFactory.buildDefaultParameterNode(), (IPTNode)val_peek(0).obj });
          break;
        case 402: 
          this.yyval.obj = val_peek(1).obj;
          break;
        case 403: 
          notImplemented();
          break;
        case 404: 
          notImplemented();
          break;
        case 405: 
          notImplemented();
          break;
        case 406: 
          notImplemented();
          break;
        case 407: 
          notImplemented();
          break;
        case 408: 
          notImplemented();
          break;
        case 409: 
          notImplemented();
          break;
        case 410: 
          notImplemented();
          break;
        case 411: 
          notImplemented();
          break;
        case 412: 
          notImplemented();
          break;
        case 413: 
          notImplemented();
          break;
        case 414: 
          notImplemented();
          break;
        case 416: 
          notImplemented();
          break;
        case 417: 
          notImplemented();
          break;
        case 418: 
          notImplemented();
          break;
        case 419: 
          notImplemented();
          break;
        case 420: 
          notImplemented();
          break;
        case 421: 
          notImplemented();
          break;
        case 422: 
          notImplemented();
          break;
        case 423: 
          notImplemented();
          break;
        case 424: 
          notImplemented();
          break;
        case 425: 
          notImplemented();
          break;
        case 426: 
          notImplemented();
          break;
        case 427: 
          notImplemented();
          break;
        case 428: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UPDATE_STATEMENT_SEARCHED, new Object[] { PTPositionalType.TABLE_NAME, val_peek(3).obj, PTPositionalType.SET_CLAUSE_LIST, val_peek(1).obj, PTPositionalType.SEARCH_COND, val_peek(0).obj });
          break;
        case 429: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.UPSERT_STATEMENT, new Object[] { PTPositionalType.TABLE_NAME, val_peek(3).obj, PTPositionalType.SET_CLAUSE_LIST, val_peek(1).obj, PTPositionalType.SEARCH_COND, val_peek(0).obj });
          break;
        case 430: 
          this.yyval.obj = this.m_nodeFactory.buildListNode(PTListType.SET_CLAUSE_LIST, new IPTNode[] { (IPTNode)val_peek(0).obj });
          break;
        case 431: 
          this.yyval.obj = ((PTListNode)val_peek(2).obj).addChild((IPTNode)val_peek(0).obj);
          break;
        case 432: 
          this.yyval.obj = this.m_nodeFactory.buildNonterminalNode(PTNonterminalType.SET_CLAUSE, new Object[] { PTPositionalType.COLUMN_NAME, val_peek(2).obj, PTPositionalType.VALUE_EXPRESSION, val_peek(0).obj });
          break;
        case 433: 
          this.yyval.obj = val_peek(0).obj;
          break;
        case 434: 
          this.yyval.obj = this.m_nodeFactory.buildFlagNode(PTFlagType.DEFAULT);
        }
        if (this.yydebug) {
          debug("reduce");
        }
        state_drop(this.yym);
        this.yystate = state_peek(0);
        val_drop(this.yym);
        this.yym = yylhs[this.yyn];
        if ((this.yystate == 0) && (this.yym == 0))
        {
          if (this.yydebug) {
            debug("After reduction, shifting from state 0 to state 15");
          }
          this.yystate = 15;
          state_push(15);
          val_push(this.yyval);
          if (this.yychar < 0)
          {
            this.yychar = yylex();
            if (this.yychar < 0) {
              this.yychar = 0;
            }
            if (this.yydebug) {
              yylexdebug(this.yystate, this.yychar);
            }
          }
          if (this.yychar == 0) {
            break;
          }
        }
        else
        {
          this.yyn = yygindex[this.yym];
          if ((this.yyn != 0) && (this.yyn += this.yystate >= 0) && (this.yyn <= 7423) && (yycheck[this.yyn] == this.yystate)) {
            this.yystate = yytable[this.yyn];
          } else {
            this.yystate = yydgoto[this.yym];
          }
          if (this.yydebug) {
            debug("after reduction, shifting from state " + state_peek(0) + " to state " + this.yystate + "");
          }
          state_push(this.yystate);
          val_push(this.yyval);
        }
      }
    }
    return 0;
  }
  
  static
  {
    yylhs = new short[] { -1, 0, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 39, 36, 48, 48, 48, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 85, 87, 124, 62, 113, 113, 112, 111, 63, 63, 63, 32, 32, 83, 58, 58, 58, 58, 58, 58, 58, 58, 58, 59, 60, 108, 108, 121, 122, 122, 89, 89, 89, 91, 91, 90, 90, 115, 115, 84, 84, 97, 97, 125, 125, 125, 105, 105, 105, 98, 99, 99, 100, 100, 100, 100, 100, 30, 1, 1, 18, 18, 18, 18, 18, 17, 17, 16, 86, 86, 119, 119, 120, 120, 120, 120, 120, 23, 22, 31, 31, 146, 146, 136, 136, 93, 93, 5, 5, 3, 3, 4, 4, 70, 70, 70, 70, 70, 70, 70, 20, 19, 19, 19, 19, 19, 19, 2, 2, 49, 49, 50, 50, 53, 53, 56, 56, 37, 37, 34, 61, 61, 81, 82, 82, 82, 40, 134, 135, 135, 25, 92, 92, 92, 92, 92, 92, 92, 92, 92, 92, 69, 69, 68, 68, 21, 9, 9, 114, 114, 45, 45, 46, 46, 47, 47, 41, 41, 41, 41, 123, 123, 123, 42, 42, 42, 42, 133, 133, 133, 133, 133, 133, 133, 133, 130, 130, 147, 147, 128, 128, 57, 57, 129, 129, 106, 106, 106, 38, 38, 127, 44, 44, 44, 43, 43, 43, 43, 43, 148, 148, 148, 148, 148, 148, 148, 148, 148, 148, 148, 148, 148, 148, 150, 151, 151, 155, 155, 159, 149, 149, 149, 152, 152, 153, 153, 154, 154, 156, 156, 157, 157, 158, 158, 24, 24, 24, 137, 67, 66, 7, 7, 8, 8, 6, 6, 11, 11, 12, 107, 94, 110, 110, 96, 96, 109, 95, 35, 35, 88, 104, 104, 103, 103, 103, 103, 103, 103, 103, 103, 103, 54, 54, 54, 78, 78, 65, 64, 55, 55, 55, 55, 55, 55, 55, 139, 160, 160, 161, 161, 162, 162, 163, 140, 140, 10, 10, 15, 15, 14, 26, 26, 26, 27, 27, 28, 28, 164, 164, 164, 164, 164, 164, 164, 164, 164, 164, 164, 164, 164, 164, 13, 13, 13, 126, 126, 117, 117, 116, 33, 165, 165, 142, 141, 138, 166, 166, 29, 118, 79, 79, 79, 79, 77, 52, 51, 51, 51, 51, 145, 145, 76, 71, 71, 72, 80, 80, 80, 80, 74, 74, 74, 73, 73, 73, 73, 73, 75, 143, 167, 167, 171, 171, 172, 172, 172, 172, 172, 172, 173, 173, 168, 168, 169, 169, 174, 174, 175, 170, 170, 144, 176, 176, 132, 132, 102, 102, 101, 131, 131 };
    yylen = new short[] { 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 1, 3, 2, 1, 1, 1, 1, 1, 3, 1, 1, 3, 4, 3, 4, 5, 6, 5, 6, 1, 1, 1, 1, 2, 1, 3, 1, 3, 3, 3, 3, 1, 1, 3, 4, 9, 4, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 7, 5, 3, 1, 2, 2, 1, 7, 6, 5, 3, 1, 1, 3, 1, 1, 1, 1, 3, 3, 1, 3, 1, 1, 2, 1, 3, 1, 1, 1, 2, 1, 1, 3, 1, 3, 1, 2, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 6, 5, 4, 3, 1, 3, 1, 3, 4, 4, 2, 1, 1, 4, 3, 4, 1, 1, 1, 2, 3, 1, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 8, 8, 1, 1, 3, 1, 3, 3, 3, 1, 1, 3, 1, 3, 1, 2, 1, 1, 3, 3, 3, 1, 3, 3, 1, 2, 2, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 6, 7, 6, 6, 7, 7, 8, 6, 7, 8, 6, 8, 6, 6, 1, 3, 1, 3, 1, 5, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 1, 2, 1, 1, 1, 1, 6, 4, 3, 3, 1, 5, 4, 1, 2, 1, 2, 4, 4, 2, 1, 1, 4, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 1, 3, 6, 5, 2, 3, 2, 2, 2, 1, 1, 1, 1, 10, 1, 1, 1, 1, 1, 3, 2, 6, 8, 8, 7, 1, 3, 3, 2, 2, 4, 3, 1, 1, 1, 4, 5, 4, 4, 5, 5, 6, 4, 5, 6, 4, 6, 4, 4, 2, 1, 1, 1, 2, 1, 3, 4, 3, 1, 1, 4, 5, 4, 2, 3, 4, 1, 5, 3, 4, 1, 1, 4, 2, 1, 3, 4, 3, 3, 3, 3, 2, 2, 5, 3, 4, 1, 2, 2, 1, 3, 2, 2, 2, 3, 2, 7, 2, 1, 1, 3, 1, 1, 2, 2, 2, 1, 3, 1, 1, 2, 1, 3, 1, 1, 1, 3, 1, 8, 3, 1, 5, 5, 1, 3, 3, 1, 1 };
    yydefred = new short[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 8, 0, 10, 1, 12, 48, 49, 58, 14, 46, 60, 59, 61, 2, 17, 4, 5, 7, 9, 11, 13, 15, 16, 0, 0, 321, 0, 322, 0, 0, 0, 0, 0, 0, 409, 0, 0, 408, 0, 413, 0, 0, 406, 0, 0, 427, 0, 81, 82, 83, 0, 0, 0, 22, 23, 24, 25, 26, 19, 27, 28, 29, 30, 31, 32, 33, 34, 36, 35, 0, 377, 373, 20, 21, 0, 0, 219, 0, 0, 0, 272, 0, 0, 0, 0, 224, 71, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 225, 0, 0, 0, 0, 0, 154, 0, 0, 276, 206, 275, 203, 232, 227, 218, 0, 195, 215, 211, 0, 273, 226, 0, 99, 63, 65, 207, 278, 296, 204, 277, 205, 0, 0, 223, 210, 214, 202, 198, 208, 229, 230, 0, 0, 0, 0, 0, 0, 0, 0, 0, 37, 0, 0, 0, 324, 323, 0, 0, 0, 364, 0, 404, 0, 415, 410, 412, 411, 0, 0, 0, 0, 0, 0, 77, 0, 0, 233, 0, 0, 228, 0, 0, 0, 217, 213, 212, 216, 220, 384, 385, 0, 0, 0, 0, 0, 0, 0, 288, 0, 0, 0, 0, 0, 269, 0, 255, 254, 256, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 270, 271, 0, 0, 0, 0, 0, 0, 200, 199, 0, 0, 0, 0, 0, 0, 0, 0, 163, 0, 0, 0, 274, 0, 0, 0, 0, 0, 0, 0, 0, 47, 0, 388, 0, 402, 386, 0, 0, 0, 52, 0, 0, 0, 50, 0, 369, 0, 0, 0, 0, 0, 119, 372, 0, 365, 366, 367, 100, 0, 102, 0, 416, 0, 407, 0, 0, 379, 0, 426, 0, 80, 79, 76, 78, 91, 87, 0, 0, 0, 0, 85, 222, 221, 0, 0, 375, 0, 430, 0, 0, 0, 0, 0, 0, 129, 122, 124, 0, 128, 134, 0, 130, 131, 132, 126, 133, 0, 0, 0, 0, 286, 0, 0, 0, 293, 289, 0, 0, 0, 0, 0, 0, 0, 0, 0, 177, 0, 0, 0, 248, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 201, 67, 0, 209, 72, 0, 0, 0, 66, 0, 0, 0, 164, 165, 162, 231, 0, 0, 0, 0, 98, 0, 0, 64, 196, 197, 0, 0, 396, 0, 389, 387, 0, 53, 0, 0, 0, 39, 0, 0, 51, 0, 0, 370, 0, 332, 0, 0, 0, 0, 0, 368, 0, 414, 417, 0, 380, 0, 0, 0, 116, 92, 0, 0, 0, 0, 0, 378, 376, 0, 0, 0, 428, 429, 0, 0, 161, 125, 0, 0, 0, 0, 0, 0, 0, 0, 136, 141, 139, 0, 0, 140, 138, 137, 0, 0, 0, 0, 0, 0, 287, 0, 292, 285, 168, 0, 0, 280, 0, 167, 0, 295, 0, 0, 172, 0, 0, 0, 0, 0, 0, 170, 0, 0, 173, 0, 171, 0, 0, 0, 0, 0, 169, 0, 0, 0, 73, 68, 69, 0, 0, 166, 0, 0, 0, 395, 0, 0, 394, 0, 56, 43, 44, 45, 41, 0, 0, 54, 371, 0, 0, 0, 18, 0, 0, 0, 328, 0, 101, 420, 422, 0, 418, 421, 0, 0, 0, 93, 90, 0, 0, 0, 110, 111, 307, 0, 0, 0, 0, 86, 374, 434, 0, 432, 431, 297, 127, 123, 0, 0, 0, 0, 145, 146, 0, 156, 0, 0, 158, 159, 160, 0, 135, 0, 291, 0, 284, 181, 182, 282, 281, 183, 184, 180, 305, 0, 0, 250, 0, 0, 0, 0, 0, 0, 0, 0, 298, 299, 0, 300, 301, 302, 0, 0, 303, 304, 0, 97, 0, 392, 0, 0, 0, 57, 40, 55, 0, 0, 0, 0, 0, 0, 0, 357, 334, 358, 0, 336, 335, 0, 359, 333, 361, 0, 0, 331, 0, 0, 0, 0, 424, 403, 0, 0, 0, 0, 0, 0, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 319, 0, 0, 0, 0, 0, 153, 151, 150, 0, 0, 0, 155, 0, 144, 157, 290, 0, 0, 262, 237, 0, 264, 241, 0, 266, 244, 0, 260, 236, 0, 0, 268, 246, 247, 0, 258, 234, 279, 0, 0, 96, 0, 390, 0, 0, 0, 0, 0, 0, 0, 356, 0, 339, 340, 341, 360, 0, 330, 0, 329, 0, 0, 419, 425, 89, 0, 308, 0, 312, 0, 109, 115, 112, 113, 107, 0, 0, 186, 0, 0, 313, 0, 314, 315, 0, 0, 143, 0, 147, 0, 249, 261, 238, 239, 0, 263, 242, 0, 265, 0, 259, 0, 267, 235, 257, 0, 0, 95, 0, 345, 0, 349, 0, 352, 344, 354, 355, 0, 342, 0, 337, 362, 0, 0, 0, 325, 423, 0, 0, 0, 0, 190, 74, 306, 0, 0, 0, 142, 0, 252, 240, 243, 245, 0, 174, 175, 346, 347, 0, 350, 0, 0, 343, 338, 363, 327, 0, 320, 88, 114, 0, 0, 0, 0, 0, 0, 253, 348, 351, 353, 326, 0, 0, 251, 0 };
    yydgoto = new short[] { 15, 448, 336, 337, 338, 339, 130, 131, 132, 358, 16, 360, 361, 651, 430, 433, 299, 300, 133, 482, 340, 363, 758, 677, 134, 259, 547, 737, 738, 17, 317, 756, 164, 19, 135, 355, 614, 692, 136, 20, 341, 137, 138, 139, 140, 764, 847, 818, 141, 343, 589, 308, 21, 696, 569, 689, 344, 208, 22, 23, 24, 345, 173, 541, 570, 676, 142, 143, 369, 370, 346, 168, 274, 416, 417, 169, 25, 87, 571, 88, 275, 347, 598, 254, 27, 144, 145, 90, 489, 348, 147, 257, 148, 468, 149, 220, 221, 197, 320, 321, 322, 329, 330, 150, 151, 67, 209, 152, 28, 352, 353, 423, 424, 425, 610, 153, 659, 660, 573, 574, 674, 29, 154, 155, 30, 314, 661, 156, 157, 158, 159, 580, 31, 160, 161, 261, 294, 162, 32, 33, 34, 35, 36, 37, 38, 39, 679, 211, 163, 231, 372, 615, 722, 714, 705, 826, 708, 711, 718, 621, 45, 179, 810, 811, 549, 298, 287, 57, 304, 558, 668, 58, 59, 187, 559, 560, 63 };
    yysindex = new short[] { 3858, 65298, 183, 65388, 65317, 681, 65381, -127, -75, 65347, 7007, 7007, 5066, -11, -6, 0, 0, 0, 65373, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7007, 7007, 0, 7007, 0, 7007, 7007, 7007, 7007, 7007, 65384, 0, 244, 244, 0, 244, 0, -121, 184, 0, 7007, -57, 0, 681, 0, 0, 0, -50, 1206, 1206, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0, 0, 14, 76, 0, 303, 5185, 378, 0, 394, 399, 400, 170, 0, 0, 0, 411, 287, 428, 451, 469, 477, 485, 504, 513, 522, 536, 170, 170, 537, 541, 542, 0, 550, 555, 6063, 6179, 4305, 0, 6646, 7007, 0, 0, 0, 0, 0, 0, 0, 69, 0, 0, 0, 62, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 505, 251, 0, 0, 0, 0, 0, 0, 0, 0, 65344, 406, 7007, 306, 466, 343, 43, 354, 75, 0, 365, 590, 591, 0, 0, 328, 268, 324, 0, 65366, 0, 7007, 0, 0, 0, 0, 6730, 327, 120, 369, 341, -19, 0, 5317, 170, 0, 170, 170, 0, 65389, 65389, 368, 0, 0, 0, 0, 0, 0, 0, 4229, 7007, 7007, -75, 4424, 4560, -25, 0, 65284, 5819, 5819, 5819, -1, 0, 5819, 0, 0, 0, 170, 5819, -75, -75, 5819, 5819, 5819, -75, -75, -75, 0, 0, 325, 333, 5819, -75, -75, 5819, 0, 0, 660, 4560, 45, 665, 373, 669, 295, 6638, 0, 170, 589, 595, 0, 5819, 5819, 5819, 6541, 5819, 5066, 5819, 5819, 0, 676, 0, 684, 0, 0, 7007, 119, 65354, 0, 5819, 132, 65354, 0, 6796, 0, 7007, 7007, 476, 7007, 4424, 0, 0, 7007, 0, 0, 0, 0, 315, 0, 7007, 0, 391, 0, 377, 6234, 0, 0, 0, 6730, 0, 0, 0, 0, 0, 0, 232, 65, 453, 716, 0, 0, 0, 7007, 719, 0, 471, 0, -17, -17, 5819, 736, 4679, 4050, 0, 0, 0, 516, 0, 0, 240, 0, 0, 0, 0, 0, 803, -104, 45, 5819, 0, 65327, 5819, 490, 0, 0, 751, 370, 448, 749, 69, 753, 102, 771, 5819, 0, 69, 755, 775, 0, 353, 776, 5819, 5819, 784, 158, 795, 5819, 5819, 5819, 764, 796, 822, 5819, 5819, 0, 0, 826, 0, 0, 827, 5436, 5436, 0, 477, 542, 829, 0, 0, 0, 0, 251, 251, 251, 7007, 0, 840, 849, 0, 0, 0, 6579, 4815, 0, 459, 0, 0, 65354, 0, 851, 69, 34, 0, 837, 65354, 0, 855, 7007, 0, 6788, 0, 856, 534, 549, 574, 580, 0, 7007, 0, 0, 6854, 0, 630, 878, 626, 0, 0, 0, 7007, 6387, 1087, 5317, 0, 0, 7007, 5568, 7007, 0, 0, 46, -11, 0, 0, 4050, 175, 669, -26, 4424, 5819, 5819, 5066, 0, 0, 0, 887, 60, 0, 0, 0, -105, 4173, 4424, 5819, 26, 639, 0, 69, 0, 0, 0, 6893, 5819, 0, 5819, 0, 6931, 0, 142, 5819, 0, 895, 895, 895, 895, 896, 895, 0, 154, 509, 0, 5819, 0, 518, 544, 551, 5819, 5819, 0, 560, 596, 0, 0, 0, 0, 900, 7007, 0, 7007, 903, 5819, 0, 69, 5819, 0, 911, 0, 0, 0, 0, 0, 5819, 912, 0, 0, 407, 276, 918, 0, 6704, 924, 695, 0, 7007, 0, 0, 0, -32, 0, 0, -11, 0, 6854, 0, 0, 921, 627, 7007, 0, 0, 0, 706, 706, -14, 646, 0, 0, 0, 69, 0, 0, 0, 0, 0, -7, -7, 713, 4560, 0, 0, 641, 0, 5066, 887, 0, 0, 0, 736, 0, 516, 0, 5819, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 607, 0, 619, 622, 625, 628, 612, 632, 632, 634, 0, 0, 613, 0, 0, 0, 207, 486, 0, 0, 7007, 0, 944, 0, 7007, 69, 69, 0, 0, 0, 895, 895, 895, 895, 896, 895, 666, 0, 0, 0, 6503, 0, 0, 673, 0, 0, 0, 649, 966, 0, 549, 967, 703, 6854, 0, 0, 0, -10, 6379, 627, 969, 646, 1087, 886, 985, 0, 7007, 985, 1087, 721, 710, 696, 0, 1087, 698, 701, 720, 5819, 0, 0, 0, 5066, 69, 667, 0, 782, 0, 0, 0, 1003, -89, 0, 0, -83, 0, 0, 65303, 0, 0, 718, 0, 0, 700, 702, 0, 0, 0, 65279, 0, 0, 0, 5819, 5819, 0, 7007, 0, 685, 692, 699, 628, 632, 632, 704, 0, 728, 0, 0, 0, 0, -54, 0, 7007, 0, 7007, 732, 0, 0, 0, 1025, 0, 0, 0, 7007, 0, 0, 0, 0, 0, 646, 806, 0, 763, 1087, 0, 535, 0, 0, 1087, 69, 0, 5819, 0, 5066, 0, 0, 0, 0, 1035, 0, 0, 1035, 0, 1035, 0, 711, 0, 0, 0, 616, 621, 0, 359, 0, -24, 0, -64, 0, 0, 0, 0, 65288, 0, 6503, 0, 0, 730, 34, 759, 0, 0, 6445, 760, 5819, 4424, 0, 0, 0, 4424, 709, 69, 0, 714, 0, 0, 0, 0, 1036, 0, 0, 0, 0, 1035, 0, 1035, 1035, 0, 0, 0, 0, 7007, 0, 0, 0, 69, 1032, 580, 580, 4424, 1041, 0, 0, 0, 0, 0, 5819, 580, 0, 69 };
    yyrindex = new short[] { 0, 0, 6970, 0, 0, 0, 0, 767, 4934, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1092, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 783, 0, 0, 0, 0, 0, 0, -30, -30, 0, -30, 0, 0, 762, 0, 0, 0, 0, 0, 0, 0, 0, 5687, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3586, 0, 0, 0, 0, 0, 0, 0, 1804, 0, 0, 0, 0, 0, 1908, 976, 0, 0, 1277, 0, 830, 0, 2031, 2207, 2733, 0, 0, 0, 0, 2330, 1381, 1505, 0, 0, 2434, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3823, 0, 0, 0, 0, 0, 0, 1681, 0, 0, 0, 0, 0, 0, 0, 0, 0, 59, 3083, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65376, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 841, 0, 0, 0, 1096, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5938, 0, 0, 0, 0, 808, 0, 0, 0, 5938, 0, 1065, 0, 0, 0, 0, 1065, 5938, 5938, 1065, 0, 1065, 5938, 5938, 5938, 0, 0, 0, 0, 1065, 5938, 5938, 0, 0, 0, 835, 0, 1066, 65376, -22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1065, 0, 0, 0, 0, 32, 0, 984, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 0, 0, 1079, 0, 113, 35, 0, 0, 0, 0, 3586, 0, 0, 0, 1096, 1096, 0, 0, 0, 0, 0, 0, 0, 99, 0, 0, 863, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 808, 0, 0, 0, 0, 0, 0, 0, 0, 789, 0, 0, 0, 0, 0, 800, 1071, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65376, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3233, 3356, 3460, 0, 0, 2856, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 165, 213, 0, 80, 0, 0, 0, 0, 0, 0, 0, 1065, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 65376, 0, 0, 0, 1147, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 907, 1563, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65309, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2557, 2557, 2557, 2557, 2557, 2557, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6311, 0, 0, 0, 0, 0, 0, 0, 39, 807, 0, 816, 818, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 213, 149, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1096, 0, 0, 0, 23, 0, 0, 0, 3759, 0, 0, 0, 0, 0, 7042, 3704, 103, 37, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3830, 3830, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3956, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 832, 0, 0, 2960, 2960, 2960, 2960, 0, 2960, 2960, 2960, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2856, 0, 0, 834, 839, 0, 0, 0, 109, 109, 109, 109, 109, 109, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 758, 0, 0, 0, 475, 0, 0, 475, 0, 181, 0, 794, 0, 0, 797, 798, 0, 0, 0, 0, 0, 0, 844, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 149, 149, 149, 149, 149, 149, 149, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3759, 0, 403, 0, 0, 0, 0, 0, 0, 0, 259, 0, 0, 148, 0, 0, 0, 0, 0, 0, 3910, 0, 0, 0, 0, 0, 0, 0, 0, 2960, 0, 0, 2960, 0, 2960, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 882, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 901, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 149, 0, 149, 149, 0, 0, 0, 0, 0, 0, 0, 0, 33, 188, 200, 482, 0, 0, 0, 0, 0, 0, 0, 0, 624, 0, 66 };
    yygindex = new short[] { 0, 0, 0, 652, 781, 643, 0, 0, 0, 0, 0, 0, 191, 0, 65280, 0, 65367, 65257, 0, 0, 0, 0, 0, 559, 0, 0, 0, 0, 319, 0, 0, 462, 1128, 0, 17, 786, -2, 558, 0, 0, 0, 680, 266, -60, 318, 0, 0, 0, 82, 0, 552, 0, 0, 0, 65103, 0, 0, 64899, 0, 463, 0, 0, 897, 344, 0, 0, 0, 960, 0, 578, 0, 0, 884, 0, 0, 0, 0, 65393, 0, 0, 0, 0, 0, 4, -66, 414, 0, 0, 65114, -5, -86, 0, 898, 65341, 0, 949, 0, 0, 0, 0, 712, 723, 957, 0, 0, 1518, 0, 0, 0, 824, 0, 0, 636, 0, 0, 65216, 441, 0, 15, 0, 65278, 0, 0, 387, 0, 0, 0, 991, 0, -67, 0, 0, 0, 380, 0, 0, 65254, 804, 0, 0, 0, 0, 0, 0, 0, 0, 869, 1121, -34, 0, 0, 888, 458, 464, 470, 254, 472, 473, 64969, 553, 0, 0, 0, 363, 0, 531, 0, 1143, 902, 644, 0, 0, 1017, 642, 543, 0, 0 };
    yytable();
    yycheck();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/parser/generated/Parser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */