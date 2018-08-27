package shadow.controllers.Random;

import shadow.AbstractBudgetPlayer;
import core.game.StateObservation;
import ontology.Types;

import java.util.ArrayList;
import java.util.Random;

public class Agent extends AbstractBudgetPlayer {

    protected Random randomGenerator;

    private String[] actions;
    private double[] probability;
	private double[] values;

    public Agent(StateObservation so){
    	super(so);
        randomGenerator = new Random();
        actions = new String[so.getAvailableActions().size()];
		probability = new double[so.getAvailableActions().size()];
		values = new double[so.getAvailableActions().size()];

		double actionProb = 1.0/((double)actions.length);

        for(int i=0; i<actions.length; i++){
        	actions[i] = so.getAvailableActions().get(i).name();
        	probability[i] = actionProb;
			values[i] = 0.0;
		}
    }

    public void setSimulationBudget(int simulationBudget){
	}

    public Types.ACTIONS act(StateObservation stateObs) {
		ArrayList<Types.ACTIONS> availActions = stateObs.getAvailableActions();
    	Types.ACTIONS action = availActions.get(randomGenerator.nextInt(availActions.size()));
		return action;
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
		return values;
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
