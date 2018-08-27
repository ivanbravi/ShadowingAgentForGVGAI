package shadow;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer{

    private static AgentBuilder mainBuilder;
    private static AgentBuilder shadowBuilder;
    private static ResultsDelegate delegate;

	private AbstractBudgetPlayer mainAgent;
	private AbstractBudgetPlayer shadowAgent;

    private static boolean VERBOSE = false;

    public Agent(StateObservation state, ElapsedCpuTimer timer){
		mainAgent = mainBuilder.build(state);
		shadowAgent = shadowBuilder.build(state);
		String [] actions = new String[state.getAvailableActions().size()];

		for(int i=0; i<actions.length; i++){
			actions[i] = state.getAvailableActions().get(i).name();
		}

		TickData.setActions(actions);
		delegate.deliverActions(TickData.getActions());
    }

    public static void setDelegate(ResultsDelegate delegate){
		Agent.delegate = delegate;
		delegate.deliverAgentNames(new String[]{"main","shadow"});
    }

	public static void setMainBuilder(AgentBuilder mainB){
    	Agent.mainBuilder = mainB;
	}

	public static void setShadowBuilder(AgentBuilder shadowB){
		Agent.shadowBuilder = shadowB;
	}


    public Types.ACTIONS act(StateObservation state, ElapsedCpuTimer timer) {
        Types.ACTIONS mainAction, shadowAction;

        if(VERBOSE)
        	System.out.println("❖ Main Agent start");

        mainAction = mainAgent.act(state);
		if(VERBOSE) {
			System.out.println("❖ Main Agent end");
			System.out.println("❖ Shadow Agent start");
		}
        shadowAction = shadowAgent.act(state);
		if(VERBOSE) {
			System.out.println("❖ Shadow Agent end");
			System.out.println("❖ Dumping data start");
		}
        // Analysis
        TickData mainData = new TickData("main",mainAction.name());
		mainData.populateActionProbability(mainAgent.getActions(), mainAgent.getActionProbabilities());
		mainData.populateActionValues(mainAgent.getActions(), mainAgent.getActionValues());
		mainData.populateIntrospection(mainAgent.budgetUsed(),Math.min(Math.max(mainAgent.finalDecisionMoment(),0),1));

		TickData shadowData = new TickData("shadow", shadowAction.name());
		shadowData.populateActionProbability(shadowAgent.getActions(), shadowAgent.getActionProbabilities());
		shadowData.populateActionValues(shadowAgent.getActions(), shadowAgent.getActionValues());
		shadowData.populateIntrospection(shadowAgent.budgetUsed(),Math.min(Math.max(shadowAgent.finalDecisionMoment(),0),1));

		if(VERBOSE) {
			System.out.println("❖ Dumping data end");
			System.out.println("❖ ACTION: " + mainAction.toString());
		}


        delegate.deliverTickData(new TickData[]{mainData, shadowData}, state.getGameScore());

        return mainAction;
    }
}
