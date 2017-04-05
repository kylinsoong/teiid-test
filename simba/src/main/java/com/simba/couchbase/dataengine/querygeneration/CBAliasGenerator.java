package com.simba.couchbase.dataengine.querygeneration;

public class CBAliasGenerator {

    private final String m_prefix;
    
    private Integer aliasCounter;
    
    public CBAliasGenerator(String inputPrefix) {
        inputPrefix = inputPrefix.replace("`", "``");
        this.m_prefix = inputPrefix;
        this.aliasCounter = Integer.valueOf(0);
        reset();
    }
    
    public void CommitContext() {
        Integer localInteger1 = this.aliasCounter;
        Integer localInteger2 = this.aliasCounter = Integer.valueOf(this.aliasCounter.intValue() + 1);
    }
    
    public String addAlias() {
        return addAlias("");
    }
    
    public String addAlias(String inputSuffix) {
        
        int index = this.aliasCounter.intValue();
        
        StringBuilder aliasResult = new StringBuilder();
        if ((null != inputSuffix) && (inputSuffix.length() == 0)) {
            aliasResult.append("`").append(this.m_prefix).append(index).append("`");
            CommitContext();
            return aliasResult.toString();
        }
        
        String suffix = inputSuffix;
        suffix.replace("`", "``");
        aliasResult.append("`").append(this.m_prefix).append(index).append("_").append(suffix).append("`");
        
        CommitContext();
        return aliasResult.toString();
    }
  




  public void reset()
  {
/* 102 */     this.aliasCounter = Integer.valueOf(0);
  }
}