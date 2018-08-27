package shadow;

import core.game.StateObservation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class AgentBuilder implements Serializable {

	AGENT type;
	int budget;
	HashMap<String, Serializable> args;

	private AgentBuilder(AGENT type, int budget, HashMap<String, Serializable> params){
		this.type = type;
		this.budget = budget;
		this.args = params;
	}

	public enum AGENT {
		MONTECARLO(1, "MonteCarloSearch"),
		RANDOM(2, "Random"),
		DONOTHING(3, "Do Nothing"),
		ONESTEPLA(4, "One Step Look Ahead"),;

		int id;
		String name;

		AGENT(int id, String name){
			this.id = id;
			this.name = name;
		}

		public int getId(){
			return id;
		}

		public String getName() {
			return name;
		}

		public String toString(){
			return name;
		}
	}

	public static class AgentDescriptionException extends Exception{

		public AgentDescriptionException(String message){
			super(message);
		}
	}

	public static AgentBuilder createAgentSpec(AGENT type, int budget, HashMap<String, Serializable> params) throws AgentDescriptionException{
		AgentBuilder agentSpecs;

		checkBudget(budget);

		if(type == null){
			throw new AgentBuilder.AgentDescriptionException("Invalid agent type");
		}

		agentSpecs = new AgentBuilder(type, budget, params);

		return agentSpecs;
	}

	private static void checkBudget(int budget) throws AgentDescriptionException{
		if(budget <= 0){
			throw new AgentDescriptionException("Invalid budget ["+budget+"]");
		}
	}

	private static void checkParameters(String[] keys, HashMap<String, Serializable> params) throws AgentDescriptionException{

		if(!params.keySet().containsAll(Arrays.asList(keys))){
			throw new AgentDescriptionException("Missing parameters\n\tRequestes: "+
					Arrays.toString(keys)+ "\n\tGiven: "+params.keySet().toString());
		}
	}

	public AbstractBudgetPlayer build(StateObservation state){
		AbstractBudgetPlayer agent = null;

		switch (this.type){
			case MONTECARLO:{
				agent = new shadow.controllers.MonteCarloSearch.Agent(state);
			}break;
			case DONOTHING:{
				agent = new shadow.controllers.DoNothing.Agent(state);
			}break;
			case ONESTEPLA:{
				agent = new shadow.controllers.OneStepLookAhead.Agent(state);
			}break;
			case RANDOM:{
				agent = new shadow.controllers.Random.Agent(state);
			}
		}

		agent.setSimulationBudget(budget);

		return agent;
	}

	public String argsToString(){
		return "";
	}

	public String toString(){
		return this.type + " " + argsToString();
	}

}
