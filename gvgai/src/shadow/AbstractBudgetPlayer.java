package shadow;

import core.game.StateObservation;
import ontology.Types;

public abstract class AbstractBudgetPlayer {

	public AbstractBudgetPlayer(StateObservation state){

	}

	public abstract void setSimulationBudget(int simulationBudget);

	public abstract Types.ACTIONS act(StateObservation state);

	public abstract String[] getActions();

	public abstract double[] getActionProbabilities();

	public abstract double[] getActionValues();

	// number of simulations done to reach a final decision
	public abstract double budgetUsed();

	// where moment is on a scale from 0 to 1 wrt the budget used
	public abstract double finalDecisionMoment();

}

