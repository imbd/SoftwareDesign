package com.imbd.spbau;

import java.util.HashMap;
import java.util.Map;

/**
 * Class with a Map which contains all current shell's variables and their values
 */

public class Environment {

    private Map<String, String> variables = new HashMap<>();

    public void setValue(String variable, String value) {

        variables.put(variable, value);
    }

    public String getValue(String variable) {

        return variables.getOrDefault(variable, "");
    }

}
