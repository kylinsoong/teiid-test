package org.teiid.test;

import org.teiid.adminapi.Admin;

public class SimpleTest {

    public static void main(String[] args) {

        System.out.println(Admin.Cache.PREPARED_PLAN_CACHE.name());
    }

}
