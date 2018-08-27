package shadow;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class ShadowingAgent extends AbstractPlayer{

	/* These are supposed to be setup at the time of launching the simulation */
    private static AgentBuilder mainBuilder;
    private static AgentBuilder shadowBuilder;

	private PlaythroughLogger logger;

	private AbstractBudgetPlayer mainAgent;
	private AbstractBudgetPlayer shadowAgent;

    public ShadowingAgent(StateObservation state, ElapsedCpuTimer timer){
		mainAgent = mainBuilder.build(state);
		shadowAgent = shadowBuilder.build(state);

		String [] actions = new String[state.getAvailableActions().size()];

		for(int i=0; i<actions.length; i++){
			actions[i] = state.getAvailableActions().get(i).name();
		}

		logger = new PlaythroughLogger("playthroughFileName.csv","frameFileName.csv");
    }

	public static void setMainBuilder(AgentBuilder mainB){
		ShadowingAgent.mainBuilder = mainB;
	}

	public static void setShadowBuilder(AgentBuilder shadowB){
		ShadowingAgent.shadowBuilder = shadowB;
	}


    public Types.ACTIONS act(StateObservation state, ElapsedCpuTimer timer) {
        Types.ACTIONS mainAction, shadowAction;

        mainAction = mainAgent.act(state);
		shadowAgent.act(state);

		// Analysis
		logger.logFrameData("main", mainAgent.getActionProbabilities(), mainAgent.getActionValues(), mainAgent.budgetUsed(), mainAgent.finalDecisionMoment());
		logger.logFrameData("shadow", shadowAgent.getActionProbabilities(), shadowAgent.getActionValues(), shadowAgent.budgetUsed(), shadowAgent.finalDecisionMoment());

		int winner = (state.getGameWinner()== Types.WINNER.PLAYER_WINS)?1:0;

		logger.logPlaythroughData("main", "shadow", state.getAvailableActions(), winner, state.getGameScore(), state.getGameTick());

        return mainAction;
    }

	public void result(StateObservation stateObs, ElapsedCpuTimer elapsedCpuTimer){
    	logger.close();
	}
}
