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

import org.teiid.core.util.EquivalenceUtil;
import org.teiid.query.sql.*;
import org.teiid.query.sql.lang.PredicateCriteria.Negatable;
import org.teiid.query.sql.symbol.Expression;


/**
 * Represents criteria such as:  "<expression> IS NULL".
 */
public class IsNullCriteria extends PredicateCriteria implements Negatable {

	private Expression expression;
    /** Negation flag. Indicates whether the criteria expression contains a NOT. */
    private boolean negated;
	
    /**
     * Constructs a default instance of this class.
     */
    public IsNullCriteria() {}
    
    /**
     * Constructs an instance of this class with an expression
     * @param expression The expression to be compared to null
     */
    public IsNullCriteria( Expression expression ) {
		this.expression = expression;
    }

	/**
	 * Set expression.
	 * @param expression Expression to compare to null
	 */
	public void setExpression(Expression expression) { 
		this.expression = expression;
	}
	
	/**
	 * Get expression.
	 * @return Expression to compare
	 */
	public Expression getExpression() {
		return this.expression;
	}
    
    /**
     * Returns whether this criteria is negated.
     * @return flag indicating whether this criteria contains a NOT
     */
    public boolean isNegated() {
        return negated;
    }
    
    /**
     * Sets the negation flag for this criteria.
     * @param negationFlag true if this criteria contains a NOT; false otherwise
     */
    public void setNegated(boolean negationFlag) {
        negated = negationFlag;
    }
    
    @Override
    public void negate() {
    	this.negated = !this.negated;
    }

    public void acceptVisitor(LanguageVisitor visitor) {
        visitor.visit(this);
    }

    /**
	 * Get hash code.  WARNING: The hash code is based on data in the criteria.
	 * If data values are changed, the hash code will change - don't hash this
	 * object and change values.
	 * @return Hash code for object
	 */
	public int hashCode() {
		return (getExpression() == null) ? 0 : getExpression().hashCode();
	}
	
    /**
     * Comparees this criteria to another object for equality
     * @param obj Other object
     * @return True if objects are equal
     */
    public boolean equals(Object obj) {
		if(this == obj) { 
			return true;
		}
		
		if(! (obj instanceof IsNullCriteria)) {
			return false;
		} 
		
		IsNullCriteria other = (IsNullCriteria) obj;
        if (isNegated() ^ other.isNegated()) {
            return false;
        }
        return EquivalenceUtil.areEqual(getExpression(), other.getExpression());			
	}
	
	/**
	 * Deep copy of object
	 * @return Deep copy of object
	 */
	public Object clone() {
	    Expression copy = null;
	    if(getExpression() != null) { 
	        copy = (Expression) getExpression().clone();
	    }
        IsNullCriteria criteriaCopy = new IsNullCriteria(copy);
        criteriaCopy.setNegated(isNegated());
		return criteriaCopy;
	}
	

}