package com.radixdlt.atommodel.procedures;

import static org.assertj.core.api.Assertions.assertThat;

import com.radixdlt.atoms.Particle;
import com.radixdlt.constraintmachine.TransitionProcedure;
import com.radixdlt.constraintmachine.TransitionProcedure.CMAction;
import com.radixdlt.constraintmachine.TransitionProcedure.ProcedureResult;
import com.radixdlt.utils.UInt256;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

public class FungibleTransitionTest {
	private static class Fungible extends Particle {
		private final UInt256 amount;
		Fungible(UInt256 amount) {
			this.amount = amount;
		}

		UInt256 getAmount() {
			return amount;
		}

		@Override
		public String toString() {
			return "Fungible " + amount;
		}
	}

	@Test
	public void when_validating_a_simple_fungible_transfer__then_validation_should_succeed() {
		TransitionProcedure<Fungible, Fungible> procedure = new FungibleTransition<>(
			Fungible::getAmount, Fungible::getAmount,
			(a, b) -> true
		);
		ProcedureResult result = procedure.execute(
			new Fungible(UInt256.ONE),
			new Fungible(UInt256.ONE),
			new AtomicReference<>(),
			null
		);

		assertThat(result.getCmAction()).isEqualTo(CMAction.POP_INPUT_OUTPUT);
	}

	@Test
	public void when_validating_a_two_to_one_transfer__then_execution_should_pop_output_and_one_left_on_input() {
		TransitionProcedure<Fungible, Fungible> procedure = new FungibleTransition<>(
			Fungible::getAmount, Fungible::getAmount,
			(a, b) -> true
		);

		AtomicReference<Object> data = new AtomicReference<>();
		ProcedureResult result = procedure.execute(
			new Fungible(UInt256.TWO),
			new Fungible(UInt256.ONE),
			data,
			null
		);

		assertThat(data).hasValue(UInt256.ONE);
		assertThat(result.getCmAction()).isEqualTo(CMAction.POP_OUTPUT);
	}

	@Test
	public void when_validating_a_one_to_two_transfer__then_input_should_succeed_and_one_left_on_stack() {
		TransitionProcedure<Fungible, Fungible> procedure = new FungibleTransition<>(
			Fungible::getAmount, Fungible::getAmount,
			(a, b) -> true
		);

		AtomicReference<Object> data = new AtomicReference<>();
		ProcedureResult result = procedure.execute(
			new Fungible(UInt256.ONE),
			new Fungible(UInt256.TWO),
			data,
			null
		);

		assertThat(result.getCmAction()).isEqualTo(CMAction.POP_INPUT);
		assertThat(data).hasValue(UInt256.ONE);
	}

	@Test
	public void when_validating_a_two_to_two_transfer__then_input_should_succeed_and_zero_left_on_stack() {
		TransitionProcedure<Fungible, Fungible> procedure = new FungibleTransition<>(
			Fungible::getAmount, Fungible::getAmount,
			(a, b) -> true
		);

		AtomicReference<Object> data = new AtomicReference<>();
		ProcedureResult result = procedure.execute(
			new Fungible(UInt256.TWO),
			new Fungible(UInt256.TWO),
			data,
			null
		);

		assertThat(result.getCmAction()).isEqualTo(CMAction.POP_INPUT_OUTPUT);
	}

	@Test
	public void when_validating_a_one_to_two_one_transfer__then_input_should_succeed_and_zero_left_on_stack() {
		TransitionProcedure<Fungible, Fungible> procedure = new FungibleTransition<>(
			Fungible::getAmount, Fungible::getAmount,
			(a, b) -> true
		);

		ProcedureResult result = procedure.execute(
			new Fungible(UInt256.ONE),
			new Fungible(UInt256.TWO),
			new AtomicReference<>(UInt256.ONE),
			new ProcedureResult(CMAction.POP_INPUT)
		);

		assertThat(result.getCmAction()).isEqualTo(CMAction.POP_INPUT_OUTPUT);
	}
}