package shadow;

import ontology.Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PlaythroughLogger {

	PrintWriter playthrough;
	PrintWriter frame;
	String csvSeparator = ",";

	public PlaythroughLogger(String playthroughDataFile, String frameDataFile){
		playthrough = intitWriter(playthroughDataFile);
		frame= intitWriter(frameDataFile);
	}

	private PrintWriter intitWriter(String completeFilePath){
		PrintWriter writer = null;
		try {
			File f = new File(completeFilePath);
			File parent = f.getParentFile();
			if(parent != null){
				if (!parent.exists()){
					f.getParentFile().mkdirs();
				}
			}
			if (!f.exists()){
				f.createNewFile();
			}

			writer = new PrintWriter(new FileOutputStream(f,true));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer;
	}

	public void logFrameData(String agentName, double[] probability, double[] values, double budgedUsed, double convergence){
		StringBuilder builder = new StringBuilder();

		builder.append(agentName);
		builder.append(csvSeparator);

		for(int i=0; i<probability.length; i++){
			builder.append(probability[i]);
			builder.append(csvSeparator);
		}

		for(int i=0; i<values.length; i++){
			builder.append(values[i]);
			builder.append(csvSeparator);
		}

		builder.append(budgedUsed);
		builder.append(csvSeparator);

		builder.append(convergence);
		builder.append(csvSeparator);

		frame.println(builder.toString());
	}

	public void logPlaythroughData(String agentName1, String agentName2, ArrayList<Types.ACTIONS> actions, int win, double score, int time){
		StringBuilder builder = new StringBuilder();

		builder.append(agentName1);
		builder.append(csvSeparator);
		builder.append(agentName2);
		builder.append(csvSeparator);

		for(int i=0; i<actions.size(); i++){
			builder.append(actions.get(i));
			builder.append(csvSeparator);
		}

		builder.append(csvSeparator);
		builder.append(win);
		builder.append(csvSeparator);
		builder.append(score);
		builder.append(csvSeparator);
		builder.append(time);

		playthrough.println(builder.toString());

	}

	public void close(){
		playthrough.flush();;
		playthrough.close();
		frame.flush();
		frame.close();
	}



}
