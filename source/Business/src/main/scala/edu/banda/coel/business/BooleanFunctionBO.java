package edu.banda.coel.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.banda.core.util.ConversionUtil;
import com.banda.function.evaluator.FunctionEvaluator;

/**
 * @author © Peter Banda
 * @since 2011
 */
public class BooleanFunctionBO {

	private FunctionEvaluator<Boolean, Boolean> evaluator;

	public BooleanFunctionBO(FunctionEvaluator<Boolean, Boolean> evaluator) {
		this.evaluator = evaluator;
	}

	public Collection<List<Boolean>> enumerateBooleanFunctionInputs(int arity) {
		Collection<List<Boolean>> inputs = new ArrayList<List<Boolean>>();
		int numberOfInputs = (int) Math.pow(2, arity);
		for (int decimalInput = 0; decimalInput < numberOfInputs; decimalInput++) {
			inputs.add(ConversionUtil.convertDecimalToBooleanList(decimalInput, arity));
		}
		return inputs;
	}

	public Boolean[] getAllOutputs() {
		Integer arity = evaluator.getArity();
		if (arity != null) {
			return getAllOutputs(arity);
		}
		throw new RuntimeException("Trying to get all outputs of the function without arity bound. Please specify arity!");
	}

	public Boolean[] getAllOutputs(int arity) {
		Collection<List<Boolean>> inputs = enumerateBooleanFunctionInputs(arity);
		Boolean[] outputs = new Boolean[inputs.size()];
		int i = 0;
		for (List<Boolean> input : inputs) {
			outputs[i] = evaluator.evaluate(input);
			i++;
		}
		return outputs;
	}
}