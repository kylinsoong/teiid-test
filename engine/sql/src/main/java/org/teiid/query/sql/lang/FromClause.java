/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */

package org.teiid.query.sql.lang;

import java.util.Collection;

import org.teiid.core.util.EquivalenceUtil;
import org.teiid.query.sql.LanguageObject;
import org.teiid.query.sql.LanguageVisitor;
import org.teiid.query.sql.lang.Option.MakeDep;
import org.teiid.query.sql.symbol.GroupSymbol;
import org.teiid.query.sql.visitor.SQLStringVisitor;


/**
 * A FromClause is an interface for subparts held in a FROM clause.  One 
 * type of FromClause is {@link UnaryFromClause}, which is the more common 
 * use and represents a single group.  Another, less common type of FromClause
 * is the {@link JoinPredicate} which represents a join between two FromClauses
 * and may contain criteria.
 */
public abstract class FromClause implements LanguageObject {
	
	public static final String PRESERVE = "PRESERVE"; //$NON-NLS-1$
	
    private boolean optional;
    private MakeDep makeDep;
    private boolean makeNotDep;
    private MakeDep makeInd;
    private boolean noUnnest;
    private boolean preserve;

    public boolean isOptional() {
        return optional;
    }
    
    public void setOptional(boolean optional) {
        this.optional = optional;
    }
    
    public MakeDep getMakeInd() {
		return makeInd;
	}
    
    public void setMakeInd(MakeDep makeInd) {
		this.makeInd = makeInd;
	}
    
    public abstract void acceptVisitor(LanguageVisitor visitor);
    public abstract void collectGroups(Collection<GroupSymbol> groups);
    protected abstract FromClause cloneDirect();
    
    public FromClause clone() {
    	FromClause clone = cloneDirect();
    	clone.makeDep = makeDep;
    	clone.makeInd = makeInd;
    	clone.makeNotDep = makeNotDep;
    	clone.optional = optional;
    	clone.noUnnest = noUnnest;
    	clone.preserve = preserve;
    	return clone;
    }
    
    public void setNoUnnest(boolean noUnnest) {
		this.noUnnest = noUnnest;
	}
    
    public boolean isNoUnnest() {
		return noUnnest;
	}

    public boolean isMakeDep() {
        return this.makeDep != null;
    }
    
    public MakeDep getMakeDep() {
		return makeDep;
	}

    public void setMakeDep(boolean makeDep) {
    	if (makeDep) {
    		if (this.makeDep == null) {
    			this.makeDep = new MakeDep();
    		}
    	} else {
    		this.makeDep = null;
    	}
    }

    public boolean isMakeNotDep() {
        return this.makeNotDep;
    }

    public void setMakeNotDep(boolean makeNotDep) {
        this.makeNotDep = makeNotDep;
    }
    
    public void setMakeDep(MakeDep makedep) {
    	this.makeDep = makedep;
    }
    
    public boolean isPreserve() {
		return preserve;
	}
    
    public void setPreserve(boolean preserve) {
		this.preserve = preserve;
	}
    
    public boolean hasHint() {
        return optional || makeDep != null || makeNotDep || makeInd != null || noUnnest || preserve;
    }
    
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        } 
        
        if(! (obj instanceof FromClause)) { 
            return false;
        }

        FromClause other = (FromClause)obj;

        return other.isOptional() == this.isOptional()
               && EquivalenceUtil.areEqual(this.makeDep, other.makeDep)
               && other.isMakeNotDep() == this.isMakeNotDep()
        	   && EquivalenceUtil.areEqual(this.makeInd, other.makeInd)
        	   && other.isNoUnnest() == this.isNoUnnest()
			   && other.isNoUnnest() == this.isNoUnnest();
    }
    
    @Override
    public String toString() {
    	return SQLStringVisitor.getSQLString(this);
    }
}
