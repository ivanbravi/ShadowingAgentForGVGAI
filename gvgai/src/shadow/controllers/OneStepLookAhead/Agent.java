package shadow.controllers.OneStepLookAhead;


import shadow.Heuristics.SimpleStateHeuristic;
import shadow.AbstractBudgetPlayer;
import core.game.StateObservation;
import ontology.Types;
import tools.Utils;

import java.util.Random;

public class Agent extends AbstractBudgetPlayer {

	private int simulationBudget;
	private String[] actions;
	private double[] probability;
	private  double[] values;
	private double budgetUsed;
	private double finalDecisionMoment;

	private Random randomGenerator;

    public Agent(StateObservation stateObs) {
        super(stateObs);
        actions = new String[stateObs.getAvailableActions().size()];
		values = new double[stateObs.getAvailableActions().size()];
		probability = new double[stateObs.getAvailableActions().size()];
        for(int i=0; i<actions.length; i++){
        	actions[i] = stateObs.getAvailableActions().get(i).name();
		}
		randomGenerator = new Random();
    }

	@Override
	public void setSimulationBudget(int simulationBudget) {
		this.simulationBudget = simulationBudget;
	}

	public Types.ACTIONS act(StateObservation stateObs) {
		int budget = this.simulationBudget;
        Types.ACTIONS bestAction = Types.ACTIONS.ACTION_NIL;
        double maxQ = Double.NEGATIVE_INFINITY;
        SimpleStateHeuristic heuristic =  new SimpleStateHeuristic(stateObs);

        for(int index=0; index<actions.length; index++){
			Types.ACTIONS action = stateObs.getAvailableActions().get(index);
            StateObservation stCopy = stateObs.copy();
            stCopy.advance(action);
            budget--;
            double q = heuristic.evaluateState(stCopy);
            q = Utils.noise(q, 1e-6, randomGenerator.nextDouble());

            values[index] = q;
			index++;

            if (q > maxQ) {
                maxQ = q;
                bestAction = action;
				finalDecisionMoment = ((double) simulationBudget-budget)/simulationBudget;
            }

            if(budget <= 0){
            	break;
			}

        }

        for(int i=0; i<actions.length; i++){
			String a = actions[i];
        	if(a.equalsIgnoreCase(bestAction.name())){
				probability[i] = 1;
			}else{
        		probability[i] = 0;
			}
		}

		budgetUsed = ((double)simulationBudget-budget)/simulationBudget;

        return bestAction;
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
		return budgetUsed;
	}

	@Override
	public double finalDecisionMoment() {
    	return finalDecisionMoment;
	}


}
