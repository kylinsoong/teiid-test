package com.simba.support;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface InvariantName {}


/* Location:              /home/kylin/work/couchbase/Driver/CouchbaseJDBC4.jar!/com/simba/support/InvariantName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */