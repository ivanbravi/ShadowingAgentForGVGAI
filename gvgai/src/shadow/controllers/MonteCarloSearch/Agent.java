package shadow.controllers.MonteCarloSearch;

import shadow.Heuristics.SimpleStateHeuristic;
import shadow.AbstractBudgetPlayer;
import core.game.StateObservation;
import ontology.Types;
import tools.Utils;

import java.util.ArrayList;
import java.util.Random;

public class Agent extends AbstractBudgetPlayer {

    protected Random randomGenerator;
    private int simulationBudget;

    private int maxMonteCarloDepth = 25;

    private String[] actions;
    private double[] probability;
	private double[] values;
	private double lastPreferredActionChange;

    public Agent(StateObservation so){
    	super(so);
        randomGenerator = new Random();
        actions = new String[so.getAvailableActions().size()];
		probability = new double[so.getAvailableActions().size()];
		values = new double[so.getAvailableActions().size()];
        for(int i=0; i<actions.length; i++){
        	actions[i] = so.getAvailableActions().get(i).name();
		}
    }

    public void setSimulationBudget(int simulationBudget){
    	this.simulationBudget = simulationBudget;
	}

    public Types.ACTIONS act(StateObservation stateObs) {
    	int budget = this.simulationBudget;
		double[] confidenceValues = new double[actions.length];
		int[] confidenceCounters = new int[actions.length];
		SimpleStateHeuristic heuristic =  new SimpleStateHeuristic(stateObs);
        Types.ACTIONS action = null;
        StateObservation stCopy = stateObs.copy();
		int firstActionIndex = 0;
		boolean firstAction = true;
		int maxConfidenceIndex = -1;
		int changeTime = 0;

		int depth = 0;

        while(budget > 0){
            ArrayList<Types.ACTIONS> actions = stateObs.getAvailableActions();
            int index = randomGenerator.nextInt(actions.size());
            action = actions.get(index);

            if(firstAction){
            	firstActionIndex = index;
			}

            stCopy.advance(action);
            firstAction = false;
			budget--;
			depth++;

            if(stCopy.isGameOver() || depth == maxMonteCarloDepth){
                stCopy = stateObs.copy();
				depth = 0;
				confidenceCounters[firstActionIndex]++;
				confidenceValues[firstActionIndex] += heuristic.evaluateState(stCopy);
                firstAction = true;

                // cycle through the data to check whether the preferred action changed
				int newMaxConfidenceIndex = 0;
				double maxConfidence = Double.NEGATIVE_INFINITY;
				for(int i=0; i<actions.size(); i++){
					if(confidenceCounters[i]>0) {
						double currConfidence = confidenceValues[i] / confidenceCounters[i];
						currConfidence = Utils.noise(currConfidence, 1e-6, randomGenerator.nextDouble());
						if (currConfidence > maxConfidence) {
							maxConfidence = currConfidence;
							newMaxConfidenceIndex = i;
						}
					}
				}

				if(newMaxConfidenceIndex != maxConfidenceIndex){
					maxConfidenceIndex = newMaxConfidenceIndex;
					changeTime = simulationBudget-budget;
				}

			}



        }

		int actionsVisits = sum(confidenceCounters);
		for(int i=0; i<actions.length; i++){
			if(confidenceCounters[i]>0) {
				double currConfidence = confidenceValues[i] / confidenceCounters[i];
				values[i] = Utils.noise(currConfidence, 1e-6, randomGenerator.nextDouble());
			}else{
				values[i] = Double.NaN;
			}
			probability[i] = confidenceCounters[i]/actionsVisits;
		}

		probability = normalize(confidenceCounters,0, sum(confidenceCounters));

		lastPreferredActionChange = (double) changeTime/simulationBudget;

        return stateObs.getAvailableActions().get(maxConfidenceIndex);
    }

    private int sum(int[] a){
		int sum = 0;
		for(int i=0; i<a.length; i++){
			sum+=a[i];
		}
		return sum;
	}

	private double[] normalize(int[] a, double min, double max){
		int size = a.length;
		double[] n = new double[size];

		for(int i=0; i<size; i++){
			n[i] = (a[i]-min)/(max-min);
		}
		return n;
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
		return 1;
	}

	@Override
	public double finalDecisionMoment() {
		return lastPreferredActionChange;
	}

}
