package edu.banda.coel.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.banda.core.util.RandomUtil;
import com.banda.function.domain.AggregateFunction;
import com.banda.function.domain.BooleanFunction.BooleanFunctionType;
import com.banda.math.domain.rand.RandomDistribution;
import com.banda.math.domain.rand.RandomDistributionType;
import com.banda.math.domain.rand.ShapeLocationDistribution;
import com.banda.math.domain.rand.UniformDistribution;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class CoreTestDataGenerator {

	protected int getRandomInt(int from, int to) {
		return RandomUtil.nextInt(from, to);
	}

	protected double getRandomDouble(double from, double to) {
		return RandomUtil.nextDouble(from, to);
	}

	protected boolean getRandomBoolean() {
		return RandomUtil.nextBoolean();
	}

	protected <T> T getRandomElement(T[] objects) {
		return RandomUtil.nextElement(objects);
	}

	protected AggregateFunction getRandomAggregateFunctionBut(AggregateFunction[] exceptFunctions) {
		Set<AggregateFunction> functions = new HashSet<AggregateFunction>();
		functions.addAll(Arrays.asList(AggregateFunction.values()));
		for (AggregateFunction exceptFunction : exceptFunctions) {
			functions.remove(exceptFunction);
		}
		return getRandomElement(functions.toArray(new AggregateFunction[0]));
	}

	protected AggregateFunction getRandomAggregateFunction() {
		return getRandomElement(AggregateFunction.values());
	}

	protected BooleanFunctionType getRandomBooleanFunctionType() {
		return getRandomElement(BooleanFunctionType.values());
	}

	protected RandomDistribution<Double> createRandomDistribution() {
		return RandomUtil.nextBoolean() ?
				new UniformDistribution<Double>(getRandomDouble(0.2, 0.6), getRandomDouble(2d, 4.5)) :
				new ShapeLocationDistribution<Double>(RandomDistributionType.Normal, getRandomDouble(2d, 2.5), getRandomDouble(0.3, 0.7));
	}
}