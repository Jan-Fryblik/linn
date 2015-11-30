package linn.core.lang;

import linn.core.Linn;
import linn.core.execute.LinnExecutor;

import org.junit.Test;

public class BuilderTest {

	@Test
	public void testBuildSimple() {
		// define and print an L-System definition
		final Linn linn = LinnBuilder.newLinn("testLinn")
				.withAuthor("Thomas Trojer")
				// H --5.5-> F H
				.withRule("H").andWeight(5.5).andProduction().F().rewrite("H")
				.done()
				// H --0.5-> F;
				.withRule("H").andWeight(0.5).andProduction().F()
				// branching
				.branch().F().done().done().build();
		System.out.println(linn);
		// configure the execution environment and execute a number of times
		final LinnExecutor executor = LinnExecutor
				.newExecutor()
				.useLinn(linn)
				.onStateChanged(
						t -> {
							System.out.println("Turtle: " + t.getX() + ", "
									+ t.getY() + ", " + t.getZ());
						}).withAxiom().rewrite("H").done();
		System.out.println(executor.getProductionResult());
		executor.executeAtMost(100, p -> {
			System.out.println(p);
		});
		System.out.println("Iterations: " + executor.getIterationCount());
	}

}
