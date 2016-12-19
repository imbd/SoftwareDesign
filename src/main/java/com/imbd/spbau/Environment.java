package com.imbd.spbau;

import java.util.HashMap;
import java.util.Map;

/**
 * Class with a Map which contains all current shell's variables and their values
 */

public class Environment {

    private Map<String, String> variables = new HashMap<>();

    /**
     * Setting value to variable
     *
     * @param variable name of variable
     * @param value    value to set
     */
    public void setValue(String variable, String value) {

        variables.put(variable, value);
    }

    /**
     * Getting value of variable
     *
     * @param variable name of variable
     * @return value of given variable
     */
    public String getValue(String variable) {

        return variables.getOrDefault(variable, "");
    }

}
