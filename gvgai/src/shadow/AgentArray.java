package shadow;

import core.game.StateObservation;
import ontology.Types;

import java.util.ArrayList;

public class AgentArray extends AbstractBudgetPlayer{

	private int size;
	private AbstractBudgetPlayer[] agents;
	private AgentBuilder builder;

	private String[] actions;
	private double[] probabilities;
	private double[] values;
	double budgetUsed;
	double decisionTime;

	public AgentArray(AgentBuilder builder, int size, StateObservation state){
		super(state);
		this.size = size;
		this.agents = new AbstractBudgetPlayer[size];
		this.builder = builder;
		initAgents(state);
		loadActions(state.getAvailableActions(true));
	}

	private void loadActions(ArrayList<Types.ACTIONS> actions){
		this.actions = new String[actions.size()];
		for(int i=0; i<actions.size(); i++){
			this.actions[i] = actions.get(i).toString();
		}
	}

	private void initAgents(StateObservation state){
		for(int i=0; i<agents.length; i++){
			agents[i] = builder.build(state);
		}
	}

	@Override
	public void setSimulationBudget(int simulationBudget) {
		System.out.println("suggested budget: "+simulationBudget+"\nreal budget used: "+builder.budget);
	}

	public Types.ACTIONS act(StateObservation state){
		Types.ACTIONS[] decisions = new Types.ACTIONS[size];

		for(int i=0; i<agents.length; i++){
			decisions[i] = agents[i].act(state);
		}

		for(int i=0; i<agents.length;i++){
			agents[i].getActions();
			agents[i].getActionProbabilities();
			agents[i].getActionValues();
			agents[i].budgetUsed();
			agents[i].finalDecisionMoment();
		}
		return decisions[0];
	}

	@Override
	public String[] getActions() {
		return this.actions;
	}

	@Override
	public double[] getActionProbabilities() {
		return probabilities;
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
		return decisionTime;
	}

}
