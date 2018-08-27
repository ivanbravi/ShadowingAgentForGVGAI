package shadow.controllers.DoNothing;

import shadow.AbstractBudgetPlayer;
import core.game.StateObservation;
import ontology.Types.ACTIONS;

public class Agent extends AbstractBudgetPlayer{

	private String [] actions;
	private double[] probability;
	private double[] value;

	public Agent(StateObservation stateObs){
		super(stateObs);
		actions = new String[stateObs.getAvailableActions().size()];
		probability = new double[stateObs.getAvailableActions().size()];
		for(int i=0; i<actions.length; i++){
			actions[i] = stateObs.getAvailableActions().get(i).name();
			probability[i] = 0;
			value[i] = 0;
		}
	}

	@Override
	public void setSimulationBudget(int simulationBudget) {

	}

	@Override
	public ACTIONS act(StateObservation stateObs) {
		return ACTIONS.ACTION_NIL;
	}

	@Override
	public String[] getActions() {
		return actions;
	}

	@Override
	public double[] getActionProbabilities() {
		return probability;
	}

	@Override
	public double[] getActionValues() {
		return value;
	}

	@Override
	public double budgetUsed() {
		return 0;
	}

	@Override
	public double finalDecisionMoment() {
		return 0;
	}
}
