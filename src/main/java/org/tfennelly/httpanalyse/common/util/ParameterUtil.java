/*
 * Copyright (C) 2013 CloudBees Inc.
 *
 * All rights reserved.
 */
package org.tfennelly.httpanalyse.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that contains methods related to handling query statements.
 * 
 * @author <a href="mailto:daniel.bevenius@gmail.com">daniel.bevenius@gmail.com</a>
 */
public class ParameterUtil {
    
    private static final String MATCH_HASH_VARIABLES = "\\s*([#]\\w+(-\\w)?)\\w*";
    private static final Pattern PARAMETER_PATTERN = Pattern.compile(MATCH_HASH_VARIABLES);
    
    private ParameterUtil() {
    }
    
    /**
     * Replaces variable names that start with a '#' (hash) character with the value passed-in.
     * 
     * @param query the SQL statement to be washed.
     * @return {@code String} a new String with hash variables replaces with questionmarks or the the 
     * passed-in string unmodified if there were no hash variables in the statement.
     */
    public static String replace(final String query, final String value) {
        final Matcher matcher = matcher(query);
        if (matcher.find()) {
            return matcher.replaceAll(value);
        }
        return query;
    }
    
    /**
     * Returns any variables specified in the passed-in query
     * 
     * A variable may be specified using a '#' symbol, like this:
     * 'select * from CUSTOMERS where NAME=#name'
     * 
     * @param query the  statement to parse.
     * @return {@code List<String>} containing any variable names that exist in the query
     * an empty list if no varibles were found.
     */
    public static List<String> getHashVariableNames(final String query) {
        final Matcher matcher = matcher(query);
        final List<String> vars = new ArrayList<String>();
        while (matcher.find()) {
            final String hashVariable = matcher.group().trim();
            vars.add(hashVariable.substring(1).replace(".", "_"));
        }
        return vars;
    }
    
    private static Matcher matcher(final String query) {
        AssertArgument.isNotNullAndNotEmpty(query, "query");
        return PARAMETER_PATTERN.matcher(query);
    }

}
