 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;

import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author janardhan
 */
public class LDAPAuthenticationService {
    private static Logger logger = LogManager.getLogger(LDAPAuthenticationService.class.getName());

    public static Attributes authenticate(String userName, String password) {
        DirContext ldapCtx = null;
        Attributes attributes = null;
        NamingEnumeration<SearchResult> results = null;
        String userNameWithDomain = com.mss.ediscv.util.Properties.getProperty("LDAP_USER_DOMAIN");
        Hashtable<String, String> environment = new Hashtable<String, String>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, com.mss.ediscv.util.Properties.getProperty("LDAP_URL"));
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, userNameWithDomain);
        environment.put(Context.SECURITY_CREDENTIALS, "secret");
        try {
            ldapCtx = new InitialDirContext(environment);
            String searchFilter = "(&(uid=" + userName + ")(userPassword=" + password + "))";
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ldapCtx.search("ou=mscvp_beta_version,dc=maxcrc,dc=com", searchFilter, controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                attributes = searchResult.getAttributes();
            }
        } catch (AuthenticationException authenticationException) {
           LoggerUtility.log(logger, "AuthenticationException occurred in getB2BChannelList method:: " + authenticationException.getMessage(), Level.ERROR, authenticationException.getCause());

        } catch (Exception exception) {
           LoggerUtility.log(logger, "Exception occurred in getB2BChannelList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ldapCtx != null) {
                try {
                    ldapCtx.close();
                } catch (Exception exception) {
                    LoggerUtility.log(logger, "Finally Exception occurred in getB2BChannelList method:: " + exception.getMessage(), Level.ERROR, exception.getCause());
                }
            }
        }
        return attributes;
    }
}
