package com.group_finity.mascot.action;

import com.group_finity.mascot.script.VariableMap;

import java.util.logging.Logger;

/**
 * Original Author: Yuki Yamada of Group Finity (http://www.group-finity.com/Shimeji/)
 * Currently developed by Shimeji-ee Group.
 */
public class Select extends ComplexAction {

    private static final Logger log = Logger.getLogger(Select.class.getName());

    public Select(final VariableMap params, final Action... actions) {
        super(params, actions);
    }

}
