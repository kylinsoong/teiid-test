package com.simba.sqlengine.aeprocessor.aebuilder;

import com.simba.sqlengine.exceptions.SQLEngineExceptionFactory;
import com.simba.sqlengine.parser.parsetree.IPTNode;
import com.simba.sqlengine.parser.parsetree.PTEmptyNode;
import com.simba.sqlengine.parser.parsetree.PTFlagNode;
import com.simba.sqlengine.parser.parsetree.PTIdentifierNode;
import com.simba.sqlengine.parser.parsetree.PTListNode;
import com.simba.sqlengine.parser.parsetree.PTLiteralNode;
import com.simba.sqlengine.parser.parsetree.PTNonterminalNode;
import com.simba.sqlengine.parser.type.PTFlagType;
import com.simba.sqlengine.parser.type.PTListType;
import com.simba.sqlengine.parser.type.PTLiteralType;
import com.simba.sqlengine.parser.type.PTNonterminalType;
import com.simba.sqlengine.parser.type.PTPositionalType;
import com.simba.support.exceptions.ErrorException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class AEBuilderCheck
{
  private AEBuilderCheck()
  {
    throw new UnsupportedOperationException("Cannot instantiate AEBuilderCheck.");
  }
  
  public static void checkThat(IPTNode paramIPTNode, ParseTreeMatcher paramParseTreeMatcher)
    throws ErrorException
  {
    if (!paramParseTreeMatcher.matches(paramIPTNode)) {
      throw paramParseTreeMatcher.generateException();
    }
  }
  
  public static <T extends ParseTreeMatcher> T is(T paramT)
  {
    return paramT;
  }
  
  public static ParseTreeMatcher allOf(Collection<ParseTreeMatcher> paramCollection)
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        Iterator localIterator = this.val$matchers.iterator();
        while (localIterator.hasNext())
        {
          AEBuilderCheck.ParseTreeMatcher localParseTreeMatcher = (AEBuilderCheck.ParseTreeMatcher)localIterator.next();
          if (!localParseTreeMatcher.matches(paramAnonymousIPTNode)) {
            return false;
          }
        }
        return true;
      }
    };
  }
  
  public static ParseTreeMatcher allOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2)
  {
    return allOf(Arrays.asList(new ParseTreeMatcher[] { paramParseTreeMatcher1, paramParseTreeMatcher2 }));
  }
  
  public static ParseTreeMatcher allOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2, ParseTreeMatcher paramParseTreeMatcher3)
  {
    return allOf(Arrays.asList(new ParseTreeMatcher[] { paramParseTreeMatcher1, paramParseTreeMatcher2, paramParseTreeMatcher3 }));
  }
  
  public static ParseTreeMatcher anyOf(Collection<ParseTreeMatcher> paramCollection)
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        Iterator localIterator = this.val$matchers.iterator();
        while (localIterator.hasNext())
        {
          AEBuilderCheck.ParseTreeMatcher localParseTreeMatcher = (AEBuilderCheck.ParseTreeMatcher)localIterator.next();
          if (localParseTreeMatcher.matches(paramAnonymousIPTNode)) {
            return true;
          }
        }
        return false;
      }
    };
  }
  
  public static ParseTreeMatcher anyOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2)
  {
    return anyOf(Arrays.asList(new ParseTreeMatcher[] { paramParseTreeMatcher1, paramParseTreeMatcher2 }));
  }
  
  public static ParseTreeMatcher anyOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2, ParseTreeMatcher paramParseTreeMatcher3)
  {
    return anyOf(Arrays.asList(new ParseTreeMatcher[] { paramParseTreeMatcher1, paramParseTreeMatcher2, paramParseTreeMatcher3 }));
  }
  
  public static ParseTreeMatcher anything()
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        return true;
      }
    };
  }
  
  public static Both both(ParseTreeMatcher paramParseTreeMatcher)
  {
    return new Both(paramParseTreeMatcher);
  }
  
  public static ParseTreeMatcher bothOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2)
  {
    return both(paramParseTreeMatcher1).and(paramParseTreeMatcher2);
  }
  
  public static Either either(ParseTreeMatcher paramParseTreeMatcher)
  {
    return new Either(paramParseTreeMatcher);
  }
  
  public static ParseTreeMatcher eitherOf(ParseTreeMatcher paramParseTreeMatcher1, ParseTreeMatcher paramParseTreeMatcher2)
  {
    return new Either(paramParseTreeMatcher1).or(paramParseTreeMatcher2);
  }
  
  public static ParseTreeMatcher empty()
  {
    return instanceOf(PTEmptyNode.class);
  }
  
  public static ParseTreeMatcher flagNode()
  {
    return instanceOf(PTFlagNode.class);
  }
  
  public static ParseTreeMatcher flagNode(PTFlagType paramPTFlagType, PTFlagType... paramVarArgs)
  {
    EnumSet localEnumSet = EnumSet.of(paramPTFlagType, paramVarArgs);
    bothOf(instanceOf(PTFlagNode.class), new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        return this.val$flags.contains(((PTFlagNode)paramAnonymousIPTNode).getFlagType());
      }
    });
  }
  
  public static ParseTreeMatcher hasListChildren(ParseTreeMatcher paramParseTreeMatcher)
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        if (!(paramAnonymousIPTNode instanceof PTListNode)) {
          return false;
        }
        PTListNode localPTListNode = (PTListNode)paramAnonymousIPTNode;
        Iterator localIterator = localPTListNode.getChildItr();
        while (localIterator.hasNext()) {
          if (!this.val$matcher.matches((IPTNode)localIterator.next())) {
            return false;
          }
        }
        return true;
      }
    };
  }
  
  public static ParseTreeMatcher hasChildren(Map<PTPositionalType, ParseTreeMatcher> paramMap)
  {
    Map<PTPositionalType, ParseTreeMatcher> localMap = paramMap;
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        if (!(paramAnonymousIPTNode instanceof PTNonterminalNode)) {
          return false;
        }
        PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramAnonymousIPTNode;
        if (!localPTNonterminalNode.getAllPositionalTypes().containsAll(this.val$c.keySet())) {
          return false;
        }
        Iterator localIterator = this.val$c.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if (!((AEBuilderCheck.ParseTreeMatcher)localEntry.getValue()).matches(localPTNonterminalNode.getChild((PTPositionalType)localEntry.getKey()))) {
            return false;
          }
        }
        return true;
      }
    };
  }
  
  public static ParseTreeMatcher hasExactChildren(Map<PTPositionalType, ParseTreeMatcher> paramMap)
  {
    Map<PTPositionalType, ParseTreeMatcher> localMap = paramMap;
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        if (!(paramAnonymousIPTNode instanceof PTNonterminalNode)) {
          return false;
        }
        PTNonterminalNode localPTNonterminalNode = (PTNonterminalNode)paramAnonymousIPTNode;
        if (!localPTNonterminalNode.getAllPositionalTypes().equals(this.val$c.keySet())) {
          return false;
        }
        Iterator localIterator = this.val$c.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          if (!((AEBuilderCheck.ParseTreeMatcher)localEntry.getValue()).matches(localPTNonterminalNode.getChild((PTPositionalType)localEntry.getKey()))) {
            return false;
          }
        }
        return true;
      }
    };
  }
  
  public static ParseTreeMatcher instanceOf(Class<? extends IPTNode> paramClass)
  {
    Class<? extends IPTNode> localClass = paramClass;
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        return this.val$clazz.isInstance(paramAnonymousIPTNode);
      }
    };
  }
  
  public static ParseTreeMatcher identifier()
  {
    return instanceOf(PTIdentifierNode.class);
  }
  
  public static ListMatcher list()
  {
    return new ListMatcher();
  }
  
  public static ListTypeMatcher list(PTListType paramPTListType)
  {
    return new ListTypeMatcher(paramPTListType);
  }
  
  public static ParseTreeMatcher literal()
  {
    return instanceOf(PTLiteralNode.class);
  }
  
  public static ParseTreeMatcher literal(PTLiteralType paramPTLiteralType)
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        return ((paramAnonymousIPTNode instanceof PTLiteralNode)) && (((PTLiteralNode)paramAnonymousIPTNode).getLiteralType() == this.val$type);
      }
    };
  }
  
  public static ParseTreeMatcher nonEmpty()
  {
    return not(empty());
  }
  
  private static ParseTreeMatcher not(ParseTreeMatcher paramParseTreeMatcher)
  {
    new BaseParseTreeMatcher()
    {
      public boolean matches(IPTNode paramAnonymousIPTNode)
      {
        return !this.val$m.matches(paramAnonymousIPTNode);
      }
    };
  }
  
  public static NonterminalMatcher nonTerminal()
  {
    return new NonterminalMatcher();
  }
  
  public static NonterminalTypeMatcher nonTerminal(PTNonterminalType paramPTNonterminalType)
  {
    return new NonterminalTypeMatcher(paramPTNonterminalType);
  }
  
  public static ParseTreeMatcher optional(ParseTreeMatcher paramParseTreeMatcher)
  {
    return either(empty()).or(paramParseTreeMatcher);
  }
  
  public static ParseTreeMatcher optionalIdentifier()
  {
    return optional(identifier());
  }
  
  public static ParseTreeMatcher optionalList()
  {
    return optional(list());
  }
  
  public static ParseTreeMatcher optionalList(PTListType paramPTListType)
  {
    return optional(list(paramPTListType));
  }
  
  public static ErrorException generateInvalidParseException()
  {
    ErrorException localErrorException = SQLEngineExceptionFactory.invalidParseTreeException();
    return localErrorException;
  }
  
  private static abstract class NonterminalBaseMatcher
    extends AEBuilderCheck.BaseParseTreeMatcher
  {
    public AEBuilderCheck.ParseTreeMatcher withNoChildren()
    {
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasExactChildren(Collections.emptyMap()));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withChildren(PTPositionalType paramPTPositionalType, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType, paramParseTreeMatcher);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withChildren(PTPositionalType paramPTPositionalType1, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher1, PTPositionalType paramPTPositionalType2, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher2)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType1, paramParseTreeMatcher1);
      localEnumMap.put(paramPTPositionalType2, paramParseTreeMatcher2);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withChildren(PTPositionalType paramPTPositionalType1, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher1, PTPositionalType paramPTPositionalType2, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher2, PTPositionalType paramPTPositionalType3, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher3)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType1, paramParseTreeMatcher1);
      localEnumMap.put(paramPTPositionalType2, paramParseTreeMatcher2);
      localEnumMap.put(paramPTPositionalType3, paramParseTreeMatcher3);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withExactChildren(PTPositionalType paramPTPositionalType, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType, paramParseTreeMatcher);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasExactChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withExactChildren(PTPositionalType paramPTPositionalType1, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher1, PTPositionalType paramPTPositionalType2, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher2)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType1, paramParseTreeMatcher1);
      localEnumMap.put(paramPTPositionalType2, paramParseTreeMatcher2);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasExactChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withExactChildren(PTPositionalType paramPTPositionalType1, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher1, PTPositionalType paramPTPositionalType2, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher2, PTPositionalType paramPTPositionalType3, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher3)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType1, paramParseTreeMatcher1);
      localEnumMap.put(paramPTPositionalType2, paramParseTreeMatcher2);
      localEnumMap.put(paramPTPositionalType3, paramParseTreeMatcher3);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasExactChildren(localEnumMap));
    }
    
    public AEBuilderCheck.ParseTreeMatcher withExactChildren(PTPositionalType paramPTPositionalType1, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher1, PTPositionalType paramPTPositionalType2, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher2, PTPositionalType paramPTPositionalType3, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher3, PTPositionalType paramPTPositionalType4, AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher4)
    {
      EnumMap localEnumMap = new EnumMap(PTPositionalType.class);
      localEnumMap.put(paramPTPositionalType1, paramParseTreeMatcher1);
      localEnumMap.put(paramPTPositionalType2, paramParseTreeMatcher2);
      localEnumMap.put(paramPTPositionalType3, paramParseTreeMatcher3);
      localEnumMap.put(paramPTPositionalType4, paramParseTreeMatcher4);
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasExactChildren(localEnumMap));
    }
  }
  
  private static abstract class ListBaseMatcher
    extends AEBuilderCheck.BaseParseTreeMatcher
  {
    public AEBuilderCheck.ParseTreeMatcher withChildren(AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      return AEBuilderCheck.both(this).and(AEBuilderCheck.hasListChildren(paramParseTreeMatcher));
    }
  }
  
  public static class ListTypeMatcher
    extends AEBuilderCheck.ListBaseMatcher
  {
    private final PTListType m_type;
    
    public ListTypeMatcher(PTListType paramPTListType)
    {
      super();
      this.m_type = paramPTListType;
    }
    
    public boolean matches(IPTNode paramIPTNode)
    {
      return ((paramIPTNode instanceof PTListNode)) && (((PTListNode)paramIPTNode).getListType() == this.m_type);
    }
  }
  
  public static class ListMatcher
    extends AEBuilderCheck.ListBaseMatcher
  {
    public ListMatcher()
    {
      super();
    }
    
    public boolean matches(IPTNode paramIPTNode)
    {
      return paramIPTNode instanceof PTListNode;
    }
    
    public AEBuilderCheck.ParseTreeMatcher withType(PTListType paramPTListType)
    {
      return new AEBuilderCheck.ListTypeMatcher(paramPTListType);
    }
  }
  
  public static class NonterminalTypeMatcher
    extends AEBuilderCheck.NonterminalBaseMatcher
  {
    private final PTNonterminalType m_type;
    
    public NonterminalTypeMatcher(PTNonterminalType paramPTNonterminalType)
    {
      super();
      this.m_type = paramPTNonterminalType;
    }
    
    public boolean matches(IPTNode paramIPTNode)
    {
      return ((paramIPTNode instanceof PTNonterminalNode)) && (((PTNonterminalNode)paramIPTNode).getNonterminalType() == this.m_type);
    }
  }
  
  public static class NonterminalMatcher
    extends AEBuilderCheck.NonterminalBaseMatcher
  {
    public NonterminalMatcher()
    {
      super();
    }
    
    public boolean matches(IPTNode paramIPTNode)
    {
      return paramIPTNode instanceof PTNonterminalNode;
    }
    
    public AEBuilderCheck.NonterminalTypeMatcher withType(PTNonterminalType paramPTNonterminalType)
    {
      return new AEBuilderCheck.NonterminalTypeMatcher(paramPTNonterminalType);
    }
  }
  
  public static class Either
  {
    private final AEBuilderCheck.ParseTreeMatcher m_matcher;
    
    public Either(AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      this.m_matcher = paramParseTreeMatcher;
    }
    
    public AEBuilderCheck.ParseTreeMatcher or(final AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      new AEBuilderCheck.BaseParseTreeMatcher()
      {
        public boolean matches(IPTNode paramAnonymousIPTNode)
        {
          return (AEBuilderCheck.Either.this.m_matcher.matches(paramAnonymousIPTNode)) || (paramParseTreeMatcher.matches(paramAnonymousIPTNode));
        }
      };
    }
  }
  
  public static class Both
  {
    private final AEBuilderCheck.ParseTreeMatcher m_matcher;
    
    public Both(AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      this.m_matcher = paramParseTreeMatcher;
    }
    
    public AEBuilderCheck.ParseTreeMatcher and(final AEBuilderCheck.ParseTreeMatcher paramParseTreeMatcher)
    {
      new AEBuilderCheck.BaseParseTreeMatcher()
      {
        public boolean matches(IPTNode paramAnonymousIPTNode)
        {
          return (AEBuilderCheck.Both.this.m_matcher.matches(paramAnonymousIPTNode)) && (paramParseTreeMatcher.matches(paramAnonymousIPTNode));
        }
      };
    }
  }
  
  public static abstract class BaseParseTreeMatcher
    implements AEBuilderCheck.ParseTreeMatcher
  {
    public abstract boolean matches(IPTNode paramIPTNode);
    
    public ErrorException generateException()
    {
      return AEBuilderCheck.generateInvalidParseException();
    }
  }
  
  public static abstract interface ParseTreeMatcher
  {
    public abstract boolean matches(IPTNode paramIPTNode);
    
    public abstract ErrorException generateException();
  }
}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/sqlengine/aeprocessor/aebuilder/AEBuilderCheck.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */