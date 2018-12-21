package ru.avicomp.map.spin.functions.avc;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.function.FunctionEnv;
import org.topbraid.spin.arq.AbstractFunction1;

/**
 * Created by @ssz on 21.12.2018.
 */
public class factorial extends AbstractFunction1 {
    @Override
    protected NodeValue exec(Node arg1, FunctionEnv env) {
        if (!arg1.isLiteral()) {
            throw new ExprEvalException("The argument must be a literal: " + arg1);
        }
        String lex = arg1.getLiteralLexicalForm();
        int val;
        try {
            val = Integer.parseInt(lex);
        } catch (NumberFormatException e) {
            throw new ExprEvalException("The argument must be an integer literal: " + arg1, e);
        }
        if (val < 0) {
            throw new ExprEvalException("The argument must be a non-negative integer: " + arg1);
        }
        double res = CombinatoricsUtils.factorialDouble(val);
        return NodeValue.makeDouble(res);
    }
}
